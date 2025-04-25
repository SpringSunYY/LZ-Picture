@echo off
echo.
echo [信息] 正在构建 Web 项目，生成 war/jar 包文件...
echo.

:: 设置 JDK 路径
set JAVA_HOME=E:\Java\Tool\JDK\JDK21
set PATH=%JAVA_HOME%\bin;%PATH%

:: 打印 Java 版本确认是否是 JDK21
java -version
javac -version

%~d0
cd %~dp0
cd ..

call mvn clean package -Dmaven.test.skip=true

pause
