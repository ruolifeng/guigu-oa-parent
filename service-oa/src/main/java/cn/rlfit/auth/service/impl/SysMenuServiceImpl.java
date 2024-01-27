package cn.rlfit.auth.service.impl;

import cn.rlfit.auth.mapper.SysMenuMapper;
import cn.rlfit.auth.service.SysMenuService;
import cn.rlfit.auth.service.SysRoleMenuService;
import cn.rlfit.auth.utils.MenuHelp;
import cn.rlfit.common.config.exception.GuiguException;
import cn.rlfit.model.system.SysMenu;
import cn.rlfit.model.system.SysRoleMenu;
import cn.rlfit.vo.system.AssginMenuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

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
}
