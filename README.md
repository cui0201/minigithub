# MiniGitHub - 微型博客系统

基于 Spring Cloud 微服务架构实现的轻量级博客平台，前端复刻 GitHub 官网 UI。

## 技术栈

### 后端
- JDK 21
- Spring Boot 3.4.2
- Spring Cloud 2024.0.1
- Spring Cloud Alibaba 2023.0.3.3
- Spring Cloud Gateway
- Nacos 3.1.1
- MySQL 8.0+
- MyBatis Plus 3.5.7
- Maven

### 前端
- Vue3
- Element Plus / Tailwind CSS

## 项目结构

```
minigithub/
├── common-service/       # 公共模块
├── gateway-server/       # 网关服务
├── user-service/         # 用户服务
├── blog-service/         # 博客文章服务
├── schema.sql            # 数据库脚本
└── README.md
```

## 快速开始

### 1. 环境准备

- JDK 21
- Maven 3.8+
- MySQL 8.0+
- Nacos 3.1.1

### 2. 启动 Nacos

下载并启动 Nacos Server 3.1.1：
```bash
# 下载 Nacos
wget https://github.com/alibaba/nacos/releases/download/3.1.1/nacos-server-3.1.1.zip
unzip nacos-server-3.1.1.zip
cd nacos/bin

# 单机模式启动
./startup.sh -m standalone
```

访问 Nacos 控制台：http://localhost:8848/nacos（默认账号密码：nacos/nacos）

### 3. 初始化数据库

执行 `schema.sql` 脚本创建数据库和表：
```bash
mysql -u root -p < schema.sql
```

### 4. 修改配置

根据实际情况修改各服务的 `application.yml` 中的数据库和 Nacos 配置。

### 5. 编译项目

```bash
mvn clean install -DskipTests
```

### 6. 启动服务

按顺序启动以下服务：

1. **user-service**（端口 8081）
2. **blog-service**（端口 8082）
3. **gateway-server**（端口 8080）

```bash
# 启动用户服务
cd user-service
mvn spring-boot:run

# 启动博客服务
cd ../blog-service
mvn spring-boot:run

# 启动网关服务
cd ../gateway-server
mvn spring-boot:run
```

## API 接口

所有接口通过网关访问：`http://localhost:8080/api`

### 用户服务 (/api/user)

- `POST /user/register` - 用户注册
- `POST /user/login` - 用户登录
- `GET /user/profile` - 获取个人信息
- `PUT /user/profile` - 更新个人信息
- `GET /user/{username}` - 获取用户信息

### 博客服务 (/api/blog)

- `POST /blog` - 发布文章
- `PUT /blog/{id}` - 编辑文章
- `DELETE /blog/{id}` - 删除文章
- `GET /blog/{id}` - 获取文章详情
- `GET /blog/list` - 获取文章列表（分页）
- `GET /blog/user/{userId}` - 获取用户文章列表
- `GET /blog/my` - 获取我的文章列表

## 核心功能

### 用户模块
- 用户注册（用户名 + 密码 + 邮箱）
- 用户登录（JWT 令牌）
- 个人中心（查看/修改个人信息）
- 密码 BCrypt 加密存储

### 文章模块
- 发布文章
- 编辑/删除自己的文章
- 查询文章列表（分页）
- 文章详情查看
- 浏览量统计

### 网关功能
- 统一接口入口
- 路由转发
- 全局跨域配置

## 许可证

MIT License
