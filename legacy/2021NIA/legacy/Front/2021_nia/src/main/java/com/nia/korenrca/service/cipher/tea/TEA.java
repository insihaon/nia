package com.nia.korenrca.service.cipher.tea;

import com.nia.korenrca.service.util.Base64;

import java.util.UUID;

public class TEA {

  private final int delta = 0x9E3779B9;

  private int[] S = new int[4];

  /**
   * Initialize the cipher for encryption or decryption.
   * 
	 * @param key a 16 byte (128-bit) key
   */
  public TEA(byte[] key) {
    if (key == null)
      throw new RuntimeException("Invalid key: Key was null");
    if (key.length < 16)
      throw new RuntimeException("Invalid key: Length was less than 16 bytes");
    for (int off = 0, i = 0; i < 4; i++) {
      S[i] = ((key[off++] & 0xff)) | ((key[off++] & 0xff) << 8) | ((key[off++] & 0xff) << 16) | ((key[off++] & 0xff) << 24);
    }

    //		System.out.println("KEY:" + Arrays.toString(S));
  }

  public TEA(String key) {
    this(key.getBytes());
  }

  /*
   * encrypt text using Corrected Block TEA (xxtea) algorithm
   *
   * @param {string} plaintext String to be encrypted (multi-byte safe)
   * 
   * @param {string} password Password to be used for encryption (1st 16 chars)
   * 
   * @returns {string} encrypted text
   */
  public byte[] encrypt(byte[] clear) {

    int[] v = strToLongs(clear);
		if (v.length <= 1)
			v = new int[]{v[0], 0};
    int n = v.length;

    // ---- <TEA coding> ---- 
    int z = v[n - 1];
    int y = v[0];

    int mx, e;
    int q = 6 + 52 / n;
    int sum = 0;

    while (q-- > 0) { // 6 + 52/n operations gives between 6 & 32 mixes on each word
      sum += delta;
      e = sum >>> 2 & 3;
      for (int p = 0; p < n; p++) {
        y = v[(p + 1) % n];
        mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (S[p & 3 ^ e] ^ z);
        z = v[p] += mx;
      }
    }
    // ---- </TEA> ----
    return longsToStr(v);
  }

	public String encrypt(String plaintext) {
		byte[] cipher = encrypt(plaintext.getBytes());
		return new String(Base64.encode(cipher));
	}

  /*
   * decrypt text using Corrected Block TEA (xxtea) algorithm
   *
   * @param {byte[]} ciphertext byte arrays to be decrypted
   * 
   * @returns {byte[]} decrypted array
   */
  public byte[] decrypt(byte[] crypt) {
    int[] v = strToLongs(crypt);
    int n = v.length;

    // ---- <TEA decoding> ---- 
    int z = v[n - 1];
    int y = v[0];

    int mx, e;
    int q = 6 + 52 / n;
    int sum = q * delta;

    while (sum != 0) {
      e = sum >>> 2 & 3;
      for (int p = n - 1; p >= 0; p--) {
        z = v[p > 0 ? p - 1 : n - 1];
        mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (S[p & 3 ^ e] ^ z);
        y = v[p] -= mx;
      }
      sum -= delta;
    }

    // ---- </TEA> ---- 

    byte[] plainBytes = longsToStr(v);

    // strip trailing null chars resulting from filling 4-char blocks:
    int len;
    for (len = 0; len < plainBytes.length; len++) {
      if (plainBytes[len] == 0)
        break;
    }

    byte[] plainTrim = new byte[len];
    System.arraycopy(plainBytes, 0, plainTrim, 0, len);

    return plainTrim;
  }

  /*
   * decrypt text using Corrected Block TEA (xxtea) algorithm
   *
   * @param {string} ciphertext String to be decrypted
   * 
   * @returns {string} decrypted text
   */
  public String decrypt(String ciphertext) {
    String plainText = null;
    byte[] plainTextBytes = decrypt(Base64.decode(ciphertext));
    try {
      plainText = new String(plainTextBytes, "UTF-8");
    } catch (Exception e) {
    }

    return plainText;
  }

  private int[] strToLongs(byte[] s) { // convert string to array of longs, each containing 4 chars
    // note chars must be within ISO-8859-1 (with Unicode code-point < 256) to fit 4/long
    int[] l = new int[(s.length + 3) / 4];

    for (int i = 0; i < l.length; i++) {
      // note little-endian encoding - endianness is irrelevant as long as 
      // it is the same in longsToStr() 
			l[i] = (s[i * 4 + 0] & 0xff) << 0;
			if ((i * 4 + 1) < s.length)
				l[i] |= (s[i * 4 + 1] & 0xff) << 8;
			if ((i * 4 + 2) < s.length)
				l[i] |= (s[i * 4 + 2] & 0xff) << 16;
			if ((i * 4 + 3) < s.length)
				l[i] |= (s[i * 4 + 3] & 0xff) << 24;
    }

    return l; // note running off the end of the string generates nulls since 
  }

  private byte[] longsToStr(int[] l) { // convert array of longs back to string
    byte[] a = new byte[l.length * 4];

    for (int i = 0; i < l.length; i++) {
			a[i * 4 + 0] = (byte)((l[i] >> 0) & 0xff);
			a[i * 4 + 1] = (byte)((l[i] >> 8) & 0xff);
			a[i * 4 + 2] = (byte)((l[i] >> 16) & 0xff);
			a[i * 4 + 3] = (byte)((l[i] >> 24) & 0xff);
    }

    return a;
  }

	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
	}
}
