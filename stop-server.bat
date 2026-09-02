@echo off
echo ===================================================
echo   Stopping Java Expense Tracker Tomcat Server...
echo ===================================================
set "CATALINA_HOME=%~dp0..\.tools\apache-tomcat-9.0.121"
set "CATALINA_BASE=%~dp0..\.tools\apache-tomcat-9.0.121"
call "%CATALINA_HOME%\bin\catalina.bat" stop
echo Server stopped.
pause
