$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
& './configurar.ps1'
Get-Content -LiteralPath '.env' | ForEach-Object {
    $par = $_.Split('=',2)
    if ($par.Length -eq 2) { [Environment]::SetEnvironmentVariable($par[0],$par[1],'Process') }
}
docker compose up -d --wait
if ($LASTEXITCODE -ne 0) { throw 'Abra o Docker Desktop. Confira se a porta 3308 está livre.' }
