@echo off
chcp 65001 > nul
title Evidencia SENA GA7-220501096-AA2-EV01 - My PC Store

echo =======================================================================
echo          COMPILANDO Y EJECUTANDO BACKEND JAVA CON JDBC Y MYSQL         
echo =======================================================================

cd /d "%~dp0"

if not exist bin (
    mkdir bin
)

echo [1/2] Compilando archivos Java con javac...
javac -encoding UTF-8 -cp "lib/mysql-connector-j-8.4.0.jar" -d bin src/com/tienda/modelo/Producto.java src/com/tienda/conexion/ConexionBD.java src/com/tienda/dao/ProductoDAO.java src/com/tienda/Main.java

if %errorlevel% neq 0 (
    echo [ERROR] Ocurrio un error durante la compilacion.
    pause
    exit /b %errorlevel%
)

echo [2/2] Ejecutando aplicacion...
echo.
java -cp "bin;lib/mysql-connector-j-8.4.0.jar" com.tienda.Main %1

pause
