package com.kydas.build.cv;


import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.cv.data.WaybillExtractedData;
import com.kydas.build.cv.extractors.PassportQualityFieldExtractor;
import com.kydas.build.cv.extractors.WaybillFieldExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(Endpoints.OCR)
@RequiredArgsConstructor
public class CvController {

    private final OcrService ocrService;

    @PostMapping("/waybills")
    public WaybillExtractedData extractWaybill(@RequestParam("file") MultipartFile file) throws IOException {
        return WaybillFieldExtractor.extract(ocrService.processFile(file).text());
    }

    @PostMapping("/passports")
    public PassportQualityFieldExtractor.Result extractPassportQuality(@RequestParam("file") MultipartFile file) throws IOException {
        return PassportQualityFieldExtractor.extract(ocrService.processFile(file).text());
    }

    @PostMapping
    public ResponseEntity<OcrResponse> extractText(@RequestParam("file") MultipartFile file) {
        try {
            OcrResponse response = ocrService.processFile(file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new OcrResponse(false, "", 0, "error"));
        }
    }
}
