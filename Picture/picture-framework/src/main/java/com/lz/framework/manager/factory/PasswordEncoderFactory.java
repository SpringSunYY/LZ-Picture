package com.lz.framework.manager.factory;

import com.lz.framework.manager.PasswordEncoderStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义解密
 */
@Component
public class PasswordEncoderFactory {
    private final Map<String, PasswordEncoderStrategy> strategies = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        strategies.put("bcrypt", new BCryptStrategy());
        strategies.put("md5", new MD5Strategy());
    }

    public PasswordEncoderStrategy getStrategy(String encryptType) {
        return strategies.getOrDefault(encryptType, strategies.get("bcrypt"));
    }

    // BCrypt实现
    private static class BCryptStrategy implements PasswordEncoderStrategy {
        private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        @Override
        public boolean matches(String raw, String encoded) {
            return encoder.matches(raw, encoded);
        }

        @Override
        public String encode(String raw) {
            return encoder.encode(raw);
        }
    }

    // MD5实现示例
    private static class MD5Strategy implements PasswordEncoderStrategy {
        @Override
        public boolean matches(String raw, String encoded) {
            return DigestUtils.md5DigestAsHex(raw.getBytes()).equals(encoded);
        }

        @Override
        public String encode(String raw) {
            return DigestUtils.md5DigestAsHex(raw.getBytes());
        }
    }
}