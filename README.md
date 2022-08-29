#cms
- 图灵后台管理系统

# 项目运行说明
- 1.获取代码
- 2.修改项目配置(非必须)：修改配置文件
- 3.运行:mvn spring-boot:run -Drun.profiles=dev（开发）或者prepare(预发布)或者profile（生产）
- 4.JDK 1.8



# 代码使用说明
- 1.获取代码
- 2.编译打包：mvn clean package -P dev（开发）或者profile（正式）-Dmaven.test.skip=true
- 3.项目成功启动验证
    ip:port/cms/healthcheck
