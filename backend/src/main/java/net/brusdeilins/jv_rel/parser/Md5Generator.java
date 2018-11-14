package net.brusdeilins.jv_rel.parser;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

public class Md5Generator {
    private static MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String md5(String data) {
        md5.update(data.getBytes());
        byte[] digest = md5.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
}
