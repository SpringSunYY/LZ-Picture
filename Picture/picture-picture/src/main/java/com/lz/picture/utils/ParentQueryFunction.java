package com.lz.picture.utils;

@FunctionalInterface
public interface ParentQueryFunction<T> {
    T apply(String parentId);
}