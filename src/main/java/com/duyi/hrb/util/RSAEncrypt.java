package com.duyi.hrb.util;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


public class RSAEncrypt {
    private static String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCw9K02Qc5B+zofEtKI3xk/0i58IwhjXKMsHexx3wgnGXkYgIMVvirPSNu6lzSZidmHPNrmtYmOeoBk28MBa5A6MsUnpBvEyHza418fB7Qbz/Dp9Aq8tUtLhfHRl4deANfpdaoK46Dat++D92lMlrsQgCu+ijDscM0mIG9XkzPsrwIDAQAB";
    private static String priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALD0rTZBzkH7Oh8S0ojfGT/SLnwjCGNcoywd7HHfCCcZeRiAgxW+Ks9I27qXNJmJ2Yc82ua1iY56gGTbwwFrkDoyxSekG8TIfNrjXx8HtBvP8On0Cry1S0uF8dGXh14A1+l1qgrjoNq374P3aUyWuxCAK76KMOxwzSYgb1eTM+yvAgMBAAECgYA1jbcmJoA/o8KRSOcCmVJQ6FE9LHULbSvM3DmEtNUI0EK/0ZTabbUTCp/FNqGqnfeBYD93dxVsU1KsM34u3djF42dTj+mLb7dADU8GtqstRty2EZyMAeLI9HWpOlN1+Fi10ldCEHPgPyT++Rg6AVvzfus8bbxZh6RBZprI1EI2YQJBANVuGVjjhkPtEa3HZlZgtG9nMnGQt6pXbjEjQhU8o8Akf7lMZaFsvqFh9KXaV6Nqz+kFJJpeEB3wVWIhSOOlnVcCQQDUQCwED7LLdu4KARhQCIRxhh3b6kPX4btrzKeEMXo1xcplidwx8PvOnk/QeL4Mgj5VVz5pqCxfseypzlXyCjxpAkEAhKSKSD4PQcPiStgz99beDKZcKUxBeTIhH5TECyLGujF015ptRyeoSe/w9ep+sOc/E5jIqM9wwMqOpg/9ls1kvQJAW57/4Rjts0q5YnQqjJHAqkicpwuvEYeNofEL9tJs0L16qu4hsSU3bW+FQJZ+Y2PtiGVKuc0wqnZhlMNO8lZc0QJBAMZhyMHMLonnmGS2TSC0thAHfPKo84HmZuZmQkiZWNBFlAZNF4lwBhtsnVpY1WV3rbi/ThhMrCvz8LNlkMtkM4k=";
    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
//    public static void genKeyPair() throws NoSuchAlgorithmException {
//        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
//        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
//        // 初始化密钥对生成器，密钥大小为96-1024位
//        keyPairGen.initialize(1024,new SecureRandom());
//        // 生成一个密钥对，保存在keyPair中
//        KeyPair keyPair = keyPairGen.generateKeyPair();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
//        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
//        // 得到私钥字符串
//        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
//        // 将公钥和私钥保存到Map
//        keyMap.put(0,publicKeyString);  //0表示公钥
//        keyMap.put(1,privateKeyString);  //1表示私钥
//    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(pubKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥 
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息 
     */
    public static String decrypt(String str) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(priKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }
    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
//        genKeyPair();
        //加密字符串
        String message = "panda";
        String messageEn = encrypt(message);
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn);
        System.out.println("还原后的字符串为:" + messageDe);
    }


}
