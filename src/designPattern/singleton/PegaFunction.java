package designPattern.singleton;

public class PegaFunction {

    public static void main(String[] args) {
        String docNo = "2L020500900000000120000901";
        System.out.println(docNo.length());
        System.out.println(getNextDocNo(docNo));
    }

    public static String getNextDocNo(String docNo) {
        int indexNo = Integer.parseInt(docNo.substring(22, 24)) + 1;
        String nextNo = "";
        if (indexNo < 10) {
            nextNo = docNo.substring(0, 23) + indexNo + docNo.substring(24);
        } else {
            nextNo = docNo.substring(0, 22) + indexNo + docNo.substring(24);
        }
        return nextNo;
    }
}
