package com.service;

import java.math.BigDecimal;

public class BuyHome {

    private static Integer YEAR = 5;
    
    private static int BREAK = 0;
    
    /**
     * 贷款金额
     */
    private static BigDecimal MONEY = new BigDecimal("500000");
    
    /**
     * 利率
     */
    private static BigDecimal RATE = new BigDecimal("0.004083");
    
    /**
     * 欠款
     */
    private static BigDecimal subtraction;
    
    /**
     * 利息
     */
    private static BigDecimal interest;
    
    public static void main(String[] args) {
        System.out.print("贷款"+YEAR+"年;");
        int month = YEAR * 12;
        System.out.print("共"+month+"期;");
        BigDecimal average = MONEY.divide(new BigDecimal(month),2,BigDecimal.ROUND_UP);
        System.out.println("每期还本金"+average+"元;");
        subtraction = MONEY;
        BigDecimal total = new BigDecimal("0");
        int count=1;
        for(int i=month;i>0;i--){
            System.out.print("第"+count+"期:还本金 "+average+"元;");
            subtraction = subtraction.subtract(average);
            System.out.print("还欠"+subtraction+"元;");
            interest = subtraction.multiply(RATE).setScale(2,BigDecimal.ROUND_UP);
            System.out.print("还利息 "+interest+"元;");
            BigDecimal current = average.add(interest);
            System.out.print("当月共还款"+current+"元;");
            total = total.add(current);
            System.out.println("总还款"+total+"元;");
            if(BREAK>0 && count==BREAK*12 && BREAK<YEAR){
                System.out.println("一次性付清总还款"+total.add(subtraction));
                break;
            }
            count++;
        }
    }
}
