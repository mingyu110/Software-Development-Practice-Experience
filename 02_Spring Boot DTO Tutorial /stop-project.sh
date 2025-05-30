#!/bin/bash

# Spring Boot DTO Tutorial 项目停止脚本

echo "=========================================="
echo "🛑 Spring Boot DTO Tutorial 项目停止"
echo "=========================================="

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 停止Spring Boot应用（如果在运行）
echo -e "${BLUE}🛑 停止Spring Boot应用...${NC}"
pkill -f "spring-boot:run" 2>/dev/null || true
sleep 2

# 停止Docker容器
echo -e "${BLUE}🗄️  停止MySQL容器...${NC}"
docker-compose down

# 清理孤立的容器
echo -e "${BLUE}🧹 清理容器...${NC}"
docker stop spring-dto-mysql 2>/dev/null || true
docker rm spring-dto-mysql 2>/dev/null || true

# 显示清理结果
echo -e "${GREEN}✅ 项目已停止${NC}"
echo -e "${YELLOW}💡 数据已保存在Docker volume中，下次启动时数据仍然存在${NC}"
echo ""
echo -e "${BLUE}如需完全清理数据，请运行:${NC}"
echo -e "   docker volume rm \$(docker volume ls -q | grep spring)"
echo ""

echo -e "${GREEN}=========================================="
echo -e "🏁 清理完成！"
echo -e "==========================================${NC}" 