package com.kydas.build.cv.extractors;

import com.kydas.build.cv.data.WaybillExtractedData;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WaybillFieldExtractor {

    public static Result extract(String rawOcrText) {
        String text = normalize(rawOcrText);

        List<String> lines = splitLines(text);
        List<String> windows = buildWindows(lines, 3);

        Context ctx = new Context(lines, windows, text);
        WaybillExtractedData data = new WaybillExtractedData();

        data.setInvoiceNumber(extractInvoiceNumber(ctx));
        data.setMaterialName(extractMaterial(ctx));
        data.setVolume(extractVolume(ctx));
        data.setNetWeight(extractMass(ctx, Field.NETTO));
        data.setGrossWeight(extractMass(ctx, Field.BRUTTO));
        data.setPackageCount(extractPackageCount(ctx));

        double confidence = ctx.totalConfidence();
        return new Result(data, confidence, ctx.debug);
    }

    enum Field {INVOICE, MATERIAL, VOLUME, NETTO, BRUTTO, PACKAGES}

    static class Context {
        final List<String> lines, windows;
        final String full;
        final Map<Field, Double> conf = new EnumMap<>(Field.class);
        final List<String> debug = new ArrayList<>();

        Context(List<String> lines, List<String> windows, String full) {
            this.lines = lines;
            this.windows = windows;
            this.full = full;
        }

        void score(Field f, double v, String why) {
            conf.put(f, Math.max(conf.getOrDefault(f, 0.0), v));
            debug.add(f + ": " + why);
        }

        double totalConfidence() {
            return conf.values().stream().mapToDouble(d -> d).average().orElse(0.0);
        }
    }

    static final Map<Field, List<String>> LABELS = Map.of(
            Field.INVOICE, List.of("накладн", "транспортная наклад", "заказ", "номер", "№", "н/н"),
            Field.MATERIAL, List.of(
                    "груз\\.?\\s*наименован",
                    "наименован\\s*груз",
                    "наименование груза",
                    "наименован", "товар", "материал", "продукц"
            ),
            Field.VOLUME,  List.of("объем", "объём", "м3", "м^3", "м³"),
            Field.NETTO,   List.of("нетто", "масса нет", "вес нет"),
            Field.BRUTTO,  List.of("брутто", "масса брут", "вес брут"),
            Field.PACKAGES,List.of("кол-во мест", "количество мест", "мест")
    );

    static final String NUM = "(?:(?:\\d{1,3}(?:[\\s ]\\d{3})+|\\d+)(?:[\\.,]\\d{1,3})?)";
    static final Pattern PAT_VOLUME = Pattern.compile("(об[ъь]?е[мм]|м\\s*[\\^]?[23]|м³)\\s*[:=–-]?\\s*(" + NUM + ")\\s*(?:м\\s*[\\^]?[23]|м³)?", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    static final Pattern PAT_NETTO = Pattern.compile("(нетт?о|масса\\s*нет)\\s*[:=–-]?\\s*(" + NUM + ")\\s*(?:т|тонн|тн)?", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    static final Pattern PAT_BRUTTO = Pattern.compile("(брутт?о|масса\\s*брут)\\s*[:=–-]?\\s*(" + NUM + ")\\s*(?:т|тонн|тн)?", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    static final Pattern PAT_PKGS = Pattern.compile("(мест|кол-во\\s*мест|количеств[оa]\\s*мест)\\s*[:=–-]?\\s*(\\d{1,6})", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    static final Pattern PAT_INVOICE_TN = Pattern.compile(
            "транспортн\\S*\\s+накладн\\S*\\s*([0-9]{3,10})",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );
    static final Pattern PAT_INVOICE_INLINE = Pattern.compile("(?:№|n[o0]|номер)\\s*[:=–-]?\\s*([\\w\\-/]+)");
    static final Pattern PAT_MATERIAL_AFTER = Pattern.compile(
            "(?:груз\\.?\\s*наименован[иияе]+|наименован[иияе]+\\s*груз[ао]?|наименован[иияе]+)\\s*[:=–-]?\\s*"
                    + "([^\\n]*?)" // сам материал до разделителя/следующей метки
                    + "(?:[,;]|\\bкол-?во\\b|\\bколичеств\\b|\\bнетт?о\\b|\\bбрутт?о\\b|\\bобъ?е[мм]\\b|\\bобъем\\b)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    static final Pattern PAT_MATERIAL_FALLBACK = Pattern.compile(
            "(бортов[ао]й\\s*камень|камень|щеб[её]н|бетон|песок|гранит|кирпич)[^\\n,]*",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    static final JaroWinklerDistance JW = new JaroWinklerDistance();

    static String extractInvoiceNumber(Context ctx) {
        Matcher mTn = PAT_INVOICE_TN.matcher(ctx.full);
        if (mTn.find()) {
            ctx.score(Field.INVOICE, .9, "invoice TN form");
            return mTn.group(1);
        }

        Matcher m = PAT_INVOICE_INLINE.matcher(ctx.full);
        if (m.find()) {
            ctx.score(Field.INVOICE, .9, "invoice inline");
            return m.group(1);
        }

        String around = findNearByLabels(ctx, Field.INVOICE, 80);
        if (around != null) {
            Matcher m2 = PAT_INVOICE_INLINE.matcher(around);
            if (m2.find()) {
                ctx.score(Field.INVOICE, .7, "invoice near label");
                return m2.group(1);
            }
            String tail = takeTailAfterLabel(around, LABELS.get(Field.INVOICE));
            if (tail != null) {
                ctx.score(Field.INVOICE, .55, "invoice tail");
                return firstToken(tail);
            }
        }
        return null;
    }

    static String extractMaterial(Context ctx) {
        // 4.1. Сначала ищем окно с меткой материала
        String w = findNearByLabels(ctx, Field.MATERIAL, 220);
        if (w != null) {
            // спец-выражение «после метки»
            Matcher ma = PAT_MATERIAL_AFTER.matcher(w);
            if (ma.find()) {
                String val = cleanupMaterial(ma.group(1));
                if (!val.isBlank()) {
                    ctx.score(Field.MATERIAL, .9, "material after label");
                    return val;
                }
            }
            // если спец-регекс не сработал — старый «хвост после метки»
            String after = takeTailAfterLabel(w, LABELS.get(Field.MATERIAL));
            if (after != null && after.length() > 2) {
                String val = cleanupMaterial(after);
                ctx.score(Field.MATERIAL, .75, "material near label (tail)");
                return val;
            }
        }

        Matcher m = PAT_MATERIAL_FALLBACK.matcher(ctx.full);
        if (m.find()) {
            String seg = m.group();
            seg = seg.replaceAll("(,.*)$", "");
            seg = seg.replaceAll("(кол-?во|количеств|нетт?о|брутт?о|объ?е[мм]|объем).*$", "");
            String val = cleanupMaterial(seg);
            if (!val.isBlank()) {
                ctx.score(Field.MATERIAL, .7, "material keyword fallback");
                return val;
            }
        }
        return null;
    }

    static String cleanupMaterial(String s) {
        if (s == null) return "";
        String val = s;
        val = val.replaceAll("\\b\\d{1,6}\\s*шт\\.?\\b", "");
        val = val.replaceAll("\\s{2,}", " ").trim();
        val = val.replaceAll("[,.;:]+\\s*$", "").trim();
        return val;
    }

    static String extractVolume(Context ctx) {
        String w = findNearByLabels(ctx, Field.VOLUME, 100);
        if (w != null) {
            Matcher m = PAT_VOLUME.matcher(w);
            if (m.find()) {
                ctx.score(Field.VOLUME, .8, "volume near label");
                return m.group(2);
            }
        }
        Matcher m2 = PAT_VOLUME.matcher(ctx.full);
        if (m2.find()) {
            ctx.score(Field.VOLUME, .6, "volume global");
            return m2.group(2);
        }
        return null;
    }

    static String extractMass(Context ctx, Field f) {
        Pattern pat = (f == Field.NETTO) ? PAT_NETTO : PAT_BRUTTO;
        String w = findNearByLabels(ctx, f, 120);
        if (w != null) {
            Matcher m = pat.matcher(w);
            if (m.find()) {
                ctx.score(f, .8, f.name() + " near label");
                return m.group(2); // строка
            }
        }
        Matcher m2 = pat.matcher(ctx.full);
        if (m2.find()) {
            ctx.score(f, .6, f.name() + " global");
            return m2.group(2);
        }
        return null;
    }

    static Integer extractPackageCount(Context ctx) {
        String w = findNearByLabels(ctx, Field.PACKAGES, 120);
        if (w != null) {
            Matcher m = PAT_PKGS.matcher(w);
            if (m.find()) {
                ctx.score(Field.PACKAGES, .8, "packages near label");
                return Integer.valueOf(m.group(2));
            }
        }
        Matcher m2 = PAT_PKGS.matcher(ctx.full);
        if (m2.find()) {
            ctx.score(Field.PACKAGES, .6, "packages global");
            return Integer.valueOf(m2.group(2));
        }
        return null;
    }

    static String normalize(String s) {
        if (s == null) return "";
        String n = s;
        n = Normalizer.normalize(n, Normalizer.Form.NFKC);
        n = n.replace('A', 'А').replace('B', 'В').replace('E', 'Е').replace('K', 'К').replace('M', 'М').replace('H', 'Н')
                .replace('O', 'О').replace('P', 'Р').replace('C', 'С').replace('T', 'Т').replace('X', 'Х');
        n = n.replaceAll("[–—−]", "-").replace('№', '№').replaceAll(" +", " ");
        n = n.replace('\u00A0', ' ').replace('\u202F', ' ').replace('\u2009', ' ');
        return n;
    }

    static List<String> splitLines(String s) {
        String[] arr = s.split("\\R+");
        List<String> out = new ArrayList<>(arr.length);
        for (String l : arr) {
            String t = l.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    static List<String> buildWindows(List<String> lines, int size) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < Math.min(lines.size(), i + size); j++) {
                if (j > i) sb.append(" ");
                sb.append(lines.get(j));
            }
            out.add(sb.toString());
        }
        return out;
    }

    static String findNearByLabels(Context ctx, Field field, int maxLen) {
        List<String> labels = LABELS.getOrDefault(field, List.of());
        double best = 0;
        String bestWin = null;
        for (String w : ctx.windows) {
            for (String lab : labels) {
                int idx = indexOfFuzzy(w, lab);
                if (idx >= 0) {
                    double score = 0.8 + 0.2 * (1.0 - Math.min(1.0, (double) idx / Math.max(1, w.length())));
                    if (w.length() <= maxLen) score += 0.05;
                    if (score > best) {
                        best = score;
                        bestWin = w;
                    }
                }
            }
        }
        if (bestWin != null) ctx.debug.add(field + ": window hit [" + truncate(bestWin, 80) + "]");
        return bestWin;
    }

    static int indexOfFuzzy(String text, String needle) {
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            double d = JW.apply(words[i].toLowerCase(Locale.ROOT), needle.toLowerCase(Locale.ROOT));
            if (d >= 0.87) return text.indexOf(words[i]);
        }
        if (text.toLowerCase(Locale.ROOT).contains(needle)) return text.toLowerCase(Locale.ROOT).indexOf(needle);
        return -1;
    }

    static String takeTailAfterLabel(String line, List<String> labels) {
        String low = line.toLowerCase(Locale.ROOT);
        int best = -1, pos = -1;
        for (String lab : labels) {
            int i = indexOfFuzzy(low, lab);
            if (i >= 0 && i > best) {
                best = i;
                pos = i + lab.length();
            }
        }
        if (pos >= 0 && pos < line.length()) {
            String tail = line.substring(pos).replaceFirst("^[\\s:=-]+", "");
            return tail;
        }
        return null;
    }

    static String firstToken(String s) {
        Matcher m = Pattern.compile("([\\w\\-/]+)").matcher(s.trim());
        return m.find() ? m.group(1) : s.trim();
    }

    static String squeeze(String s) {
        return s.replaceAll("\\s{2,}", " ").trim();
    }

    static Double toNumber(String s) {
        if (s == null) return null;
        String n = s.replace(" ", "").replace(" ", "").replace(",", ".");
        try {
            return Double.parseDouble(n);
        } catch (Exception e) {
            return null;
        }
    }

    static String truncate(String s, int n) {
        return s.length() <= n ? s : s.substring(0, n) + "…";
    }

    public record Result(WaybillExtractedData data, double confidence, List<String> debugLog) {
    }
}
