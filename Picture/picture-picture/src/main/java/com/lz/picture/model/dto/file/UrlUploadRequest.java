package com.lz.picture.model.dto.file;

import lombok.Data;

/**
 * UrlUploadRequest
 *
 * @author YY
 * @version 1.0
 * @project Picture
 * @package com.lz.picture.model.dto.file
 * @createTime 2025-04-24  23:50
 */
@Data
public class UrlUploadRequest {
    private String url;
    private String pictureId;
}
