package com.ob.common.sm3token.utils;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

public class SM3Util {
    //生成秘钥
    public static String GetSm3Str(String arg5) {
        byte[] v0 = new byte[32];
        byte[] v1 = arg5.getBytes();
        SM3Digest v2 = new SM3Digest();
        v2.update(v1, 0, v1.length);
        v2.doFinal(v0, 0);
        return new String(Hex.encode(v0)).toUpperCase();
    }
}