package com.tcl.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcl.mp.entity.Person;
import com.tcl.mp.enums.GenderEnum;
import com.tcl.mp.mapper.PersonMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    PersonMapper personMapper;

    @Test
    void selectAll() {
        log.info("测试查询");
        List<Person> people = personMapper.selectList(null);
        Assertions.assertEquals(1, people.size());
        people.forEach(System.out::println);

    }

    @Test
    void testInsert() {
        log.info("测试插入");
        Person person = new Person();
        person.setName("li");
        person.setAge(22);
        person.setEmail("li@qq.com");
        person.setGender(GenderEnum.MALE);
        int insert = personMapper.insert(person);
        log.info("插入成功数量：" + insert);
        Long id = person.getId();
        log.info("自动生成主键：" + id);
    }


    @Test
    void testQueryWrapper() {
        QueryWrapper<Person> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", "hundanli@qq.com");
        Person person = personMapper.selectOne(queryWrapper);
        System.out.println(person);

    }

    @Test
    void testLambdaQueryWrapper() {
        LambdaQueryWrapper<Person> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Person::getEmail, "hundanli@qq.com");
        System.out.println(personMapper.selectOne(queryWrapper));
    }


    @Test
    void testUpdate() {
        Person person = personMapper.selectOne(null);
        person.setGender(GenderEnum.FEMALE);
        personMapper.updateById(person);
    }

    @Test
    void testUpdateWrapper() {
        UpdateWrapper<Person> updateWrapper = new UpdateWrapper<>();
        Person person = personMapper.selectOne(null);
        updateWrapper.set("gender", GenderEnum.FEMALE);
        personMapper.update(person, updateWrapper);
    }

    @Test
    void testLambdaUpdateWrapper() {
        LambdaUpdateWrapper<Person> updateWrapper = new LambdaUpdateWrapper<>();
        Person person = personMapper.selectOne(null);
        updateWrapper.set(Person::getGender, GenderEnum.FEMALE);
        personMapper.update(person, updateWrapper);
    }

    @Test
    void testPage() {
        Page<Person> page = new Page<>(1, 5);
        page = personMapper.selectPage(page, null);
        page.getRecords().forEach(System.out::println);
    }

    @Test
    void testLogicDelete() {
        Person person = personMapper.selectById(1295325847524339715L);
        int i = personMapper.deleteById(person.getId());
        Assertions.assertEquals(1, i);

    }
}
