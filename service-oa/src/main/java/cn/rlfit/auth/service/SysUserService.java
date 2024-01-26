package cn.rlfit.auth.service;

import cn.rlfit.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 26/01/2024 3:38 PM
 */
public interface SysUserService extends IService<SysUser> {
    void updateStatus(Long id, Integer status);
}
