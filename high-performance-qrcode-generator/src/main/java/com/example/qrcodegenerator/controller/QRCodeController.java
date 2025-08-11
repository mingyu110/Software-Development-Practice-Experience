package com.example.qrcodegenerator.controller;

import com.example.qrcodegenerator.service.QRCodeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(
            @RequestParam("text") String text,
            @RequestParam(value = "size", defaultValue = "256") int size) {
        try {
            byte[] image = qrCodeService.generateQRCodeImage(text, size, size);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
        } catch (Exception e) {
            // A more robust error handling would be better in a real application
            return ResponseEntity.internalServerError().build();
        }
    }
}
