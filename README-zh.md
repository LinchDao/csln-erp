## 1. 项目简介

`csln-erp` 是一个基于 `Spring Boot 3` + `MyBatis-Plus` 的 ERP 后端项目，面向服装/商品类业务场景，覆盖用户权限、商品、库存、订单、采购、入库、客户、字典、文件上传等核心能力。

## 2. 技术栈

- Java 17
- Spring Boot 3.2.3
- MyBatis-Plus 3.5.x
- MySQL 5.7
- Redis
- JWT（双 Token：访问令牌 + 刷新令牌）
- Knife4j / OpenAPI 3
- EasyExcel
- RocketMQ（可选，用于操作日志异步路由）

## 3. 主要功能模块

- 登录鉴权与会话管理（支持续期、踢下线）
- 用户/角色/菜单/权限管理
- 字典管理（含缓存）
- 客户管理
- 商品与 SKU 管理（含导出）
- 商品库存分页查询
- 订单主单/子单管理
- 采购单与入库单管理
- 文件上传与业务关联
- 统计接口（含缓存）
- 操作日志（AOP 自动采集，支持 `SYNC/MQ` 路由）

## 4. 目录结构

```text
csln-erp
├─ src/main/java/com/lin/csln
│  ├─ controller      
│  ├─ service        
│  ├─ service/impl    
│  ├─ mapper          
│  ├─ entity          
│  ├─ dto           
│  ├─ enums          
│  ├─ common/config/utils
│  └─ log           
├─ src/main/resources
│  ├─ application.yaml
│  ├─ application-docker.yaml
│  └─ mapper/*.xml
├─ erp.version
└─ pom.xml
```
## 6. 快速启动（本地）

### 6.1 初始化数据库

创建数据库：
    - `docs/sql/data/init.sql`
    - `docs/sql/data/sysdict.sql`
    - `docs/sql/data/sysmenu.sql`
    - `docs/sql/data/role.sql`

### 6.2 修改配置

编辑 `src/main/resources/application.yaml`，至少确认以下配置：

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.data.redis.host`
- `spring.data.redis.port`
- `file.upload.base-path`
- `file.upload.temp-path`


服务默认地址：
- 应用：`http://localhost:8000/erp-service`
- Swagger UI：`http://localhost:8000/erp-service/swagger-ui.html`
- Knife4j：`http://localhost:8000/erp-service/doc.html`



1. 本地先打包 Jar
2. 基于 `Dockerfile` 构建镜像
3. 通过环境与挂载配置数据库、Redis、上传目录与日志目录
4. 启动时使用 `SPRING_PROFILES_ACTIVE=docker`

## 7. 配置补充说明

### 7.1 鉴权相关

- `jwt.secret`：
- `jwt.tokenKey`：
- `jwt.expire.normal`：
- `jwt.expire.remember`：

### 7.2 业务安全相关

- `app.security.password-salt`
- `app.security.salt-enabled`
- `app.security.default-password`

### 7.3 操作日志路由

- `csln.log.route`：`SYNC` 或 `MQ`
- `csln.log.mq.topic`
- `csln.log.mq.tag`
- `csln.log.mq.consumer-group`

当路由设置为 `MQ` 时，需要额外配置 RocketMQ（`rocketmq.name-server`、`rocketmq.producer.group`）。

## 8. 版本记录

详细版本说明见根目录 `erp.version` 文件。当前已记录到：


