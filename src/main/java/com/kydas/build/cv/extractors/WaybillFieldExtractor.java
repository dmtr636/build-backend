package com.kydas.build.cv.extractors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kydas.build.cv.data.WaybillExtractedData;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WaybillFieldExtractor {

    enum Field {INVOICE, MATERIAL, VOLUME, NETTO, BRUTTO, PACKAGES}

    static final Map<Field, List<String>> LABELS = loadLabelsFromJson();

    public static WaybillExtractedData extract(String rawOcrText) {
        String text = normalize(rawOcrText);
        WaybillExtractedData data = new WaybillExtractedData();

        data.setInvoiceNumber(extractInvoiceNumber(text, Field.INVOICE));
        data.setMaterialName(extractAfterLabel(text, Field.MATERIAL));
        data.setVolume(extractPattern(PAT_VOLUME, text));
        data.setNetWeight(extractPattern(PAT_NETTO, text));
        data.setGrossWeight(extractPattern(PAT_BRUTTO, text));
        data.setPackageCount(extractIntPattern(PAT_PKGS, text));

        return data;
    }

    private static String extractAfterLabel(String text, Field field) {
        for (String label : LABELS.getOrDefault(field, List.of())) {
            int idx = text.toLowerCase(Locale.ROOT).indexOf(label.toLowerCase(Locale.ROOT));
            if (idx >= 0) {
                String tail = text.substring(idx + label.length()).trim();
                String firstLine = tail.split("\\R")[0].trim();
                if (!firstLine.isBlank()) return firstLine;
            }
        }
        return null;
    }

    private static String extractInvoiceNumber(String text, Field field) {
        String afterLabel = extractAfterLabel(text, field);
        if (afterLabel == null) return null;

        afterLabel = afterLabel.replaceAll("\\s+", ""); // убираем пробелы

        Matcher m = PAT_INVOICE.matcher(afterLabel);
        if (m.find()) {
            if (m.group(2) != null) {
                return m.group(1) + "/" + m.group(2);
            } else {
                return m.group(1);
            }
        }
        return null;
    }

    private static final String NUM = "(?:(?:\\d{1,3}(?:[\\s ]\\d{3})+|\\d+)(?:[\\.,]\\d{1,3})?)";
    private static final Pattern PAT_VOLUME = Pattern.compile("(об[ъь]?е[мм]|м\\s*[\\^]?[23]|м³)\\s*[:=–-]?\\s*(" + NUM + ")", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern PAT_NETTO = Pattern.compile("(нетт?о|масса\\s*нет)\\s*[:=–-]?\\s*(" + NUM + ")", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern PAT_BRUTTO = Pattern.compile("(брутт?о|масса\\s*брут)\\s*[:=–-]?\\s*(" + NUM + ")", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern PAT_PKGS = Pattern.compile("(мест|кол-во\\s*мест|количеств[оa]\\s*мест)\\s*[:=–-]?\\s*(\\d{1,6})", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern PAT_INVOICE = Pattern.compile("(\\d+)(?:\\s*/\\s*([\\dA-Za-z]{1,5}))?", Pattern.CASE_INSENSITIVE);

    private static String extractPattern(Pattern pat, String text) {
        Matcher m = pat.matcher(text);
        if (m.find()) return m.group(2);
        return null;
    }

    private static Integer extractIntPattern(Pattern pat, String text) {
        Matcher m = pat.matcher(text);
        if (m.find()) return Integer.valueOf(m.group(2));
        return null;
    }

    private static String normalize(String s) {
        if (s == null) return "";
        String n = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFKC);
        n = n.replaceAll("[–—−]", "-").replaceAll(" +", " ");
        n = n.replace('\u00A0', ' ').replace('\u202F', ' ').replace('\u2009', ' ');
        return n;
    }

    @SuppressWarnings("unchecked")
    static Map<Field, List<String>> loadLabelsFromJson() {
        try (InputStream is = WaybillFieldExtractor.class.getResourceAsStream("/data/ocr_labels.json")) {
            if (is == null) throw new IllegalStateException("Missing ocr_labels.json in resources");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<String>> raw = mapper.readValue(is, Map.class);
            Map<Field, List<String>> result = new EnumMap<>(Field.class);
            for (Map.Entry<String, List<String>> e : raw.entrySet()) {
                result.put(Field.valueOf(e.getKey()), e.getValue());
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load OCR label dictionary", e);
        }
    }
}

