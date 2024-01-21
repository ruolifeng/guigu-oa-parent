package cn.rlfit.auth;

import cn.rlfit.auth.mapper.SysRoleMapper;
import cn.rlfit.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest //spring提供的测试注解，包含基本上所有的测试包
public class TestMpDemo1 {


    //注入
    @Autowired
    private SysRoleMapper mapper;
    @Test
    public void getAll(){
        List<SysRole> list = mapper.selectList(null); //查询所有数据并返回成一个集合
        System.out.println(list);
    }
}
