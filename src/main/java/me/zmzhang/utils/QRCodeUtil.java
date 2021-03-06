package me.zmzhang.utils;

/**
 * Created by zmzhang2 on 8/5/17.
 */
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * 二维码工具类
 */
public class QRCodeUtil {
    private static final int width = 300;// 默认二维码宽度
    private static final int height = 300;// 默认二维码高度
    private static final String format = "png";// 默认二维码文件格式
    private int argb = 0xff000000; // 二维码颜色（ARGB）(默认值：0xff000000)
    private static final Map<EncodeHintType, Object> hints = new HashMap();// 二维码参数
    private static final Map<DecodeHintType, Object> decodeHints = new HashMap();

    static {
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 字符编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 容错等级 L、M、Q、H 其中 L 为最低, H 为最高
        hints.put(EncodeHintType.MARGIN, 2);// 二维码与图片边距
        hints.put(EncodeHintType.QR_VERSION, 40); //二维码数据量
    }
    static {
        decodeHints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        decodeHints.put(DecodeHintType.TRY_HARDER, true);
    }
    /**
     * 返回一个 BufferedImage 对象
     * @param content 二维码内容
     * @param width   宽
     * @param height  高
     */
    public static BufferedImage toBufferedImage(String content, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    /**
     * 将二维码图片输出到一个流中
     * @param content 二维码内容
     * @param stream  输出流
     * @param width   宽
     * @param height  高
     */
    public static void writeToStream(String content, OutputStream stream, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
    }
    /**
     * 生成二维码图片文件
     * @param content 二维码内容
     * @param path    文件保存路径
     * @param width   宽
     * @param height  高
     */
    public static void createQRCode(String content, String path, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        //toPath() 方法由 jdk1.7 及以上提供
        MatrixToImageWriter.writeToPath(bitMatrix, format, new File(path).toPath());
    }

    /**
     * 二维码图片解码
     * @param filename 图片路径
     */
    public static String decoder(String filename) throws NotFoundException, IOException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filename)))));
        Result result = new MultiFormatReader().decode(binaryBitmap, decodeHints);
        return result.getText();
    }
    /**
     * 一图多个二维码图片解码
     * @param filename 图片路径
     */
    public static String multiDecoder(String filename) throws IOException, FormatException, ChecksumException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filename)))));
        Result[] result = new QRCodeMultiReader().decodeMultiple(binaryBitmap, decodeHints);
        String data = "";
        for (Result item: result){
            data += item.getText();
        }
        return data;
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        // String filePath = "QRCode.png";
        // String qrCodeData = FileUtils.getFileContent(new File("test.csv"));
        // createQRCode(qrCodeData, filePath, 300, 300);
        // String filePath = "single.png";
//        try{
//            String filePath = "images/19.jpg";
//            String result = multiDecoder(filePath);
//            System.out.print(result);
//            System.out.print("found");
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.print("not found");
//        }
//        System.out.println("QR Code image created successfully!");
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec("./video_to_image.sh");
        pr.waitFor();
        String folderPath = "images";
        File folder = new File(folderPath);
        File[]  fileList = folder.listFiles();
        List<File> fileArray = Arrays.asList(fileList);
        Collections.sort(fileArray, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int str1 = Integer.parseInt(o1.getName().split("\\.")[0]);
                int str2 = Integer.parseInt(o2.getName().split("\\.")[0]);
                if(str1 > str2){
                    return 1;
                }else if (str1 < str2){
                    return -1;
                }else {
                    return  0;
                }
            }
        });
        String result = "";
        String temp = "";
        for (File file : fileArray){
            if (file.isFile()){
                System.out.print(file.getName());
                String current = "";
                try{
                    current = multiDecoder(file.getPath());
                } catch (FormatException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ChecksumException e) {
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
                System.out.print(current+"_text");
                if(!temp.equals(current)){
                    temp = current;
                    result += current;
                }
            }
        }
        System.out.print(result);
        FileUtils.writeStringToFile(new File("5471.txt"), result);
    }
}
