<#
.SYNOPSIS
  Deploy nhiftz-wrapper-jsp-example to a local or remote GlassFish 7 domain
  and wire the NHIF_* configuration via JVM system properties.

.DESCRIPTION
  Idempotent. Safe to re-run. Cleans any existing -DNHIF_* JVM options so
  you never end up with stale config, then applies the five values passed in.

.PARAMETER War
  Path to the WAR to deploy. Defaults to the built artifact under target\.

.PARAMETER ContextRoot
  Servlet context. Default: nhiftz-wrapper-jsp-example.

.PARAMETER AsAdmin
  Path to asadmin (or asadmin.bat). Default: looks on PATH.

.PARAMETER Domain
  GlassFish domain name. Default: domain1.

.PARAMETER AuthUrl, ServiceUrl, ClientId, ClientSecret, Username
  NHIF endpoint / credential values.

.PARAMETER HealthUrl
  Full URL to hit after deploy to verify configuration. Optional.

.EXAMPLE
  .\deploy-glassfish.ps1 `
    -War .\target\nhiftz-wrapper-jsp-example.war `
    -AuthUrl    'https://test.nhif.or.tz' `
    -ServiceUrl 'https://test.nhif.or.tz/servicehub' `
    -ClientId   '11014' `
    -ClientSecret 'ntbzRGbrwwHj8Jwd7bbPsg==' `
    -Username   'Mtundi' `
    -HealthUrl  'http://localhost:8080/nhiftz-wrapper-jsp-example/health'
#>

[CmdletBinding()]
param(
    [string]$War          = "target\nhiftz-wrapper-jsp-example.war",
    [string]$ContextRoot  = "nhiftz-wrapper-jsp-example",
    [string]$AsAdmin      = "asadmin",
    [string]$Domain       = "domain1",

    [Parameter(Mandatory=$true)][string]$AuthUrl,
    [Parameter(Mandatory=$true)][string]$ServiceUrl,
    [Parameter(Mandatory=$true)][string]$ClientId,
    [Parameter(Mandatory=$true)][string]$ClientSecret,
    [Parameter(Mandatory=$true)][string]$Username,

    [string]$HealthUrl
)

$ErrorActionPreference = 'Stop'

function Invoke-AsAdmin {
    param([Parameter(ValueFromRemainingArguments=$true)][string[]]$Args)
    Write-Host "  asadmin $($Args -join ' ')" -ForegroundColor DarkGray
    & $AsAdmin @Args
    if ($LASTEXITCODE -ne 0) { throw "asadmin exited $LASTEXITCODE" }
}

if (-not (Test-Path $War)) {
    throw "WAR not found: $War  (build it first: mvn package)"
}

Write-Host "==> Ensuring domain '$Domain' is running" -ForegroundColor Cyan
& $AsAdmin start-domain $Domain 2>$null | Out-Null   # no-op if already up

Write-Host "==> Clearing any existing -DNHIF_* JVM options" -ForegroundColor Cyan
$existing = & $AsAdmin list-jvm-options 2>$null | Where-Object { $_ -match '^-DNHIF_' }
foreach ($opt in $existing) {
    $trimmed = $opt.Trim()
    if ($trimmed) {
        Write-Host "  removing $trimmed" -ForegroundColor DarkGray
        # '--' separates asadmin flags from the literal option value that starts with '-'.
        & $AsAdmin delete-jvm-options -- $trimmed | Out-Null
    }
}

Write-Host "==> Applying NHIF_* JVM options" -ForegroundColor Cyan
$opts = @(
    "-DNHIF_AUTH_URL=$AuthUrl",
    "-DNHIF_SERVICE_URL=$ServiceUrl",
    "-DNHIF_CLIENT_ID=$ClientId",
    "-DNHIF_CLIENT_SECRET=$ClientSecret",
    "-DNHIF_USERNAME=$Username"
)
foreach ($o in $opts) {
    # asadmin uses ':' as a list separator, so any literal ':' inside the value
    # must be escaped with '\:' for create-jvm-options.
    $escaped = $o -replace ':', '\:'
    Invoke-AsAdmin create-jvm-options -- $escaped
}

Write-Host "==> Undeploying previous '$ContextRoot' (if any)" -ForegroundColor Cyan
$apps = & $AsAdmin list-applications 2>$null | Where-Object { $_ -match "^$ContextRoot\b" }
if ($apps) {
    Invoke-AsAdmin undeploy $ContextRoot
}

Write-Host "==> Deploying $War" -ForegroundColor Cyan
Invoke-AsAdmin deploy --contextroot $ContextRoot --name $ContextRoot --force=true $War

Write-Host "==> Restarting domain so JVM options take effect" -ForegroundColor Cyan
Invoke-AsAdmin restart-domain $Domain

if ($HealthUrl) {
    Write-Host "==> Probing health at $HealthUrl" -ForegroundColor Cyan
    $deadline = (Get-Date).AddSeconds(60)
    do {
        try {
            $r = Invoke-WebRequest -Uri $HealthUrl -UseBasicParsing -TimeoutSec 5 -SkipCertificateCheck
            if ($r.StatusCode -eq 200) {
                Write-Host "health OK: $($r.Content)" -ForegroundColor Green
                exit 0
            }
        } catch {
            Start-Sleep -Seconds 2
        }
    } while ((Get-Date) -lt $deadline)
    throw "Health probe did not return 200 within 60s"
}

Write-Host "Done. Open: http(s)://<host>:<port>/$ContextRoot/" -ForegroundColor Green
