package engineer.echo.logic;

/**
 * Middle.java
 * Info: 中级算法
 * Created by Plucky(plucky@echo.engineer) on 2020-01-12 - 22:05
 * more about me: http://www.1991th.com
 */
public class Middle {

    public static int maxProfit(int[] prices) {
        int len = prices.length;
        if (len == 0) return 0;
        int max = 0;
        for (int i = 1; i < len; i++) {
            if (prices[i] - prices[i - 1] > 0) {
                max += prices[i] - prices[i - 1];
            }
        }
        return max;
    }
}
