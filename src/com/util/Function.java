package com.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;

public class Function {

    public static void main(String[] args) {
        // 2.469135780246
        String testRst = taxCalculation("1", "1", "2", new BigDecimal("2.469135780246"), new BigDecimal("616"));
        System.out.println(testRst);
        String testRst2 = taxCalculation("1", "1", "2", new BigDecimal("24691357802.46"), new BigDecimal("616"));
        System.out.println(testRst2);
        String testRst3 = taxCalculation("1", "1", "2", new BigDecimal("400"), new BigDecimal("616"));
        System.out.println(testRst3);
    }

    /**
     *
     * 約定金額に対する消費税額を算出する
     *
     * @param autoKbn        消費税自動計算区分
     * @param conTaxKbn      消費税課税区分
     * @param conTaxRateKbn  消費税率区分
     * @param agreementAmont 約定金額
     * @param conAmont       消費税額
     *
     * @return 消費税計算‐ＦＲＴ
     */
    public static String taxCalculation(String autoKbn, String conTaxKbn, String conTaxRateKbn,
            BigDecimal agreementAmont, BigDecimal conAmont) {
        BigDecimal result = BigDecimal.ZERO;
        // 消費税自動計算区分 = "2"
        if ("2".equals(autoKbn)) {
            // 消費税課税区分 = "1"
            if ("1".equals(conTaxKbn)) {
                result = conAmont;
            } else {
                result = BigDecimal.ZERO;
            }
        } else {
            // 消費税課税区分 ≠ "1"
            if (!"1".equals(conTaxKbn)) {
                result = BigDecimal.ZERO;
            } else {
                if ("1".equals(conTaxRateKbn)) {
                    // 消費税率区分 = 1
                    result = agreementAmont.multiply(new BigDecimal("0.03"), MathContext.DECIMAL64);

                } else if ("2".equals(conTaxRateKbn)) {
                    // 消費税率区分 = 2
                    result = agreementAmont.multiply(new BigDecimal("0.05"), MathContext.DECIMAL64);

                } else if ("3".equals(conTaxRateKbn)) {
                    // 消費税率区分 = 3
                    result = agreementAmont.multiply(new BigDecimal("0.08"), MathContext.DECIMAL64);

                } else if ("4".equals(conTaxRateKbn)) {
                    // 消費税率区分 = 4
                    result = agreementAmont.multiply(new BigDecimal("0.1"), MathContext.DECIMAL64);

                } else {
                    result = BigDecimal.ZERO;
                }
            }
        }

        // 小数点以下桁数取得
        String strResult = result.toString();
        int preNumLen = strResult.split("\\.")[0].length();
        int digits = 13 - preNumLen > 0 ? 13 - preNumLen : 0;

        if (strResult.length() > 13) {
            strResult = strResult.substring(0, 13);
        }

        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMaximumFractionDigits(digits);
        strResult = fmt.format(result);
        return strResult;
    }
}
