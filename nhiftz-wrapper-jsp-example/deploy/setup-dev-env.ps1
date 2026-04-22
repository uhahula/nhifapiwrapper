<#
.SYNOPSIS
  One-shot dev setup. Persists the five NHIF_* values as User-level
  environment variables so NetBeans (and the GlassFish it spawns) pick
  them up on next launch, and also exports them to the current shell
  so existing tools see them immediately.

.DESCRIPTION
  Run once. Close and reopen NetBeans. Then F6 / Run on the project -
  no JVM options, no admin console, no deploy script.

  Re-run any time credentials change.

.EXAMPLE
  .\setup-dev-env.ps1                              # test env values (baked in defaults below)
  .\setup-dev-env.ps1 -ClientSecret 'abc=='        # override one value
#>

[CmdletBinding()]
param(
    [string]$AuthUrl      = 'https://test.nhif.or.tz',
    [string]$ServiceUrl   = 'https://test.nhif.or.tz/servicehub',
    [string]$ClientId     = '11014',
    [string]$ClientSecret = 'ntbzRGbrwwHj8Jwd7bbPsg==',
    [string]$Username     = 'Mtundi'
)

$pairs = [ordered]@{
    NHIF_AUTH_URL      = $AuthUrl
    NHIF_SERVICE_URL   = $ServiceUrl
    NHIF_CLIENT_ID     = $ClientId
    NHIF_CLIENT_SECRET = $ClientSecret
    NHIF_USERNAME      = $Username
}

Write-Host "Setting 5 User-level env vars for NHIF integration..." -ForegroundColor Cyan
foreach ($k in $pairs.Keys) {
    [Environment]::SetEnvironmentVariable($k, $pairs[$k], 'User')  # persistent
    Set-Item -Path "env:$k" -Value $pairs[$k]                      # current shell
    $masked = if ($k -eq 'NHIF_CLIENT_SECRET') { '(hidden)' } else { $pairs[$k] }
    Write-Host ("  {0,-22} = {1}" -f $k, $masked) -ForegroundColor DarkGray
}

Write-Host ""
Write-Host "Done." -ForegroundColor Green
Write-Host "Next:" -ForegroundColor Yellow
Write-Host "  1. Close NetBeans (if open) so it re-reads the environment."
Write-Host "  2. Reopen NetBeans -> open the nhiftz-wrapper-jsp-example project."
Write-Host "  3. Right-click project -> Run (or press F6)."
Write-Host "  4. Verify: http://localhost:8080/nhiftz-wrapper-jsp-example/health"
