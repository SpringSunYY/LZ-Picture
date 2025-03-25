package com.lz.points.controller.admin;

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
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.model.vo.accountInfo.AccountInfoVo;
import com.lz.points.model.dto.accountInfo.AccountInfoQuery;
import com.lz.points.model.dto.accountInfo.AccountInfoInsert;
import com.lz.points.model.dto.accountInfo.AccountInfoEdit;
import com.lz.points.service.IAccountInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 积分账户Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/points/accountInfo")
public class AccountInfoController extends BaseController
{
    @Resource
    private IAccountInfoService accountInfoService;

    /**
     * 查询积分账户列表
     */
    @PreAuthorize("@ss.hasPermi('points:accountInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccountInfoQuery accountInfoQuery)
    {
        AccountInfo accountInfo = AccountInfoQuery.queryToObj(accountInfoQuery);
        startPage();
        List<AccountInfo> list = accountInfoService.selectAccountInfoList(accountInfo);
        List<AccountInfoVo> listVo= list.stream().map(AccountInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出积分账户列表
     */
    @PreAuthorize("@ss.hasPermi('points:accountInfo:export')")
    @Log(title = "积分账户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccountInfoQuery accountInfoQuery)
    {
        AccountInfo accountInfo = AccountInfoQuery.queryToObj(accountInfoQuery);
        List<AccountInfo> list = accountInfoService.selectAccountInfoList(accountInfo);
        ExcelUtil<AccountInfo> util = new ExcelUtil<AccountInfo>(AccountInfo.class);
        util.exportExcel(response, list, "积分账户数据");
    }

    /**
     * 获取积分账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:accountInfo:query')")
    @GetMapping(value = "/{accountId}")
    public AjaxResult getInfo(@PathVariable("accountId") String accountId)
    {
        AccountInfo accountInfo = accountInfoService.selectAccountInfoByAccountId(accountId);
        return success(AccountInfoVo.objToVo(accountInfo));
    }

    /**
     * 新增积分账户
     */
    @PreAuthorize("@ss.hasPermi('points:accountInfo:add')")
    @Log(title = "积分账户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AccountInfoInsert accountInfoInsert)
    {
        AccountInfo accountInfo = AccountInfoInsert.insertToObj(accountInfoInsert);
        return toAjax(accountInfoService.insertAccountInfo(accountInfo));
    }

    /**
     * 修改积分账户
     */
    @PreAuthorize("@ss.hasPermi('points:accountInfo:edit')")
    @Log(title = "积分账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AccountInfoEdit accountInfoEdit)
    {
        AccountInfo accountInfo = AccountInfoEdit.editToObj(accountInfoEdit);
        return toAjax(accountInfoService.updateAccountInfo(accountInfo));
    }

    /**
     * 删除积分账户
     */
    @PreAuthorize("@ss.hasPermi('points:accountInfo:remove')")
    @Log(title = "积分账户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{accountIds}")
    public AjaxResult remove(@PathVariable String[] accountIds)
    {
        return toAjax(accountInfoService.deleteAccountInfoByAccountIds(accountIds));
    }
}
