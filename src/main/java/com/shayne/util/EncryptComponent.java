package com.shayne.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.apache.tomcat.util.security.MD5Encoder;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.shayne.domain.User;

/**
 * MD5工具类
 * @Author WY
 * @Date 2017年12月12日
 */
@Component
public class EncryptComponent {
   
    /** 日志记录对象  */
    private Logger logger = LoggerFactory.getLogger(EncryptComponent.class);

    /** 随机数生成器 */
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
   
    /**
     * 字符串MD运算
     * @param source
     * @return
     * String
     */
    public String baseEncode(String source) {
        // 确定计算方法
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("获取加密对象失败：", e);
        }
        //转换字节数据
        byte[] bytes = null;
        try {
            bytes = md5.digest(source.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("转换MD5字节数据失败：", e);
        }
        // 加密后的字符串
        return MD5Encoder.encode(bytes);
    }
    
    /**
     * Hex encode
     * @param source
     * @return
     * String
     */
    public String hexEncode(String source) {
     // 确定计算方法
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("获取加密对象失败：", e);
        }
        //转换字节数据
        byte[] bytes = Md5Hash.toBytes(source, Md5Hash.PREFERRED_ENCODING);
        bytes = md5.digest(bytes);
        return Hex.encodeToString(bytes);
    }
    
    /**
     * Base64 encode
     * @param source
     * @return
     * String
     */
    public String base64Encode(String source) {
        // 确定计算方法
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("获取加密对象失败：", e);
        }
        //转换字节数据
        byte[] bytes = Md5Hash.toBytes(source, Md5Hash.PREFERRED_ENCODING);
        bytes = md5.digest(bytes);
        return Base64.encodeToString(bytes);
    }
    
    /**
     * 生成随机盐值对密码进行加密
     * @param user
     * @return
     * User
     */
    public User encrypt(User user, String algorithmName, Integer hashIterations) {
        if(null == user || StringUtils.isBlank(algorithmName) || null == hashIterations) {
            logger.error("加密用户为空或加密配置为空");
            throw new ServiceException("加密用户为空或加密配置为空");
        }
        String salt = randomNumberGenerator.nextBytes().toHex();
        user.setSalt(salt);
        ByteSource byteSource = ByteSource.Util.bytes(user.getSalt());
        SimpleHash simpleHash = new SimpleHash(algorithmName, user.getPassword(), 
                byteSource, hashIterations); 
        String newPassword = simpleHash.toHex();
        user.setPassword(newPassword);
        return user;
    }
}
