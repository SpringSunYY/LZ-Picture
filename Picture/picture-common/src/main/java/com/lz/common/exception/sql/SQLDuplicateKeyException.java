package com.lz.common.exception.sql;

import java.io.Serial;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Project: Picture
 * Package: com.lz.common.exception.sql
 * Author: YY
 * CreateTime: 2025-02-28  22:05
 * Description: SQLDuplicateException
 * Version: 1.0
 */
public class SQLDuplicateKeyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public SQLDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);  // 传递原始异常作为原因
    }

    public SQLDuplicateKeyException(String message) {
        super(message);
    }
}
