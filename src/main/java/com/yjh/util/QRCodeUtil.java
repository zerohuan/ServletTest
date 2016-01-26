package com.yjh.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.util.HashMap;

/**
 * Created by yjh on 16-1-18.
 */
public final class QRCodeUtil {
    private QRCodeUtil() {
    }
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final String FORMAT = "png";
    public static void saveQR(String text, String uid) throws Exception {
        HashMap<EncodeHintType, String> hints= new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        File outputFile = new File(ContextConstantUtil.getQrPicPath() + uid + "." + FORMAT);
        if (!outputFile.exists())
            outputFile.mkdirs();
            MatrixToImageWriter.writeToFile(bitMatrix, FORMAT, outputFile);
    }

    public static void main(String[] args) throws Exception {
        saveQR("http://www.baidu.com/", "1123");
    }
}
