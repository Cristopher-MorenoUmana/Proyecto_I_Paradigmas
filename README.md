# Proyecto_I_Paradigmas

## Descripcion

Este proyecto implementa un procesamiento secuencial de datos utilizando cuatro lenguajes de programacion:

1. **BASIC-256** - Validacion y normalizacion de los datos.
2. **Fortran** - Procesamiento numerico y calculo de metricas.
3. **Java** - Evaluacion de reglas y generacion de alertas.
4. **C** - Procesamiento de alertas y calculo del checksum final.

El procesamiento se realiza mediante un script de PowerShell que coordina la ejecucion de cada etapa.

## Estructura del proyecto

```text
Proyecto/
│
├── csv/
│   ├── datos.csv
│   ├── datos_normalizados.csv
│   ├── metricas.csv
│   └── alertas.csv
│
├── Basic256/
│   └── validation.kbs
│
├── Fortran/
│   └── numerical_processing.f90
│
├── Java/
│   ├── RulesEngine.java
│   ├── Rule.java
│   ├── RuleData.java
│   ├── RuleParser.java
│   ├── TemperatureRule.java
│   ├── PrecipitationRule.java
│   ├── WindRule.java
│   ├── BatteryRule.java
│   └── rules.txt
│
├── c/
│   └── alert_processor.c
│
└── run_pipeline.ps1
```

Los archivos CSV generados durante el procesamiento se encuentran en la carpeta `csv/`.

## Requisitos

El proyecto requiere tener instalados y disponibles en el `PATH` los siguientes programas:

* BASIC-256
* gfortran
* Java JDK
* GCC
* PowerShell

Versiones utilizadas durante el desarrollo:

```text
BASIC-256 2.1.1
gfortran 16.1.0
Java 26.0.2.1
GCC 16.1.0
```

## Archivos de entrada

El archivo de entrada principal es:

```text
csv/datos.csv
```

Este archivo debe existir antes de ejecutar el pipeline.

Los siguientes archivos son generados automaticamente y no deben utilizarse como entrada:

```text
csv/datos_normalizados.csv
csv/metricas.csv
csv/alertas.csv
```

## Funcionamiento del pipeline

El procesamiento se realiza en el siguiente orden:

```text
datos.csv
    |
    v
BASIC-256
    |
    v
datos_normalizados.csv
    |
    v
Fortran
    |
    v
metricas.csv
    |
    v
Java
    |
    v
alertas.csv
    |
    v
C
    |
    v
Checksum final
```

### 1. BASIC-256

BASIC-256 lee `datos.csv` y valida cada registro.

Se verifican:

* Campos faltantes.
* Cantidad correcta de columnas.
* Valores numericos.
* Temperatura entre -90 y 60 grados Celsius.
* Precipitacion no negativa.
* Viento no negativo.
* Bateria entre 0 y 100.

Las filas validas se escriben en:

```text
csv/datos_normalizados.csv
```

Las filas que contienen datos invalidos son descartadas.

### 2. Fortran

Fortran lee:

```text
csv/datos_normalizados.csv
```

y calcula:

* Cantidad de registros.
* Temperatura promedio.
* Temperatura maxima.
* Temperatura minima.
* Precipitacion acumulada.
* Viento promedio.
* Viento maximo.
* Bateria promedio.

Las metricas se almacenan en:

```text
csv/metricas.csv
```

Antes de ejecutarse, el programa Fortran es compilado automaticamente por el pipeline.

### 3. Java

Java lee:

```text
csv/metricas.csv
```

y las reglas definidas en:

```text
Java/rules.txt
```

Las reglas tienen el siguiente formato:

```text
IDENTIFICADOR OPERADOR NUMERO
```

Por ejemplo:

```text
TEMP_ALTA > 35
LLUVIA_INTENSA > 50
VIENTO_FUERTE > 40
BATERIA_BAJA < 20
```

Java evalua cada regla utilizando la metrica correspondiente y genera:

```text
csv/alertas.csv
```

El resultado de cada regla se representa como:

```text
1 = regla activada
0 = regla no activada
```

### 4. C

C lee:

```text
csv/alertas.csv
```

Ignora las reglas que tengan resultado `0`.

Las alertas activas se convierten en codigos:

```text
TEMP_ALTA       -> 10
LLUVIA_INTENSA  -> 20
VIENTO_FUERTE   -> 30
BATERIA_BAJA    -> 40
```

Finalmente calcula el checksum utilizando:

