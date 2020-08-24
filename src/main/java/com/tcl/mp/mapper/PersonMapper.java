package com.tcl.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcl.mp.entity.Person;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author li
 * @version 1.0
 * @date 2020/8/6 9:47
 */
@Mapper
public interface PersonMapper extends BaseMapper<Person> {

}
