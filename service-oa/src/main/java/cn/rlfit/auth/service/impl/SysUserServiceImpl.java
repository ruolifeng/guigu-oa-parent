package cn.rlfit.auth.service.impl;

import cn.rlfit.auth.mapper.SysUserMapper;
import cn.rlfit.auth.service.SysUserService;
import cn.rlfit.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 26/01/2024 3:39 PM
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public void updateStatus(Long id, Integer status) {
//        1.根据用户id查询用户对象信息
        SysUser user = baseMapper.selectById(id);
//        2.设置修改的状态值
        user.setStatus(status);
//        3.调用方法进行修改
        baseMapper.updateById(user);
    }
}
