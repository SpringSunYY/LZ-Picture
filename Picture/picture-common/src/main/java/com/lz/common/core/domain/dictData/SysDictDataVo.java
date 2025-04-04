package com.lz.common.core.domain.dictData;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lz.common.annotation.Excel;
import com.lz.common.annotation.Excel.ColumnType;
import com.lz.common.constant.UserConstants;
import com.lz.common.core.domain.BaseEntity;
import com.lz.common.core.domain.entity.SysDictData;
import com.lz.common.utils.bean.BeanUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 字典数据表 sys_dict_data
 *
 * @author YY
 */
@Data
public class SysDictDataVo extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /**
     * 表格字典样式
     */
    private String listClass;

    /**
     * 转换为vo
     */
    public static SysDictDataVo objToVo(SysDictData sysDictData) {
        SysDictDataVo sysDictDataVo = new SysDictDataVo();
        BeanUtils.copyBeanProp(sysDictDataVo, sysDictData);
        return sysDictDataVo;
    }

    /**
     * 转换vo数组
     */
    public static List<SysDictDataVo> objToVo(List<SysDictData> sysDictDataList) {
        return sysDictDataList.stream().map(SysDictDataVo::objToVo).toList();
    }
}
