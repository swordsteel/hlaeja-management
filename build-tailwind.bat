@echo off
setlocal

:: Define variables for paths
set TAILWIND_CLI=tailwindcss.exe
set CONFIG_PATH=.\tailwindcss\tailwind.config.js
set INPUT_CSS=.\tailwindcss\input.css
set OUTPUT_CSS=.\src\main\resources\static\css\tailwind.css

:: Check if watch mode is requested
if /I "%1"=="watch" (
    echo Running Tailwind CSS in watch mode...
    %TAILWIND_CLI% --config %CONFIG_PATH% -i %INPUT_CSS% -o %OUTPUT_CSS% --watch
) else (
    echo Building Tailwind CSS with minification...
    %TAILWIND_CLI% --config %CONFIG_PATH% -i %INPUT_CSS% -o %OUTPUT_CSS% --minify
)

:: Check for errors
if %ERRORLEVEL% neq 0 (
    echo Error: Tailwind CSS build failed!
    exit /b %ERRORLEVEL%
)

echo Tailwind CSS build completed successfully!
endlocal
