package com.lz.picture.controller.user;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.ServletUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.picture.model.domain.PictureTagInfo;
import com.lz.picture.model.dto.pictureTagInfo.PictureTagInfoEdit;
import com.lz.picture.model.dto.pictureTagInfo.PictureTagInfoInsert;
import com.lz.picture.model.dto.pictureTagInfo.PictureTagInfoQuery;
import com.lz.picture.model.enums.PTagStatus;
import com.lz.picture.model.vo.pictureTagInfo.PictureTagInfoVo;
import com.lz.picture.service.IPictureTagInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.lz.common.core.page.TableSupport.PAGE_SIZE;

/**
 * 图片标签信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/user/picture/pictureTagInfo")
public class UserPictureTagInfoController extends BaseController {
    @Resource
    private IPictureTagInfoService pictureTagInfoService;

    /**
     * 查询图片标签信息列表
     */
    @PreAuthorize("@uss.hasPermi('picture:tag')")
    @GetMapping("/list")
    public TableDataInfo list(PictureTagInfoQuery pictureTagInfoQuery) {
        PictureTagInfo pictureTagInfo = PictureTagInfoQuery.queryToObj(pictureTagInfoQuery);
        startPage();
        //拿到分页参数
        String pageSize = ServletUtils.getParameter(PAGE_SIZE);
        if (StringUtils.isNotEmpty(pageSize)) {
            int pageSizeInt = Integer.parseInt(pageSize);
            if (pageSizeInt > 50) {
                return getDataTable(null);
            }
        }
        pictureTagInfo.setTagsStatus(PTagStatus.TAG_STATUS_0.getValue());
        List<PictureTagInfo> list = pictureTagInfoService.selectPictureTagInfoList(pictureTagInfo);
        List<PictureTagInfoVo> listVo = list.stream().map(PictureTagInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

}
