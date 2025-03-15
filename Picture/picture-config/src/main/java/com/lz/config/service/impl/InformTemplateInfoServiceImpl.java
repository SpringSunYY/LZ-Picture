package com.lz.config.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import com.lz.config.model.dto.informTemplateInfo.InformTemplateInfoHistory;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.config.mapper.InformTemplateInfoMapper;
import com.lz.config.model.domain.InformTemplateInfo;
import com.lz.config.service.IInformTemplateInfoService;
import com.lz.config.model.dto.informTemplateInfo.InformTemplateInfoQuery;
import com.lz.config.model.vo.informTemplateInfo.InformTemplateInfoVo;

/**
 * 通知模版Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-14
 */
@Service
public class InformTemplateInfoServiceImpl extends ServiceImpl<InformTemplateInfoMapper, InformTemplateInfo> implements IInformTemplateInfoService {
    @Resource
    private InformTemplateInfoMapper informTemplateInfoMapper;

    //region mybatis代码

    /**
     * 查询通知模版
     *
     * @param templateId 通知模版主键
     * @return 通知模版
     */
    @Override
    public InformTemplateInfo selectInformTemplateInfoByTemplateId(Long templateId) {
        return informTemplateInfoMapper.selectInformTemplateInfoByTemplateId(templateId);
    }

    /**
     * 查询通知模版列表
     *
     * @param informTemplateInfo 通知模版
     * @return 通知模版
     */
    @Override
    public List<InformTemplateInfo> selectInformTemplateInfoList(InformTemplateInfo informTemplateInfo) {
        return informTemplateInfoMapper.selectInformTemplateInfoList(informTemplateInfo);
    }

    /**
     * 新增通知模版
     *
     * @param informTemplateInfo 通知模版
     * @return 结果
     */
    @Override
    public int insertInformTemplateInfo(InformTemplateInfo informTemplateInfo) {
        informTemplateInfo.setTemplateVersion(1L);
        //存储历史记录
        InformTemplateInfoHistory informTemplateInfoHistory = InformTemplateInfoHistory.objToHistory(informTemplateInfo);
        HashMap<Long, String> historyMap = new HashMap<>();
        historyMap.put(informTemplateInfo.getTemplateVersion(),informTemplateInfoHistory.toString());
        //转换为String，后续好装换回历史记录对象
        String history = JSONObject.toJSONString(historyMap);
        informTemplateInfo.setTemplateVersionHistory(history);
        informTemplateInfo.setCreateBy(SecurityUtils.getUsername());
        informTemplateInfo.setCreateTime(DateUtils.getNowDate());
        return informTemplateInfoMapper.insertInformTemplateInfo(informTemplateInfo);
    }

    /**
     * 修改通知模版
     *
     * @param informTemplateInfo 通知模版
     * @return 结果
     */
    @Override
    public int updateInformTemplateInfo(InformTemplateInfo informTemplateInfo) {
        informTemplateInfo.setUpdateTime(DateUtils.getNowDate());
        return informTemplateInfoMapper.updateInformTemplateInfo(informTemplateInfo);
    }

    /**
     * 批量删除通知模版
     *
     * @param templateIds 需要删除的通知模版主键
     * @return 结果
     */
    @Override
    public int deleteInformTemplateInfoByTemplateIds(Long[] templateIds) {
        return informTemplateInfoMapper.deleteInformTemplateInfoByTemplateIds(templateIds);
    }

    /**
     * 删除通知模版信息
     *
     * @param templateId 通知模版主键
     * @return 结果
     */
    @Override
    public int deleteInformTemplateInfoByTemplateId(Long templateId) {
        return informTemplateInfoMapper.deleteInformTemplateInfoByTemplateId(templateId);
    }
    //endregion

    @Override
    public String getExample(InformTemplateInfo informTemplateInfo) {
        String content = informTemplateInfo.getContent();
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        String variables = informTemplateInfo.getVariables();
        if (StringUtils.isEmpty(variables)) {
            return content;
        }
        //把variables转成map
        Map<String, String> variableMap = new HashMap<>();
        // variables转成map variables{"userName":"YY","tell":"你好"}
        //1、去掉收尾空格和大括号
        String replaceAll = variables.trim().replaceAll("^\\{|}", "");
        //2、用逗号分割 跳过肯呢个嵌套的逗号
        String[] pairs = replaceAll.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String pair : pairs) {
            // 3. 按冒号分割键值（跳过可能嵌套的冒号）
            String[] keyValue = pair.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 2);
            if (keyValue.length != 2) {
                continue; // 忽略无效格式
            }
            //4、去除键和值收尾空格和引号
            // 4. 去除键和值的首尾空格及引号
            String key = keyValue[0].trim().replaceAll("^\"|\"$", "");
            String value = keyValue[1].trim().replaceAll("^\"|\"$", "");

            variableMap.put(key, value);
        }
        return StringUtils.parseTemplate(content, variableMap);
    }

    @Override
    public QueryWrapper<InformTemplateInfo> getQueryWrapper(InformTemplateInfoQuery informTemplateInfoQuery) {
        QueryWrapper<InformTemplateInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = informTemplateInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long templateId = informTemplateInfoQuery.getTemplateId();
        queryWrapper.eq(StringUtils.isNotNull(templateId), "template_id", templateId);

        String templateName = informTemplateInfoQuery.getTemplateName();
        queryWrapper.like(StringUtils.isNotEmpty(templateName), "template_name", templateName);

        String locale = informTemplateInfoQuery.getLocale();
        queryWrapper.eq(StringUtils.isNotEmpty(locale), "locale", locale);

        String templateType = informTemplateInfoQuery.getTemplateType();
        queryWrapper.eq(StringUtils.isNotEmpty(templateType), "template_type", templateType);

        String channel = informTemplateInfoQuery.getChannel();
        queryWrapper.eq(StringUtils.isNotEmpty(channel), "channel", channel);

        String status = informTemplateInfoQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status), "status", status);

        String createBy = informTemplateInfoQuery.getCreateBy();
        queryWrapper.like(StringUtils.isNotEmpty(createBy), "create_by", createBy);

        Date createTime = informTemplateInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String updateBy = informTemplateInfoQuery.getUpdateBy();
        queryWrapper.like(StringUtils.isNotEmpty(updateBy), "update_by", updateBy);

        Date updateTime = informTemplateInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<InformTemplateInfoVo> convertVoList(List<InformTemplateInfo> informTemplateInfoList) {
        if (StringUtils.isEmpty(informTemplateInfoList)) {
            return Collections.emptyList();
        }
        return informTemplateInfoList.stream().map(InformTemplateInfoVo::objToVo).collect(Collectors.toList());
    }

}
