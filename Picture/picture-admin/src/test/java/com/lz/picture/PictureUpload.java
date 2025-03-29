package com.lz.picture;

import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.manager.file.model.PictureResponse;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

/**
 * Project: Picture
 * Package: com.lz.picture
 * Author: YY
 * CreateTime: 2025-03-27  11:05
 * Description: PictureUpload
 * Version: 1.0
 */
@SpringBootTest
public class PictureUpload {
    @Resource
    private PictureUploadManager pictureUploadManager;

    @Test
    public void uploadPicture() {
        File file = new File("D:\\壁纸\\YY00075T.JPG");
        String name = file.getName();
        PictureResponse pictureResponse = pictureUploadManager.uploadPicture(file);
        System.out.println(pictureResponse);
    }
}
