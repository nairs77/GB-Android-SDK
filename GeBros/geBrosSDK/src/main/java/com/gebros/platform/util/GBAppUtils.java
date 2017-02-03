package com.gebros.platform.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.gebros.platform.exception.GBException;
import com.gebros.platform.log.GBLog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by gebros.nairs77@gmail.com on 5/18/16.
 */
public class GBAppUtils {

    private static final String TAG = String.format("[" + GBAppUtils.class.getCanonicalName() + "]");

    public static final String getKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);

                GBLog.d(TAG + "KeyHash:%s", keyHash);
                return keyHash;
            }

        } catch (PackageManager.NameNotFoundException e) {
            GBLog.d(TAG + "getKeyHas Error:%s", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            GBLog.d(TAG + "getKeyHas Error:%s", e.getMessage());
        }
        return "";
    }

    public static String getMetadata(Context context, String property) {

        GBValidator.notNull(context, "context");

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                return ai.metaData.getString(property);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // if we can't find it in the manifest, just return null
        }

        return null;
    }

    public static String hashWithAlgorithm(String algorithm, String key) {
        MessageDigest hash = null;
        try {
            hash = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        hash.update(key.getBytes());
        byte[] digest = hash.digest();
        StringBuilder builder = new StringBuilder();
        for (int b : digest) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString((b >> 0) & 0xf));
        }
        return builder.toString();
    }

    public static String enCrypt(String param, String cryptkey) throws Exception {
        if(param == null || cryptkey == null) throw new GBException("string : null or keystring : null");

        byte[] xor = XorCrypt(param.getBytes(), cryptkey);
        return byteArrayToHex(xor);
    }

    public static String deCrypt(String param, String cryptkey) throws Exception {
        if(param == null || cryptkey == null) throw new GBException("string : null or keystring : null");

        byte[] xor = XorCrypt(hexToByteArray(param), cryptkey);
        return new String(xor, "UTF-8");
    }

    public static byte[] XorCrypt(byte[] bytes, String cryptkey) throws GBException {
        int cryptLength = cryptkey.length();
        byte[] res = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            char cha = (char)(bytes[i]^cryptkey.charAt(i%cryptLength));
            res[i] = (byte)cha;
        }
        return res;
    }

    /**
     * hex to byte[] : 16진수 문자열을 바이트 배열로 변환한다.
     *
     * @param hex
     *            hex string
     * @return
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }

    /**
     * byte[] to hex : unsigned byte(바이트) 배열을 16진수 문자열로 바꾼다.
     *
     * @param ba
     *        byte[]
     * @return
     */
    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }
}
