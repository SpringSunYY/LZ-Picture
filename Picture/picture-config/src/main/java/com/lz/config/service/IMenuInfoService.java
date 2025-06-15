package com.lz.config.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.config.model.domain.MenuInfo;
import com.lz.config.model.dto.menuInfo.MenuInfoQuery;
import com.lz.config.model.vo.menuInfo.MenuInfoVo;

import java.util.List;

/**
 * 菜单信息Service接口
 *
 * @author YY
 * @date 2025-03-30
 */
public interface IMenuInfoService extends IService<MenuInfo> {
    //region mybatis代码

    /**
     * description: 重置缓存
     * author: YY
     * method: initMenuInfoCache
     * date: 2025/4/2 10:29
     * param:
     * return: int
     **/
    int initMenuInfoCache();

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
     * 批量删除菜单信息
     *
     * @param menuIds 需要删除的菜单信息主键集合
     * @return 结果
     */
    public int deleteMenuInfoByMenuIds(Long[] menuIds);

    /**
     * 删除菜单信息信息
     *
     * @param menuId 菜单信息主键
     * @return 结果
     */
    public int deleteMenuInfoByMenuId(Long menuId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param menuInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<MenuInfo> getQueryWrapper(MenuInfoQuery menuInfoQuery);

    /**
     * 转换vo
     *
     * @param menuInfoList MenuInfo集合
     * @return MenuInfoVO集合
     */
    List<MenuInfoVo> convertVoList(List<MenuInfo> menuInfoList);

    /**
     * 校验菜单
     *
     * @param permission
     * @return
     */
    boolean checkMenu(String permission);

    /**
     * 获取菜单信息
     *
     * @param
     * @return List<MenuInfo>
     * @author: YY
     * @method: getMenuInfo
     * @date: 2025/6/15 21:36
     **/
    List<MenuInfo> getMenuInfo();
}
