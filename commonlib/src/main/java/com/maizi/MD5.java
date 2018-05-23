package com.maizi;


import com.maizi.log.L;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * * Created by daixinglong on 2017/5/11.
 */

public class MD5 {

    public static final String calculateMD5(String str) {
        return calculateMD5(str.getBytes());
    }

    public static final String calculateMD5(byte[] data) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(data);
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                if (h.length() < 2) {
                    hexString.append('0');
                }
                hexString.append(h);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            L.e("MD5", "No Such Algorithm Exception", e);
        }
        return "";
    }

    public static final String calculateMD5(InputStream is) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();

            byte[] buffer = new byte[1024];
            int readCount = is.read(buffer);
            while (readCount != -1) {
                digest.update(buffer, 0, readCount);
                readCount = is.read(buffer);
            }
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                if (h.length() < 2) {
                    hexString.append('0');
                }
                hexString.append(h);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            L.e("MD5", "No Such Algorithm Exception", e);
        } catch (IOException e) {
            L.e("MD5", "IOException", e);
        }
        return "";

    }


    /**
     * 进行MD5加密算法
     *
     * @param str
     * @return
     */
    public static String stringMD5(String str) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(i));
            }
        } catch (Exception e) {
            return null;
            //          e.printStackTrace();
        }
        return sb.toString();
    }
}
