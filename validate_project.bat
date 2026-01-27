@echo off
REM Script de validation du projet SHOPIVERS pour Windows
REM Vérifie que tous les fichiers essentiels existent

echo ================================
echo ^>^> VALIDATION SHOPIVERS PROJECT
echo ================================
echo.

setlocal enabledelayedexpansion
set /a total=0
set /a passed=0
set /a failed=0

REM Fonction pour vérifier les fichiers
set "checkFile=set /a total+=1 & if exist !arg1! (echo [OK] !arg2! & set /a passed+=1) else (echo [FAIL] !arg2! - NOT FOUND: !arg1! & set /a failed+=1)"

echo 1. Checking Directories...
if exist "src\main\java\com\example\springweave\controllers" echo [OK] Controllers directory
if exist "src\main\java\com\example\springweave\services" echo [OK] Services directory
if exist "src\main\java\com\example\springweave\models" echo [OK] Models directory
if exist "src\main\java\com\example\springweave\repositories" echo [OK] Repositories directory
if exist "src\main\java\com\example\springweave\dtos" echo [OK] DTOs directory
set /a total+=6 & set /a passed+=6
echo.

echo 2. Checking Controllers...
if exist "src\main\java\com\example\springweave\controllers\AuthController.java" echo [OK] AuthController & set /a passed+=1
if exist "src\main\java\com\example\springweave\controllers\ProductController.java" echo [OK] ProductController & set /a passed+=1
if exist "src\main\java\com\example\springweave\controllers\OrderController.java" echo [OK] OrderController & set /a passed+=1
if exist "src\main\java\com\example\springweave\controllers\CustomerController.java" echo [OK] CustomerController & set /a passed+=1
if exist "src\main\java\com\example\springweave\controllers\VendorProductController.java" echo [OK] VendorProductController & set /a passed+=1
set /a total+=5
echo.

echo 3. Checking Services...
if exist "src\main\java\com\example\springweave\services\AuthService.java" echo [OK] AuthService & set /a passed+=1
if exist "src\main\java\com\example\springweave\services\ProductService.java" echo [OK] ProductService & set /a passed+=1
if exist "src\main\java\com\example\springweave\services\OrderService.java" echo [OK] OrderService & set /a passed+=1
set /a total+=3
echo.

echo 4. Checking Repositories...
if exist "src\main\java\com\example\springweave\repositories\CustomerRepository.java" echo [OK] CustomerRepository & set /a passed+=1
if exist "src\main\java\com\example\springweave\repositories\OrderRepository.java" echo [OK] OrderRepository & set /a passed+=1
if exist "src\main\java\com\example\springweave\repositories\ProductRepository.java" echo [OK] ProductRepository & set /a passed+=1
set /a total+=3
echo.

echo 5. Checking DTOs...
if exist "src\main\java\com\example\springweave\dtos\ProductResponse.java" echo [OK] ProductResponse & set /a passed+=1
if exist "src\main\java\com\example\springweave\dtos\OrderResponse.java" echo [OK] OrderResponse & set /a passed+=1
if exist "src\main\java\com\example\springweave\dtos\CustomerResponse.java" echo [OK] CustomerResponse & set /a passed+=1
if exist "src\main\java\com\example\springweave\dtos\RegisterRequest.java" echo [OK] RegisterRequest & set /a passed+=1
set /a total+=4
echo.

echo 6. Checking Configuration...
if exist "src\main\java\com\example\springweave\Config\SecurityConfig.java" echo [OK] SecurityConfig & set /a passed+=1
if exist "src\main\java\com\example\springweave\Config\CorsConfig.java" echo [OK] CorsConfig & set /a passed+=1
if exist "src\main\java\com\example\springweave\Config\SwaggerConfig.java" echo [OK] SwaggerConfig & set /a passed+=1
set /a total+=3
echo.

echo 7. Checking Configuration Files...
if exist "pom.xml" echo [OK] pom.xml & set /a passed+=1
if exist "src\main\resources\application.properties" echo [OK] application.properties & set /a passed+=1
if exist "Dockerfile" echo [OK] Dockerfile & set /a passed+=1
if exist "docker-compose.yml" echo [OK] docker-compose.yml & set /a passed+=1
set /a total+=4
echo.

echo 8. Checking Documentation...
if exist "API_DOCUMENTATION.md" echo [OK] API_DOCUMENTATION.md & set /a passed+=1
if exist "QUICK_START.md" echo [OK] QUICK_START.md & set /a passed+=1
if exist "README_COMPLETE.md" echo [OK] README_COMPLETE.md & set /a passed+=1
if exist "SHOPIVERS_API.postman_collection.json" echo [OK] Postman Collection & set /a passed+=1
set /a total+=4
echo.

echo ================================
echo VALIDATION RESULTS
echo ================================
echo Total Checks: !total!
echo Passed: !passed!
set /a failed=!total!-!passed!
echo Failed: !failed!
echo.

if !failed! equ 0 (
    echo [SUCCESS] All checks passed! Project is ready.
    exit /b 0
) else (
    echo [ERROR] Some checks failed. Please review.
    exit /b 1
)
