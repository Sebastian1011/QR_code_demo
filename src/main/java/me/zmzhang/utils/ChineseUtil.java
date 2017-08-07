package me.zmzhang.utils;

/**
 * Created by zmzhang2 on 8/7/17.
 */

import java.io.*;
import java.util.Date;
import java.util.Random;

public class ChineseUtil {
    private static Random random = null;

    private static Random getRandomInstance() {
        if (random == null) {
            random = new Random(new Date().getTime());
        }
        return random;
    }

    public static String getChinese() {
        String str = null;
        int highPos, lowPos;
        Random random = getRandomInstance();
        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = 161 + Math.abs(random.nextInt(93));
        byte[] b = new byte[2];
        b[0] = (new Integer(highPos)).byteValue();
        b[1] = (new Integer(lowPos)).byteValue();
        try {
            str = new String(b, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getFixedLengthChinese(int length) {
        String str = "";
        for (int i = length; i > 0; i--) {
            str = str + getChinese();
        }
        return str;
    }

    public static String getRandomLengthChiness(int start, int end) {
        String str = "";
        int length = new Random().nextInt(end + 1);
        if (length < start) {
            str = getRandomLengthChiness(start, end);
        } else {
            for (int i = 0; i < length; i++) {
                str = str + getChinese();
            }
        }
        return str;
    }

    public static void main(String args[]) throws IOException {
        String path = new FileUtils().getFilePath("large.csv");
        for (int i = 0; i< 160000; i++){
            String appendStr = getFixedLengthChinese(11) + "," + NumberUtil.getFixLengthNumber(7).toString() + "," + NumberUtil.getFixLengthNumber(7).toString() + "\n";
            System.out.print(appendStr);
            File f1 = new File(path);
            if(!f1.exists()) {
                f1.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(f1.getName(),true);
            BufferedWriter bw = new BufferedWriter(fileWritter);
            bw.write(appendStr);
            bw.close();
        }
    }
}
