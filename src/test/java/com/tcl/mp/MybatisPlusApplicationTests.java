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
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Test
    void testAutoFillInsert() {
        Person person = new Person();
        person.setName("gao");
        person.setGender(GenderEnum.FEMALE);
        person.setEmail("gao@qq.com");
        person.setAge(22);
        personMapper.insert(person);
        log.warn(person.toString());
    }

    @Test
    void testAutoFillUpdate() throws InterruptedException {
        Person person = new Person();
        person.setName("gao");
        person.setGender(GenderEnum.FEMALE);
        person.setEmail("gao@qq.com");
        person.setAge(22);
        personMapper.insert(person);
        log.warn(person.toString());

        TimeUnit.SECONDS.sleep(10);
        person.setName("gaoxiaoyan");
        person.setEmail("gaoxiaoyan@qq.com");
        person.setCreateTime(null);
        person.setUpdateTime(null);
        personMapper.updateById(person);

    }

    @Test
    void testEncrypt() {
        String encryptUsername = encrypt("root");
        String encryptPassword = encrypt("root");
        System.err.println(encryptUsername + "==>" + encryptPassword);

    }

    /**
     * 根据密钥对指定的明文plainText进行加密.
     *
     * @param plainText 明文
     * @return 加密后的密文.
     */
    public static String encrypt(String plainText) {
        Key secretKey = getKey("hundanli");
        System.out.println(secretKey.getFormat());
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] p = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] result = cipher.doFinal(p);
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据密钥对指定的密文cipherText进行解密.
     *
     * @param cipherText 密文
     * @return 解密后的明文.
     */
    public static String decrypt(String cipherText) {
        Key secretKey = getKey("hundanli");
        System.out.println(secretKey.toString());
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] c = decoder.decodeBuffer(cipherText);
            byte[] result = cipher.doFinal(c);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Key getKey(String keySeed) {
        if (keySeed == null) {
            keySeed = System.getenv("AES_SYS_KEY");
        }
        if (keySeed == null) {
            keySeed = System.getProperty("AES_SYS_KEY");
        }
        if (keySeed == null || keySeed.trim().length() == 0) {
            keySeed = "abcd1234!@#$";// 默认种子
        }
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keySeed.getBytes());
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}