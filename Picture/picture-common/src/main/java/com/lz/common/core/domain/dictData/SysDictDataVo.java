package com.lz.common.core.domain.dictData;

import com.lz.common.core.domain.entity.SysDictData;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典数据表 sys_dict_data
 *
 * @author YY
 */
@Data
public class SysDictDataVo implements Serializable {
    @Serial
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
        if (StringUtils.isNull(sysDictData)) {
            return new SysDictDataVo();
        }
        SysDictDataVo sysDictDataVo = new SysDictDataVo();
        BeanUtils.copyBeanProp(sysDictDataVo, sysDictData);
        return sysDictDataVo;
    }

    /**
     * 转换vo数组
     */
    public static List<SysDictDataVo> objToVo(List<SysDictData> sysDictDataList) {
        if (StringUtils.isEmpty(sysDictDataList)) {
            return new ArrayList<>();
        }
        return sysDictDataList.stream().map(SysDictDataVo::objToVo).toList();
    }
}
