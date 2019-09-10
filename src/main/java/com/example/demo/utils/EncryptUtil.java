package com.example.demo.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 加密算法
 * https://www.cnblogs.com/huangzedong/p/10098196.html
 */
public class EncryptUtil {

    //KeyGenerator 提供对称密钥生成器的功能，支持各种算法
    private KeyGenerator keygen;
    //SecretKey 负责保存对称密钥
    private SecretKey deskey;
    //根据自定义key，生成的特定密钥
    private SecretKeySpec symkey;

    //Cipher负责完成加密或解密工作
    private Cipher cipher;

    //负责保存加密的结果
    private byte[] cipherByte;
    private String cipherString;


    public EncryptUtil(){
        //非对称或单向算法的初始化
    }

    /**
     * 对称算法的构造器初始化
     * @param symWay 对称加密算法：DES、3DES（DESede）、AES
     */
    public EncryptUtil(String symWay, String symKeyCustom){
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        try {
            //实例化支持对称算法的密钥生成器
            //keygen = KeyGenerator.getInstance(symWay);
            //生成对称密钥
            //deskey = keygen.generateKey();
            //上面方法是属于随机生成密钥，则每次加密获取到的密文都是不一样的，故不采用
            symkey = getKey(symKeyCustom.getBytes("utf-8"), symWay);

            //生成Cipher对象,指定其支持的算法
            cipher = Cipher.getInstance(symWay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对称加密算法：第一种 DES算法
     * 被成为美国数据加密标准
     * 明文按64位进行分组, 密钥长64位，密钥事实上是56位参与DES运算（第8、16、24、32、40、48、56、64位是校验位， 使得每个密钥都有奇数个1）
     * 分组后的明文组和56位的密钥按位替代或交换的方法形成密文组的加密方法。
     */
    //对字符串加密
    public String encrytorSym(String context){
        if(context == null || "".equals(context.trim())){
            return null;
        }
        try {
            //根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
            cipher.init(Cipher.ENCRYPT_MODE, symkey);

            //加密，结果保存进cipherString
            byte[] bytes = context.getBytes("utf-8");
            cipherByte = cipher.doFinal(bytes);
            //字节数组转字符串
            cipherString = byteArrToHexStr(cipherByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cipherString;
    }

    //对字符串解密
    public String decryptorSym(String contextDecryp){
        if(contextDecryp == null || "".equals(contextDecryp.trim())){
            return null;
        }
        try {
            //根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示解密模式
            cipher.init(Cipher.DECRYPT_MODE, symkey);

            byte[] buff  = hexStrToByteArr(contextDecryp);
            cipherByte = cipher.doFinal(buff);
            cipherString = new String(cipherByte, "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        return cipherString;
    }


    /**
     * 对称加密算法：第二种 3DES算法
     * 是DES向AES过渡的加密算法，是DES的一个更安全的变形。它以DES为基本模块，通过组合分组方法设计出分组加密算法，其具体实现如下：
     * 设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的密钥，P代表明文，C代表密文，　　
     * 加密过程为：C=Ek3(Dk2(Ek1(P)))
     * 解密过程为：P=Dk1((EK2(Dk3(C)))
     * 注意：DES算法传递的DES，3DES传递的DESede（way）
     */


    /**
     * 对称加密算法：第三种 AES
     * 高级加密标准，代替DES
     */


    /**
     * 非对称加密算法：第一种 RSA公钥加密算法   2.DSA算法
     * 公钥数据加密标准，能够抵抗到目前为止已知的所有密码攻击
     * RSA算法基于一个十分简单的数论事实：将两个大素数相乘十分容易，但那时想要对其乘积进行因式分解却极其困难，
     * 因此可以将乘积公开作为加密密钥。
     */
    //加密
    public String encryptAsym(String asymWay, RSAPublicKey publicKey, String context){
        try{
            if(publicKey!=null){
                //Cipher负责完成加密或解密工作，基于RSA
                Cipher cipher = Cipher.getInstance(asymWay);

                //根据公钥，对Cipher对象进行初始化
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                byte[] resultBytes = cipher.doFinal(context.getBytes("utf-8"));
                return byteArrToHexStr(resultBytes);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //解密
    protected String decryptAsym(String asymWay, RSAPrivateKey privateKey, String contextDecryp){
        try {
            if (privateKey != null) {
                //Cipher负责完成加密或解密工作，基于RSA
                Cipher cipher = Cipher.getInstance(asymWay);

                //根据私钥，对Cipher对象进行初始化
                cipher.init(Cipher.DECRYPT_MODE, privateKey);

                byte[] resultBytes = cipher.doFinal(hexStrToByteArr(contextDecryp));
                return new String(resultBytes, "utf-8");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单向加密（信息摘要）：第一种 MD5算法
     * 哈希算法，不可逆，即明文加密成密文，密文不能解密成原明文，一般用于密码的保存；双向加密，一般用于证件号的保存
     * 让大容量信息在用数字签名软件签署私人密钥前被"压缩"成一种保密的格式
     * （就是把一个任意长度的字节串变换成一定长的十六进制数字串）
     */
    //加密
    public String eccryptSingle(String singleWay, String context){
        try {
            //根据MD5算法生成MessageDigest对象
            MessageDigest md5 = MessageDigest.getInstance(singleWay);

            byte[] srcBytes = context.getBytes();
            //使用srcBytes更新摘要
            md5.update(srcBytes);

            //完成哈希计算，得到result
            byte[] resultBytes = md5.digest();
            return byteArrToHexStr(resultBytes);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args) throws Exception {
        //对称算法
        String[] symWays = new String[]{"DES", "DESede", "AES"};
        String symWay = symWays[2];
        //初始化 生成特定的密钥
        String symKeyCustom = "ShaoHappyGirl";
        EncryptUtil des = new EncryptUtil(symWay, symKeyCustom);
        String context ="不负时光不负自己abc123";
        String encrytorSym = des.encrytorSym(context); //加密
        String decryptorSym = des.decryptorSym(encrytorSym); //解密

        System.out.println("明文是:" + context);
        System.out.println("对称加密后:" + encrytorSym);
        System.out.println("对称解密后:" + decryptorSym);

        //非对称算法
        String[] asymWays = new String[]{"RSA", "DSA"};
        String asymWay = asymWays[0];
        EncryptUtil encryptUtil = new EncryptUtil();

        //KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(asymWay);
        //初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024);
        //生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        //得到公钥
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();

        String encryptAsym = encryptUtil.encryptAsym(asymWay, publicKey, context); //加密
        String decryptAsym = encryptUtil.decryptAsym(asymWay, privateKey, encryptAsym); //解密
        //私钥和公钥是随机生成的，故密文每次不一样
        System.out.println("非对称加密后:" + encryptAsym);
        System.out.println("非对称解密后:" + decryptAsym);

        //单向加密 获取的密文不变
        String[] singleWays = new String[]{"MD5", "SHA"};
        String singleWay = singleWays[0];
        String eccryptMD5 = encryptUtil.eccryptSingle(singleWay, context);
        System.out.println("MD5加密后:" + eccryptMD5);

    }


    //把自定义key，封装成对应算法的对称密钥，只要key不变，该密钥就不变
    private  SecretKeySpec getKey(byte[] arrBTmp, String symWay){
        //DES算法：密钥是8位
        int length = 8;
        if(!"DES".equals(symWay)){
           //AES算法：32 24 16位；3DES算法：24位
           length = 24;
        }
        if("AES".equals(symWay)){
            try {
                //换种方式，生成更加复杂的特定密钥
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(arrBTmp);
                kgen.init(128, random);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

                return key;
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            byte[] arrB = new byte[length];
            for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
                arrB[i] = arrBTmp[i];
            }
            SecretKeySpec key = new SecretKeySpec(arrB, symWay);

            return key;
        }

        return null;
    }


    //字节数组转字符串
    private String byteArrToHexStr(byte[] arrB){
        int iLen = arrB.length;
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            if (intTmp < 16) {
                sb.append(0);
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    //字符串转字节数组
    private byte[] hexStrToByteArr(String strln) throws Exception {
        byte[] arrB = strln.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }


}
