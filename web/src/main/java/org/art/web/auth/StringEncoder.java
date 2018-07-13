package org.art.web.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Password encoder (based on {@link BASE64Encoder})
 */
public class StringEncoder {

    private static final Logger LOG = LogManager.getLogger(StringEncoder.class);

    public static String encode(String str) {
        byte[] dig;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(str.getBytes());
            dig = md.digest();
            BASE64Encoder encoder = new BASE64Encoder();
            str = encoder.encode(dig);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("StringEncoder: NoSuchAlgorithmException during string encoding!", e);
        }
        return str;
    }
}
