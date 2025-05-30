-- 初始化脚本
-- 这个文件会在MySQL容器启动时自动执行

-- 确保数据库存在
CREATE DATABASE IF NOT EXISTS springbootdb;

-- 切换到目标数据库
USE springbootdb;

-- 设置字符集
ALTER DATABASE springbootdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 显示当前数据库信息
SELECT 'Database springbootdb created successfully!' as message; 