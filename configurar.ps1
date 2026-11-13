$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
if (-not (Test-Path -LiteralPath '.env')) {
    $senhaBanco = [guid]::NewGuid().ToString('N')
    $senhaAdmin = [guid]::NewGuid().ToString('N')
    $senhaRoot = [guid]::NewGuid().ToString('N')
    @("DB_PASSWORD=$senhaBanco", "ADMIN_PASSWORD=$senhaAdmin", "MYSQL_ROOT_PASSWORD=$senhaRoot") | Set-Content -LiteralPath '.env' -Encoding ascii
    Write-Host 'Configuração local criada. A senha de admin está no arquivo .env (ADMIN_PASSWORD).'
} else { Write-Host 'Configuração existente preservada.' }
