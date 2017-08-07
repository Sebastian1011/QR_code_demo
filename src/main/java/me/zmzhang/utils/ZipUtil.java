package me.zmzhang.utils;

import com.google.zxing.WriterException;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by zmzhang2 on 8/7/17.
 */
public class ZipUtil {

    public static byte[] compress(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data.getBytes());
        gzip.close();
        byte[] compressed = bos.toByteArray();
        bos.close();
        return compressed;
    }

    public static String decompress(final byte[] compressed) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(bis);
        byte[] bytes = IOUtils.toByteArray(gis);
        return new String(bytes, "UTF-8");
    }

    public static String encode(String data) throws IOException {
        byte[] compressed = compress(data);
        String encoded = Base64.encode(compressed);
        return  encoded;
    }

    public static String decode(String data) throws IOException {
        byte[] compressed = new byte[0];
        try {
            compressed = Base64.decode(data);
        } catch (Base64DecodingException e) {
            e.printStackTrace();
        }
        String decode = decompress(compressed);
        return  decode;
    }

    public static void main(String[] args) throws IOException, WriterException {
        FileUtils fileUtils = new FileUtils();
        File file = new File("large.csv");
        String qrCodeData = FileUtils.getFileContent(new File("large.csv"));
        String encoded = ZipUtil.encode(qrCodeData);
        String decode = ZipUtil.decode(encoded);
        System.out.print(qrCodeData.getBytes().length);
        System.out.print("\n");
        System.out.print(encoded.getBytes().length);
        System.out.print("\n");
        System.out.print(decode.getBytes().length);
    }
}
