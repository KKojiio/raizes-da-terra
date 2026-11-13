$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
. './preparar-ambiente.ps1'
& './maven.ps1' 'verify'
exit $LASTEXITCODE
