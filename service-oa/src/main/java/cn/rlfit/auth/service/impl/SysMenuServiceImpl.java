package cn.rlfit.auth.service.impl;

import cn.rlfit.auth.mapper.SysMenuMapper;
import cn.rlfit.auth.service.SysMenuService;
import cn.rlfit.auth.service.SysRoleMenuService;
import cn.rlfit.auth.utils.MenuHelp;
import cn.rlfit.common.config.exception.GuiguException;
import cn.rlfit.model.system.SysMenu;
import cn.rlfit.model.system.SysRoleMenu;
import cn.rlfit.vo.system.AssginMenuVo;
import cn.rlfit.vo.system.MetaVo;
import cn.rlfit.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 27/01/2024 12:20 PM
 */

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 查询菜单列表，使用递归的形式进行实现
     * 1.查询所有的菜单数据
     * 2.构建成树形结构
     * 1.使用工具类进行实现
     * 1.创建一个buildTree方法传入需要处理的数据，最终返回一个处理好的集合
     *
     * @return 树形结构数据
     */
    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenus = baseMapper.selectList(null);
        List<SysMenu> sysMenus1 = MenuHelp.buildTree(sysMenus);
        return sysMenus1;
    }

    /**
     * 删除菜单
     *
     * @param id
     */
    @Override
    public void removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0)
            throw new GuiguException(201, "菜单不能删除");
        baseMapper.deleteById(id);
    }

    /**
     * 查询所有菜单，1表示可用，0表示不可用
     * 根据角色id查询所有的菜单id
     * 根据菜单生成菜单树对象
     *
     * @param roleId 角色id
     * @return 角色对应可以使用的菜单
     */
    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getStatus, 1);
        List<SysMenu> AllSysMenuList = baseMapper.selectList(wrapper);
        LambdaQueryWrapper<SysRoleMenu> wrapperRoleId = new LambdaQueryWrapper<>();
        wrapperRoleId.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> roleMenus = sysRoleMenuService.list(wrapperRoleId);
        List<Long> IdList = roleMenus.stream().map(SysRoleMenu::getMenuId).toList();
        AllSysMenuList.forEach(item -> {
            if (IdList.contains(item.getId())) {
                item.setSelect(true);
            } else {
                item.setSelect(false);
            }
        });
        List<SysMenu> sysMenus = MenuHelp.buildTree(AllSysMenuList);
        return sysMenus;
    }

    /**
     * 给角色分配菜单
     *
     * @param vo
     */
    @Override
    public void doAssign(AssginMenuVo vo) {
//        根据角色id删除角色的菜单
//        从参数列表中获取角色新分配的菜单列表，遍历列表，将每一个数据添加到角色菜单中
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, vo.getRoleId());
        sysRoleMenuService.remove(wrapper);
        List<Long> menuIdList = vo.getMenuIdList();
        for (Long menuId : menuIdList) {
            if (StringUtils.isEmpty(menuId)) continue;
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(vo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }

    @Override
    public List<RouterVo> getUserMenuListById(Long userId) {
//        通过用户id进行查询，判断是不是管理员，如果是管理员就直接查询所有值
        List<SysMenu> sysMenuList = null;
        if (userId == 1) {
//            根据状态查询所有，然后进行排序
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
//            封装查询条件，条件值是1表示查询管理员
            wrapper.eq(SysMenu::getStatus, "1");
//            表示按照这个字段进行排序
            wrapper.orderByAsc(SysMenu::getSortValue);
//            从菜单表中查询出所有的菜单封装成一个集合
            sysMenuList = baseMapper.selectList(wrapper);
        } else {
            //        如果不是管理员就根据id进行查找，这里需要写sql语句
            sysMenuList = baseMapper.getMenuListById(userId);
        }
//        将查询到的数据组建成路由结构，但是这个结构不符合我们的要求，我们需要重新构建，每一次递归都是一个新的集合，存储在上一个集合中为List类型的字段中
        List<SysMenu> buildTree = MenuHelp.buildTree(sysMenuList);
        List<RouterVo> routerTree = this.buildRouter(buildTree);
        return routerTree;
    }

    /**
     * 构建router树形结构
     * @param menus 之前构建的树形结构
     * @return 新构建的符合要求的树形结构
     */
    private List<RouterVo> buildRouter(List<SysMenu> menus) {
        List<RouterVo> routers = new ArrayList<>();
        for (SysMenu menu : menus) {
            RouterVo vo = new RouterVo();
            vo.setHidden(false);
            vo.setAlwaysShow(false);
            vo.setPath(getRouterPath(menu));
            vo.setComponent(menu.getComponent());
            vo.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> children = menu.getChildren();
//            如果说存在隐藏路由，那么需要将隐藏路由加载出来
            if (menu.getType().intValue() == 1) {
                List<SysMenu> hiddenMenuList = children.stream()
                        .filter(item -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
//                    表示这个是一个隐藏路由
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    if (children.size() > 0) {
                        vo.setAlwaysShow(true);
                    }
                    vo.setChildren(buildRouter(children));
                }
            }
            routers.add(vo);
        }
        return routers;
    }

    @Override
    public List<String> getUserPermsById(Long userId) {
//        判断是否是管理员，如果是则查询所有按钮
        List<SysMenu> sysMenuList = null;
        if (userId == 1) {
            //            根据状态查询所有，然后进行排序
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
//            封装查询条件，条件值是1表示查询管理员
            wrapper.eq(SysMenu::getStatus, "1");
//            从菜单表中查询出所有的菜单封装成一个集合
            sysMenuList = baseMapper.selectList(wrapper);
        }
//        如果不是则根据用户id进行查询
        sysMenuList = baseMapper.getMenuListById(userId);
        List<String> list = sysMenuList.stream()
                .filter(item -> item.getType() == 2).map(SysMenu::getPerms).toList();
        return list;
    }

    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }
}
