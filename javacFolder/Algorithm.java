import java.util.*;

/**
 * 算法刷题
 */
public class Algorithm {

    public static void main(String[] args) {
      Integer[] poker = [1,2,3,4,5,6,7,8,9,10];
    }

    /**
     * 在一堆数组中任意组合(1张-N张),得到目标数字
     *
     * 思路
     * 1.循环数组,取得当前值,依次相加值.把结果放入Map中,这样得到所有的可能
     *   这样做无视了目标数字取得所有可能结果.
     * 2.在不求多种可能新情况在,在循环中途判断和目标数字相等当即返回并结束处理
     * 3.若要得到各种可能性的组合,则不结束循环,将符合的结果放入新的结果集合中
     *
     */
    public static List<> getTargetNmInArrs(Integer[] poker,int targetNum){
        // 可能性Map,java中数组无法改变长度,使用String进行拼接做Key
        Map<String,int> resultMap = new HashMap<String,int>
        for(int i = 0;i<poker.length;i++) {
            int result = poker[i];
            for(int j = i;i<poker.length;j++) {
                String key = i;
                if(j>i) {
                  key += "," + j;
                  result = result + poker[j];
                }
                resultMap.put(key,result);
            }
        }
    }

}
