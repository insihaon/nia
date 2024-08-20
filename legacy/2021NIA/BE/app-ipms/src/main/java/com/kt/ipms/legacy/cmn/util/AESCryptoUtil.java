package com.kt.ipms.legacy.cmn.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.ByteUtils;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

@Component
public class AESCryptoUtil {
	
	private final String CRYPTO_ALGORITHM = "AES";

	private final String CRYPTO_ALGORITHM_CBC = "AES/CBC/ISO10126Padding";
	
	public final String DEFAULT_KEY_FILE = "keys/aes_defaultkey.key";
	
	public final String DEFAULT_IV_FILE = "keys/iv.key";
	
	protected KTLogger logger = KTLoggerFactory.getLogger(getClass());
	
	public String encrypt(String message) throws ServiceException {
		File keyFile = null;
		byte[] encrypted = null;
		SecretKeySpec sks;
		try {
			Resource resource = new ClassPathResource(DEFAULT_KEY_FILE);
			keyFile = resource.getFile();
			Security.addProvider(new BouncyCastleProvider());
			sks = getSecretKeySpec(keyFile);
			
			String Name= "BC";
	        if (Security.getProvider(Name) == null)
	        {
	        	PrintLogUtil.printLogInfo("not installed");	
	            // System.out.println("not installed"); //Codeeyes-Critical-sysout
	        }
	        else
	        {
	        	PrintLogUtil.printLogInfo("installed");
	            // System.out.println("installed"); //Codeeyes-Critical-sysout
	        }

	        
			Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM_CBC);
			cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(getIvBytesKey()));
			encrypted = cipher.doFinal(message.getBytes());
		} catch (Exception e) {
			e.printStackTrace(); //Codeeyes-Urgent-내용 없는 문장 제한
			//throw new ServiceException("CMN.HIGH.00000");
		}
		return ByteUtils.toHexString(encrypted);
	}
	
	public String decrypt(String message) throws ServiceException {
		File keyFile = null;
		SecretKeySpec sks;
		byte[] decrypted = null;
		try {
			Resource resource = new ClassPathResource(DEFAULT_KEY_FILE);
			keyFile = resource.getFile();
			Security.addProvider(new BouncyCastleProvider());
			sks = getSecretKeySpec(keyFile);
			Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM_CBC);
			cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(getIvBytesKey()));
			decrypted = cipher.doFinal(ByteUtils.toBytesFromHexString(message));
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return new String(decrypted);
	}
	
	private byte[] getIvBytes() {
		return "1234567812345678".getBytes();
	}
	
	private SecretKeySpec getSecretKeySpec(File keyFile) throws NoSuchAlgorithmException, IOException {
			return new SecretKeySpec(readKeyFile(keyFile), CRYPTO_ALGORITHM);
	}
	
	private byte[] readKeyFile(File keyFile)
		throws FileNotFoundException {
		Scanner scanner = new Scanner(keyFile).useDelimiter("\\Z");
		String keyValue = scanner.next();
		scanner.close();
		return ByteUtils.toBytesFromHexString(keyValue);
	}
	
	private byte[] getIvBytesKey() throws FileNotFoundException {
		Resource resource = new ClassPathResource(DEFAULT_IV_FILE);
		try {
			File keyFile = resource.getFile();
			Scanner scanner = new Scanner(keyFile).useDelimiter("\\Z");
			String ivKeyValue = scanner.next();
			scanner.close();
			return ivKeyValue.getBytes();
		} catch (IOException e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		
	}

}
