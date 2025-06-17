package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.PictureApplyInfo;
import com.lz.picture.model.vo.pictureApplyInfo.PictureApplyInfoVo;
import com.lz.picture.model.dto.pictureApplyInfo.PictureApplyInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 图片申请信息Service接口
 *
 * @author YY
 * @date 2025-06-17
 */
public interface IPictureApplyInfoService extends IService<PictureApplyInfo>
{
    //region mybatis代码
    /**
     * 查询图片申请信息
     *
     * @param applyId 图片申请信息主键
     * @return 图片申请信息
     */
    public PictureApplyInfo selectPictureApplyInfoByApplyId(String applyId);

    /**
     * 查询图片申请信息列表
     *
     * @param pictureApplyInfo 图片申请信息
     * @return 图片申请信息集合
     */
    public List<PictureApplyInfo> selectPictureApplyInfoList(PictureApplyInfo pictureApplyInfo);

    /**
     * 新增图片申请信息
     *
     * @param pictureApplyInfo 图片申请信息
     * @return 结果
     */
    public int insertPictureApplyInfo(PictureApplyInfo pictureApplyInfo);

    /**
     * 修改图片申请信息
     *
     * @param pictureApplyInfo 图片申请信息
     * @return 结果
     */
    public int updatePictureApplyInfo(PictureApplyInfo pictureApplyInfo);

    /**
     * 批量删除图片申请信息
     *
     * @param applyIds 需要删除的图片申请信息主键集合
     * @return 结果
     */
    public int deletePictureApplyInfoByApplyIds(String[] applyIds);

    /**
     * 删除图片申请信息信息
     *
     * @param applyId 图片申请信息主键
     * @return 结果
     */
    public int deletePictureApplyInfoByApplyId(String applyId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureApplyInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureApplyInfo> getQueryWrapper(PictureApplyInfoQuery pictureApplyInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureApplyInfoList PictureApplyInfo集合
     * @return PictureApplyInfoVO集合
     */
    List<PictureApplyInfoVo> convertVoList(List<PictureApplyInfo> pictureApplyInfoList);
}
