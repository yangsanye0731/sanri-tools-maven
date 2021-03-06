package com.sanri.tools.modules.database.dtos;

import lombok.Data;

import javax.sql.DataSource;
import java.util.List;

/**
 * 项目代码生成配置
 */
@Data
public class CodeGeneratorConfig {
    private String filePath;
    private String projectName;
    private String author;

    private MavenConfig mavenConfig;
    private DataSourceConfig dataSourceConfig;
    private PackageConfig packageConfig;
    private GlobalConfig globalConfig;
    private FetureConfig fetureConfig;

    @Data
    public static class MavenConfig{
        private String groupId;
        private String artifactId;
        private String version = "1.0-SNAPSHOT";
        private String springBootVersion = "2.0.5.RELEASE";
    }

    @Data
    public static class DataSourceConfig {
        private String connName;
        private String catalog;
        private String schema;
        private List<String> tableNames;
    }

    @Data
    public static class PackageConfig{
        private String parent;

        private String mapper;
        private String service;
        private String controller;

        private String entity;
        private String vo;
        private String dto;
        private String param;

    }

    @Data
    public static class GlobalConfig{
        // entity 配置 可以支持 swagger2 , lombok , persistenceApi
        private boolean swagger2;
        private boolean lombok;
        private boolean persistence;
        private String idAnnotation;        // id 列上的注解
        private boolean serialVersionUID;
        private boolean serializer;         // 是否要实现序列化
        private String supperClass;         // 实体超类
        private List<String> exclude;       // 排除列
        private boolean dateFormat;         // 日期相关属性加 json 格式注解
        private List<String> jsonIgnores;   // 忽略输出的字段列表

        // mapper.xml 配置
        private boolean baseColumnList;
        private boolean baseResultMap;

        // 重命名策略
        private String renameStrategy;

        // 如果使用 tk.mybatis , 则可以给出基础 Mapper
        private String mappers;

    }

    @Data
    private static class FetureConfig{
        private boolean schedule;
        private boolean threadPool;
        private boolean inputOutput;

        private boolean redis;
        private boolean mongo;

        private boolean kafka;
        private boolean rocketmq;
        private boolean rabbitmq;

        private boolean mysql;
        private boolean postgresql;
    }

}
