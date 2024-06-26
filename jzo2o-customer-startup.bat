@echo off
chcp 65001
title jzo2o-customer.jar
echo.
echo [信息] 打包客户工程。
echo.
call  mvn  package -DskipTests=true
echo.
echo [信息] 启动客户工程。
echo.
java -Dfile.encoding=utf-8 -Xmx256m -jar target/jzo2o-customer.jar
