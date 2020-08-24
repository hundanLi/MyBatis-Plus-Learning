package com.tcl.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tcl.mp.enums.GenderEnum;
import lombok.Data;

/**
 * @author li
 * @version 1.0
 * @date 2020/8/6 9:41
 */
@Data
public class Person {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private GenderEnum gender;
}
