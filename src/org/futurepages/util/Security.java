package org.futurepages.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.futurepages.enums.HashType;
import sun.security.provider.DSAPrivateKey;
import sun.security.provider.DSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

/**
 * Classe que possui métodos estáticos para tratamento de strings no controle
 * da segurança da aplicação web
 */
public class Security {
	private static final String hexDigits = "0123456789abcdef";
	private static final int KEYSIZE = 256;
	private static String ALGORITHM = "DSA";
	private static String SIGNALGORITHM = "SHA1withRSA";


	public static String asymKeyAlgorithm = "RSA";
	public static String asymAlgorithm = "RSA";
	public static int asymKeyAlgorithmStrength = 1024;
	public static String symKeyAlgorithm = "RIJNDAEL";
	public static String symAlgorithm = "RIJNDAEL";
	public static int symAlgorithmStrength = 128;

	/**
	 * Classe com métodos para gestão de segurança
	 */
	static {
		java.security.Security.addProvider(new BouncyCastleProvider());
	}


	/**
	 * Retorna o algoritmo que está sendo usado para encriptação e assinatura
	 *
	 * @return
	 */
	public static String getALGORITHM() {
		return ALGORITHM;
	}

	public static String getSIGNALGORITHM() {
		return SIGNALGORITHM;
	}

