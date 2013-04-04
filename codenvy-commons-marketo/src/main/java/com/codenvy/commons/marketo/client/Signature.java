package com.codenvy.commons.marketo.client;

import java.security.SignatureException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class Signature {
	// Algorithm type
	private static final String HMAC_SHA1 = "HmacSHA1";

	/**
	 * Calculate HMAC signature.
	 * 
	 * @param data Input data to be signed.
	 * @param key Secret signing key.
	 * @return HMAC (RFC 2104) signature converted to HEX.
	 * @throws java.security.SignatureException
	 */
	public static String calculateHMAC(String data, String key) throws java.security.SignatureException {
		
		String result;
		try {
			// Get an hmac_sha1 key bytes
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1);

			// Get Mac instance and initialize with the secret key
			Mac mac = Mac.getInstance(HMAC_SHA1);
			mac.init(secretKey);

			// Calculate the HMAC on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes());

			// HHMAC bytes as HEX encoded string
			char[] hexChars = Hex.encodeHex(rawHmac);
			result = new String(hexChars);

		} catch (Exception e) {
			throw new SignatureException("Error generating HMAC signature: " + e.getMessage(), e);
		}
		return result;
	}
}