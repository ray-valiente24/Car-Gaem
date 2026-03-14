@echo off
title The Car Gaem Windows Launcher
color 0A

echo =================================
echo  Car Gaem 26.1 - Windows Launcher
echo =================================
echo.

rem Check if javac exists in PATH
where javac >nul 2>&1
if errorlevel 1 (
    echo JDK not found! Make sure Java JDK is installed and added to PATH.
    pause
    exit /b
)

rem Create bin folder if it doesn't exist
if not exist bin (
    mkdir bin
)

echo Compiling Java source...
echo.
javac -d bin src\main\*.java
if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b
)

echo Compilation successful!
echo Launching game...
echo.
java -cp bin main.Main

exit