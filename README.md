# solrdemo

SpringBoot结合Solr单机版做搜索

解决了SpringBoot与jsp页面的兼容性问题

spring-boot访问不到jsp页面的解决方案
这是很头疼的一件事，因为spring-boot对jsp的支持不是很好，要想使用，需要一番配置。

需要在pox.xml中加入依赖（打包方式为war）


<!--加入tomcat依赖，，以及jsp依赖的jasper依赖-->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-tomcat</artifactId>
<scope>provided</scope>
</dependency>
<dependency>
<groupId>org.apache.tomcat.embed</groupId>
<artifactId>tomcat-embed-jasper</artifactId>
<scope>compile</scope>
</dependency>

同时需要在application.properties中配置
#springmvc
spring.mvc.view.prefix:/WEB-INF/jsp/
spring.mvc.view.suffix:.jsp



<!-- 打包时将jsp文件拷贝到META-INF目录下 -->
<resource>
<!-- 指定resources插件处理哪个目录下的资源文件 -->
<directory>src/main/webapp</directory>
<!--注意此次必须要放在此目录下才能被访问到 -->
<targetPath>META-INF/resources</targetPath>
<includes>
<include>**/**</include>
</includes>
</resource>
<resource>
<directory>src/main/resources</directory>
<includes>
<include>**/**</include>
</includes>
<filtering>false</filtering>
</resource>




最终的pom文件如下
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0http://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>

<groupId>cn.it8090</groupId>
<artifactId>solrdemo</artifactId>
<version>0.0.1-SNAPSHOT</version>
<packaging>war</packaging>

<name>solrdemo</name>
<description>Demo project for Spring Boot</description>

<parent>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-parent</artifactId>
<version>1.5.9.RELEASE</version>
<!-- lookup parent from repository -->
</parent>

<properties>
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
<java.version>1.7</java.version>
</properties>

<dependencies>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-solr</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-test</artifactId>
<scope>test</scope>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-tomcat</artifactId>
<scope>provided</scope>
</dependency>
<dependency>
<groupId>org.apache.tomcat.embed</groupId>
<artifactId>tomcat-embed-jasper</artifactId>
<scope>compile</scope>
</dependency>
<!-- servlet api -->
<!--<dependency>
<groupId>javax.servlet</groupId>
<artifactId>javax.servlet-api</artifactId>
</dependency>-->
<!-- jstl -->
<dependency>
<groupId>jstl</groupId>
<artifactId>jstl</artifactId>
<version>${jstl.version}</version>
</dependency>
</dependencies>

<build>
<plugins>
<!-- spring dev -->
<plugin>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-maven-plugin</artifactId>
<version>1.4.2.RELEASE</version>
<configuration>
<mainClass>test.Application</mainClass>
</configuration>
<executions>
<execution>
<goals>
<goal>repackage</goal>
</goals>
</execution>
</executions>
<dependencies>
<!-- spring热部署 -->
<dependency>
<groupId>org.springframework</groupId>
<artifactId>springloaded</artifactId>
<version>1.2.6.RELEASE</version>
</dependency>
</dependencies>
</plugin>
<!-- 忽略无web.xml警告 -->
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-war-plugin</artifactId>
<configuration>
<failOnMissingWebXml>false</failOnMissingWebXml>
</configuration>
</plugin>
</plugins>
<resources>
<!-- 打包时将jsp文件拷贝到META-INF目录下 -->
<resource>
<!-- 指定resources插件处理哪个目录下的资源文件 -->
<directory>src/main/webapp</directory>
<!--注意此次必须要放在此目录下才能被访问到 -->
<targetPath>META-INF/resources</targetPath>
<includes>
<include>**/**</include>
</includes>
</resource>
<resource>
<directory>src/main/resources</directory>
<includes>
<include>**/**</include>
</includes>
<filtering>false</filtering>
</resource>
</resources>
</build>


</project>
