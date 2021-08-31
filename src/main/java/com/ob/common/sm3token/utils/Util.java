package com.ob.common.sm3token.utils;
import java.math.BigInteger;

/**
 * @描述
 * https://www.cnblogs.com/CYCLearning/p/12187688.html
 */
public class Util {
    public static final byte MAX_VALUE = -1;
    public static byte[] hexToByte(String arg8) {
        if(arg8.length() % 2 == 0) {
            char[] v0 = arg8.toCharArray();
            byte[] v1 = new byte[arg8.length() / 2];
            int v2 = 0;
            int v3 = 0;
            int v4 = arg8.length();
            while(v2 < v4) {
                StringBuilder v5 = new StringBuilder();
                v5.append("");
                int v6 = v2 + 1;
                v5.append(v0[v2]);
                v5.append(v0[v6]);
                v1[v3] = new Integer(Integer.parseInt(v5.toString(), 16) & 255).byteValue();
                v2 = v6 + 1;
                ++v3;
            }

            return v1;
        }
        throw new IllegalArgumentException();
    }
    public static String byteToHex(byte[] arg5) {
        if(arg5 != null) {
            String v0 = "";
            int v2;
            for(v2 = 0; v2 < arg5.length; ++v2) {
                String v1 = Integer.toHexString(arg5[v2] & 255);
                v0 = v1.length() == 1 ? v0 + "0" + v1 : v0 + v1;
            }

            return v0.toUpperCase();
        }
        throw new IllegalArgumentException("Argument b ( byte array ) is null! ");
    }

    public static byte[] byteConvert32Bytes(BigInteger arg5) {
        byte[] v0 = null;
        if(arg5 == null) {
            return v0;
        }

        int v4 = 32;
        if(arg5.toByteArray().length == 33) {
            v0 = new byte[v4];
            System.arraycopy(arg5.toByteArray(), 1, v0, 0, v4);
        }
        else if(arg5.toByteArray().length == v4) {
            v0 = arg5.toByteArray();
        }
        else {
            v0 = new byte[v4];
            int v1;
            for(v1 = 0; v1 < 32 - arg5.toByteArray().length; ++v1) {
                v0[v1] = 0;
            }

            System.arraycopy(arg5.toByteArray(), 0, v0, v4 - arg5.toByteArray().length, arg5.toByteArray().length);
        }

        return v0;
    }

    public static byte[] hexStringToBytes(String arg7) {
        if(arg7 != null) {
            if(arg7.equals("")) {
            }
            else {
                arg7 = arg7.toUpperCase();
                int v0 = arg7.length() / 2;
                char[] v1 = arg7.toCharArray();
                byte[] v2 = new byte[v0];
                int v3;
                for(v3 = 0; v3 < v0; ++v3) {
                    int v4 = v3 * 2;
                    v2[v3] = ((byte)(Util.charToByte(v1[v4]) << 4 | Util.charToByte(v1[v4 + 1])));
                }

                return v2;
            }
        }

        return null;
    }

    public static byte charToByte(char arg1) {
        return ((byte)"0123456789ABCDEF".indexOf(arg1));
    }

}