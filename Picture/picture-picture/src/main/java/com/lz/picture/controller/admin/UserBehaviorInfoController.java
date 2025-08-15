package com.lz.picture.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.config.OssConfig;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.model.dto.userBehaviorInfo.UserBehaviorInfoEdit;
import com.lz.picture.model.dto.userBehaviorInfo.UserBehaviorInfoInsert;
import com.lz.picture.model.dto.userBehaviorInfo.UserBehaviorInfoQuery;
import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoVo;
import com.lz.picture.service.IUserBehaviorInfoService;
import com.lz.system.service.ISysConfigService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.lz.common.constant.ConfigConstants.PICTURE_P;

/**
 * 用户行为Controller
 *
 * @author YY
 * @date 2025-04-12
 */
@RestController
@RequestMapping("/admin/picture/userBehaviorInfo")
public class UserBehaviorInfoController extends BaseController {
    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    @Resource
    private ISysConfigService sysConfigService;

    /**
     * 查询用户行为列表
     */
    @PreAuthorize("@ss.hasPermi('picture:userBehaviorInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserBehaviorInfoQuery userBehaviorInfoQuery) {
        UserBehaviorInfo userBehaviorInfo = UserBehaviorInfoQuery.queryToObj(userBehaviorInfoQuery);
        startPage();
        List<UserBehaviorInfo> list = userBehaviorInfoService.selectUserBehaviorInfoList(userBehaviorInfo);
        List<UserBehaviorInfoVo> listVo = list.stream().map(UserBehaviorInfoVo::objToVo).collect(Collectors.toList());
        String inCache = sysConfigService.selectConfigByKey(PICTURE_P);
        for (UserBehaviorInfoVo vo : listVo) {
            vo.setTargetCover(OssConfig.builderPictureUrl(vo.getTargetCover(), inCache));
        }
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户行为列表
     */
    @PreAuthorize("@ss.hasPermi('picture:userBehaviorInfo:export')")
    @Log(title = "用户行为", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserBehaviorInfoQuery userBehaviorInfoQuery) {
        UserBehaviorInfo userBehaviorInfo = UserBehaviorInfoQuery.queryToObj(userBehaviorInfoQuery);
        List<UserBehaviorInfo> list = userBehaviorInfoService.selectUserBehaviorInfoList(userBehaviorInfo);
        ExcelUtil<UserBehaviorInfo> util = new ExcelUtil<UserBehaviorInfo>(UserBehaviorInfo.class);
        util.exportExcel(response, list, "用户行为数据");
    }

    /**
     * 获取用户行为详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:userBehaviorInfo:query')")
    @GetMapping(value = "/{behaviorId}")
    public AjaxResult getInfo(@PathVariable("behaviorId") String behaviorId) {
        UserBehaviorInfo userBehaviorInfo = userBehaviorInfoService.selectUserBehaviorInfoByBehaviorId(behaviorId);
        return success(UserBehaviorInfoVo.objToVo(userBehaviorInfo));
    }

    /**
     * 新增用户行为
     */
    @PreAuthorize("@ss.hasPermi('picture:userBehaviorInfo:add')")
    @Log(title = "用户行为", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserBehaviorInfoInsert userBehaviorInfoInsert) {
        UserBehaviorInfo userBehaviorInfo = UserBehaviorInfoInsert.insertToObj(userBehaviorInfoInsert);
        return toAjax(userBehaviorInfoService.insertUserBehaviorInfo(userBehaviorInfo));
    }

    /**
     * 修改用户行为
     */
    @PreAuthorize("@ss.hasPermi('picture:userBehaviorInfo:edit')")
    @Log(title = "用户行为", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserBehaviorInfoEdit userBehaviorInfoEdit) {
        UserBehaviorInfo userBehaviorInfo = UserBehaviorInfoEdit.editToObj(userBehaviorInfoEdit);
        return toAjax(userBehaviorInfoService.updateUserBehaviorInfo(userBehaviorInfo));
    }

    /**
     * 删除用户行为
     */
    @PreAuthorize("@ss.hasPermi('picture:userBehaviorInfo:remove')")
    @Log(title = "用户行为", businessType = BusinessType.DELETE)
    @DeleteMapping("/{behaviorIds}")
    public AjaxResult remove(@PathVariable String[] behaviorIds) {
        return toAjax(userBehaviorInfoService.deleteUserBehaviorInfoByBehaviorIds(behaviorIds));
    }
}
