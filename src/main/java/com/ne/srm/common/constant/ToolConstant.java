package com.ne.srm.common.constant;

import com.google.zxing.BarcodeFormat;

import java.util.Arrays;
import java.util.List;

public class ToolConstant {

    // 二维码（矩阵码）
    public static final List<BarcodeFormat> QR_2D_FORMATS = Arrays.asList(
            BarcodeFormat.AZTEC,
            BarcodeFormat.DATA_MATRIX,
            BarcodeFormat.MAXICODE,
            BarcodeFormat.PDF_417,
            BarcodeFormat.QR_CODE
    );

    // 一维码（条形码）
    public static final List<BarcodeFormat> BAR_1D_FORMATS = Arrays.asList(
            BarcodeFormat.CODABAR,
            BarcodeFormat.CODE_39,
            BarcodeFormat.CODE_93,
            BarcodeFormat.CODE_128,
            BarcodeFormat.EAN_8,
            BarcodeFormat.EAN_13,
            BarcodeFormat.ITF,
            BarcodeFormat.RSS_14,
            BarcodeFormat.RSS_EXPANDED,
            BarcodeFormat.UPC_A,
            BarcodeFormat.UPC_E,
            BarcodeFormat.UPC_EAN_EXTENSION
    );
}
