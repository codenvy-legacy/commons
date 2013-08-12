/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2013] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.commons.security;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/** DSA signature for signing data or verification signature. */
public final class SignatureDSA {
    private static final Logger LOG = LoggerFactory.getLogger(SignatureDSA.class);

    /* ======================= Helpers ======================= */
    private static Signature newSignature() {
        try {
            return Signature.getInstance("SHA1withDSA");
        } catch (NoSuchAlgorithmException e) {
            // should not happen since "SHA1withDSA" is supported.
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static KeyFactory newKeyFactory() {
        try {
            return KeyFactory.getInstance("DSA");
        } catch (NoSuchAlgorithmException e) {
            // should not happen since "DSA" is supported.
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static byte[] read(java.io.File f) throws IOException {
        return read(new FileInputStream(f));
    }

    private static byte[] read(String resource) throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(resource);
        if (is == null) {
            throw new IOException("Unreachable resource: " + resource);
        }
        return read(is);
    }

    private static byte[] read(InputStream is) throws IOException {
        try {
            byte[] buf = new byte[512];
            ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
            int r;
            while ((r = is.read(buf)) != -1) {
                bout.write(buf, 0, r);
            }
            return bout.toByteArray();
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
                LOG.error(ignored.getLocalizedMessage(), ignored);
            }
        }
    }

   /* ======================================================= */

    private static final String defaultPrivateKeyFile = "META-INF/keys/cloudide.key";

    private static final String defaultPublicKeyFile = "META-INF/keys/cloudide.pub";

    private final Signature delegate = newSignature();

    /** Create new DSA signature. */
    public SignatureDSA() {
    }

    /**
     * Update signed|verified data.
     *
     * @param data
     *         data
     * @throws SignatureException
     */
    public void update(String data) throws SignatureException {
        delegate.update(data.getBytes());
    }

   /* ============================== Signing ============================== */

    /**
     * Initialize signature for verification.
     *
     * @param prv
     *         private key
     * @throws InvalidKeyException
     * @see Signature#initSign(java.security.PrivateKey)
     */
    public void initSign(PrivateKey prv) throws InvalidKeyException {
        delegate.initSign(prv);
    }

    /**
     * Private key for signing loaded from specified bytes.
     *
     * @throws InvalidKeyException
     *         if private key is invalid
     * @throws InvalidKeySpecException
     * @see Signature#initSign(java.security.PrivateKey)
     */
    public void initSign(byte[] prvEcnKey) throws InvalidKeyException, InvalidKeySpecException {
        KeyFactory kf = newKeyFactory();
        PrivateKey prv = kf.generatePrivate(new PKCS8EncodedKeySpec(prvEcnKey));
        delegate.initSign(prv);
    }

    /**
     * Private key for signing loaded from specified stream.
     *
     * @throws IOException
     *         if any i/o errors occurs when try to load encoded key from stream
     * @throws InvalidKeyException
     *         if private key is invalid
     * @throws InvalidKeySpecException
     * @see Signature#initSign(java.security.PrivateKey)
     */
    public void initSign(InputStream prvEcnIS) throws IOException, InvalidKeyException, InvalidKeySpecException {
        initSign(read(prvEcnIS));
    }

    /**
     * Private key for signing loaded from {@link #defaultPrivateKeyFile}.
     *
     * @throws IOException
     *         if any i/o errors occurs when try to load encoded key from file
     * @throws InvalidKeyException
     *         if private key is invalid
     * @throws InvalidKeySpecException
     */
    public void initSign() throws IOException, InvalidKeyException, InvalidKeySpecException {
        initSign(read(defaultPrivateKeyFile));
    }

    /**
     * Generate signature for data.
     *
     * @param base64
     *         if <code>true</code> signature also encoded to Base64.
     * @return signature
     * @throws SignatureException
     * @see #update(String)
     */
    public byte[] sign(boolean base64) throws SignatureException {
        byte[] sign = delegate.sign();
        if (base64) {
            return Base64.encodeBase64(sign);
        }
        return sign;
    }

   /* ============================ verification ============================ */

    /**
     * Initialize signature for verification.
     *
     * @param pub
     *         public key
     * @throws InvalidKeyException
     * @see Signature#initVerify(java.security.PublicKey)
     */
    public void initVerify(PublicKey pub) throws InvalidKeyException {
        delegate.initVerify(pub);
    }

    /**
     * Public key for verification loaded from specified bytes.
     *
     * @throws InvalidKeyException
     *         if public key is invalid
     * @throws InvalidKeySpecException
     * @see Signature#initVerify(java.security.PublicKey)
     */
    public void initVerify(byte[] pubEncKey) throws InvalidKeyException, InvalidKeySpecException {
        KeyFactory kf = newKeyFactory();
        PublicKey pub = kf.generatePublic(new X509EncodedKeySpec(pubEncKey));
        delegate.initVerify(pub);
    }

    /**
     * Public key for verification loaded from specified stream.
     *
     * @throws IOException
     *         if any i/o errors occurs when try to load encoded key from stream
     * @throws InvalidKeyException
     *         if public key is invalid
     * @throws InvalidKeySpecException
     * @see Signature#initVerify(java.security.PublicKey)
     */
    public void initVerify(InputStream pubEcnIS) throws IOException, InvalidKeyException, InvalidKeySpecException {
        initVerify(read(pubEcnIS));
    }

    /**
     * Public key for verification loaded from {@link #defaultPublicKeyFile}.
     *
     * @throws IOException
     *         if any i/o errors occurs when try to load encoded key from file
     * @throws InvalidKeyException
     *         if public key is invalid
     * @throws InvalidKeySpecException
     * @see Signature#initVerify(java.security.PublicKey)
     */
    public void initVerify() throws IOException, InvalidKeyException, InvalidKeySpecException {
        initVerify(read(defaultPublicKeyFile));
    }

    /**
     * Verify signature for data.
     *
     * @param sign
     *         signature to be verified. May be base64 encoded.
     * @return <code>true</code> if signature verified and <code>false</code> otherwise
     * @throws SignatureException
     * @see #update(String)
     */
    public boolean verify(byte[] sign) throws SignatureException {
        if (Base64.isBase64(sign)) {
            sign = Base64.decodeBase64(sign);
        }
        return delegate.verify(sign);
    }

    /**
     * Return signature of the given data
     *
     * @param data
     * @return Base64 encoded signature
     * @throws Exception
     */
    public static String getBase64Signature(String data) throws GeneralSecurityException, IOException {
        SignatureDSA dsa = new SignatureDSA();
        dsa.initSign();
        dsa.update(data);
        return new String(dsa.sign(true));
    }

    /**
     * Check data signature
     *
     * @param data
     * @return true if signature is valid.
     * @throws Exception
     */
    public static boolean isSignatureValid(String data, String signature) throws Exception {
        SignatureDSA dsa = new SignatureDSA();
        dsa.initVerify();
        dsa.update(data);
        if (dsa.verify(signature.getBytes())) {
            LOG.debug("Signature verification for {} successful ", data);
            return true;
        } else {
            LOG.error("Signature verification for {} failed ", data);
            return false;
        }
    }

}
