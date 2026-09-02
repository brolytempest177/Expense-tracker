@echo off
echo ===================================================
echo   Starting Java Expense Tracker on Apache Tomcat...
echo ===================================================
if not defined JAVA_HOME (
    set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.2"
)
set "CATALINA_HOME=%~dp0..\.tools\apache-tomcat-9.0.121"
set "CATALINA_BASE=%~dp0..\.tools\apache-tomcat-9.0.121"
call "%CATALINA_HOME%\bin\catalina.bat" start
echo.
echo ===================================================
echo Server is running!
echo Open your browser at:
echo    http://localhost:8080/expense-tracker/
echo    OR
echo    http://localhost:8080/
echo ===================================================
echo.
pause
