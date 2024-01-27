package cn.rlfit.auth.utils;

import cn.rlfit.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 处理数据返回树形结构数据
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 27/01/2024 1:18 PM
 */
public class MenuHelp {
    public static List<SysMenu> buildTree(List<SysMenu> sysMenusList) {
//        创建一个list集合
        List<SysMenu> tree = new ArrayList<>();
//        把所有的菜单数据遍历处理
        for (SysMenu sysMenu : sysMenusList) {
//            递归入口
            if (sysMenu.getParentId() == 0) {
                tree.add(getChildren(sysMenu, sysMenusList));
            }
        }
        return tree;
    }

    public static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu it : sysMenuList) {
            if (sysMenu.getId().longValue() == it.getParentId().longValue()) {
                if (sysMenu.getChildren() == null)
                    sysMenu.setChildren(new ArrayList<SysMenu>());
                sysMenu.getChildren().add(getChildren(it, sysMenuList));
            }
        }
        return sysMenu;
    }

}
