@echo off
setlocal enableextensions enabledelayedexpansion

echo ===================================================
echo   Compiling Bank Account ^& Loan Evaluator System  
echo ===================================================

set "JDK_BIN=C:\Users\yashw\Downloads\oracleJdk-26\bin"

if exist "%JDK_BIN%\javac.exe" (
    set "JAVAC_CMD=%JDK_BIN%\javac.exe"
) else (
    set "JAVAC_CMD=javac"
)

if not exist "bin" mkdir bin

echo Compiling Java source files...
"%JAVAC_CMD%" -d bin src\com\bank\*.java

if %ERRORLEVEL% equ 0 (
    echo.
    echo [SUCCESS] Compilation finished successfully! Class files saved in 'bin' directory.
) else (
    echo.
    echo [ERROR] Compilation failed with errors!
)
