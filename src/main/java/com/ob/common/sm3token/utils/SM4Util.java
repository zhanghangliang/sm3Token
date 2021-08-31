package com.ob.common.sm3token.utils;





import java.util.Base64;
import java.util.regex.Pattern;

/**
 * SM4工具
 */
public class SM4Util {
    public boolean hexString;
    public String iv;
    public String secretKey;

    /**
     * @描述 加密_ECB模式
     */
    public static  String getSm4EncryptECB(String input, String appSecret) {
        String password = StringToHexString(appSecret).substring(0, 32);
        SM4Util sm4 = new SM4Util();
        sm4.secretKey = password;
        sm4.hexString = true;

        String cipherText = sm4.encryptData_ECB(input);
        return cipherText.toUpperCase();
    }

    /**
     * @描述 解密_ECB模式
     */
    public static String getSm4DecryptECB(String input, String appSecret) {
        String password = StringToHexString(appSecret).substring(0, 32);
        SM4Util sm4 = new SM4Util();
        sm4.secretKey = password;
        sm4.hexString = true;

        return sm4.decryptData_ECB(input);
    }

    /**
     * @描述 加密_CBC模式
     */
    public static  String getSm4EncryptCBC(String input, String appSecret) {
        String password = StringToHexString(appSecret).substring(0, 32);
        SM4Util sm4 = new SM4Util();
        sm4.secretKey = password;
        sm4.hexString = true;

        String cipherText = sm4.encryptData_CBC(input);
        return cipherText.toUpperCase();
    }

    /**
     * @描述 解密_CBC模式
     */
    public static String getSm4DecryptCBC(String input, String appSecret) {
        String password = StringToHexString(appSecret).substring(0, 32);
        SM4Util sm4 = new SM4Util();
        sm4.secretKey = password;
        sm4.hexString = true;

        String cipherText = sm4.decryptData_CBC(input);
        return cipherText.toUpperCase();
    }

    public static String StringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    public String decryptData_CBC(String arg9) {
        byte[] v3;
        byte[] v2;
        try {
            arg9 = Base64.getEncoder().encodeToString(Util.hexToByte(arg9)/*,Base64.DEFAULT*/);
            if(arg9 != null && arg9.trim().length() > 0) {
                arg9 = Pattern.compile("\\s*|\t|\r|\n").matcher(((CharSequence)arg9)).replaceAll("");
            }

            SM4_Context v1 = new SM4_Context();
            v1.isPadding = true;
            v1.mode = 0;
            if(this.hexString) {
                v2 = Util.hexStringToBytes(this.secretKey);
                v3 = Util.hexStringToBytes(this.iv);
            }
            else {
                v2 = this.secretKey.getBytes();
                v3 = this.iv.getBytes();
            }

            SM4 v4 = new SM4();
            v4.sm4_setkey_dec(v1, v2);
            return new String(v4.sm4_crypt_cbc(v1, v3, Base64.getDecoder().decode(arg9/*,Base64.DEFAULT*/)), "UTF-8");
        }
        catch(Exception v0) {
            v0.printStackTrace();
            return null;
        }
    }

    public String encryptData_CBC(String arg7) {
        byte[] v2;
        byte[] v1;
        try {
            SM4_Context v0_1 = new SM4_Context();
            v0_1.isPadding = true;
            v0_1.mode = 1;
            if(this.hexString) {
                v1 = Util.hexStringToBytes(this.secretKey);
                v2 = Util.hexStringToBytes(this.iv);
            }
            else {
                v1 = this.secretKey.getBytes();
                v2 = this.iv.getBytes();
            }

            SM4 v3 = new SM4();
            v3.sm4_setkey_enc(v0_1, v1);
            return Util.byteToHex(v3.sm4_crypt_cbc(v0_1, v2, arg7.getBytes("UTF-8")));
        }
        catch(Exception v0) {
            v0.printStackTrace();
            return null;
        }
    }

    public String decryptData_ECB(String arg8) {
        try {
            arg8 = Base64.getEncoder().encodeToString(Util.hexToByte(arg8)/*,Base64.DEFAULT*/);
            if(arg8 != null && arg8.trim().length() > 0) {
                arg8 = Pattern.compile("\\s*|\t|\r|\n").matcher(((CharSequence)arg8)).replaceAll("");
            }

            SM4_Context v1 = new SM4_Context();
            v1.isPadding = true;
            v1.mode = 0;
            byte[] v2 = this.hexString ? Util.hexStringToBytes(this.secretKey) : this.secretKey.getBytes();
            SM4 v3 = new SM4();
            v3.sm4_setkey_dec(v1, v2);
            return new String(v3.sm4_crypt_ecb(v1, Base64.getDecoder().decode(arg8/*,Base64.DEFAULT*/)), "UTF-8");
        }
        catch(Exception v0) {
            v0.printStackTrace();
            return null;
        }
    }

    public String encryptData_ECB(String arg6) {
        try {
            SM4_Context v0_1 = new SM4_Context();
            v0_1.isPadding = true;
            v0_1.mode = 1;
            byte[] v1 = this.hexString ? Util.hexStringToBytes(this.secretKey) : Util.hexStringToBytes(this.secretKey);
            SM4 v2 = new SM4();
            v2.sm4_setkey_enc(v0_1, v1);
            return Util.byteToHex(v2.sm4_crypt_ecb(v0_1, arg6.getBytes("UTF-8")));
        }
        catch(Exception v0) {
            v0.printStackTrace();
            return null;
        }
    }

}