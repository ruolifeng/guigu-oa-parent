package cn.rlfit.auth.service;

import cn.rlfit.model.system.SysMenu;
import cn.rlfit.vo.system.AssginMenuVo;
import cn.rlfit.vo.system.RouterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 27/01/2024 12:19 PM
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    List<SysMenu> findMenuByRoleId(Long roleId);

    void doAssign(AssginMenuVo vo);

    List<RouterVo> getUserMenuListById(Long userId);

    List<String> getUserPermsById(Long userId);

}
