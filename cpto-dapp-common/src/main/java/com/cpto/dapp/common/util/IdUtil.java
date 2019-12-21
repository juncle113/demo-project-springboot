package com.cpto.dapp.common.util;

import java.util.Random;

/**
 * id生成
 *
 * @author sunli
 * @date 2018/12/27
 */
public class IdUtil {
    private static final char[] BASE_NUMBER = "0123456789".toCharArray();
    private static final char[] BASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
//    /**
//     * 各种随机名称生成
//     */
//    public static String genTagName()
//    {
//        // 取当前时间的长整形值包含毫秒
//        long millis = System.currentTimeMillis();
//        // 加上三位随机数
//        Random random = new Random();
//        int end3 = random.nextInt(999);
//        // 如果不足三位前面补0
//        String str = millis + String.format("%03d", end3);
//
//        return str;
//    }
//
//    /**
//     * 各种高精度随机名称生成
//     */
//    public static String genHighTagName()
//    {
//        // 取当前时间的长整形值包含毫秒
//         long nano = System.nanoTime();
//        // 加上三位随机数
//        Random random = new Random();
//        int end3 = random.nextInt(999);
//        // 如果不足三位前面补0
//        String str = nano + String.format("%03d", end3);
//
//        return str;
//    }

    /**
     * 根据当前时间随机生成id
     */
    public static Long generateIdByCurrentTime() {
        // 取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        // 加上随机数
        Random random = new Random();
        int postfix = random.nextInt(99);
        // 如果不足两位前面补0
        return Long.valueOf(millis + String.format("%02d", postfix));
    }

    /**
     * 随机生成指定位数的编码（包括英文字母和数字）
     *
     * @param length 指定位数
     * @return 随机编码
     */
    public static String generateCode(int length) {
        Random rand = new Random();
        StringBuffer code = new StringBuffer();

        for (int i = 0; i < length; i++) {
            code.append(BASE_CHARS[rand.nextInt(BASE_CHARS.length)]);
        }

        return code.toString();
    }

    /**
     * 随机生成指定位数的数字编码
     *
     * @param length 指定位数
     * @return 随机编码
     */
    public static String generateNumberCode(int length) {
        Random rand = new Random();
        StringBuffer code = new StringBuffer();

        for (int i = 0; i < length; i++) {
            code.append(BASE_NUMBER[rand.nextInt(BASE_NUMBER.length)]);
        }

        return code.toString();
    }

//    /**
//     * 生成高精度id
//     */
//    public static long generateHighId()
//    {
//        // 取当前时间的长整形值包含纳秒
//        long nano = System.nanoTime();
//        // 加上两位随机数
//        Random random = new Random();
//        int end2 = random.nextInt(99);
//        // 如果不足两位前面补0
//        String str = nano + String.format("%02d", end2);
//
//        return Long.valueOf(str);
//    }
//
//    /**
//     * 生成指定长度的字母或数字组合的字符串
//     * @param length 生成字符串的长度
//     * @return
//     */
//    public static String getRandomText(int length)
//    {
//        Random rand = new Random();
//        StringBuffer text = new StringBuffer();
//        for (int i = 0; i < length; i++)
//        {
//            text.append(BASE_CHARS[rand.nextInt(BASE_CHARS.length)]);
//        }
//
//        return text.toString();
//    }
//
//    /**
//     * 生成指定长度的数字随机字符串
//     * @param length 生成数字字符串的长度
//     * @return
//     */
//    public static String getRandomNumbers(int length)
//    {
//        Random rand = new Random();
//        StringBuffer numbers = new StringBuffer();
//        for (int i = 0; i < length; i++)
//        {
//            numbers.append(baseNumbers[rand.nextInt(baseNumbers.length)]);
//        }
//
//        return numbers.toString();
//    }
//
//    public static void main(String[] args)
//    {
//        System.out.println("generate Ids Begin");
//        for (int i = 0; i < 2; i++)
//        {
//            System.out.println(generateId());
//            System.out.println(generateHighId());
//        }
//        System.out.println(generateId());
//        System.out.println("generate Ids End");
//    }
}