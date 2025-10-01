package com.kydas.build.cv.extractors;

import com.kydas.build.cv.data.PassportQualityExtractedData;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PassportQualityFieldExtractor {

    public static Result extract(String rawOcrText) {
        String text = normalize(rawOcrText);

        PassportQualityExtractedData data = new PassportQualityExtractedData();
        data.setManufacturer(find(text, "изготовител[ьяяи]*\\s+(.+?)(?=\\s+Наименован|$)"));
        data.setConsumerNameAndAddress(find(text, "Наименован.*?потребител[ьяяи]*[,.:\\s]*(.+?)(?=контракт|номер контракт|Наименован.*издел|$)"));
        data.setContractNumber(find(text, "(?:контракт[^\\d]{0,10}|Наименован[^\\d]{0,10})(\\d{3,})"));
        data.setProductNameAndGrade(find(text, "Наименован.*?(?:марка|издел)[^\\n]*?(.+?)(?=Номер партии|Дата|Отгружаем|$)"));

        String batchNum = find(text, "Номер парт[иы][^\\d]{0,10}(\\d{5,})");
        String batchCnt = find(text, "(?:кол-во|количество|шт\\.?)[^\\d]{0,10}(\\d{1,6})");
        data.setBatchNumber(batchNum);
        if (batchCnt != null) data.setBatchCount(Integer.parseInt(batchCnt));

        String date = find(text, "Дата (?:изготовлен|выдач|выгруз)[^\\d]{0,10}(\\d{2}[./-]\\d{2}[./-]\\d{4})");
        if (date != null) {
            data.setManufactureDate(LocalDate.parse(date.replace('/', '.').replace('-', '.'), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        String shipped = find(text, "Отгружаем[оеы]* количество[^\\d]{0,10}(\\d{1,6})");
        if (shipped != null) data.setShippedQuantity(Integer.parseInt(shipped));

        String lab = find(text, "Начальник лаборатор[ии][^А-ЯЁ]*(?:[А-ЯЁ][а-яё]+(?:\\s*[А-ЯЁ]\\.){0,2})");
        if (lab != null) {
            lab = lab.replaceAll("Начальник лаборатор[ии]\\s*", "").replaceAll("подп.*$", "").trim();
            data.setLabChief(lab);
        }

        return new Result(data, 0.9, List.of("parsed with simplified regex extractor"));
    }

    private static String find(String text, String regex) {
        Matcher m = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(text);
        if (m.find()) return m.group(1).trim();
        return null;
    }

    private static String normalize(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFKC);
        n = n.replace('A', 'А').replace('B', 'В').replace('E', 'Е').replace('K', 'К')
                .replace('M', 'М').replace('H', 'Н').replace('O', 'О').replace('P', 'Р')
                .replace('C', 'С').replace('T', 'Т').replace('X', 'Х');
        return n.replaceAll("\\s+", " ").trim();
    }

    public record Result(PassportQualityExtractedData data, double confidence, List<String> debugLog) {
    }
}
