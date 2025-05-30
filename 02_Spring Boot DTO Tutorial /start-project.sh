#!/bin/bash

# Spring Boot DTO Tutorial 项目启动脚本

echo "=========================================="
echo "🚀 Spring Boot DTO Tutorial 项目启动"
echo "=========================================="

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 检查Docker是否运行
echo -e "${BLUE}📋 检查Docker状态...${NC}"
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Docker未运行，请先启动Docker${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Docker运行正常${NC}\n"

# 检查端口3306是否被占用
echo -e "${BLUE}📋 检查端口3306...${NC}"
if lsof -Pi :3306 -sTCP:LISTEN -t >/dev/null ; then
    echo -e "${YELLOW}⚠️  端口3306已被占用，正在停止现有MySQL容器...${NC}"
    docker stop spring-dto-mysql 2>/dev/null || true
    docker rm spring-dto-mysql 2>/dev/null || true
    sleep 2
fi

# 启动MySQL数据库
echo -e "${BLUE}🗄️  启动MySQL数据库容器...${NC}"
docker-compose up -d mysql

# 等待MySQL启动
echo -e "${YELLOW}⏳ 等待MySQL启动完成...${NC}"
while ! docker exec spring-dto-mysql mysqladmin ping -h"localhost" --silent; do
    echo -e "${YELLOW}   MySQL还在启动中，请稍候...${NC}"
    sleep 3
done
echo -e "${GREEN}✅ MySQL启动成功！${NC}\n"

# 显示数据库连接信息
echo -e "${BLUE}📊 数据库连接信息:${NC}"
echo -e "   主机: localhost"
echo -e "   端口: 3306"
echo -e "   数据库: springbootdb"
echo -e "   用户名: root"
echo -e "   密码: rootpassword"
echo ""

# 检查Java和Maven
echo -e "${BLUE}📋 检查Java和Maven...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ 未找到Java，请安装Java 17+${NC}"
    exit 1
fi
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ 未找到Maven，请安装Maven${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Java和Maven检查通过${NC}\n"

# 编译项目
echo -e "${BLUE}🔨 编译Spring Boot项目...${NC}"
mvn clean compile -q
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ 项目编译失败${NC}"
    exit 1
fi
echo -e "${GREEN}✅ 项目编译成功${NC}\n"

# 启动Spring Boot应用
echo -e "${BLUE}🚀 启动Spring Boot应用...${NC}"
echo -e "${YELLOW}   应用将在 http://localhost:8080 启动${NC}"
echo -e "${YELLOW}   按 Ctrl+C 停止应用${NC}\n"

# 在后台启动应用
mvn spring-boot:run &
SPRING_PID=$!

# 等待应用启动
echo -e "${YELLOW}⏳ 等待Spring Boot应用启动...${NC}"
for i in {1..30}; do
    if curl -s http://localhost:8080/api/employees/health > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Spring Boot应用启动成功！${NC}\n"
        break
    fi
    if [ $i -eq 30 ]; then
        echo -e "${RED}❌ 应用启动超时${NC}"
        kill $SPRING_PID 2>/dev/null
        exit 1
    fi
    sleep 2
done

# 显示快速测试命令
echo -e "${BLUE}🧪 快速测试命令:${NC}"
echo -e "   健康检查: curl http://localhost:8080/api/employees/health"
echo -e "   获取所有员工: curl http://localhost:8080/api/employees"
echo -e "   运行完整测试: ./test-api.sh"
echo ""

echo -e "${GREEN}=========================================="
echo -e "🎉 项目启动完成！"
echo -e "   应用地址: http://localhost:8080"
echo -e "   API文档请查看 README.md"
echo -e "==========================================${NC}"

# 等待用户输入
echo -e "${YELLOW}按 Enter 键来运行API测试，或 Ctrl+C 退出${NC}"
read

# 运行API测试
if [ -f "./test-api.sh" ]; then
    echo -e "${BLUE}🧪 运行API测试...${NC}\n"
    ./test-api.sh
fi

# 清理函数
cleanup() {
    echo -e "\n${YELLOW}🧹 正在清理资源...${NC}"
    kill $SPRING_PID 2>/dev/null
    echo -e "${GREEN}✅ 清理完成${NC}"
}

# 设置退出时清理
trap cleanup EXIT 