package com.tcl.mp.testgen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcl.mp.testgen.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hundanli
 * @date  2020-08-14
 * @version 0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
