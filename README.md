# 功能说明

- 字典管理功能，提供控制台功能
- 是一个依赖包，依赖后应用启动后会字典数据加载到内存中
- 支持对字典的转义api，支持map、普通java对象，以及部分其他api



# 1.安装

```bash
git clone https://github.com/hbq969/h-dict.git
cd h-dict/src/main/resources/static
nvm use 20
npm run build
cd h-dict/
mvn -U package install
```



# 2.配置

1. 依赖

```xml
<dependency>
  <groupId>com.github.hbq969</groupId>
  <artifactId>h-dict</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```



2. 配置

​		h2数据库

```yaml
# 数据源配置（省略）
...

# mybatis配置
mybatis:
  # 数据库类型，支持 embedded/mysql/oracle
	dialog: embedded
	# 追加一下内容（如果原有内容存在）
  mapper-locations: 
    - classpath*:**/mapper/common/*Mapper.xml
    - classpath*:**/mapper/${mybatis.dialog}/*Mapper.xml
  
# 添加字典管理控制台页面
spring:
  mvc:
    resource-handler-registry:
      entries:
        - handlers: /ui-dict/**
          locations: classpath:/static/ui-dict/

# 开启字典管理
dict:
  enabled: true
  # dic控制台接口访问token头
  dictToken: hbq969@gmail.com
  # map转义时转义后字段的前缀
  map-key-prefix: hbq
  # 缓存字典数据重载周期
  reload:
    cron: 0,30 * * * * *
```



# 3.管理界面

http://ip:port/[${server.servlet.context-path}]/ui-dict/index.html



1. 查询列表

![](src/main/resources/readme/1.png)



2. 新增字典

![](src/main/resources/readme/2.png)


![](src/main/resources/readme/3.png)


3. 新增固定枚举值

![](src/main/resources/readme/4.png)



# 4.使用

- api接口说明

![](src/main/resources/readme/6.png)






- 针对map转义

```yaml
# 开启字典管理
dict:
  enabled: true
  # map转义时转义后字段的前缀
  map-key-prefix: fmt
```

```java
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;

@Autowired
private MapDictHelperImpl mapDic;

@Autowired
private ListMapDictHelperImpl listDic;
```

```java
Map<String,Object> map = new HashMap<>();
map.put('region','suzhou');
mapDic.tranForDict(map);

List<Map> list = new ArrayList<>();
list.add(map);
listDic.tranForDict(list);

// 转义后map
{'region':'suzhou','fmtRegion':'苏州'}
```





- java对象转义

```java
import com.github.hbq969.code.dict.service.api.impl.ModelDictHelperImpl;

@Autowired
private ModelDictHelperImpl modelDic;

@Autowired
private ListModelDictHelperImpl listDic;
```

```java
@Data
Class Foo{
  @Td(fmtFieldName="fmtRegion")
	private String region;
	private String fmtRegion;
}

Foo foo = new Foo();
foo.setRegion('suzhou');
modelDic.tranForDict(foo);

List<Foo> list = new ArrayList<>();
list.add(foo);
listDic.tranForDict(list);

// 转义后
foo.getFmtRegion()=> '苏州'
```

# 5.demo演示

1. 使用maven模版创建一个SpringBoot工程

```bash
# 安装模版
git clone https://github.com/hbq969/h-archetype.git
cd h-archetype/target/generated-sources/archetype
mvn install

# 使用模版创建工程
cd -
mvn archetype:generate -DarchetypeGroupId=com.github.hbq969 -DarchetypeArtifactId=h-archetype -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=com.github.hbq969 -Dversion=1.0-SNAPSHOT -DartifactId=h-example -Dpackage=com.github.hbq969 -Dproduct=code -Dmodule=example -DappPort=30000 -Dauthor=hbq969@gmail.com

# 运行demo工程
cd h-example
mvn spring-boot:run
```



2. 访问Dashboard页面

> http://localhost:30000/api/hbq/code/example/ui-dict/index.html

​	这时你就可以管理这个应用的字典数据了，控制台添加、删除、更新字典数据都会被实时更新到应用缓存中，应用中在通过提供的api接口进行字典查询、转义等操作。

## 6.许可 (License)
The MIT License (MIT)

Copyright (c) 2024 hbq969@gmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.