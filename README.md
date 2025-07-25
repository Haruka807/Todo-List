> 关于部署请看release

# 项目简介

本项目是一个支持多用户注册登录的待办事项web应用。
用户可以创建、编辑、标记完成及删除任务，实现个人任务管理和同步。系统采用前后端分离架构，保障数据持久化和多端访问。

---

## 技术栈

* 前端：Vue 3 + Vite
* 后端：Spring Boot + Maven
* 数据库：MySQL

---

## 环境准备

* 安装 Node.js（建议版本20）
* 安装 JDK（建议版本17）
* 安装 Maven
* 安装 MySQL

---

## 数据库初始化

```bash
mysql -u 用户名 -p 数据库名 < sql/database.sql
```

---

## 本地启动

### 1. 后端

* 修改 `src/main/resources/application.properties`，配置数据库连接

```properties
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
```
> 可以通过 `server.port` 指定后端端口，同时在前端 `frontend/vite.config.js` 中同步修改代理转发端口。

* 编译并启动后端：

```bash
mvn clean spring-boot:run
```

### 2. 前端

* 进入前端目录，安装依赖并启动开发服务器，如果后端端口被修改过，要在vite.config.js同步修改代理转发端口：

```bash
cd frontend
npm install
npm run dev
```

---

## 构建发布版本（即release内容）

### 1. 前端打包

```bash
npm run build
```

打包结果位于 `dist/` 目录。

### 2. 后端打包

```bash
mvn clean package
```

生成 `target/td-backend-0.0.1-SNAPSHOT.jar`。
