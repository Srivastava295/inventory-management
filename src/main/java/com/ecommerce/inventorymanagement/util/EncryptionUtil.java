package com.ecommerce.inventorymanagement.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecommerce.inventorymanagement.constants.Constants;

@Component
public class EncryptionUtil {

	@Autowired
	private ConfigMgrUtil configMgr;

	private static final Logger LOG = Logger.getLogger(EncryptionUtil.class.getName());

	private static SecretKeySpec secretKey;
	private static byte[] key;

	public void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes(Constants.CHARSET_UTF_8);
			sha = MessageDigest.getInstance(configMgr.getPropertyValueAsString(Constants.SHA_ALGORITHM_1));
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key,
					configMgr.getPropertyValueAsString(Constants.ENCRYPTION_DECRYPTION_ALGORITHM));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			LOG.log(Level.SEVERE, "Error while generating secret key: ", e);
		}
	}

	public String encrypt(String strToEncrypt) {
		try {

			String secret = configMgr.getPropertyValueAsString(Constants.SECRET_KEY);
			setKey(secret);

			Cipher cipher = Cipher.getInstance(configMgr.getPropertyValueAsString(Constants.CIPHER_TRANSFORMATION));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(Constants.CHARSET_UTF_8)));
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Error while encrypting: ", e);
		}
		return null;
	}

	public String decrypt(String strToDecrypt, String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance(configMgr.getPropertyValueAsString(Constants.CIPHER_TRANSFORMATION));
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Error while decrypting: ", e);
		}
		return null;
	}
}