	/**
	 * Realiza um digest em um array de bytes através do algoritmo especificado
	 *
	 * @param input     - O array de bytes a ser criptografado
	 * @param algoritmo - O algoritmo a ser utilizado
	 * @return byte[] - O resultado da criptografia
	 * @throws NoSuchAlgorithmException - Caso o algoritmo fornecido não seja
	 *                                  válido
	 */
	public static byte[] digest(byte[] input, String algoritmo)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algoritmo);
		md.reset();
		return md.digest(input);
	}

	public static String digest(String value) {
		return digest(value, HashType.SHA256);
	}

	public static String digest(String value, HashType type) {

		try {
			return byteArrayToHexString(digest(value.getBytes(), type.getDescricao()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retorna o valor da string de entrada com codificação md5
	 */
	public static String md5(String inputText) {
		String sen = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5"); //SHA1, TIGER
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		BigInteger hash = new BigInteger(1, md.digest(inputText.getBytes()));
		sen = hash.toString(16);

		//correção da falta de zeros
		if (sen.length() < 32) {
			int numZeros = 32 - sen.length();
			StringBuffer zeros = new StringBuffer("");
			for (int i = 1; i <= numZeros; i++) {
				zeros.append("0");
			}
			return (zeros + sen);
		}
		return sen;
	}

	/**
	 * Encripta value com a chave pública publicKey usando criptografia assimétrica
	 *
	 * @param value
	 * @param publicKey
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static byte[] encrypt(byte[] value, PublicKey publicKey) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(asymAlgorithm);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] result = cipher.doFinal(value);
		return result;
	}

	/**
	 * * Encripta value com a chave secreta secreteKey usando criptografia simétrica
	 *
	 * @param toEncrypt
	 * @param key
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static byte[] encrypt(byte[] toEncrypt, SecretKey key) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(symAlgorithm);
		System.out.println("got cipher, blocksize = " + cipher.getBlockSize());
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] result = cipher.doFinal(toEncrypt);
		return result;
	}

	/**
	 * Decripta value com a chave secreta secreteKey usando criptografia simétrica
	 *
	 * @param toDecrypt
	 * @param key
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(byte[] toDecrypt, SecretKey key)
			throws GeneralSecurityException {
		Cipher deCipher = Cipher.getInstance(symAlgorithm);
		deCipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = deCipher.doFinal(toDecrypt);
		return result;
	}

	/**
	 * Decripta value com a chave privada privateKey usando criptografia assimétrica
	 *
	 * @param toDecrypt
	 * @param key
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(byte[] toDecrypt, PrivateKey key)
			throws GeneralSecurityException {
		Cipher deCipher = Cipher.getInstance(asymAlgorithm);
		deCipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = deCipher.doFinal(toDecrypt);
		return result;
	}


	private static String getProvider() {
		return "BC";
	}

	/**
	 * Filtra a string de entrada e retorna uma string vazia caso exista algum
	 * caractere malicioso.
	 */
	@Deprecated
	public static String filtered(String in) {
		if (in.contains("'")) {
			in = in.replaceAll("'", "''");
		}
		if ((in.contains("<")) || (in.contains(">")) || (in.contains("%")) || (in.contains(";"))) {
			in = "";
		}
		return in;
	}

	/**
	 * Inserção segura, previne injeção de scripts
	 */
	@Deprecated
	public static String filteredInsert(String in) {
		if (in.contains("'")) in = in.replaceAll("'", "''");
		if (in.contains("<")) in = in.replaceAll("<", " ");
		if (in.contains(">")) in = in.replaceAll(">", " ");

		return in;
	}

	/**
	 * Converte o array de bytes em uma representação hexadecimal.
	 *
	 * @param b - O array de bytes a ser convertido.
	 * @return Uma String com a representação hexa do array
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < b.length; i++) {
			int j = ((int) b[i]) & 0xFF;
			buf.append(hexDigits.charAt(j / 16));
			buf.append(hexDigits.charAt(j % 16));
		}

		return buf.toString();
	}

	/**
	 * Converte uma String hexa no array de bytes correspondente.
	 *
	 * @param hexa - A String hexa
	 * @return O vetor de bytes
	 * @throws IllegalArgumentException - Caso a String não sej auma
	 *                                  representação haxadecimal válida
	 */
	public static byte[] hexStringToByteArray(String hexa)
			throws IllegalArgumentException {

		//verifica se a String possui uma quantidade par de elementos
		if (hexa.length() % 2 != 0) {
			throw new IllegalArgumentException("String hexa inválida");
		}

		byte[] b = new byte[hexa.length() / 2];

		for (int i = 0; i < hexa.length(); i += 2) {
			b[i / 2] = (byte) ((hexDigits.indexOf(hexa.charAt(i)) << 4) |
					(hexDigits.indexOf(hexa.charAt(i + 1))));
		}
		return b;
	}

	/**
	 * Gera um par de chaves baseado na semente do usuário
	 *
	 * @param seed
	 * @return
	 */
	public static KeyPair gerarParDeChavesAssimetricas(String seed) {

		KeyPairGenerator gen = null;
		try {
			gen = KeyPairGenerator.getInstance(asymKeyAlgorithm, getProvider());
			SecureRandom sr = new SecureRandom(seed.getBytes());
			gen.initialize(asymKeyAlgorithmStrength, sr);
			return gen.generateKeyPair();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Gera chave secreta para criptografia híbrida.
	 *
	 * @param seed
	 * @return
	 */
	public static SecretKey gerarChaveSimetrica(String seed) {

		KeyPairGenerator gen = null;
		try {
			SecureRandom sr = new SecureRandom(seed.getBytes());
			KeyGenerator kg = KeyGenerator.getInstance(symKeyAlgorithm);
			kg.init(symAlgorithmStrength, sr);
			return kg.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getKeysize() {
		return KEYSIZE;
	}

	/**
	 * Assina um determinado dado passado e retorna sua assinatura
	 *
	 * @param data
	 * @param pk
	 * @return
	 */
	public static String assinar(String data, PrivateKey pk) {
		try {
			Signature s = Signature.getInstance(getSIGNALGORITHM());
			s.initSign(pk);
			s.update(data.getBytes());
			byte[] sign = s.sign();
			return byteArrayToHexString(sign);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	/**
	 * Valida a assinatura de um dado.
	 *
	 * @param data
	 * @param sign
	 * @param pbKey
	 * @return
	 */
	public static boolean validarAssinatura(String data, byte[] sign, PublicKey pbKey) {
		try {
			Signature clientSig = Signature.getInstance(getSIGNALGORITHM());
			clientSig.initVerify(pbKey);
			clientSig.update(data.getBytes());
			return clientSig.verify(sign);
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Faz encode de um código em Hexadecimal para Chave Pública
	 *
	 * @param publicKey
	 * @return
	 */
	public static PublicKey obterChavePublica(String publicKey) {
		try {
			byte[] encoded = hexStringToByteArray(publicKey);
			return new DSAPublicKey(encoded);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Faz encode de um código em Hexadecimal para Chave Privada
	 *
	 * @param privateKey
	 * @return
	 */
	public static PrivateKey obterChavePrivada(String privateKey) {
		try {
			byte[] encoded = hexStringToByteArray(privateKey);
			return new DSAPrivateKey(encoded);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Faz encode de uma chave pública para um texto em Hexadecimal
	 * @param publicKey
	 * @return
	 */
	public static String obterChavePublica(PublicKey publicKey) {
		return byteArrayToHexString(publicKey.getEncoded());
	}


	/**
	 * Faz encode de uma chave privada para um texto em Hexadecimal
	 * @param privateKey
	 * @return
	 */
	public static String obterChavePrivada(PrivateKey privateKey) {
		return byteArrayToHexString(privateKey.getEncoded());
	}

	public static void main(String[] args) throws GeneralSecurityException {
		KeyPair kp = gerarParDeChavesAssimetricas("dksm");
		String plainText = "Texto Plano";

		PublicKey pbk = kp.getPublic();
		PrivateKey pvk = kp.getPrivate();

		String assinatura = assinar(plainText, pvk);

		System.out.println("Texto plano: " + plainText);
		System.out.println("assinatura: " + assinatura);


		System.out.println("\n\n\n");

		System.out.println("Validando Assinatura");
		System.out.println("[TRUE]:" + validarAssinatura(plainText, hexStringToByteArray(assinatura), pbk));

		String assinaturaErrada = assinatura + "0F";

		System.out.println("[FALSE]:" + validarAssinatura(plainText, hexStringToByteArray(assinaturaErrada), pbk));

		System.out.println("[FALSE]:" + validarAssinatura(plainText + "1", hexStringToByteArray(assinatura), pbk));


		SecretKey key = gerarChaveSimetrica("dksm");

		String cifroTextoSecret = byteArrayToHexString(encrypt(plainText.getBytes(), key));

		System.out.println("\n\n");

		System.out.println("Texto Plano: " + plainText);
		System.out.println("Encriptado: " + cifroTextoSecret);


		String textoPlano = byteArrayToHexString(decrypt(cifroTextoSecret.getBytes(), key));


		System.out.println("Decriptado: " + textoPlano);


	}
}