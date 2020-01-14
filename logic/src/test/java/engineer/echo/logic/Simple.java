package engineer.echo.logic;

/**
 * Simple.java
 * Info: 简单算法
 * Created by Plucky(plucky@echo.engineer) on 2020-01-11 - 20:52
 * more about me: http://www.1991th.com
 */
public class Simple {

    public static int removeDuplicates(int[] arr) {
        if (arr.length < 1) return 0;
        int i = 0;
        for (int j = 1; j < arr.length; j++) {
            if (arr[i] != arr[j]) {
                i++;
                arr[i] = arr[j];
            }
        }
        return i + 1;
    }

    public static int maxProfit1(int[] prices) {
        int len = prices.length;
        if (len < 1) return 0;
        int max = 0;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (prices[j] - prices[i] > max) {
                    max = prices[j] - prices[i];
                }
            }
        }
        return max;
    }

    public static int maxProfit2(int[] prices) {
        int len = prices.length;
        if (len < 1) return 0;
        int max = 0;
        int min = prices[0];
        for (int i = 1; i < len; i++) {
            if (prices[i] < min) {
                min = prices[i];
            }
            if (prices[i] - min > max) {
                max = prices[i] - min;
            }
        }
        return max;
    }

    public static int[] rotateArray(int[] arr, int shift) {
        int len = arr.length;
        if (len < 2 || shift <= 0) return arr;
        int s = shift % len;
        if (s == 0) return arr;
        int[] temp = new int[arr.length * 2];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = arr[i % len];
        }
        for (int i = len - s; i < 2 * len - s; i++) {
            arr[i - (len-s)] = temp[i];
        }
        return arr;
    }
}
