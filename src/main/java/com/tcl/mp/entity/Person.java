package com.tcl.mp.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.tcl.mp.enums.GenderEnum;
import lombok.Data;

import java.time.LocalDateTime;

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
    @TableLogic(value = "0")
    private Byte isDeleted;

    // 自动填充策略

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
