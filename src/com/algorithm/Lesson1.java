package com.algorithm;

public class Lesson1 {
    private static final int[] question = new int[] { 1, -2, 3, 10, -4, 7, 2, -5 };

    public static void main(String[] args) {
        Lesson1 lesson = new Lesson1();
        System.out.println(lesson.getMaxSon());
    }

    private int getMaxSon() {
        int maxsum = question[0];
        int currentSum;
        // i:数组开始下标
        for (int i = 1; i < question.length; i++) {
            // j:j必须在i后面，比如第二次循环时从数组第二位开始
            for (int j = i; j < question.length; j++) {
                currentSum = 0;
                // k:最大长度
                for (int k = i; k < j; k++) {
                    currentSum += question[k];
                }
                if (currentSum > maxsum) {
                    maxsum = currentSum;
                }
            }
        }
        return maxsum;
    }

}
