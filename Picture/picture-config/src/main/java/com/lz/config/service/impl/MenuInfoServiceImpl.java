package com.lz.config.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.config.mapper.MenuInfoMapper;
import com.lz.config.model.domain.MenuInfo;
import com.lz.config.service.IMenuInfoService;
import com.lz.config.model.dto.menuInfo.MenuInfoQuery;
import com.lz.config.model.vo.menuInfo.MenuInfoVo;

import static com.lz.common.constant.redis.ConfigRedisConstants.CONFIG_MENU_PERMISSION;
import static com.lz.config.model.enmus.CMenuVisible.MENU_VISIBLE_1;

/**
 * 菜单信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-30
 */
@Service
public class MenuInfoServiceImpl extends ServiceImpl<MenuInfoMapper, MenuInfo> implements IMenuInfoService {
    @Resource
    private MenuInfoMapper menuInfoMapper;

    @Resource
    private RedisCache redisCache;

    //region mybatis代码

    /**
     * 查询菜单信息
     *
     * @param menuId 菜单信息主键
     * @return 菜单信息
     */
    @Override
    public MenuInfo selectMenuInfoByMenuId(Long menuId) {
        return menuInfoMapper.selectMenuInfoByMenuId(menuId);
    }

    /**
     * 查询菜单信息列表
     *
     * @param menuInfo 菜单信息
     * @return 菜单信息
     */
    @Override
    public List<MenuInfo> selectMenuInfoList(MenuInfo menuInfo) {
        return menuInfoMapper.selectMenuInfoList(menuInfo);
    }

    /**
     * 新增菜单信息
     *
     * @param menuInfo 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenuInfo(MenuInfo menuInfo) {
        //查询是否有此菜单名称
        MenuInfo one = this.getOne(new LambdaQueryWrapper<>(MenuInfo.class).eq(MenuInfo::getMenuName, menuInfo.getMenuName()));
        if (StringUtils.isNotNull(one)) {
            throw new RuntimeException("菜单名称重复");
        }
        MenuInfo per = this.getOne(new LambdaQueryWrapper<>(MenuInfo.class).eq(MenuInfo::getPerms, menuInfo.getPerms()));
        if (StringUtils.isNotNull(per)) {
            throw new RuntimeException("权限标识重复");
        }
        menuInfo.setCreateBy(SecurityUtils.getUsername());
        menuInfo.setCreateTime(DateUtils.getNowDate());
        return menuInfoMapper.insertMenuInfo(menuInfo);
    }

    /**
     * 修改菜单信息
     *
     * @param menuInfo 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenuInfo(MenuInfo menuInfo) {
        //修改名称
        MenuInfo one = this.getOne(new LambdaQueryWrapper<>(MenuInfo.class).eq(MenuInfo::getMenuName, menuInfo.getMenuName()));
        if (StringUtils.isNotNull(one) && !one.getMenuId().equals(menuInfo.getMenuId())) {
            throw new RuntimeException("菜单名称重复");
        }
        MenuInfo per = this.getOne(new LambdaQueryWrapper<>(MenuInfo.class).eq(MenuInfo::getPerms, menuInfo.getPerms()));
        if (StringUtils.isNotNull(per) && !per.getMenuId().equals(menuInfo.getMenuId())) {
            throw new RuntimeException("权限标识重复");
        }
        //判断父类是否是自己
        if (menuInfo.getMenuId().toString().equals(menuInfo.getParentId())) {
            throw new RuntimeException("父类不能是自己");
        }
        menuInfo.setUpdateBy(SecurityUtils.getUsername());
        menuInfo.setUpdateTime(DateUtils.getNowDate());
        return menuInfoMapper.updateMenuInfo(menuInfo);
    }

    /**
     * 批量删除菜单信息
     *
     * @param menuIds 需要删除的菜单信息主键
     * @return 结果
     */
    @Override
    public int deleteMenuInfoByMenuIds(Long[] menuIds) {
        //获取所有的数据
        List<MenuInfo> menuInfos = this.listByIds(Arrays.asList(menuIds));
        for (MenuInfo menuInfo : menuInfos) {
            //判断是否有子节点
            if (menuInfoMapper.selectCount(new LambdaQueryWrapper<>(MenuInfo.class).eq(MenuInfo::getParentId, menuInfo.getMenuId())) > 0) {
                throw new RuntimeException(menuInfo.getMenuName() + "存在子节点不允许删除");
            }
        }
        return menuInfoMapper.deleteMenuInfoByMenuIds(menuIds);
    }

