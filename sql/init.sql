-- CMDB 初始化脚本
CREATE DATABASE IF NOT EXISTS cmdb DEFAULT CHARACTER SET utf8mb4;
USE cmdb;

CREATE TABLE applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100) UNIQUE NOT NULL,
    owner VARCHAR(100),
    repo_url VARCHAR(500),
    biz_line VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE environments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    app_id BIGINT NOT NULL,
    env_type ENUM('DEV','TEST','PRE','PROD') NOT NULL,
    nacos_namespace VARCHAR(200),
    scaling_group_id VARCHAR(200),
    isolation VARCHAR(50) DEFAULT 'SHARED',
    FOREIGN KEY (app_id) REFERENCES applications(id)
);

CREATE TABLE instances (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    env_id BIGINT NOT NULL,
    ip VARCHAR(50) NOT NULL,
    hostname VARCHAR(100),
    status ENUM('ONLINE','OFFLINE','FAULT') DEFAULT 'ONLINE',
    agent_version VARCHAR(50),
    last_heartbeat TIMESTAMP,
    app_version VARCHAR(50),
    metadata JSON,
    FOREIGN KEY (env_id) REFERENCES environments(id)
);

CREATE TABLE deploy_strategies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    app_id BIGINT NOT NULL,
    env_id BIGINT NOT NULL,
    strategy_type VARCHAR(50) DEFAULT 'BATCH',
    batch_config JSON,
    health_check_url VARCHAR(500),
    auto_promote TINYINT(1) DEFAULT 0,
    rollback_enabled TINYINT(1) DEFAULT 1,
    FOREIGN KEY (app_id) REFERENCES applications(id),
    FOREIGN KEY (env_id) REFERENCES environments(id)
);

CREATE TABLE deploy_tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    app_id BIGINT NOT NULL,
    env_id BIGINT NOT NULL,
    version VARCHAR(100) NOT NULL,
    package_url VARCHAR(500),
    status ENUM('PENDING','RUNNING','BATCH_IN_PROGRESS','PAUSED','COMPLETED','FAILED','ROLLING_BACK','ROLLBACK_COMPLETED') DEFAULT 'PENDING',
    rollback_to_version VARCHAR(100),
    creator VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (app_id) REFERENCES applications(id),
    FOREIGN KEY (env_id) REFERENCES environments(id)
);

CREATE TABLE scaling_policies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    env_id BIGINT NOT NULL,
    metric VARCHAR(50) DEFAULT 'cpu',
    threshold_up INT,
    threshold_down INT,
    scale_out_count INT DEFAULT 1,
    scale_in_count INT DEFAULT 1,
    duration INT COMMENT '持续分钟',
    cooldown INT COMMENT '冷却秒',
    min_instances INT DEFAULT 1,
    max_instances INT DEFAULT 50,
    FOREIGN KEY (env_id) REFERENCES environments(id)
);

CREATE TABLE scaling_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    env_id BIGINT NOT NULL,
    action ENUM('SCALE_OUT','SCALE_IN'),
    old_count INT,
    new_count INT,
    triggered_by VARCHAR(50),
    status VARCHAR(50),
    detail JSON,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP,
    FOREIGN KEY (env_id) REFERENCES environments(id)
);