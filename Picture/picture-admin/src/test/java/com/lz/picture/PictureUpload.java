package com.lz.picture;

import com.lz.common.manager.file.PictureUploadManager;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;

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
//        PictureFileResponse pictureResponse = pictureUploadManager.uploadPicture(file);
//        System.out.println(pictureResponse);
    }

    @Test
    public void deleteFile(){
        ArrayList<String> keys = new ArrayList<>();
//        keys.add("picture/20250310-1906003663118274560-compressed.webp");
//        keys.add("picture/20250310-1906009152556240896-compressed.webp");
//        keys.add("picture/20250310-1906003663118274560.png");
//        keys.add("picture/20250310-1906009152556240896.png");
        pictureUploadManager.deleteFile(keys);
    }
}
