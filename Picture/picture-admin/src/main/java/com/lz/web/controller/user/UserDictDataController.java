package com.lz.web.controller.user;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.domain.dictData.SysDictDataVo;
import com.lz.common.core.domain.entity.SysDictData;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.system.service.ISysDictDataService;
import com.lz.system.service.ISysDictTypeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 * 
 * @author YY
 */
@RestController
@RequestMapping("/user/system/dict/data")
public class UserDictDataController extends BaseController
{

    @Autowired
    private ISysDictTypeService dictTypeService;


    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        List<SysDictDataVo> sysDictDataVos = SysDictDataVo.objToVo(data);
        if (StringUtils.isNull(sysDictDataVos))
        {
            sysDictDataVos = new ArrayList<SysDictDataVo>();
        }
        return success(sysDictDataVos);
    }

}
