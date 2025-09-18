@echo off
REM Compile Java files with Gson library
javac -cp ".;gson-2.10.1.jar" main.java StudentRecord.java

REM Check if compilation succeeded
IF %ERRORLEVEL% NEQ 0 (
    echo Compilation failed. Fix errors and try again.
    pause
    exit /b
)

REM Run the program with Gson library
java -cp ".;gson-2.10.1.jar" main

REM Keep the console open after the program exits
pause
