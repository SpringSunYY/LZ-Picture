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
import com.lz.user.model.domain.UserFriendInfo;
import com.lz.user.model.vo.userFriendInfo.UserFriendInfoVo;
import com.lz.user.model.dto.userFriendInfo.UserFriendInfoQuery;
import com.lz.user.model.dto.userFriendInfo.UserFriendInfoInsert;
import com.lz.user.model.dto.userFriendInfo.UserFriendInfoEdit;
import com.lz.user.service.IUserFriendInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户好友关系Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/admin/user/userFriendInfo")
public class UserFriendInfoController extends BaseController
{
    @Resource
    private IUserFriendInfoService userFriendInfoService;

    /**
     * 查询用户好友关系列表
     */
    @PreAuthorize("@ss.hasPermi('user:userFriendInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserFriendInfoQuery userFriendInfoQuery)
    {
        UserFriendInfo userFriendInfo = UserFriendInfoQuery.queryToObj(userFriendInfoQuery);
        startPage();
        List<UserFriendInfo> list = userFriendInfoService.selectUserFriendInfoList(userFriendInfo);
        List<UserFriendInfoVo> listVo= list.stream().map(UserFriendInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户好友关系列表
     */
    @PreAuthorize("@ss.hasPermi('user:userFriendInfo:export')")
    @Log(title = "用户好友关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserFriendInfoQuery userFriendInfoQuery)
    {
        UserFriendInfo userFriendInfo = UserFriendInfoQuery.queryToObj(userFriendInfoQuery);
        List<UserFriendInfo> list = userFriendInfoService.selectUserFriendInfoList(userFriendInfo);
        ExcelUtil<UserFriendInfo> util = new ExcelUtil<UserFriendInfo>(UserFriendInfo.class);
        util.exportExcel(response, list, "用户好友关系数据");
    }

    /**
     * 获取用户好友关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('user:userFriendInfo:query')")
    @GetMapping(value = "/{relationId}")
    public AjaxResult getInfo(@PathVariable("relationId") String relationId)
    {
        UserFriendInfo userFriendInfo = userFriendInfoService.selectUserFriendInfoByRelationId(relationId);
        return success(UserFriendInfoVo.objToVo(userFriendInfo));
    }

    /**
     * 新增用户好友关系
     */
    @PreAuthorize("@ss.hasPermi('user:userFriendInfo:add')")
    @Log(title = "用户好友关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserFriendInfoInsert userFriendInfoInsert)
    {
        UserFriendInfo userFriendInfo = UserFriendInfoInsert.insertToObj(userFriendInfoInsert);
        return toAjax(userFriendInfoService.insertUserFriendInfo(userFriendInfo));
    }

    /**
     * 修改用户好友关系
     */
    @PreAuthorize("@ss.hasPermi('user:userFriendInfo:edit')")
    @Log(title = "用户好友关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserFriendInfoEdit userFriendInfoEdit)
    {
        UserFriendInfo userFriendInfo = UserFriendInfoEdit.editToObj(userFriendInfoEdit);
        return toAjax(userFriendInfoService.updateUserFriendInfo(userFriendInfo));
    }

    /**
     * 删除用户好友关系
     */
    @PreAuthorize("@ss.hasPermi('user:userFriendInfo:remove')")
    @Log(title = "用户好友关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{relationIds}")
    public AjaxResult remove(@PathVariable String[] relationIds)
    {
        return toAjax(userFriendInfoService.deleteUserFriendInfoByRelationIds(relationIds));
    }
}
