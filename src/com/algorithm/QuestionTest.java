package com.algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.application.util.DateTimeUtil;

/**
 * 算法刷题
 */
public class QuestionTest {

    public static void main(String[] args) {
        // 开始时间
        String startTime = DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_HMS_S);
        Date stTime = new Date();
        System.out.println("开始:" + startTime);

        // 实际处理
        // twoSum(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, 99);
        // lengthOfLongestSubstring("dabcabcbb");
        maxArea(new Integer[] { 1, 8, 6, 2, 5, 4, 8, 3, 7 });

        // 结束时间
        String endTime = DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_HMS_S);
        Date edTime = new Date();
        System.out.println("结束:" + endTime);
        System.out.println("耗时:" + DateTimeUtil.getUseTime(stTime, edTime) + "ms");
    }

    /**
     * 在一堆数组中任意两张组合,得到目标数字
     *
     * 思路 1.循环数组,取得当前值,依次相加值.把结果放入Map中,得到所有的可能 这样做无视了目标数字取得所有可能结果.
     * 2.在不求多种可能新情况在,在循环中途判断和目标数字相等当即返回并结束处理 3.若要得到各种可能性的组合,则不结束循环,将符合的结果放入新的结果集合中
     *
     */
    public static Map<String, Integer> twoSum(Integer[] poker, int targetNum) {
        // 可能性Map,java中数组无法改变长度,使用String进行拼接做Key
        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        for (int i = 0; i < poker.length; i++) {
            for (int j = i + 1; j < poker.length; j++) {
                String key = String.valueOf(i + 1 + "," + (j + 1));
                int result = poker[i] + poker[j];
                resultMap.put(key, result);
                if (result == targetNum) {
                    System.out.println("出第" + key + "张牌");
                    // return resultMap;
                }
            }
        }
        return resultMap;
    }

    /**
     * 在⼀个字符串重寻找没有重复字⺟的最⻓⼦串
     *
     * @param targetText
     */
    public static String lengthOfLongestSubstring(String targetText) {
        String wd = "";
        for (int startIndex = 0; startIndex < targetText.length() - 1; startIndex++) {
            for (int endIndex = targetText.length() - 1; endIndex > startIndex; endIndex--) {
                wd = targetText.substring(startIndex, endIndex);
                String[] splitArr = targetText.split(wd);
                if (splitArr.length > 2) {
                    System.out.println(wd);
                    return wd;
                }
            }
        }
        return wd;
    }

    public static int maxArea(Integer[] height) {
        int area = 0;
        for (int i = 1; i < height.length - 1; i++) {
            for (int j = i + 1; j < height.length; j++) {
                // 高度
                int x = j - i;
                int y = height[j] < height[i] ? height[j] : height[i];
                area = area > x * y ? area : x * y;
            }
        }
        System.out.println(area);
        return area;
    }
}
