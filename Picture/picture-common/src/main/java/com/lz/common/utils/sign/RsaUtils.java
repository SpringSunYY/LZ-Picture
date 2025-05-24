package com.lz.common.utils.sign;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密解密
 *
 * @author ruoyi
 **/
public class RsaUtils
{
    // Rsa 私钥
    public static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDVBHXfQd2vf6/C\n" +
            "34zEMtyuL7hC1vmuxBJ3lodftHqHfr0+5cbDx6JfenZWdTL201AAfS7hIVk7HmWT\n" +
            "6PCO8Jm1XTB5MHg4BQiZnqYTQ4MguG8GI+854Bw0e7q/kLRBo6MbY9gRaC7uDju7\n" +
            "mWQmkiRC7Fb0wOXVEiEVRtsklDvyT2MllKOmvzGmwpf+AAuA5Lju/MD96ZXuFDlo\n" +
            "K0xlLEHsXL9A05KjI5iFseg2zhUnbIfJlMmp1mjhEt3SsZ/CST8UNQ33g5KH/Gjz\n" +
            "O3/7BnXWeB8iuEqyxvBF5TraV/d3901R++UmJPp3Rf4FbuIc2H82+TfsCL1tb8G3\n" +
            "EAgSYKPRAgMBAAECggEAIgtGc1npfiTgCDsGGk0uq8AAqN4GpYG8mdQ66nbDpmgB\n" +
            "As5iR+Pd9Xhy9Tph3MTE4m8fUOethR9EiLEP0ShuyGeOWa//ZqSXTUXnsOh3SoFv\n" +
            "bQHNN+izWUf0/qy3wk3OwY/hdla6OV2Y+lB3ixqMJQ30j8VMdhMhqm7akywFsKHo\n" +
            "xilc5hpaWWhc6ojGU3ILqjd+BTARbEG/CEzL6FBYZOZOjpdXMDT1IFyH/12xvart\n" +
            "fJrxCWL+P7DHvlo6+BCVnKWWZai3kS/J/2FHLXMlIoYRVsqcXUnjbXBg/LVi3EFf\n" +
            "dLp2+Lxd7GcnZM4gkommfBQO/urpS5vN+Vu2B3ZciQKBgQDsuSOuFKY8IUwwyQxY\n" +
            "uWkSwyr9Luem/ArA5CMJDbh1lXm3MmU2ePJuIn3snsEs/dzUgOEW4fMd8mrJ5Edf\n" +
            "CKbd4fC5T1pKKDovSCvV6xQAaehxFHrqUQSO6kSozjrCwDodFyHHQyDERQw2LXbf\n" +
            "iwFAy4eStkf4fExXXC//c4p0ywKBgQDmXSM1ntNKMUTHb3jup8w63d8nK8kEycca\n" +
            "513aHk1PXTl4RQktfh7Zy9Iow6laqWw7YrJ8K6hAQlGqqJxf2zpQ1uAYWo6JyCO3\n" +
            "wfTD2s2vNjkQC4Dni7HXIiVG/ERSpJI6MxThoja9lUj8+vsOVZALlouf9KSoChc0\n" +
            "dSIKZoOSUwKBgQCpTeCO6IXcC5SKb8A81J6ppJxiZ+GWND0cqA3Gs+Fxd26N76Uj\n" +
            "yGzbCCA303Ml0orWETbrPr66dF6b9oB1H6L4nleksXRKBWtX1a5EWNT9VsG/3n3q\n" +
            "h/dSn4prkNPF5eWqWJj4ArK0rQ2G0g1q4+gJh4d3Dpd6yKtw76YEdYZY8QKBgQCH\n" +
            "LyLpZYICqPT+wEjMbIXqCafZudUmTS7dd+bYiC3AJCoEjSa8nqWGFBkJOQEaJhBQ\n" +
            "rTOmsIv04bZBGt9hVSpcpCwvvnylGGD1hgmYIb/QUVadNtL3jj/Xyr7rcEC76r2x\n" +
            "D6byXhoXmRKS+sn3eHimhDr9rDgwMHO7of1O8yfE5wKBgBCxBovHiZhFD3ExSPBn\n" +
            "3hE0kHZlNBmNMByX1J8jG526ZMje3Z8wP5jCNRR7HQduXBGcwfjF4UFE8O5JBMCs\n" +
            "SQWaA2gbaIbC3CAP8naZS3rDbMF1iSUWYL6bFHfi8jJSjlU5tE1oXBKOooUHnA5F\n" +
            "KRmnmEP5m6gaAUGKfzxaK0j6";

    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String text) throws Exception
    {
        return decryptByPrivateKey(privateKey, text);
    }

    /**
     * 公钥解密
     *
     * @param publicKeyString 公钥
     * @param text 待解密的信息
     * @return 解密后的文本
     */
    public static String decryptByPublicKey(String publicKeyString, String text) throws Exception
    {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param privateKeyString 私钥
     * @param text 待加密的信息
     * @return 加密后的文本
     */
    public static String encryptByPrivateKey(String privateKeyString, String text) throws Exception
    {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String privateKeyString, String text) throws Exception
    {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyString 公钥
     * @param text 待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String publicKeyString, String text) throws Exception
    {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 构建RSA密钥对
     *
     * @return 生成后的公私钥信息
     */
    public static RsaKeyPair generateKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        return new RsaKeyPair(publicKeyString, privateKeyString);
    }

    /**
     * RSA密钥对对象
     */
    public static class RsaKeyPair
    {
        private final String publicKey;
        private final String privateKey;

        public RsaKeyPair(String publicKey, String privateKey)
        {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey()
        {
            return publicKey;
        }

        public String getPrivateKey()
        {
            return privateKey;
        }
    }
}
