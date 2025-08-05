-- 如果 shedlock 表不存在，则创建该表用于分布式锁
CREATE TABLE IF NOT EXISTS shedlock (
    -- 锁的唯一名称，对应 @SchedulerLock 注解中的 name 属性
    name VARCHAR(64) NOT NULL,
    -- 锁的到期时间，防止因节点故障而导致锁无法释放
    lock_until TIMESTAMP(3) NOT NULL,
    -- 锁的获取时间
    locked_at TIMESTAMP(3) NOT NULL,
    -- 锁的持有者标识，通常是主机名或进程 ID
    locked_by VARCHAR(255) NOT NULL,
    -- 将 name 字段设置为主键，确保锁名称的唯一性
    PRIMARY KEY (name)
);
