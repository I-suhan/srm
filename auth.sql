#CREATE DATABASE srm DEFAULT CHARACTER SET utf8mb4;
CREATE TABLE sys_user(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    username        VARCHAR(50) UNIQUE NOT NULL,
    nickname        VARCHAR(100)       NOT NULL,
    password        VARCHAR(100)       NOT NULL,
    status          INT DEFAULT 1,
    disabled         BOOLEAN DEFAULT FALSE,
    token_version    BIGINT,
    created_time     TIMESTAMP(3),
    updated_time     TIMESTAMP(3),
    INDEX idx_user_status(status),
    INDEX idx_user_token_version(token_version)
);
CREATE TABLE sys_role(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(50)        NOT NULL,
    code            VARCHAR(50) UNIQUE NOT NULL,
    status          INT DEFAULT 1,
    created_time     TIMESTAMP(3),
    INDEX idx_role_status(status)
);
CREATE TABLE sys_permission(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(100),
    code            VARCHAR(100) UNIQUE,
    status          INT DEFAULT 1,
    created_time     TIMESTAMP(3)
);
CREATE TABLE sys_user_role(
    user_id         BIGINT NOT NULL,
    role_id         BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    INDEX idx_user_role_role(role_id,user_id)
);
CREATE TABLE sys_role_permission(
    role_id         BIGINT NOT NULL,
    permission_id   BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    INDEX idx_permission_role(permission_id,role_id)
);
CREATE TABLE sys_refresh_token(
    id              BIGINT primary key auto_increment,
    token_hash      VARCHAR(255) not null unique,
    session_id      VARCHAR(64),
    user_id         BIGINT not null,
    status          SMALLINT default 'ACTIVE', -- 1-有效 2-已轮换 3-已撤销 4-已过期
    expired_time     TIMESTAMP(3),
    created_time     TIMESTAMP(3) DEFAULT NOW(),
    revoked_time        TIMESTAMP(3) DEFAULT NULL,
    INDEX idx_status_expires (status, expired_time),
    INDEX idx_refresh_session(session_id),
    INDEX idx_refresh_user(user_id)
);
CREATE TABLE sys_user_session (
id              BIGINT primary key,
user_id         BIGINT,
session_id      VARCHAR(64),
device_id       VARCHAR(100),
device_name     VARCHAR(100),
os              VARCHAR(100),
ipv4            VARCHAR(100),
ipv6            VARCHAR(100),
raw_ip          VARCHAR(100),
user_agent      VARCHAR(500),
status          VARCHAR(20),
created_time      TIMESTAMP(3),
expired_time      TIMESTAMP(3),
last_active_time  TIMESTAMP(3),
refreshed_time    TIMESTAMP(3),
revoked_time      TIMESTAMP(3),
revoked_reason      varchar(50),
INDEX uk_session_id(session_id),
INDEX idx_session_user(user_id),
INDEX idx_session_user_status(user_id,status),
INDEX idx_session_status_expire(status,expired_time)
);
INSERT INTO sys_role
(id,name,code,status,created_time)
VALUES

    (1,'超级管理员','ROLE_ADMIN',1,NOW()),

    (2,'普通用户','ROLE_USER',1,NOW()),

    (3,'审计员','ROLE_AUDIT',1,NOW()),

    (4,'禁用角色','ROLE_DISABLED',1,NOW());
INSERT INTO sys_permission
(id,name,code,create_time)
VALUES
    (1,'用户查询','user:view',NOW()),
    (2,'用户新增','user:create',NOW()),
    (3,'用户修改','user:update',NOW()),
    (4,'用户删除','user:delete',NOW()),
    (5,'日志查看','log:view',NOW()),
    (6,'系统配置','system:config',NOW());
INSERT INTO sys_user_role
(user_id,role_id)
VALUES
    (1,1),
    (2,2),
    (3,3),
    (4,2);
INSERT INTO sys_role_permission
(role_id,permission_id)
VALUES
    (1,1),
    (1,2),
    (1,3),
    (1,4),
    (1,5),
    (1,6);
INSERT INTO sys_user
(id, username, nickname, password, status, disable, token_version, created_time, updated_time)
VALUES
    (1,'admin','系统管理员',
     '$2a$10$admin_password_hash',
     1,
     false,
     1,
     NOW(),
     NOW()),

    (2,'zhangsan','张三',
     '$2a$10$zhangsan_password_hash',
     1,
     false,
     3,
     NOW(),
     NOW()),

    (3,'lisi','李四',
     '$2a$10$lisi_password_hash',
     1,
     false,
     1,
     NOW(),
     NOW()),

    (4,'test','测试账号',
     '$2a$10$test_password_hash',
     0,
     true,
     5,
     NOW(),
     NOW());