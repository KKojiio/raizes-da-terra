# Inicializador do Maven; não é o Maven Wrapper oficial.
$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
if (-not $env:JAVA_HOME) {
    $jdks = Join-Path $env:USERPROFILE '.jdks'
    if (Test-Path -LiteralPath $jdks) {
        $jdk21 = Get-ChildItem -LiteralPath $jdks -Directory | Where-Object { $_.Name -match '21' -and (Test-Path -LiteralPath (Join-Path $_.FullName 'bin/javac.exe')) } | Select-Object -First 1
        if ($jdk21) { $env:JAVA_HOME = $jdk21.FullName }
    }
}
$mavenVersion = '3.9.11'
$mavenDir = Join-Path $PSScriptRoot ".tools/apache-maven-$mavenVersion"
if (-not (Test-Path -LiteralPath "$mavenDir/bin/mvn.cmd")) {
    New-Item -ItemType Directory -Force -Path '.tools' | Out-Null
    $url = "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$mavenVersion/apache-maven-$mavenVersion-bin.zip"
    Invoke-WebRequest -Uri $url -OutFile '.tools/maven.zip'
    Invoke-WebRequest -Uri "$url.sha512" -OutFile '.tools/maven.zip.sha512'
    $checksum = (Get-Content -LiteralPath '.tools/maven.zip.sha512' -Raw).Trim().Split(' ')[0]
    $actual = (Get-FileHash -Algorithm SHA512 -LiteralPath '.tools/maven.zip').Hash
    if ($actual -ne $checksum) { throw 'Checksum do Maven não confere.' }
    Expand-Archive -LiteralPath '.tools/maven.zip' -DestinationPath '.tools' -Force
}
& "$mavenDir/bin/mvn.cmd" @args
exit $LASTEXITCODE
