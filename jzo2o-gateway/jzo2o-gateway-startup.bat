@echo off
chcp 65001
title jzo2o-gateway
echo.
echo [信息] 打包网关工程。
echo.
call  mvn  package -DskipTests=true
echo.
echo [信息] 启动网关工程。
echo.
java -Dfile.encoding=utf-8 -Xmx128m -jar target/jzo2o-gateway.jar
