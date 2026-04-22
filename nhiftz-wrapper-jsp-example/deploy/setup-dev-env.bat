@echo off
rem One-shot dev setup. Persists the five NHIF_* values as User-level
rem environment variables so NetBeans (and the GlassFish it spawns) pick
rem them up on next launch, and also exports them to THIS cmd session so
rem existing tools see them immediately.
rem
rem Run once. Close and reopen NetBeans. Then F6 / Run on the project -
rem no JVM options, no admin console, no deploy script.
rem
rem Override any value via arguments, e.g.
rem   setup-dev-env.bat NHIF_CLIENT_SECRET=abc==

setlocal ENABLEDELAYEDEXPANSION

rem ---- Defaults (test environment) --------------------------------------
set "NHIF_AUTH_URL=https://test.nhif.or.tz"
set "NHIF_SERVICE_URL=https://test.nhif.or.tz/servicehub"
set "NHIF_CLIENT_ID=11014"
set "NHIF_CLIENT_SECRET=ntbzRGbrwwHj8Jwd7bbPsg=="
set "NHIF_USERNAME=Mtundi"

rem ---- Argument overrides ( KEY=VALUE ... ) ------------------------------
:parse
if "%~1"=="" goto apply
for /f "tokens=1,* delims==" %%A in ("%~1") do (
    set "%%A=%%B"
)
shift
goto parse

:apply
echo Setting 5 User-level env vars for NHIF integration...
call :persist NHIF_AUTH_URL      "%NHIF_AUTH_URL%"
call :persist NHIF_SERVICE_URL   "%NHIF_SERVICE_URL%"
call :persist NHIF_CLIENT_ID     "%NHIF_CLIENT_ID%"
call :persist NHIF_CLIENT_SECRET "%NHIF_CLIENT_SECRET%" HIDE
call :persist NHIF_USERNAME      "%NHIF_USERNAME%"

echo.
echo Done.
echo Next:
echo   1. Close NetBeans (if open) so it re-reads the environment.
echo   2. Reopen NetBeans -^> open the nhiftz-wrapper-jsp-example project.
echo   3. Right-click project -^> Run  (or press F6).
echo   4. Verify: http://localhost:8080/nhiftz-wrapper-jsp-example/health
echo.
endlocal & (
    set "NHIF_AUTH_URL=%NHIF_AUTH_URL%"
    set "NHIF_SERVICE_URL=%NHIF_SERVICE_URL%"
    set "NHIF_CLIENT_ID=%NHIF_CLIENT_ID%"
    set "NHIF_CLIENT_SECRET=%NHIF_CLIENT_SECRET%"
    set "NHIF_USERNAME=%NHIF_USERNAME%"
)
goto :eof

:persist
rem %1 = key, %2 = "value" (quoted), %3 = HIDE to mask output
setx %1 %~2 >nul
if /i "%~3"=="HIDE" (
    echo   %1 = ^(hidden^)
) else (
    echo   %1 = %~2
)
goto :eof
