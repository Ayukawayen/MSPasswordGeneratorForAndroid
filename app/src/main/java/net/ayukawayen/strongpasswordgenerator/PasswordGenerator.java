package net.ayukawayen.strongpasswordgenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordGenerator {
    static MessageDigest md = null;
    public static byte[] gen(String mKey, String sKey) {
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return digestToBytes(mKey + sKey);
        }

        String str;

        str = digestToHex(mKey, ")!@#$%^&*(ABCDEFG");
        str = digestToHex(str, ")!@#$%^&*(ABCDEFG");

        return digestToBytes(str+sKey);
    }

    public static String baseConvert64(byte[] bytes, String base64Codec) {
        if(bytes.length <= 0) return "";

        String[] chars = base64Codec.split("");
        String result = "";

        int buf = 0;
        int bitLen = 0;

        for(int i=bytes.length-1; i>=0; --i) {
            buf |= (0x00ff & bytes[i]) << bitLen;
            bitLen += 8;

            while(bitLen >= 6) {
                result = chars[buf&0x3f] + result;

                buf >>= 6;
                bitLen -= 6;
            }
        }

        result = chars[buf&0x3f] + result;
        return result;
    }

    static byte[] digestToBytes(String str) {
        if(md == null) return str.getBytes();

        return md.digest(str.getBytes());
    }

    static String digestToHex(String str, String hexCodec) {
        if(md == null) return str;

        String[] hexChars = hexCodec.split("");

        String result = "";
        byte[] digest = md.digest(str.getBytes());
        for(byte b : digest) {
            result += hexChars[(b>>4)&0xf] + hexChars[b&0xf];
        }
        return result;
    }


}
