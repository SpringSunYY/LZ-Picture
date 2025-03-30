package com.lz.config.mapper;

import java.util.List;
import com.lz.config.model.domain.MenuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 菜单信息Mapper接口
 *
 * @author YY
 * @date 2025-03-30
 */
public interface MenuInfoMapper extends BaseMapper<MenuInfo>
{
    /**
     * 查询菜单信息
     *
     * @param menuId 菜单信息主键
     * @return 菜单信息
     */
    public MenuInfo selectMenuInfoByMenuId(Long menuId);

    /**
     * 查询菜单信息列表
     *
     * @param menuInfo 菜单信息
     * @return 菜单信息集合
     */
    public List<MenuInfo> selectMenuInfoList(MenuInfo menuInfo);

    /**
     * 新增菜单信息
     *
     * @param menuInfo 菜单信息
     * @return 结果
     */
    public int insertMenuInfo(MenuInfo menuInfo);

    /**
     * 修改菜单信息
     *
     * @param menuInfo 菜单信息
     * @return 结果
     */
    public int updateMenuInfo(MenuInfo menuInfo);

    /**
     * 删除菜单信息
     *
     * @param menuId 菜单信息主键
     * @return 结果
     */
    public int deleteMenuInfoByMenuId(Long menuId);

    /**
     * 批量删除菜单信息
     *
     * @param menuIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMenuInfoByMenuIds(Long[] menuIds);
}
