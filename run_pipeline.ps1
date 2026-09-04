$ErrorActionPreference = "Stop"

Write-Host "========================================"
Write-Host "       INICIO DEL PROCESAMIENTO"
Write-Host "========================================"


# ========================================
# 1. BASIC-256
# ========================================

Write-Host ""
Write-Host "[1/4] Ejecutando BASIC-256..."

Set-Location "$PSScriptRoot\Basic256"

$tempOutput = Join-Path $env:TEMP "basic256_output.txt"
$tempError = Join-Path $env:TEMP "basic256_error.txt"

Remove-Item $tempOutput, $tempError -ErrorAction SilentlyContinue

$process = Start-Process `
    -FilePath "basic256.exe" `
    -ArgumentList "--silent", "validation.kbs" `
    -PassThru `
    -Wait `
    -RedirectStandardOutput $tempOutput `
    -RedirectStandardError $tempError

# Mostrar la salida de BASIC-256 cuando el proceso ya termino
if (Test-Path $tempOutput) {
    Get-Content $tempOutput | Write-Host
}

if ($process.ExitCode -ne 0) {

    Write-Host ""
    Write-Host "ERROR: BASIC-256 termino con codigo $($process.ExitCode)."

    if (Test-Path $tempError) {
        Get-Content $tempError | Write-Host
    }

    exit 1
}

if (-not (Test-Path "../csv/datos_normalizados.csv")) {
    Write-Host "ERROR: BASIC-256 no genero datos_normalizados.csv."
    exit 1
}

Remove-Item $tempOutput, $tempError -ErrorAction SilentlyContinue

Write-Host "BASIC-256 completado correctamente."


# ========================================
# 2. FORTRAN
# ========================================

Write-Host ""
Write-Host "[2/4] Compilando Fortran..."

Set-Location "$PSScriptRoot\Fortran"

gfortran numerical_processing.f90 -o numerical_processing.exe

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: No se pudo compilar Fortran."
    exit 1
}

Write-Host "[2/4] Ejecutando Fortran..."

.\numerical_processing.exe

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Fortran termino con codigo $LASTEXITCODE."
    exit 1
}

if (-not (Test-Path "../csv/metricas.csv")) {
    Write-Host "ERROR: Fortran no genero metricas.csv."
    exit 1
}

Write-Host "Fortran completado correctamente."


# ========================================
# 3. JAVA
# ========================================

Write-Host ""
Write-Host "[3/4] Compilando Java..."

Set-Location "$PSScriptRoot\Java"

javac *.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: No se pudo compilar Java."
    exit 1
}

Write-Host "[3/4] Ejecutando Java..."

java RulesEngine.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Java termino con codigo $LASTEXITCODE."
    exit 1
}

if (-not (Test-Path "../csv/alertas.csv")) {
    Write-Host "ERROR: Java no genero alertas.csv."
    exit 1
}

Write-Host "Java completado correctamente."


# ========================================
# 4. C
# ========================================

Write-Host ""
Write-Host "[4/4] Compilando C..."

Set-Location "$PSScriptRoot\c"

gcc alert_processor.c -o alert_processor.exe

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: No se pudo compilar C."
    exit 1
}

Write-Host "[4/4] Ejecutando C..."

.\alert_processor.exe

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: C termino con codigo $LASTEXITCODE."
    exit 1
}

Write-Host "C completado correctamente."


# ========================================
# FIN
# ========================================

Write-Host ""
Write-Host "========================================"
Write-Host "       PROCESAMIENTO COMPLETADO"
Write-Host "========================================"