package com.lz.picture.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoVo;
import com.lz.system.service.ISysConfigService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.enums.BusinessType;
import com.lz.picture.model.domain.UserViewLogInfo;
import com.lz.picture.model.vo.userViewLogInfo.UserViewLogInfoVo;
import com.lz.picture.model.dto.userViewLogInfo.UserViewLogInfoQuery;
import com.lz.picture.model.dto.userViewLogInfo.UserViewLogInfoInsert;
import com.lz.picture.model.dto.userViewLogInfo.UserViewLogInfoEdit;
import com.lz.picture.service.IUserViewLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

import static com.lz.common.constant.ConfigConstants.PICTURE_P;

/**
 * 用户浏览记录Controller
 *
 * @author YY
 * @date 2025-04-12
 */
@RestController
@RequestMapping("/admin/picture/userViewLogInfo")
public class UserViewLogInfoController extends BaseController {
    @Resource
    private IUserViewLogInfoService userViewLogInfoService;
    @Resource
    private ISysConfigService sysConfigService;

    /**
     * 查询用户浏览记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:userViewLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserViewLogInfoQuery userViewLogInfoQuery) {
        UserViewLogInfo userViewLogInfo = UserViewLogInfoQuery.queryToObj(userViewLogInfoQuery);
        startPage();
        List<UserViewLogInfo> list = userViewLogInfoService.selectUserViewLogInfoList(userViewLogInfo);
        List<UserViewLogInfoVo> listVo = list.stream().map(UserViewLogInfoVo::objToVo).collect(Collectors.toList());
        String inCache = sysConfigService.selectConfigByKey(PICTURE_P);
        for (UserViewLogInfoVo vo : listVo) {
            vo.setTargetCover(vo.getTargetCover() + "?x-oss-process=image/resize,p_" + inCache);
        }
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户浏览记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:userViewLogInfo:export')")
    @Log(title = "用户浏览记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserViewLogInfoQuery userViewLogInfoQuery) {
        UserViewLogInfo userViewLogInfo = UserViewLogInfoQuery.queryToObj(userViewLogInfoQuery);
        List<UserViewLogInfo> list = userViewLogInfoService.selectUserViewLogInfoList(userViewLogInfo);
        ExcelUtil<UserViewLogInfo> util = new ExcelUtil<UserViewLogInfo>(UserViewLogInfo.class);
        util.exportExcel(response, list, "用户浏览记录数据");
    }

    /**
     * 获取用户浏览记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:userViewLogInfo:query')")
    @GetMapping(value = "/{viewId}")
    public AjaxResult getInfo(@PathVariable("viewId") String viewId) {
        UserViewLogInfo userViewLogInfo = userViewLogInfoService.selectUserViewLogInfoByViewId(viewId);
        return success(UserViewLogInfoVo.objToVo(userViewLogInfo));
    }

    /**
     * 新增用户浏览记录
     */
    @PreAuthorize("@ss.hasPermi('picture:userViewLogInfo:add')")
    @Log(title = "用户浏览记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserViewLogInfoInsert userViewLogInfoInsert) {
        UserViewLogInfo userViewLogInfo = UserViewLogInfoInsert.insertToObj(userViewLogInfoInsert);
        return toAjax(userViewLogInfoService.insertUserViewLogInfo(userViewLogInfo));
    }

    /**
     * 修改用户浏览记录
     */
    @PreAuthorize("@ss.hasPermi('picture:userViewLogInfo:edit')")
    @Log(title = "用户浏览记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserViewLogInfoEdit userViewLogInfoEdit) {
        UserViewLogInfo userViewLogInfo = UserViewLogInfoEdit.editToObj(userViewLogInfoEdit);
        return toAjax(userViewLogInfoService.updateUserViewLogInfo(userViewLogInfo));
    }

    /**
     * 删除用户浏览记录
     */
    @PreAuthorize("@ss.hasPermi('picture:userViewLogInfo:remove')")
    @Log(title = "用户浏览记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{viewIds}")
    public AjaxResult remove(@PathVariable String[] viewIds) {
        return toAjax(userViewLogInfoService.deleteUserViewLogInfoByViewIds(viewIds));
    }
}
