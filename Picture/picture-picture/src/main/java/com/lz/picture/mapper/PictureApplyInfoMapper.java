package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureApplyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 图片申请信息Mapper接口
 *
 * @author YY
 * @date 2025-06-17
 */
public interface PictureApplyInfoMapper extends BaseMapper<PictureApplyInfo>
{
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
     * 删除图片申请信息
     *
     * @param applyId 图片申请信息主键
     * @return 结果
     */
    public int deletePictureApplyInfoByApplyId(String applyId);

    /**
     * 批量删除图片申请信息
     *
     * @param applyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureApplyInfoByApplyIds(String[] applyIds);
}
