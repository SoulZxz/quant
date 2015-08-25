package com.ricequant.strategy.support;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DigestUtil {

	private DigestUtil() {

	}

	public static String sign(String paramString, String signType) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance(signType);
			return byte2hex(sha1.digest(paramString.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static String byte2hex(byte[] b) {
		StringBuilder sbDes = new StringBuilder();
		String tmp = null;
		for (int i = 0; i < b.length; i++) {
			tmp = (Integer.toHexString(b[i] & 0xFF));
			if (tmp.length() == 1) {
				sbDes.append("0");
			}
			sbDes.append(tmp);
		}
		return sbDes.toString();
	}

}
