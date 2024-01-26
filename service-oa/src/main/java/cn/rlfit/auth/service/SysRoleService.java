package cn.rlfit.auth.service;

import cn.rlfit.model.system.SysRole;
import cn.rlfit.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    Map<String, Object> findRoleDataByUserId(Long userId);

    void doAssign(AssginRoleVo vo);
}
