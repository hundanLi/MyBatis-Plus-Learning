package com.tcl.mp;

import com.tcl.mp.entity.Person;
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
        person.setName("hundanli");
        int insert = personMapper.insert(person);
        log.info("插入成功数量：" + insert);
        Long id = person.getId();
        log.info("自动生成主键："+id);
    }

}
