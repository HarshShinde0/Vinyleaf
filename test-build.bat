@echo off
echo Testing Vinyleaf project build...
cd /d "C:\Users\lmfph\Downloads\Vinyleaf"
gradlew.bat assembleDebug
if %ERRORLEVEL% EQU 0 (
    echo Build successful!
) else (
    echo Build failed with error code %ERRORLEVEL%
)
pause

