package cn.rlfit.auth.mapper;

import cn.rlfit.model.system.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 27/01/2024 12:18 PM
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> getMenuListById(@Param("userId") Long userId);
}
