package com.lz.user.controller.admin;

import java.util.List;
import java.util.stream.Collectors;
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
import com.lz.user.model.domain.UserRelationInfo;
import com.lz.user.model.vo.userRelationInfo.UserRelationInfoVo;
import com.lz.user.model.dto.userRelationInfo.UserRelationInfoQuery;
import com.lz.user.model.dto.userRelationInfo.UserRelationInfoInsert;
import com.lz.user.model.dto.userRelationInfo.UserRelationInfoEdit;
import com.lz.user.service.IUserRelationInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户关系Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/admin/user/userRelationInfo")
public class UserRelationInfoController extends BaseController
{
    @Resource
    private IUserRelationInfoService userRelationInfoService;

    /**
     * 查询用户关系列表
     */
    @PreAuthorize("@ss.hasPermi('user:userRelationInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserRelationInfoQuery userRelationInfoQuery)
    {
        UserRelationInfo userRelationInfo = UserRelationInfoQuery.queryToObj(userRelationInfoQuery);
        startPage();
        List<UserRelationInfo> list = userRelationInfoService.selectUserRelationInfoList(userRelationInfo);
        List<UserRelationInfoVo> listVo= list.stream().map(UserRelationInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户关系列表
     */
    @PreAuthorize("@ss.hasPermi('user:userRelationInfo:export')")
    @Log(title = "用户关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserRelationInfoQuery userRelationInfoQuery)
    {
        UserRelationInfo userRelationInfo = UserRelationInfoQuery.queryToObj(userRelationInfoQuery);
        List<UserRelationInfo> list = userRelationInfoService.selectUserRelationInfoList(userRelationInfo);
        ExcelUtil<UserRelationInfo> util = new ExcelUtil<UserRelationInfo>(UserRelationInfo.class);
        util.exportExcel(response, list, "用户关系数据");
    }

    /**
     * 获取用户关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('user:userRelationInfo:query')")
    @GetMapping(value = "/{relationId}")
    public AjaxResult getInfo(@PathVariable("relationId") String relationId)
    {
        UserRelationInfo userRelationInfo = userRelationInfoService.selectUserRelationInfoByRelationId(relationId);
        return success(UserRelationInfoVo.objToVo(userRelationInfo));
    }

    /**
     * 新增用户关系
     */
    @PreAuthorize("@ss.hasPermi('user:userRelationInfo:add')")
    @Log(title = "用户关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserRelationInfoInsert userRelationInfoInsert)
    {
        UserRelationInfo userRelationInfo = UserRelationInfoInsert.insertToObj(userRelationInfoInsert);
        return toAjax(userRelationInfoService.insertUserRelationInfo(userRelationInfo));
    }

    /**
     * 修改用户关系
     */
    @PreAuthorize("@ss.hasPermi('user:userRelationInfo:edit')")
    @Log(title = "用户关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserRelationInfoEdit userRelationInfoEdit)
    {
        UserRelationInfo userRelationInfo = UserRelationInfoEdit.editToObj(userRelationInfoEdit);
        return toAjax(userRelationInfoService.updateUserRelationInfo(userRelationInfo));
    }

    /**
     * 删除用户关系
     */
    @PreAuthorize("@ss.hasPermi('user:userRelationInfo:remove')")
    @Log(title = "用户关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{relationIds}")
    public AjaxResult remove(@PathVariable String[] relationIds)
    {
        return toAjax(userRelationInfoService.deleteUserRelationInfoByRelationIds(relationIds));
    }
}
