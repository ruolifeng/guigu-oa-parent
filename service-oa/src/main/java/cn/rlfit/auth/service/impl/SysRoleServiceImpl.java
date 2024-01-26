package cn.rlfit.auth.service.impl;

import cn.rlfit.auth.mapper.SysRoleMapper;
import cn.rlfit.auth.service.SysRoleService;
import cn.rlfit.auth.service.SysUserRoleService;
import cn.rlfit.model.system.SysRole;
import cn.rlfit.model.system.SysUserRole;
import cn.rlfit.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    SysUserRoleService sysUserRoleService;

    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
//        1.查询所有角色信息，返回list集合
        List<SysRole> allRoleList = baseMapper.selectList(null);
//        2.根据userid查询角色用户关系表，查询userid对应的所有角色的id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
//        查询用户对应的角色id
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);
//        3.根据上面两步的信息找到次用户对应的角色，将这角色信息作为map集合返回
        List<Long> existUserRoleIdList = existUserRoleList.stream().map(SysUserRole::getRoleId).parallel().toList();
        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole ro : allRoleList) {
            if (existUserRoleIdList.contains(ro.getId())) assignRoleList.add(ro);
        }
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList", assignRoleList);
        roleMap.put("allRoleList", allRoleList);
        return roleMap;
    }

    //    为用户分配角色
    @Override
    public void doAssign(AssginRoleVo vo) {
//        把用户之前分配角色的数据删除，根据用户角色关系表中的id进行删除
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, vo.getUserId());
        sysUserRoleService.remove(wrapper);
//        重新分配角色
        List<Long> roleIdList = vo.getRoleIdList();
        for (Long roleId : roleIdList) {
            if (StringUtils.isEmpty(roleId)) continue;
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }
    }
}
