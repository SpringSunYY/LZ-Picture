package com.lz.framework.manager;

/**
 * 加密
 */
public interface PasswordEncoderStrategy {
    boolean matches(String rawPassword, String encodedPassword);
    String encode(String rawPassword);
}