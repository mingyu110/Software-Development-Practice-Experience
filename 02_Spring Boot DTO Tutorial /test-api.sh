#!/bin/bash

# Spring Boot DTO Tutorial API 测试脚本
# 使用curl命令测试所有API接口

BASE_URL="http://localhost:8080/api/employees"

echo "=========================================="
echo "Spring Boot DTO Tutorial API 测试"
echo "=========================================="

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 检查服务是否运行
echo -e "${BLUE}1. 检查服务健康状态...${NC}"
curl -s "${BASE_URL}/health"
echo -e "\n"

# 获取所有员工
echo -e "${BLUE}2. 获取所有员工...${NC}"
curl -s "${BASE_URL}" | jq '.'
echo -e "\n"

# 创建新员工
echo -e "${BLUE}3. 创建新员工...${NC}"
NEW_EMPLOYEE=$(curl -s -X POST "${BASE_URL}" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "测试",
    "lastName": "用户",
    "email": "test@example.com",
    "department": "QA",
    "salary": 60000
  }')
echo $NEW_EMPLOYEE | jq '.'

# 提取新创建员工的ID
EMPLOYEE_ID=$(echo $NEW_EMPLOYEE | jq -r '.id')
echo -e "${GREEN}创建的员工ID: $EMPLOYEE_ID${NC}\n"

# 根据ID获取员工
echo -e "${BLUE}4. 根据ID获取员工...${NC}"
curl -s "${BASE_URL}/${EMPLOYEE_ID}" | jq '.'
echo -e "\n"

# 更新员工信息
echo -e "${BLUE}5. 更新员工信息...${NC}"
curl -s -X PUT "${BASE_URL}/${EMPLOYEE_ID}" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "更新的",
    "lastName": "用户",
    "salary": 70000
  }' | jq '.'
echo -e "\n"

# 根据邮箱获取员工
echo -e "${BLUE}6. 根据邮箱获取员工...${NC}"
curl -s "${BASE_URL}/email/test@example.com" | jq '.'
echo -e "\n"

# 根据部门获取员工
echo -e "${BLUE}7. 根据部门获取员工 (IT部门)...${NC}"
curl -s "${BASE_URL}/department/IT" | jq '.'
echo -e "\n"

# 根据姓名搜索员工
echo -e "${BLUE}8. 根据姓名搜索员工 (搜索'张')...${NC}"
curl -s "${BASE_URL}/search?name=张" | jq '.'
echo -e "\n"

# 根据薪水范围查找员工
echo -e "${BLUE}9. 根据薪水范围查找员工 (60000-80000)...${NC}"
curl -s "${BASE_URL}/salary?min=60000&max=80000" | jq '.'
echo -e "\n"

# 检查邮箱是否存在
echo -e "${BLUE}10. 检查邮箱是否存在...${NC}"
echo -e "检查 test@example.com:"
curl -s "${BASE_URL}/check-email?email=test@example.com"
echo -e "\n检查 nonexistent@example.com:"
curl -s "${BASE_URL}/check-email?email=nonexistent@example.com"
echo -e "\n"

# 测试验证错误
echo -e "${BLUE}11. 测试数据验证 (应该返回错误)...${NC}"
curl -s -X POST "${BASE_URL}" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "",
    "lastName": "",
    "email": "invalid-email",
    "salary": -1000
  }' | jq '.'
echo -e "\n"

# 测试重复邮箱
echo -e "${BLUE}12. 测试重复邮箱 (应该返回错误)...${NC}"
curl -s -X POST "${BASE_URL}" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "另一个",
    "lastName": "用户",
    "email": "test@example.com",
    "department": "HR",
    "salary": 50000
  }' | jq '.'
echo -e "\n"

# 删除员工
echo -e "${YELLOW}13. 删除刚创建的测试员工...${NC}"
curl -s -X DELETE "${BASE_URL}/${EMPLOYEE_ID}"
echo -e "${GREEN}员工已删除${NC}\n"

# 验证删除
echo -e "${BLUE}14. 验证员工已删除 (应该返回404)...${NC}"
curl -s "${BASE_URL}/${EMPLOYEE_ID}" | jq '.'
echo -e "\n"

echo -e "${GREEN}=========================================="
echo -e "API 测试完成!"
echo -e "==========================================${NC}" 