```text
checksum = checksum + valor
checksum = checksum XOR posicion
```

La posicion comienza en `1`.

## Ejecucion normal

Abrir PowerShell en la carpeta raiz del proyecto:

```powershell
cd "C:\UNA\II_ciclo_2026\Paradigmas\Proyecto"
```

Ejecutar:

```powershell
.\run_pipeline.ps1
```

El script realiza automaticamente:

1. Ejecucion de BASIC-256.
2. Compilacion de Fortran.
3. Ejecucion de Fortran.
4. Compilacion de Java.
5. Ejecucion de Java.
6. Compilacion de C.
7. Ejecucion de C.

Si una de las etapas falla, el pipeline se detiene.

## Ejecucion limpia

Para realizar una prueba desde cero, primero hay que eliminar los archivos generados por ejecuciones anteriores.

**Importante:** estos comandos deben ejecutarse desde la carpeta raiz `Proyecto`, no desde `Proyecto\c`, `Proyecto\Java` u otra subcarpeta.

La ubicacion debe verse asi:

```text
PS C:\UNA\II_ciclo_2026\Paradigmas\Proyecto>
```

### 1. Ir a la carpeta del proyecto

```powershell
cd "C:\UNA\II_ciclo_2026\Paradigmas\Proyecto"
```

### 2. Eliminar los CSV generados

```powershell
Remove-Item .\csv\datos_normalizados.csv -ErrorAction SilentlyContinue
Remove-Item .\csv\metricas.csv -ErrorAction SilentlyContinue
Remove-Item .\csv\alertas.csv -ErrorAction SilentlyContinue
```

**No eliminar `csv/datos.csv`**, ya que es el archivo de entrada.

### 3. Eliminar los archivos compilados

Eliminar el ejecutable de Fortran:

```powershell
Remove-Item .\Fortran\numerical_processing.exe -ErrorAction SilentlyContinue
```

Eliminar el ejecutable de C:

```powershell
Remove-Item .\c\alert_processor.exe -ErrorAction SilentlyContinue
```

Eliminar los archivos compilados de Java:

```powershell
Remove-Item .\Java\*.class -ErrorAction SilentlyContinue
```

### 4. Ejecutar el pipeline

```powershell
.\run_pipeline.ps1
```

De esta manera, el proyecto comienza unicamente con el archivo de entrada:

```text
csv/datos.csv
```

y genera nuevamente todos los archivos necesarios durante la ejecucion.

## Resultado esperado

Con los datos utilizados durante las pruebas, el programa produce las siguientes metricas principales:

```text
Cantidad de registros: 12
Temperatura promedio: 25.833334
Temperatura maxima: 60.000000
Temperatura minima: -10.000000
Precipitacion acumulada: 398.500000
Viento promedio: 25.666666
Viento maximo: 60.000000
Bateria promedio: 54.166668
```

Las reglas activadas son:

```text
TEMP_ALTA
LLUVIA_INTENSA
VIENTO_FUERTE
```

Mientras que:

```text
BATERIA_BAJA
```

no se activa.

Los codigos procesados por C son:

```text
TEMP_ALTA       -> 10
LLUVIA_INTENSA  -> 20
VIENTO_FUERTE   -> 30
```

El resultado esperado del procesamiento es:

```text
CHECKSUM FINAL: 56
```

## Limpieza despues de una prueba

Si se desea dejar nuevamente el proyecto sin archivos generados, ejecutar desde la carpeta `Proyecto`:

```powershell
Remove-Item .\csv\datos_normalizados.csv -ErrorAction SilentlyContinue
Remove-Item .\csv\metricas.csv -ErrorAction SilentlyContinue
Remove-Item .\csv\alertas.csv -ErrorAction SilentlyContinue

Remove-Item .\Fortran\numerical_processing.exe -ErrorAction SilentlyContinue
Remove-Item .\c\alert_processor.exe -ErrorAction SilentlyContinue

Remove-Item .\Java\*.class -ErrorAction SilentlyContinue
```

El archivo:

```text
csv/datos.csv
```

debe conservarse.

## Nota sobre las rutas

Cada programa utiliza rutas relativas hacia la carpeta `csv`.

Por ejemplo:

```text
../csv/datos.csv
```

Por esta razon, el pipeline cambia automaticamente a la carpeta correspondiente antes de ejecutar cada programa.

Se recomienda ejecutar siempre:

```powershell
.\run_pipeline.ps1
```

desde la carpeta raiz del proyecto.
