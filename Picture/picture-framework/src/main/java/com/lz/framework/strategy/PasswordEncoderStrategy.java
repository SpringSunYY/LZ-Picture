package com.lz.framework.strategy;

/**
 * 加密
 */
public interface PasswordEncoderStrategy {
    boolean matches(String rawPassword, String encodedPassword);
    String encode(String rawPassword);
}