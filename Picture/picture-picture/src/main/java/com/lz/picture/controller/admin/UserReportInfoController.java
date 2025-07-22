package com.lz.picture.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.config.OssConfig;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.picture.model.domain.UserReportInfo;
import com.lz.picture.model.dto.userReportInfo.UserReportInfoAudit;
import com.lz.picture.model.dto.userReportInfo.UserReportInfoEdit;
import com.lz.picture.model.dto.userReportInfo.UserReportInfoInsert;
import com.lz.picture.model.dto.userReportInfo.UserReportInfoQuery;
import com.lz.picture.model.vo.userReportInfo.UserReportInfoVo;
import com.lz.picture.service.IUserReportInfoService;
import com.lz.system.service.ISysConfigService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.lz.common.constant.ConfigConstants.PICTURE_P;

/**
 * 用户举报信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/userReportInfo")
public class UserReportInfoController extends BaseController {
    @Resource
    private IUserReportInfoService userReportInfoService;

    @Resource
    private ISysConfigService sysConfigService;


    /**
     * 查询用户举报信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:userReportInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserReportInfoQuery userReportInfoQuery) {
        UserReportInfo userReportInfo = UserReportInfoQuery.queryToObj(userReportInfoQuery);
        startPage();
        List<UserReportInfo> list = userReportInfoService.selectUserReportInfoList(userReportInfo);
        List<UserReportInfoVo> listVo = list.stream().map(UserReportInfoVo::objToVo).collect(Collectors.toList());
        String inCache = sysConfigService.selectConfigByKey(PICTURE_P);
        for (UserReportInfoVo vo : listVo) {
            vo.setTargetCover(OssConfig.builderUrl(vo.getTargetCover()) + "?x-oss-process=image/resize,p_" + inCache);
        }
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户举报信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:userReportInfo:export')")
    @Log(title = "用户举报信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserReportInfoQuery userReportInfoQuery) {
        UserReportInfo userReportInfo = UserReportInfoQuery.queryToObj(userReportInfoQuery);
        List<UserReportInfo> list = userReportInfoService.selectUserReportInfoList(userReportInfo);
        ExcelUtil<UserReportInfo> util = new ExcelUtil<UserReportInfo>(UserReportInfo.class);
        util.exportExcel(response, list, "用户举报信息数据");
    }

    /**
     * 获取用户举报信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:userReportInfo:query')")
    @GetMapping(value = "/{reportId}")
    public AjaxResult getInfo(@PathVariable("reportId") String reportId) {
        UserReportInfo userReportInfo = userReportInfoService.selectUserReportInfoByReportId(reportId);
        return success(UserReportInfoVo.objToVo(userReportInfo));
    }

    /**
     * 新增用户举报信息
     */
    @PreAuthorize("@ss.hasPermi('picture:userReportInfo:add')")
    @Log(title = "用户举报信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserReportInfoInsert userReportInfoInsert) {
        UserReportInfo userReportInfo = UserReportInfoInsert.insertToObj(userReportInfoInsert);
        return toAjax(userReportInfoService.insertUserReportInfo(userReportInfo));
    }

    /**
     * 修改用户举报信息
     */
    @PreAuthorize("@ss.hasPermi('picture:userReportInfo:edit')")
    @Log(title = "用户举报信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserReportInfoEdit userReportInfoEdit) {
        UserReportInfo userReportInfo = UserReportInfoEdit.editToObj(userReportInfoEdit);
        return toAjax(userReportInfoService.updateUserReportInfo(userReportInfo));
    }

    /**
     * 删除用户举报信息
     */
    @PreAuthorize("@ss.hasPermi('picture:userReportInfo:remove')")
    @Log(title = "用户举报信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{reportIds}")
    public AjaxResult remove(@PathVariable String[] reportIds) {
        return toAjax(userReportInfoService.deleteUserReportInfoByReportIds(reportIds));
    }

    /**
     * 审核用户举报信息
     */
    @PreAuthorize("@ss.hasPermi('picture:userReportInfo:audit')")
    @Log(title = "用户举报信息", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody UserReportInfoAudit userReportInfoAudit) {
        UserReportInfo userReportInfo = UserReportInfoAudit.editToObj(userReportInfoAudit);
        userReportInfo.setReviewUserId(getUserId());
        return toAjax(userReportInfoService.auditUserReportInfo(userReportInfo));
    }
}
