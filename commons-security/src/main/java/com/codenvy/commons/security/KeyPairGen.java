/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
public class KeyPairGen
{
   /** Key size, may be set by argument -keysize. */
   private int keySize = 1024;

   /** File for store private key size, may be set by argument -privatefile. */
   private String privateKeyFileName = "priv";

   /** File for store public key size, may be set by argument -publicfile. */
   private String publicKeyFileName = "pub";

   /** Encode results to base64, may be set by argument -base64. */
   private boolean encodeBase64 = false;

   private KeyPairGen()
   {
   }

   public static void main(String[] args)
   {
      KeyPairGen gen = new KeyPairGen();
      for (int i = 0, length = args.length; i < length; i++)
      {
         if ("-keysize".equals(args[i]))
         {
            if ((i + 1) < length && !args[i + 1].isEmpty() && args[i + 1].charAt(0) != '-')
            {
               try
               {
                  gen.keySize = Integer.parseInt(args[i + 1]);
                  i++;
               }
               catch (NumberFormatException e)
               {
                  System.err.println("Invalid key size: " + args[i + 1]); //NOSONAR
                  System.exit(1); //NOSONAR
               }
            }
            else
            {
               System.err.println("Key size not set. "); //NOSONAR
               System.exit(1); //NOSONAR
            }
         }
         else if ("-privatefile".equals(args[i]))
         {
            if ((i + 1) < length && !args[i + 1].isEmpty() && args[i + 1].charAt(0) != '-')
            {
               gen.privateKeyFileName = args[i + 1];
               i++;
            }
            else
            {
               System.err.println("Private key file name is not set. "); //NOSONAR
               System.exit(1); //NOSONAR
            }
         }
         else if ("-publicfile".equals(args[i]))
         {
            if ((i + 1) < length && !args[i + 1].isEmpty() && args[i + 1].charAt(0) != '-')
            {
               gen.publicKeyFileName = args[i + 1];
               i++;
            }
            else
            {
               System.err.println("Public key file name is not set. "); //NOSONAR
               System.exit(1); //NOSONAR
            }
         }
         else if ("-base64".equals(args[i]))
         {
            gen.encodeBase64 = true;
         }
         else
         {
            System.err.println("Invalid argument: " + args[i]); //NOSONAR
            System.exit(1); //NOSONAR
         }
      }

      gen.generate();
   }

   private void generate()
   {
      try
      {
         KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
         keyGen.initialize(keySize);
         KeyPair pair = keyGen.genKeyPair();
         PublicKey pub = pair.getPublic();
         PrivateKey prv = pair.getPrivate();
         byte[] publicKeyBytes;
         byte[] privateKeyBytes;
         if (encodeBase64)
         {
            publicKeyBytes = Base64.encodeBase64(pub.getEncoded());
            privateKeyBytes = Base64.encodeBase64(prv.getEncoded());
         }
         else
         {
            publicKeyBytes = pub.getEncoded();
            privateKeyBytes = prv.getEncoded();
         }

         File publicFile = new File(publicKeyFileName);
         if (!publicFile.getParentFile().exists())
         {
            publicFile.getParentFile().mkdirs();
         }

         File privateFile = new File(privateKeyFileName);
         if (!privateFile.getParentFile().exists())
         {
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
      }
      catch (Exception e)
      {
         System.err.println("Caught error: " + e.toString()); //NOSONAR
         System.exit(1); //NOSONAR
      }
   }
}
