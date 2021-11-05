import java.math.BigDecimal;
import java.text.NumberFormat;

public class Function {

    public static void main(String[] args) {
        String testRst = taxCalculation("1", "1", "1", new BigDecimal("515"), new BigDecimal("616"));
        System.out.println(testRst);
    }

    public static String taxCalculation(String autoKbn, String conTaxKbn, String conTaxRateKbn,
            BigDecimal agreementAmont, BigDecimal conAmont) {
        BigDecimal result = BigDecimal.ZERO;
        if ("2".equals(autoKbn)) {
            if ("1".equals(conTaxKbn)) {
                result = conAmont;
            } else {
                result = BigDecimal.ZERO;
            }
        } else {
            if (!"1".equals(conTaxKbn)) {
                result = BigDecimal.ZERO;
            } else {
                if ("1".equals(conTaxRateKbn)) {
                    result = agreementAmont.multiply(new BigDecimal("0.03"));

                } else if ("2".equals(conTaxRateKbn)) {
                    result = agreementAmont.multiply(new BigDecimal("0.05"));

                } else if ("3".equals(conTaxRateKbn)) {
                    result = agreementAmont.multiply(new BigDecimal("0.08"));

                } else if ("4".equals(conTaxRateKbn)) {
                    result = agreementAmont.multiply(new BigDecimal("0.1"));

                } else {
                    result = BigDecimal.ZERO;
                }
            }
        }

        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMaximumFractionDigits(13);
        return fmt.format(result);
    }
}
