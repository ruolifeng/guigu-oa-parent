package cn.rlfit.auth.mapper;

import cn.rlfit.model.system.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
@Repository //将接口的一个实现类交给spring进行管理
public interface SysRoleMapper extends BaseMapper<SysRole> {

}

