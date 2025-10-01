package com.kydas.build.cv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OcrServiceRestTemplate {

    private final RestTemplate restTemplate;

    @Value("${ocr.service.url}")
    private String ocrUrl;

    public OcrServiceRestTemplate(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public OcrResponse doOcr(byte[] pdfBytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        HttpEntity<byte[]> request = new HttpEntity<>(pdfBytes, headers);
        ResponseEntity<OcrResponse> response = restTemplate.exchange(
                ocrUrl + "/ocr",
                HttpMethod.POST,
                request,
                OcrResponse.class
        );
        return response.getBody();
    }
}
