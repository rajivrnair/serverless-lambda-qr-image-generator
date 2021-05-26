package com.serverless;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Modified from https://aboullaite.me/generate-qrcode-with-logo-image-using-zxing/
 */
public class QRCodeGenerator {
    private static final Logger LOG = Logger.getLogger(QRCodeGenerator.class);

    public byte[] generate(String encodedContent) {
        String content = new String(Base64.getDecoder().decode(encodedContent), StandardCharsets.UTF_8);
        LOG.info("decoded content: " + content);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            // Create a qr code with the content and a size of WxH px
            BufferedImage qrImage = createQRImage(content);

            // Write image as PNG to OutputStream
            ImageIO.write(qrImage, "png", os);
            return os.toByteArray();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<EncodeHintType, ErrorCorrectionLevel> getHints() {
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        return hints;
    }

    private BufferedImage createQRImage(String content) throws WriterException {
        int qrDimension = 300;
        // Create new configuration that specifies the error correction
        Map<EncodeHintType, ErrorCorrectionLevel> hints = getHints();
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, qrDimension, qrDimension, hints);

        // Load QR image
        MatrixToImageConfig matrixConfig = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF);
        return MatrixToImageWriter.toBufferedImage(bitMatrix, matrixConfig);
    }
}
