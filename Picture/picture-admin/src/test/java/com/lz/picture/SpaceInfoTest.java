package com.lz.picture;

import com.lz.picture.utils.SpaceAuthUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

/**
 * 空间测试类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-01  17:19
 * @Version: 1.0
 */
@SpringBootTest
public class SpaceInfoTest {
    @Resource
    private SpaceAuthUtils spaceAuthUtils;

    @Test
    public void getSpaceMemberPerm() {
        Set<String> spaceMemberPerm = spaceAuthUtils.getSpaceMemberPerm("2");
        System.out.println(spaceMemberPerm);
        boolean b = spaceAuthUtils.checkSpaceMemberPerm("2", spaceAuthUtils.buildSpaceMemberPerm("2", "1939242093233516546", "1"));
        System.out.println("b = " + b);
    }
}
