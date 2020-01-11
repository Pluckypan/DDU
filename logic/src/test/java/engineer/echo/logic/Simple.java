package engineer.echo.logic;

/**
 * Simple.java
 * Info: 简单算法
 * Created by Plucky(plucky@echo.engineer) on 2020-01-11 - 20:52
 * more about me: http://www.1991th.com
 */
public class Simple {

    public static int removeDuplicates(int[] arr) {
        int i = 0;
        for (int j = 1; j < arr.length; j++) {
            if (arr[i] != arr[j]) {
                i++;
                arr[i] = arr[j];
            }
        }
        return i + 1;
    }
}
