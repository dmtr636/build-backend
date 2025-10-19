package com.kydas.build.cv;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class OcrService {

    private final OcrServiceRestTemplate ocrRestTemplate;

    public OcrService(OcrServiceRestTemplate ocrRestTemplate) {
        this.ocrRestTemplate = ocrRestTemplate;
    }

    public OcrResponse processFile(MultipartFile file) throws IOException {
        byte[] pdfBytes;

        if ("application/pdf".equals(file.getContentType())) {
            pdfBytes = extractFirstPage(file.getBytes());
        } else {
            pdfBytes = convertToPdf(file);
            pdfBytes = extractFirstPage(pdfBytes);
        }

        return ocrRestTemplate.doOcr(pdfBytes);
    }

    private byte[] extractFirstPage(byte[] pdfBytes) throws IOException {
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            Splitter splitter = new Splitter();
            splitter.setStartPage(1);
            splitter.setEndPage(1);
            splitter.setSplitAtPage(1);

            List<PDDocument> pages = splitter.split(document);

            try (PDDocument firstPage = pages.get(0);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                PDDocumentInformation info = new PDDocumentInformation();
                firstPage.setDocumentInformation(info);
                firstPage.getDocumentCatalog().setMetadata(null);

                COSArray id = new COSArray();
                COSString fixed = new COSString("00000000000000000000000000000000");
                id.add(fixed);
                id.add(fixed);
                firstPage.getDocument().getTrailer().setItem(COSName.ID, id);

                firstPage.save(baos);
                return baos.toByteArray();
            }
        }
    }

    private byte[] convertToPdf(MultipartFile file) throws IOException {
        String contentType = file.getContentType();

        if ("application/pdf".equals(contentType)) {
            return file.getBytes();
        }

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(doc, page);

            if (contentType != null && (contentType.equals("image/png") || contentType.equals("image/jpeg"))) {
                PDImageXObject image = PDImageXObject.createFromByteArray(doc, file.getBytes(), file.getOriginalFilename());

                float pageWidth = page.getMediaBox().getWidth() - 50;
                float pageHeight = page.getMediaBox().getHeight() - 50;
                float scale = Math.min(pageWidth / image.getWidth(), pageHeight / image.getHeight());

                float imgWidth = image.getWidth() * scale;
                float imgHeight = image.getHeight() * scale;

                contentStream.drawImage(image, 25, page.getMediaBox().getHeight() - imgHeight - 25, imgWidth, imgHeight);
            } else {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText("File: " + file.getOriginalFilename() + " (cannot render preview)");
                contentStream.endText();
            }

            contentStream.close();

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                doc.save(out);
                return out.toByteArray();
            }
        }
    }
}
