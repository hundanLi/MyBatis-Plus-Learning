# MyBatis-Plus学习笔记

[完整代码](https://github.com/hundanLi/MyBatis-Plus-Learning)

## 1. 简介

[MyBatis-Plus](https://github.com/baomidou/mybatis-plus)（简称 MP）是一个 [MyBatis](http://www.mybatis.org/mybatis-3/) 的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

### 特性

- **无侵入**：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑
- **损耗小**：启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作
- **强大的 CRUD 操作**：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求
- **支持 Lambda 形式调用**：通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错
- **支持主键自动生成**：支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题
- **支持 ActiveRecord 模式**：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作
- **支持自定义全局通用操作**：支持全局通用方法注入（ Write once, use anywhere ）
- **内置代码生成器**：采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码，支持模板引擎，更有超多自定义配置等您来使用
- **内置分页插件**：基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询
- **分页插件支持多种数据库**：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库
- **内置性能分析插件**：可输出 Sql 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询
- **内置全局拦截插件**：提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作



## 2. 快速入门

### 2.1 创建数据表

```mysql
DROP TABLE IF EXISTS user;

CREATE TABLE user
(
	id BIGINT(20) NOT NULL COMMENT '主键ID',
	name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
	age INT(11) NULL DEFAULT NULL COMMENT '年龄',
	email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
	PRIMARY KEY (id)
);

DELETE FROM user;

INSERT INTO user (id, name, age, email) VALUES
(1, 'Jone', 18, 'test1@tcl.com'),
(2, 'Jack', 20, 'test2@tcl.com'),
(3, 'Tom', 28, 'test3@tcl.com'),
(4, 'Sandy', 21, 'test4@tcl.com'),
(5, 'Billie', 24, 'test5@tcl.com');
```



### 2.2 初始化项目

使用maven新建Spring Boot项目。添加依赖：

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!--mybatis plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.3.2</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
    </dependencies>

```

### 2.3 配置数据源

`application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=Asia/Shanghai
    username: root
    password: root
```



### 2.4 代码编写

实体类：

```java
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```

Mapper接口：

```java
public interface UserMapper extends BaseMapper<User> {

}
```

### 2.5 配置 `@MapperScan`

```java
@SpringBootApplication
@MapperScan(basePackages = "com.tcl.mp.mapper")
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

}
```



### 2.6 运行测试

```java
@Slf4j
@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void selectAll() {
        log.info("测试查询");
        List<User> users = userMapper.selectList(null);
        Assertions.assertEquals(5, users.size());
        users.forEach(System.out::println);

    }

}
```



## 3. 常用注解

### 3.1 @TableName

描述：表名注解，作用于实体类上，映射到数据库的表中。属性列表：

|       属性       |   类型   | 必须指定 | 默认值 | 描述                                                         |
| :--------------: | :------: | :------: | :----: | ------------------------------------------------------------ |
|      value       |  String  |    否    |   ""   | 表名                                                         |
|      schema      |  String  |    否    |   ""   | schema                                                       |
| keepGlobalPrefix | boolean  |    否    | false  | 是否保持使用全局的 tablePrefix 的值(如果设置了全局 tablePrefix 且自行设置了 value 的值) |
|    resultMap     |  String  |    否    |   ""   | xml 中 resultMap 的 id                                       |
|  autoResultMap   | boolean  |    否    | false  | 是否自动构建 resultMap 并使用(如果设置 resultMap 则不会进行 resultMap 的自动构建并注入) |
| excludeProperty  | String[] |    否    |   []   | 需要排除的属性名                                             |



### 3.2 @TableId

描述：主键注解，用于映射数据表的主键列名和主键生成策略。默认是映射同名列且驼峰转下划线（如有）。

| 属性  |  类型  | 必须指定 |   默认值    |    描述    |
| :---: | :----: | :------: | :---------: | :--------: |
| value | String |    否    |     ""      | 主键字段名 |
| type  |  Enum  |    否    | IdType.NONE |  主键类型  |

IdType：主键类型枚举类

|      值       |                             描述                             |
| :-----------: | :----------------------------------------------------------: |
|     AUTO      |                         数据库ID自增                         |
|     NONE      | 无状态,该类型为未设置主键类型(注解里等于跟随全局，全局里约等于 INPUT) |
|     INPUT     |                    insert前自行set主键值                     |
|   ASSIGN_ID   | 分配ID(主键类型为Number(Long和Integer)或String)(since 3.3.0),使用接口`IdentifierGenerator`的方法`nextId`(默认实现类为`DefaultIdentifierGenerator`雪花算法) |
|  ASSIGN_UUID  | 分配UUID,主键类型为String(since 3.3.0),使用接口`IdentifierGenerator`的方法`nextUUID`(默认default方法) |
|   ID_WORKER   |     分布式全局唯一ID 长整型类型(please use `ASSIGN_ID`)      |
|     UUID      |           32位UUID字符串(please use `ASSIGN_UUID`)           |
| ID_WORKER_STR |     分布式全局唯一ID 字符串类型(please use `ASSIGN_ID`)      |



### 3.3 @TableField

描述：字段注解(非主键)

|       属性       |             类型             | 必须指定 |          默认值          |                             描述                             |
| :--------------: | :--------------------------: | :------: | :----------------------: | :----------------------------------------------------------: |
|      value       |            String            |    否    |            ""            |                         数据库字段名                         |
|        el        |            String            |    否    |            ""            | 映射为原生 `#{ ... }` 逻辑,相当于写在 xml 里的 `#{ ... }` 部分 |
|      exist       |           boolean            |    否    |           true           |                      是否为数据库表字段                      |
|    condition     |            String            |    否    |            ""            | 字段 `where` 实体查询比较条件,有值设置则按设置的值为准,没有则为默认全局的 `%s=#{%s}`,[参考](https://github.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-annotation/src/main/java/com/baomidou/mybatisplus/annotation/SqlCondition.java) |
|      update      |            String            |    否    |            ""            | 字段 `update set` 部分注入, 例如：update="%s+1"：表示更新时会set version=version+1(该属性优先级高于 `el` 属性) |
|  insertStrategy  |             Enum             |    N     |         DEFAULT          | 举例：NOT_NULL: `insert into table_a(<if test="columnProperty != null">column</if>) values (<if test="columnProperty != null">#{columnProperty}</if>)` |
|  updateStrategy  |             Enum             |    N     |         DEFAULT          | 举例：IGNORED: `update table_a set column=#{columnProperty}` |
|  whereStrategy   |             Enum             |    N     |         DEFAULT          | 举例：NOT_EMPTY: `where <if test="columnProperty != null and columnProperty!=''">column=#{columnProperty}</if>` |
|       fill       |             Enum             |    否    |    FieldFill.DEFAULT     |                       字段自动填充策略                       |
|      select      |           boolean            |    否    |           true           |                     是否进行 select 查询                     |
| keepGlobalFormat |           boolean            |    否    |          false           |              是否保持使用全局的 format 进行处理              |
|     jdbcType     |           JdbcType           |    否    |    JdbcType.UNDEFINED    |           JDBC类型 (该默认值不代表会按照该值生效)            |
|   typeHandler    | Class<? extends TypeHandler> |    否    | UnknownTypeHandler.class |          类型处理器 (该默认值不代表会按照该值生效)           |
|   numericScale   |            String            |    否    |            ""            |                    指定小数点后保留的位数                    |

FieldStrategy：字段策略枚举类

|    值     |                           描述                            |
| :-------: | :-------------------------------------------------------: |
|  IGNORED  |                         忽略判断                          |
| NOT_NULL  |                        非NULL判断                         |
| NOT_EMPTY | 非空判断(只对字符串类型字段,其他类型字段依然为非NULL判断) |
|  DEFAULT  |                       追随全局配置                        |
|   NEVER   |                         不加入SQL                         |

FieldFill枚举类：

|      值       |         描述         |
| :-----------: | :------------------: |
|    DEFAULT    |      默认不处理      |
|    INSERT     |    插入时填充字段    |
|    UPDATE     |    更新时填充字段    |
| INSERT_UPDATE | 插入和更新时填充字段 |



### 3.4 @Version

描述：乐观锁注解、标记 `@Verison` 在字段上

### 3.5 @EnumValue

描述：支持普通枚举类字段，只用在enum类的字段上。当实体类的属性是普通枚举，使用该注解来标注枚举类里的哪个属性来对应字段。

使用示例：

1. 首先新建枚举类，放在 **com.tcl.mp.enums**包下，然后添加@EnumValue注解

   ```java
   public enum GenderEnum {
   
       /**
        * 性别
        */
       MALE(1, "male"),
       FEMALE(0, "female"),
       UNDEFINED(-1, "undefined")
       ;
   
       @EnumValue
       private final Integer code;
       private final String msg;
   
       GenderEnum(int code, String msg) {
           this.code = code;
           this.msg = msg;
       }
   
       public int getCode() {
           return code;
       }
   
       public String getMsg() {
           return msg;
       }
   }
   ```

2. 实体类添加枚举字段

   ```java
       private GenderEnum gender;
   ```

3. **最重要**：在`application.xml`配置**mybatis-plus.type-enums-package**属性值为枚举类所在的包名

   ```
   mybatis-plus:
     type-enums-package: com.tcl.mp.enums
   ```



### 3.6  @TableLogic

- 描述：表字段逻辑处理注解（逻辑删除）

|  属性  |  类型  | 必须指定 | 默认值 |     描述     |
| :----: | :----: | :------: | :----: | :----------: |
| value  | String |    否    |   ""   | 逻辑未删除值 |
| delval | String |    否    |   ""   |  逻辑删除值  |





## 4. 功能特性

### 4.1 代码生成器

MyBatis Plus提供了快速生成 Entity、Mapper、Mapper XML、Service、Controller 等各个模块的代码生成器。

#### 4.1.1 添加依赖

```xml
        <!--mybatis plus代码生成器-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.3.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-engine-core</artifactId>
            <version>2.2</version>
        </dependency>
```

#### 4.1.2 生成代码

```java
public class MybatisPlusCodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (!StringUtils.isBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("hundanli");
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=Asia/Shanghai&useUnicode=true&useSSL=false&characterEncoding=utf8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent("com.tcl.mp");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 velocity
         String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板，可以使用默认
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setEntity("templates/entity.java");
        templateConfig.setService("templates/service.java");
        templateConfig.setController("templates/controller.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");
        templateConfig.setXml("templates/mapper.xml");
        templateConfig.setMapper("templates/mapper.java");


        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }

}

```

配置自定义输出模板时，可以将模板添加到`templates`目录下，不添加时会使用mybatis-generator的jar包中的模板文件，路径也是一致的。



### 4.2 CRUD接口

MyBatis Plus提供了两种方式调用单表的基本CRUD接口：

- IService接口：自定义的接口类只要实现该接口并且继承它的默认实现类ServiceImpl类即可获得基本的CRUD方法，无需编写xml。如：

  ```java
  @Service
  public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
  
  }
  ```

- BaseMapper接口：自定义的Mapper接口只要实现该接口即可获得CRUD能力，无需编写xml。如：

  ```java
  @Mapper
  public interface UserMapper extends BaseMapper<User> {
  
  }
  ```

  

### 4.3 条件构造器

MyBatis Plus提供了`Wrapper`抽象类来构造查询条件。抽象子类`AbstractWrapper`实现了通用的条件构造方法。`AbstractWrapper`的方法通过字符串来接收数据库列名，`AbstractLambdaWrapper`更方便，接收实体类的getter方法来指定数据表列名，更不容易出错。可直接使用的子类分为查询和更新构造器。对于删除操作，可以使用任意一个wrapper类来构造条件。

#### 4.3.1 QueryWrapper

QueryWrapper不仅可用于构造条件，还提供了`select`方法来自定义取出哪些字段。LambdaQueryWrapper则提供了函数式参数版本，使用更加方便。如下两个示例为等效的查询操作：

```java
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
        queryWrapper.eq(Person::getEmail, "hunadanli@qq.com");
        System.out.println(personMapper.selectOne(queryWrapper));

    }
```

#### 4.3.2 UpdateWrapper

UpdateWrapper则提供了set方法用于特定更新字段。如下示例实现了一致的更新功能：

```java
    @Test
    void testUpdate() {
        UpdateWrapper<Person> updateWrapper = new UpdateWrapper<>();
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

```



### 4.4 分页插件

MyBatis Plus提供了便利的分页插件实现分页查询，使用Spring Boot时只需要极少的配置：

```java
@Configuration
@MapperScan("com.tcl.mp.**.mapper")
public class MyBatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }
}
```

使用方式：构造IPage实例，传入`selectPage()`方法即可。示例：

```java
    @Test
    void testPage() {
        Page<Person> page = new Page<>(1, 5);
        page = personMapper.selectPage(page, null);
        page.getRecords().forEach(System.out::println);
    }
```



### 4.5 主键回写

回顾MyBatis，当使用自增主键时，插入数据时可以配置`useGeneratedKey=true`将生成的主键回写到对象当中。MyBatis Plus可以在application.yml文件中配置：

```yaml
mybatis-plus:
  configuration:
    use-generated-keys: true
```

另外，还要在实体类上使用`@TableId`注解指定主键类型为`IdType.AuTO`。

```java
    @TableId(type = IdType.AUTO)
    private Long id;
```



## 5. 插件扩展

### 5.1 逻辑删除

逻辑删除与物理删除：逻辑删除是对数据做特殊标记，数据可以恢复；而物理删除就是实实在在的摧毁，数据不能恢复。

#### 1. 全局配置

在application.yml文件中进行全局配置：

```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: flag  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
```

#### 2. 特定配置

还可以在类字段上标注`@TableLogic`注解覆盖全局配置：

```java
    @TableLogic(value = "0")
    private Byte isDeleted;
```

测试：

```java

    @Test
    void testLogicDelete() {
        Person person = personMapper.selectById(1295325847524339715L);
        int i = personMapper.deleteById(person.getId());
        Assertions.assertEquals(1, i);

    }
```

### 5.2 通用枚举类

详见3.5节

### 5.3 类型处理器

类型处理器，用于 JavaType 与 JdbcType 之间的转换，用于 PreparedStatement 设置参数值和从 ResultSet 或 CallableStatement 中取出一个值。MyBatis Plus支持通过`@TableField`注解注册自定义类型处理器。如：

```java
    @TableField(typeHandler = JacksonTypeHandler.class)
    // @TableField(typeHandler = FastjsonTypeHandler.class)
    private OtherInfo otherInfo;
```



### 5.4 自动填充功能

MyBatis Plus允许对未设置的对象字段进行填充之后再插入或者更新数据库。对于添加/更新时间字段很有用，使用方法：

#### 1. 使用`@TableField`注解指定策略

```java
    // 自动填充策略
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
```

#### 2. 注册`MetaObjectHandler`组件

```java
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
```

#### 3. 简单测试

```java
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
```



### 5.5 SQL分析打印器

该功能依赖 `p6spy` 组件，完美的输出打印 SQL 及执行时长。使用方式如下：

#### 1. 添加依赖

```xml
        <dependency>
            <groupId>p6spy</groupId>
            <artifactId>p6spy</artifactId>
            <version>3.9.1</version>
        </dependency>
```

#### 2. 配置数据源

```yaml
spring:
  datasource:
    url: jdbc:p6spy:mysql://localhost:3306/mybatis_plus?serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.p6spy.engine.spy.P6SpyDriver

```

#### 3. 配置spy.properties

在resources目录下添加spy.properties文件：

```properties
#3.2.1以上使用
modulelist=com.baomidou.mybatisplus.extension.p6spy.MybatisPlusLogFactory,com.p6spy.engine.outage.P6OutageFactory
#3.2.1以下使用或者不配置
#modulelist=com.p6spy.engine.logging.P6LogFactory,com.p6spy.engine.outage.P6OutageFactory
# 自定义日志打印
logMessageFormat=com.baomidou.mybatisplus.extension.p6spy.P6SpyLogger
#日志输出到控制台
appender=com.baomidou.mybatisplus.extension.p6spy.StdoutLogger
# 使用日志系统记录 sql
#appender=com.p6spy.engine.spy.appender.Slf4JLogger
# 设置 p6spy driver 代理
deregisterdrivers=true
# 取消JDBC URL前缀
useprefix=true
# 配置记录 Log 例外,可去掉的结果集有error,info,batch,debug,statement,commit,rollback,result,resultset.
excludecategories=info,debug,result,commit,resultset
# 日期格式
dateformat=yyyy-MM-dd HH:mm:ss
# 实际驱动可多个
#driverlist=com.mysql.cj.jdbc.Driver
# 是否开启慢SQL记录
outagedetection=true
# 慢SQL记录标准 2 秒
outagedetectioninterval=2
```

该功能不建议在生产上使用。

### 5.6 乐观锁插件

#### 1. 适用场景

当要更新一条记录的时候，希望这条记录没有被别人更新

乐观锁实现方式：

- 取出记录时，获取当前version
- 更新时，带上这个version
- 执行更新时， set version = newVersion where version = oldVersion
- 如果version不对，就更新失败

#### 2. 使用方式

1. 注册乐观锁组件

   ```java
   @Bean
   public OptimisticLockerInterceptor optimisticLockerInterceptor() {
       return new OptimisticLockerInterceptor();
   }
   ```

2. 字段标`@Version`注解

   ```java
   @Version
   private Integer version;
   ```

3. version支持的类型

   int,Integer,long,Long,Date,Timestamp,LocalDateTime

4. 支持的修改方法

   updateById(id)` 与 `update(entity, wrapper)，wrapper不可以复用。

### 5.7 数据安全

该功能为了保护数据库配置及数据安全，在一定的程度上控制开发人员流动导致敏感信息泄露。3.3.2版本以上支持。示例待补充。

#### 1. 秘钥生成和加密



#### 2. 安全配置



#### 3. 启动参数









