package com.tcl.mp.testgen.service.impl;

import com.tcl.mp.testgen.entity.User;
import com.tcl.mp.testgen.mapper.UserMapper;
import com.tcl.mp.testgen.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hundanli
 * @date  2020-08-14
 * @version 0.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
