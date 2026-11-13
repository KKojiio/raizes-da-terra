param([switch]$Pronto)
$ErrorActionPreference = 'Stop'
[Console]::InputEncoding = New-Object System.Text.UTF8Encoding($false)
[Console]::OutputEncoding = New-Object System.Text.UTF8Encoding($false)
$OutputEncoding = [Console]::OutputEncoding
Set-Location $PSScriptRoot
. './preparar-ambiente.ps1'
$jarPronto = Join-Path $PSScriptRoot 'executavel/raizes-da-terra-console-1.0.0.jar'
if ($Pronto -and (Test-Path -LiteralPath $jarPronto)) {
    if (-not $env:JAVA_HOME) {
        $jdks = Join-Path $env:USERPROFILE '.jdks'
        if (Test-Path -LiteralPath $jdks) {
            $jdk21 = Get-ChildItem -LiteralPath $jdks -Directory | Where-Object { $_.Name -match '21' -and (Test-Path -LiteralPath (Join-Path $_.FullName 'bin/javac.exe')) } | Select-Object -First 1
            if ($jdk21) { $env:JAVA_HOME = $jdk21.FullName }
        }
    }
    if ($env:JAVA_HOME) { $javaRaizes = Join-Path $env:JAVA_HOME 'bin/java.exe' } else { $javaRaizes = 'java' }
    & $javaRaizes '-jar' $jarPronto
} else {
    & './maven.ps1' 'spring-boot:run'
}
exit $LASTEXITCODE
