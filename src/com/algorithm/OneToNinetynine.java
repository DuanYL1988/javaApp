package com.algorithm;

import java.util.Date;

import com.application.util.DateTimeUtil;

public class OneToNinetynine {

    public int[] numList;

    public OneToNinetynine(int start, int count) {
        numList = new int[count];
        for (int i = 0; i < count; i++) {
            numList[i] = start + i;
        }
    }

    public static void main(String[] args) {
        OneToNinetynine target = new OneToNinetynine(1, 99);

        System.out.println("循环求和：" + target.getSum01());

        System.out.println("算法求和：" + target.getSum02());
    }

    private int getSum01() {
        Date start = new Date();
        int sum = 0;
        for (int i = 0; i < numList.length; i++) {
            sum += numList[i];
        }
        Date end = new Date();
        DateTimeUtil.getMs(start, end);
        return sum;
    }

    private int getSum02() {
        Date start = new Date();
        int sum = 0;
        if (numList.length % 2 == 0) {
            int onePare = numList[0] + numList[numList.length - 1];
            int pareCount = numList.length / 2;
            sum = onePare * pareCount;
        } else {
            int mid = numList.length / 2;
            int onePare = numList[0] + numList[numList.length - 1];
            int pareCount = numList.length / 2;
            sum = onePare * pareCount;
            sum += numList[mid];
        }
        Date end = new Date();
        DateTimeUtil.getMs(start, end);
        return sum;
    }

}
