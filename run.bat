@echo off
setlocal enableextensions enabledelayedexpansion

set "JDK_BIN=C:\Users\yashw\Downloads\oracleJdk-26\bin"

if exist "%JDK_BIN%\java.exe" (
    set "JAVA_CMD=%JDK_BIN%\java.exe"
) else (
    set "JAVA_CMD=java"
)

if not exist "bin\com\bank\Main.class" (
    echo [INFO] Binaries not found. Running compilation first...
    call build.bat
    if %ERRORLEVEL% neq 0 exit /b %ERRORLEVEL%
)

echo.
echo Launching Bank Account & Loan Evaluation System...
echo.
"%JAVA_CMD%" -ea -cp bin com.bank.Main
