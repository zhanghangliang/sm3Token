package com.ob.common.sm3token.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SM4 {
	public static final int[] CK = new int[]{462357, 472066609, 943670861, 1415275113, 1886879365, -1936483679, -1464879427, -993275175, -521670923, -66909679, 404694573, 876298825, 1347903077, 1819507329, -2003855715, -1532251463, -1060647211, -589042959, -117504499, 337322537, 808926789, 1280531041, 1752135293, -2071227751, -1599623499, -1128019247, -656414995, -184876535, 269950501, 741554753, 1213159005, 1684763257};
	public static final int[] FK = new int[]{-1548633402, 1453994832, 1736282519, -1301273892};
	public static final int SM4_DECRYPT = 0;
	public static final int SM4_ENCRYPT = 1;
	public static final byte[] SboxTable = new byte[]{-42, -112, -23, -2, -52, -31, 61, -73, 22, -74, 20, -62, 40, -5, 44, 5, 43, 103, -102, 118, 42, -66, 4, -61, -86, 68, 19, 38, 73, -122, 6, -103, -100, 66, 80, -12, -111, -17, -104, 122, 51, 84, 11, 67, -19, -49, -84, 98, -28, -77, 28, -87, -55, 8, -24, -107, -128, -33, -108, -6, 117, -113, 63, -90, 71, 7, -89, -4, -13, 115, 23, -70, -125, 89, 60, 25, -26, -123, 79, -88, 104, 107, -127, -78, 113, 100, -38, -117, -8, -21, 15, 75, 112, 86, -99, 53, 30, 36, 14, 94, 99, 88, -47, -94, 37, 34, 124, 59, 1, 33, 120, -121, -44, 0, 70, 87, -97, -45, 39, 82, 76, 54, 2, -25, -96, -60, -56, -98, -22, -65, -118, -46, 64, -57, 56, -75, -93, -9, -14, -50, -7, 97, 21, -95, -32, -82, 93, -92, -101, 52, 26, 85, -83, -109, 50, 48, -11, -116, -79, -29, 29, -10, -30, 46, -126, 102, -54, 96, -64, 41, 35, -85, 13, 83, 78, 111, -43, -37, 55, 69, -34, -3, -114, 47, 3, -1, 106, 114, 109, 108, 91, 81, -115, 27, -81, -110, -69, -35, -68, 127, 17, -39, 92, 65, 31, 16, 90, -40, 10, -63, 49, -120, -91, -51, 123, -67, 45, 116, -48, 18, -72, -27, -76, -80, -119, 105, -105, 74, 12, -106, 119, 126, 101, -71, -15, 9, -59, 110, -58, -124, 24, -16, 125, -20, 58, -36, 77, 32, 121, -18, 95, 62, -41, -53, 57, 72};

	public SM4() {
		super();
	}

	private long GET_ULONG_BE(byte[] arg7, int arg8) {
		return (((long)(arg7[arg8] & 255))) << 24 | (((long)((arg7[arg8 + 1] & 255) << 16))) | (((long)((arg7[arg8 + 2] & 255) << 8))) | (((long)(arg7[arg8 + 3] & 255))) & 4294967295L;
	}

	private void PUT_ULONG_BE(long arg7, byte[] arg9, int arg10) {
		arg9[arg10] = ((byte)(((int)(arg7 >> 24 & 255))));
		arg9[arg10 + 1] = ((byte)(((int)(arg7 >> 16 & 255))));
		arg9[arg10 + 2] = ((byte)(((int)(arg7 >> 8 & 255))));
		arg9[arg10 + 3] = ((byte)(((int)(arg7 & 255))));
	}

	private long ROTL(long arg5, int arg7) {
		return this.SHL(arg5, arg7) | arg5 >> 32 - arg7;
	}

	private long SHL(long arg3, int arg5) {
		return (-1 & arg3) << arg5;
	}

	private void SWAP(long[] arg5, int arg6) {
		long v0 = arg5[arg6];
		arg5[arg6] = arg5[31 - arg6];
		arg5[31 - arg6] = v0;
	}

	private byte[] padding(byte[] arg6, int arg7) {
		int v1;
		byte[] v0 = null;
		if(arg6 == null) {
			return v0;
		}

		if(arg7 == 1) {
			v1 = 16 - arg6.length % 16;
			v0 = new byte[arg6.length + v1];
			System.arraycopy(arg6, 0, v0, 0, arg6.length);
			int v2;
			for(v2 = 0; v2 < v1; ++v2) {
				v0[arg6.length + v2] = ((byte)v1);
			}
		}
		else {
			v1 = arg6[arg6.length - 1];
			v0 = new byte[arg6.length - v1];
			System.arraycopy(arg6, 0, v0, 0, arg6.length - v1);
		}

		return v0;
	}

	private long sm4CalciRK(long arg11) {
		byte[] v5 = new byte[4];
		byte[] v4 = new byte[4];
		this.PUT_ULONG_BE(arg11, v5, 0);
		v4[0] = this.sm4Sbox(v5[0]);
		v4[1] = this.sm4Sbox(v5[1]);
		v4[2] = this.sm4Sbox(v5[2]);
		v4[3] = this.sm4Sbox(v5[3]);
		long v0 = this.GET_ULONG_BE(v4, 0);
		return this.ROTL(v0, 13) ^ v0 ^ this.ROTL(v0, 23);
	}

	private long sm4F(long arg3, long arg5, long arg7, long arg9, long arg11) {
		return this.sm4Lt(arg5 ^ arg7 ^ arg9 ^ arg11) ^ arg3;
	}

	private long sm4Lt(long arg11) {
		byte[] v5 = new byte[4];
		byte[] v4 = new byte[4];
		this.PUT_ULONG_BE(arg11, v5, 0);
		v4[0] = this.sm4Sbox(v5[0]);
		v4[1] = this.sm4Sbox(v5[1]);
		v4[2] = this.sm4Sbox(v5[2]);
		v4[3] = this.sm4Sbox(v5[3]);
		long v0 = this.GET_ULONG_BE(v4, 0);
		return this.ROTL(v0, 2) ^ v0 ^ this.ROTL(v0, 10) ^ this.ROTL(v0, 18) ^ this.ROTL(v0, 24);
	}

	private byte sm4Sbox(byte arg3) {
		return SM4.SboxTable[arg3 & 255];
	}

	public byte[] sm4_crypt_cbc(SM4_Context arg13, byte[] arg14, byte[] arg15) throws Exception {
		byte[] v8;
		byte[] v6;
		byte[] v2;
		if(arg14 != null) {
			int v1 = 16;
			if(arg14.length == v1) {
				if(arg15 != null) {
					if((arg13.isPadding) && arg13.mode == 1) {
						arg15 = this.padding(arg15, 1);
					}

					int v3 = arg15.length;
					ByteArrayInputStream v4 = new ByteArrayInputStream(arg15);
					ByteArrayOutputStream v5 = new ByteArrayOutputStream();
					if(arg13.mode == 1) {
						while(v3 > 0) {
							v2 = new byte[v1];
							v6 = new byte[v1];
							v8 = new byte[v1];
							v4.read(v2);
							int v0;
							for(v0 = 0; v0 < v1; ++v0) {
								v6[v0] = ((byte)(v2[v0] ^ arg14[v0]));
							}

							this.sm4_one_round(arg13.sk, v6, v8);
							System.arraycopy(v8, 0, arg14, 0, v1);
							v5.write(v8);
							v3 += -16;
						}
					}
					else {
						v2 = new byte[v1];
						while(v3 > 0) {
							v6 = new byte[v1];
							v8 = new byte[v1];
							byte[] v9 = new byte[v1];
							v4.read(v6);
							System.arraycopy(v6, 0, v2, 0, v1);
							this.sm4_one_round(arg13.sk, v6, v8);
							int v0;
							for(v0 = 0; v0 < v1; ++v0) {
								v9[v0] = ((byte)(v8[v0] ^ arg14[v0]));
							}

							System.arraycopy(v2, 0, arg14, 0, v1);
							v5.write(v9);
							v3 += -16;
						}
					}

					byte[] v1_1 = v5.toByteArray();
					if((arg13.isPadding) && arg13.mode == 0) {
						v1_1 = this.padding(v1_1, 0);
					}

					v4.close();
					v5.close();
					return v1_1;
				}
				else {
					throw new Exception("input is null!");
				}
			}
		}

		throw new Exception("iv error!");
	}

	public byte[] sm4_crypt_ecb(SM4_Context arg7, byte[] arg8) throws Exception {
		byte[] v3;
		if(arg8 != null) {
			if((arg7.isPadding) && arg7.mode == 1) {
				arg8 = this.padding(arg8, 1);
			}

			int v0 = arg8.length;
			ByteArrayInputStream v1 = new ByteArrayInputStream(arg8);
			ByteArrayOutputStream v2 = new ByteArrayOutputStream();
			while(v0 > 0) {
				byte[] v4 = new byte[16];
				v3 = new byte[16];
				v1.read(v4);
				this.sm4_one_round(arg7.sk, v4, v3);
				v2.write(v3);
				v0 += -16;
			}

			v3 = v2.toByteArray();
			if((arg7.isPadding) && arg7.mode == 0) {
				v3 = this.padding(v3, 0);
			}

			v1.close();
			v2.close();
			return v3;
		}

		throw new Exception("input is null!");
	}

	private void sm4_one_round(long[] arg23, byte[] arg24, byte[] arg25) {
		int v0;
		SM4 v11 = this;
		byte[] v13 = arg25;
		long[] v14 = new long[36];
		v14[0] = v11.GET_ULONG_BE(arg24, 0);
		v14[1] = v11.GET_ULONG_BE(arg24, 4);
		v14[2] = v11.GET_ULONG_BE(arg24, 8);
		v14[3] = v11.GET_ULONG_BE(arg24, 12);
		int v8;
		for(v8 = 0; true; ++v8) {
			v0 = 32;
			if(v8 >= v0) {
				break;
			}

			v14[v8 + 4] = this.sm4F(v14[v8], v14[v8 + 1], v14[v8 + 2], v14[v8 + 3], arg23[v8]);
		}

		v11.PUT_ULONG_BE(v14[35], v13, 0);
		v11.PUT_ULONG_BE(v14[34], v13, 4);
		v11.PUT_ULONG_BE(v14[33], v13, 8);
		v11.PUT_ULONG_BE(v14[v0], v13, 12);
	}

	private void sm4_setkey(long[] arg13, byte[] arg14) {
		long[] v1 = new long[4];
		long[] v2 = new long[36];
		int v3 = 0;
		v1[0] = this.GET_ULONG_BE(arg14, 0);
		v1[1] = this.GET_ULONG_BE(arg14, 4);
		v1[2] = this.GET_ULONG_BE(arg14, 8);
		v1[3] = this.GET_ULONG_BE(arg14, 12);
		long v5 = v1[0];
		int[] v9 = SM4.FK;
		v2[0] = v5 ^ (((long)v9[0]));
		v2[1] = v1[1] ^ (((long)v9[1]));
		v2[2] = v1[2] ^ (((long)v9[2]));
		v2[3] = v1[3] ^ (((long)v9[3]));
		while(v3 < 32) {
			v2[v3 + 4] = v2[v3] ^ this.sm4CalciRK(v2[v3 + 1] ^ v2[v3 + 2] ^ v2[v3 + 3] ^ (((long)SM4.CK[v3])));
			arg13[v3] = v2[v3 + 4];
			++v3;
		}
	}

	public void sm4_setkey_dec(SM4_Context arg4, byte[] arg5) throws Exception {
		if(arg4 != null) {
			if(arg5 != null) {
				int v1 = 16;
				if(arg5.length == v1) {
					arg4.mode = 0;
					this.sm4_setkey(arg4.sk, arg5);
					int v0;
					for(v0 = 0; v0 < v1; ++v0) {
						this.SWAP(arg4.sk, v0);
					}

					return;
				}
			}

			throw new Exception("key error!");
		}

		throw new Exception("ctx is null!");
	}

	public void sm4_setkey_enc(SM4_Context arg3, byte[] arg4) throws Exception {
		if(arg3 != null) {
			if(arg4 != null && arg4.length == 16) {
				arg3.mode = 1;
				this.sm4_setkey(arg3.sk, arg4);
				return;
			}

			throw new Exception("key error!");
		}

		throw new Exception("ctx is null!");
	}
}