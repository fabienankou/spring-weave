#!/bin/bash

# Script de validation du projet SHOPIVERS
# Vérifie que tous les fichiers essentiels existent et que l'application compile

echo "================================"
echo "🔍 VALIDATION SHOPIVERS PROJECT"
echo "================================"
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Compteurs
total=0
passed=0
failed=0

# Fonction de test
check_file() {
    total=$((total + 1))
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $2"
        passed=$((passed + 1))
    else
        echo -e "${RED}✗${NC} $2 - FILE NOT FOUND: $1"
        failed=$((failed + 1))
    fi
}

check_directory() {
    total=$((total + 1))
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} $2"
        passed=$((passed + 1))
    else
        echo -e "${RED}✗${NC} $2 - DIRECTORY NOT FOUND: $1"
        failed=$((failed + 1))
    fi
}

echo "📁 Checking Directory Structure..."
check_directory "src/main/java/com/example/springweave/controllers" "Controllers directory"
check_directory "src/main/java/com/example/springweave/services" "Services directory"
check_directory "src/main/java/com/example/springweave/models" "Models directory"
check_directory "src/main/java/com/example/springweave/repositories" "Repositories directory"
check_directory "src/main/java/com/example/springweave/dtos" "DTOs directory"
check_directory "src/main/java/com/example/springweave/security" "Security directory"
check_directory "src/main/java/com/example/springweave/Config" "Config directory"
check_directory "src/main/java/com/example/springweave/exceptions" "Exceptions directory"
echo ""

echo "🎮 Checking Controllers..."
check_file "src/main/java/com/example/springweave/controllers/AuthController.java" "AuthController"
check_file "src/main/java/com/example/springweave/controllers/ProductController.java" "ProductController"
check_file "src/main/java/com/example/springweave/controllers/OrderController.java" "OrderController"
check_file "src/main/java/com/example/springweave/controllers/CustomerController.java" "CustomerController"
check_file "src/main/java/com/example/springweave/controllers/VendorProductController.java" "VendorProductController"
echo ""

echo "⚙️ Checking Services..."
check_file "src/main/java/com/example/springweave/services/AuthService.java" "AuthService"
check_file "src/main/java/com/example/springweave/services/ProductService.java" "ProductService"
check_file "src/main/java/com/example/springweave/services/OrderService.java" "OrderService"
echo ""

echo "💾 Checking Repositories..."
check_file "src/main/java/com/example/springweave/repositories/CustomerRepository.java" "CustomerRepository"
check_file "src/main/java/com/example/springweave/repositories/OrderRepository.java" "OrderRepository"
check_file "src/main/java/com/example/springweave/repositories/OrderItemRepository.java" "OrderItemRepository"
check_file "src/main/java/com/example/springweave/repositories/ProductRepository.java" "ProductRepository"
echo ""

echo "📦 Checking DTOs..."
check_file "src/main/java/com/example/springweave/dtos/ProductResponse.java" "ProductResponse DTO"
check_file "src/main/java/com/example/springweave/dtos/OrderResponse.java" "OrderResponse DTO"
check_file "src/main/java/com/example/springweave/dtos/CustomerResponse.java" "CustomerResponse DTO"
check_file "src/main/java/com/example/springweave/dtos/RegisterRequest.java" "RegisterRequest DTO"
check_file "src/main/java/com/example/springweave/dtos/CreateOrderRequest.java" "CreateOrderRequest DTO"
echo ""

echo "🔐 Checking Security..."
check_file "src/main/java/com/example/springweave/security/JwtService.java" "JwtService"
check_file "src/main/java/com/example/springweave/security/JwtAuthenticationFilter.java" "JwtAuthenticationFilter"
echo ""

echo "⚙️ Checking Configuration..."
check_file "src/main/java/com/example/springweave/Config/SecurityConfig.java" "SecurityConfig"
check_file "src/main/java/com/example/springweave/Config/CorsConfig.java" "CorsConfig"
check_file "src/main/java/com/example/springweave/Config/SwaggerConfig.java" "SwaggerConfig"
echo ""

echo "🚨 Checking Exception Handling..."
check_file "src/main/java/com/example/springweave/exceptions/GlobalExceptionHandler.java" "GlobalExceptionHandler"
echo ""

echo "📋 Checking Configuration Files..."
check_file "pom.xml" "Maven POM"
check_file "src/main/resources/application.properties" "Application Properties"
check_file "Dockerfile" "Dockerfile"
check_file "docker-compose.yml" "Docker Compose"
check_file ".env.example" "Environment Example"
echo ""

echo "📚 Checking Documentation..."
check_file "API_DOCUMENTATION.md" "API Documentation"
check_file "QUICK_START.md" "Quick Start Guide"
check_file "WORK_SUMMARY.md" "Work Summary"
check_file "IMPLEMENTATION_CHECKLIST.md" "Implementation Checklist"
check_file "README_COMPLETE.md" "Complete README"
check_file "SHOPIVERS_API.postman_collection.json" "Postman Collection"
echo ""

# Vérification de la compilation
echo "🔨 Checking Maven Compilation..."
total=$((total + 1))

if command -v mvn &> /dev/null; then
    echo -e "${YELLOW}⏳ Compiling project with Maven...${NC}"
    if mvn clean compile -q 2>/dev/null; then
        echo -e "${GREEN}✓${NC} Maven compilation successful"
        passed=$((passed + 1))
    else
        echo -e "${RED}✗${NC} Maven compilation failed"
        failed=$((failed + 1))
    fi
elif command -v ./mvnw &> /dev/null; then
    echo -e "${YELLOW}⏳ Compiling project with Maven Wrapper...${NC}"
    if ./mvnw clean compile -q 2>/dev/null; then
        echo -e "${GREEN}✓${NC} Maven Wrapper compilation successful"
        passed=$((passed + 1))
    else
        echo -e "${RED}✗${NC} Maven Wrapper compilation failed"
        failed=$((failed + 1))
    fi
else
    echo -e "${YELLOW}⚠${NC} Maven not found, skipping compilation check"
fi
echo ""

# Résumé
echo "================================"
echo "📊 VALIDATION RESULTS"
echo "================================"
echo -e "Total Checks: ${total}"
echo -e "${GREEN}Passed: ${passed}${NC}"
echo -e "${RED}Failed: ${failed}${NC}"
echo ""

if [ $failed -eq 0 ]; then
    echo -e "${GREEN}✅ ALL CHECKS PASSED - PROJECT IS READY!${NC}"
    exit 0
else
    echo -e "${RED}❌ SOME CHECKS FAILED - PLEASE REVIEW${NC}"
    exit 1
fi
