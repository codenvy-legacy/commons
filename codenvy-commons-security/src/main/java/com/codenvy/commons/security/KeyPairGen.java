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

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/** DSA key pair generator. */
public class KeyPairGen {
    /** Key size, may be set by argument -keysize. */
    private int keySize = 1024;

    /** File for store private key size, may be set by argument -privatefile. */
    private String privateKeyFileName = "priv";

    /** File for store public key size, may be set by argument -publicfile. */
    private String publicKeyFileName = "pub";

    /** Encode results to base64, may be set by argument -base64. */
    private boolean encodeBase64 = false;

    private KeyPairGen() {
    }

    public static void main(String[] args) {
        KeyPairGen gen = new KeyPairGen();
        for (int i = 0, length = args.length; i < length; i++) {
            if ("-keysize".equals(args[i])) {
                if ((i + 1) < length && !args[i + 1].isEmpty() && args[i + 1].charAt(0) != '-') {
                    try {
                        gen.keySize = Integer.parseInt(args[i + 1]);
                        i++;
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid key size: " + args[i + 1]); //NOSONAR
                        System.exit(1); //NOSONAR
                    }
                } else {
                    System.err.println("Key size not set. "); //NOSONAR
                    System.exit(1); //NOSONAR
                }
            } else if ("-privatefile".equals(args[i])) {
                if ((i + 1) < length && !args[i + 1].isEmpty() && args[i + 1].charAt(0) != '-') {
                    gen.privateKeyFileName = args[i + 1];
                    i++;
                } else {
                    System.err.println("Private key file name is not set. "); //NOSONAR
                    System.exit(1); //NOSONAR
                }
            } else if ("-publicfile".equals(args[i])) {
                if ((i + 1) < length && !args[i + 1].isEmpty() && args[i + 1].charAt(0) != '-') {
                    gen.publicKeyFileName = args[i + 1];
                    i++;
                } else {
                    System.err.println("Public key file name is not set. "); //NOSONAR
                    System.exit(1); //NOSONAR
                }
            } else if ("-base64".equals(args[i])) {
                gen.encodeBase64 = true;
            } else {
                System.err.println("Invalid argument: " + args[i]); //NOSONAR
                System.exit(1); //NOSONAR
            }
        }

        gen.generate();
    }

    private void generate() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
            keyGen.initialize(keySize);
            KeyPair pair = keyGen.genKeyPair();
            PublicKey pub = pair.getPublic();
            PrivateKey prv = pair.getPrivate();
            byte[] publicKeyBytes;
            byte[] privateKeyBytes;
            if (encodeBase64) {
                publicKeyBytes = Base64.encodeBase64(pub.getEncoded());
                privateKeyBytes = Base64.encodeBase64(prv.getEncoded());
            } else {
                publicKeyBytes = pub.getEncoded();
                privateKeyBytes = prv.getEncoded();
            }

            File publicFile = new File(publicKeyFileName);
            if (!publicFile.getParentFile().exists()) {
                publicFile.getParentFile().mkdirs();
            }

            File privateFile = new File(privateKeyFileName);
            if (!privateFile.getParentFile().exists()) {
                privateFile.getParentFile().mkdirs();
            }

            FileOutputStream publicFileInputStream = new FileOutputStream(publicFile);
            publicFileInputStream.write(publicKeyBytes);
            publicFileInputStream.close();

            FileOutputStream privateFileInputStream = new FileOutputStream(privateFile);
            privateFileInputStream.write(privateKeyBytes);
            privateFileInputStream.close();

            System.out.println("Public key:  " + publicFile.getAbsolutePath()); //NOSONAR
            System.out.println("Private key: " + privateFile.getAbsolutePath()); //NOSONAR
        } catch (Exception e) {
            System.err.println("Caught error: " + e.toString()); //NOSONAR
            System.exit(1); //NOSONAR
        }
    }
}
