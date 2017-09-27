package org.art.web.auth;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Password encoder (by means of {@link BASE64Encoder})
 */
public class Encoder {

    public static String encode(String str) {
        byte[] dig = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(str.getBytes());
            dig = md.digest();
            BASE64Encoder encoder = new BASE64Encoder();
            str = encoder.encode(dig);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }
}
