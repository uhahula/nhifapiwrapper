<#
.SYNOPSIS
  Write the five NHIF_* values into %CATALINA_BASE%\bin\setenv.bat so
  Tomcat on Windows picks them up - whether it runs as a service, from
  startup.bat, or under NetBeans.

.DESCRIPTION
  Tomcat's startup scripts source bin\setenv.bat (if present) before
  launching the JVM. Unlike User-level env vars set with setx, this
  works even when the Tomcat service runs under LocalSystem.

  Idempotent. Preserves any other content in setenv.bat by rewriting
  only the managed block between '# >>> nhif-config' and '# <<< nhif-config'.

.PARAMETER CatalinaHome
  Path to the Tomcat install (the dir containing bin\, webapps\, etc.).
  Defaults to $env:CATALINA_HOME.

.PARAMETER Restart
  If present, attempts to restart the Tomcat Windows service after
  writing setenv.bat.

.PARAMETER ServiceName
  Windows service name. Default: Tomcat8.

.EXAMPLE
  .\setup-tomcat-win.ps1 -CatalinaHome 'C:\apache-tomcat-8.5.87' -Restart

.EXAMPLE
  .\setup-tomcat-win.ps1 `
    -CatalinaHome 'C:\apache-tomcat-8.5.87' `
    -ClientSecret 'realSecretHere=='
#>

[CmdletBinding()]
param(
    [string]$CatalinaHome = $env:CATALINA_HOME,
    [string]$ServiceName  = 'Tomcat8',
    [switch]$Restart,

    [string]$AuthUrl      = 'https://test.nhif.or.tz',
    [string]$ServiceUrl   = 'https://test.nhif.or.tz/servicehub',
    [string]$ClientId     = '11014',
    [string]$ClientSecret = 'ntbzRGbrwwHj8Jwd7bbPsg==',
    [string]$Username     = 'Mtundi'
)

$ErrorActionPreference = 'Stop'

if (-not $CatalinaHome) {
    throw "CatalinaHome not set. Pass -CatalinaHome 'C:\path\to\tomcat' or set `$env:CATALINA_HOME."
}
$binDir = Join-Path $CatalinaHome 'bin'
if (-not (Test-Path $binDir)) {
    throw "Tomcat bin dir not found: $binDir"
}
$setenv = Join-Path $binDir 'setenv.bat'

$BEGIN = 'rem >>> nhif-config (managed by setup-tomcat-win.ps1)'
$END   = 'rem <<< nhif-config'

# Build the managed block.
$block = @(
    $BEGIN,
    "set `"NHIF_AUTH_URL=$AuthUrl`"",
    "set `"NHIF_SERVICE_URL=$ServiceUrl`"",
    "set `"NHIF_CLIENT_ID=$ClientId`"",
    "set `"NHIF_CLIENT_SECRET=$ClientSecret`"",
    "set `"NHIF_USERNAME=$Username`"",
    $END
) -join "`r`n"

# Read existing file (if any), strip previous managed block, append fresh one.
$existing = ''
if (Test-Path $setenv) {
    $raw = Get-Content $setenv -Raw
    # Remove any prior managed block, including surrounding blank lines.
    $existing = [Regex]::Replace(
        $raw,
        '(?ms)^\s*rem >>> nhif-config.*?^\s*rem <<< nhif-config\s*',
        '',
        'Multiline'
    ).TrimEnd()
}

$final = if ($existing) { "$existing`r`n`r`n$block`r`n" } else { "@echo off`r`n`r`n$block`r`n" }
Set-Content -Path $setenv -Value $final -Encoding ASCII
Write-Host ("Wrote managed block to {0}" -f $setenv) -ForegroundColor Green

# Display what landed (secret masked).
Write-Host "  NHIF_AUTH_URL      = $AuthUrl"      -ForegroundColor DarkGray
Write-Host "  NHIF_SERVICE_URL   = $ServiceUrl"   -ForegroundColor DarkGray
Write-Host "  NHIF_CLIENT_ID     = $ClientId"     -ForegroundColor DarkGray
Write-Host "  NHIF_CLIENT_SECRET = (hidden)"      -ForegroundColor DarkGray
Write-Host "  NHIF_USERNAME      = $Username"     -ForegroundColor DarkGray

if ($Restart) {
    $svc = Get-Service -Name $ServiceName -ErrorAction SilentlyContinue
    if (-not $svc) {
        Write-Warning "Service '$ServiceName' not found. Skipping restart. Pass -ServiceName <name> or start Tomcat yourself."
    } else {
        Write-Host "Restarting service '$ServiceName' ..." -ForegroundColor Cyan
        Restart-Service -Name $ServiceName -Force
        Write-Host "Service status: $((Get-Service $ServiceName).Status)" -ForegroundColor Green
    }
} else {
    Write-Host ""
    Write-Host "Next: restart Tomcat for the new values to take effect." -ForegroundColor Yellow
    Write-Host "  Windows service:   net stop $ServiceName && net start $ServiceName"
    Write-Host "  NetBeans-managed:  Services -> Tomcat -> right-click -> Restart"
    Write-Host "  Manual:            $binDir\shutdown.bat then $binDir\startup.bat"
    Write-Host ""
    Write-Host "Verify: http://localhost:8080/demo/health  (or whatever your context root is)" -ForegroundColor Yellow
}