    /**
     * 删除菜单信息信息
     *
     * @param menuId 菜单信息主键
     * @return 结果
     */
    @Override
    public int deleteMenuInfoByMenuId(Long menuId) {
        return menuInfoMapper.deleteMenuInfoByMenuId(menuId);
    }

    //endregion
    @Override
    public QueryWrapper<MenuInfo> getQueryWrapper(MenuInfoQuery menuInfoQuery) {
        QueryWrapper<MenuInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = menuInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long menuId = menuInfoQuery.getMenuId();
        queryWrapper.eq(StringUtils.isNotNull(menuId), "menu_id", menuId);

        String menuName = menuInfoQuery.getMenuName();
        queryWrapper.like(StringUtils.isNotEmpty(menuName), "menu_name", menuName);

        String parentId = menuInfoQuery.getParentId();
        queryWrapper.eq(StringUtils.isNotEmpty(parentId), "parent_id", parentId);

        String routeName = menuInfoQuery.getRouteName();
        queryWrapper.like(StringUtils.isNotEmpty(routeName), "route_name", routeName);

        String menuAddress = menuInfoQuery.getMenuAddress();
        queryWrapper.eq(StringUtils.isNotEmpty(menuAddress), "menu_address", menuAddress);

        String isFrame = menuInfoQuery.getIsFrame();
        queryWrapper.eq(StringUtils.isNotEmpty(isFrame), "is_frame", isFrame);

        String isCache = menuInfoQuery.getIsCache();
        queryWrapper.eq(StringUtils.isNotEmpty(isCache), "is_cache", isCache);

        String menuType = menuInfoQuery.getMenuType();
        queryWrapper.eq(StringUtils.isNotEmpty(menuType), "menu_type", menuType);

        String visible = menuInfoQuery.getVisible();
        queryWrapper.eq(StringUtils.isNotEmpty(visible), "visible", visible);

        String status = menuInfoQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status), "status", status);

        String perms = menuInfoQuery.getPerms();
        queryWrapper.eq(StringUtils.isNotEmpty(perms), "perms", perms);

        String icon = menuInfoQuery.getIcon();
        queryWrapper.eq(StringUtils.isNotEmpty(icon), "icon", icon);

        String createBy = menuInfoQuery.getCreateBy();
        queryWrapper.like(StringUtils.isNotEmpty(createBy), "create_by", createBy);

        Date createTime = menuInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String updateBy = menuInfoQuery.getUpdateBy();
        queryWrapper.like(StringUtils.isNotEmpty(updateBy), "update_by", updateBy);

        Date updateTime = menuInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<MenuInfoVo> convertVoList(List<MenuInfo> menuInfoList) {
        if (StringUtils.isEmpty(menuInfoList)) {
            return Collections.emptyList();
        }
        return menuInfoList.stream().map(MenuInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public boolean checkMenu(String permission) {
        MenuInfo menu = redisCache.getCacheObject(CONFIG_MENU_PERMISSION + permission);
        if (StringUtils.isNull(menu)) {
            menu = this.getOne(new LambdaQueryWrapper<>(MenuInfo.class).eq(MenuInfo::getPerms, permission));
            if (StringUtils.isNull(menu)) {
                return false;
            }
            redisCache.setCacheObject(CONFIG_MENU_PERMISSION + permission, menu);
        }
        if (menu.getStatus().equals(MENU_VISIBLE_1.getValue())) {
            return true;
        }
        return false;
    }

}
