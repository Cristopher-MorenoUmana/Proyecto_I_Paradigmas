program numerical_processing

    implicit none

    integer :: unit
    integer :: metrics_unit
    integer :: ios
    integer :: record_count

    character(len=200) :: line
    character(len=10) :: id
    character(len=20) :: station

    real :: temperature
    real :: precipitation
    real :: wind
    real :: battery

    real :: temperature_sum
    real :: temperature_average
    real :: temperature_max
    real :: temperature_min

    real :: precipitation_total

    real :: wind_sum
    real :: wind_average
    real :: wind_max

    real :: battery_sum
    real :: battery_average

    unit = 10
    metrics_unit = 20

    ! Inicializar acumuladores
    record_count = 0

    temperature_sum = 0.0
    precipitation_total = 0.0
    wind_sum = 0.0
    battery_sum = 0.0

    ! Abrir archivo normalizado
    open(unit=unit, file="../csv/datos_normalizados.csv", &
         status="old", action="read", iostat=ios)

    if (ios /= 0) then
        print *, "Error al abrir el archivo CSV."
        stop
    end if

    ! Leer encabezado
    read(unit, '(A)', iostat=ios) line

    if (ios /= 0) then
        print *, "Error al leer el encabezado."
        close(unit)
        stop
    end if

    ! Leer el primer registro
    read(unit, *, iostat=ios) id, station, temperature, &
                               precipitation, wind, battery

    if (ios /= 0) then
        print *, "No hay datos para procesar."
        close(unit)
        stop
    end if

    ! Inicializar maximos y minimos con el primer registro
    temperature_max = temperature
    temperature_min = temperature
    wind_max = wind

    ! Procesar registros
    do

        record_count = record_count + 1

        ! Acumular valores
        temperature_sum = temperature_sum + temperature
        precipitation_total = precipitation_total + precipitation
        wind_sum = wind_sum + wind
        battery_sum = battery_sum + battery

        ! Actualizar temperatura maxima
        if (temperature > temperature_max) then
            temperature_max = temperature
        end if

        ! Actualizar temperatura minima
        if (temperature < temperature_min) then
            temperature_min = temperature
        end if

        ! Actualizar viento maximo
        if (wind > wind_max) then
            wind_max = wind
        end if

        ! Leer siguiente registro
        read(unit, *, iostat=ios) id, station, temperature, &
                                   precipitation, wind, battery

        if (ios /= 0) exit

    end do

    close(unit)

    ! Calcular promedios
    temperature_average = temperature_sum / record_count
    wind_average = wind_sum / record_count
    battery_average = battery_sum / record_count

    ! Abrir archivo de metricas
    open(unit=metrics_unit, file="../csv/metricas.csv", &
         status="replace", action="write", iostat=ios)

    if (ios /= 0) then
        print *, "Error al crear el archivo de metricas."
        stop
    end if

    ! Escribir encabezado
    write(metrics_unit, '(A)') "METRICA,VALOR"

    ! Escribir metricas
    write(metrics_unit, '(A,F0.6)') "TEMPERATURA_PROMEDIO,", temperature_average
    write(metrics_unit, '(A,F0.6)') "TEMPERATURA_MAXIMA,", temperature_max
    write(metrics_unit, '(A,F0.6)') "TEMPERATURA_MINIMA,", temperature_min
    write(metrics_unit, '(A,F0.6)') "PRECIPITACION_ACUMULADA,", precipitation_total
    write(metrics_unit, '(A,F0.6)') "VIENTO_PROMEDIO,", wind_average
    write(metrics_unit, '(A,F0.6)') "VIENTO_MAXIMO,", wind_max
    write(metrics_unit, '(A,F0.6)') "BATERIA_PROMEDIO,", battery_average

    close(metrics_unit)

    print *, "Cantidad de registros: ", record_count

    print *, "Temperatura promedio: ", temperature_average
    print *, "Temperatura maxima: ", temperature_max
    print *, "Temperatura minima: ", temperature_min

    print *, "Precipitacion acumulada: ", precipitation_total

    print *, "Viento promedio: ", wind_average
    print *, "Viento maximo: ", wind_max

    print *, "Bateria promedio: ", battery_average

end program numerical_processing