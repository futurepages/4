package org.futurepages.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.UrlBase64;
import org.futurepages.core.exception.DefaultExceptionLogger;

/**
 *
 * @author leandro
 */
public class CryptoUtil {

	private static final String DES_INTERNAL_KEY = "cryp:gpf"; //não alterar!!

    public static String md5FromUB64(String password){
		 BigInteger hash = new BigInteger(1, ub64decode(password));
		 password = hash.toString(16);

        //correção da falta de zeros
        if(password.length()<32){
            int numZeros = 32 - password.length();
            StringBuffer zeros = new StringBuffer("");
            for(int i = 1 ; i<=numZeros;i++){
                zeros.append("0");
            }
            return (zeros + password);
        }
        return password;
	}

    public static String md5UB64(String senha){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5"); //SHA1, TIGER
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return  ub64encode(md.digest(senha.getBytes()));
    }

	public static String encryptDES(String keyDES, String clearText) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			SecretKey key = new SecretKeySpec(keyDES.getBytes(), "DES"); //AES seria com 16 caracteres (128bits)

			// for CBC; must be 8 bytes
			byte[] initVector = DES_INTERNAL_KEY.getBytes(); //se  fosse AES seriam 16 caracteres

			AlgorithmParameterSpec algParamSpec = new IvParameterSpec(initVector);

			Cipher m_encrypter = Cipher.getInstance("DES/CBC/PKCS5Padding");
			Cipher m_decrypter = Cipher.getInstance("DES/CBC/PKCS5Padding");

			m_encrypter.init(Cipher.ENCRYPT_MODE, key, algParamSpec);
			m_decrypter.init(Cipher.DECRYPT_MODE, key, algParamSpec);


			byte[] encryptedBin = m_encrypter.doFinal(clearText.getBytes());

			return ub64encode(encryptedBin);
		} catch (Exception ex) {
			DefaultExceptionLogger.getInstance().execute(ex);
			return null;
		}
	}

	public static String decryptDES(String keyDES, String encryptedText) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

			SecretKey key = new SecretKeySpec(keyDES.getBytes(), "DES"); //AES seria com 16 caracteres (128bits)

			byte[] initVector = DES_INTERNAL_KEY.getBytes(); //se  fosse AES seriam 16 caracteres

			AlgorithmParameterSpec algParamSpec = new IvParameterSpec(initVector);
			Cipher m_decrypter = Cipher.getInstance("DES/CBC/PKCS5Padding");

			m_decrypter.init(Cipher.DECRYPT_MODE, key, algParamSpec);

			byte[] decryptedBin = m_decrypter.doFinal(ub64decode(encryptedText));

			return new String(decryptedBin);
		} catch (Exception ex) {
			DefaultExceptionLogger.getInstance().execute(ex);
			return null;
		}
	}

	private static String ub64encode(byte[] in) {
		String result = (new String(UrlBase64.encode(in)));

		if (result.endsWith(".")) {
			if (!result.endsWith("..")) {
				return result.substring(0, result.length() - 1);
			} else {
				return result.substring(0, result.length() - 2);
			}
		} else {
			return result;
		}
	}

	private static byte[] ub64decode(String in) {
		int mod = in.length() % 4;
		if (mod != 0) {
			if (mod == 2) {
				in = in + "..";
			} else if (mod == 3) {
				in = in + ".";
			} else {
				throw new RuntimeException("invalid input text to decode in ub64");
			}
		}
		return UrlBase64.decode(in);
	}
}
