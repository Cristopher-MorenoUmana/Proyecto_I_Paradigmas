#include <stdio.h>
#include <string.h>

#define INPUT_FILE "../csv/alertas.csv"

int getAlertCode(const char *rule)
{
    if (strcmp(rule, "TEMP_ALTA") == 0)
    {
        return 10;
    }

    if (strcmp(rule, "LLUVIA_INTENSA") == 0)
    {
        return 20;
    }

    if (strcmp(rule, "VIENTO_FUERTE") == 0)
    {
        return 30;
    }

    if (strcmp(rule, "BATERIA_BAJA") == 0)
    {
        return 40;
    }

    return 0;
}

int main()
{
    FILE *file;
    char line[100];
    char rule[50];
    int result;
    int code;

    int checksum = 0;
    int position = 1;

    file = fopen(INPUT_FILE, "r");

    if (file == NULL)
    {
        printf("Error: no se pudo abrir alertas.csv\n");
        return 1;
    }

    /* Ignorar encabezado */
    fgets(line, sizeof(line), file);

    printf("ALERTAS ACTIVAS\n");

    while (fgets(line, sizeof(line), file) != NULL)
    {
        if (sscanf(line, "%49[^,],%d", rule, &result) != 2)
        {
            continue;
        }

        if (result == 1)
        {
            code = getAlertCode(rule);

            if (code != 0)
            {
                printf("%s -> %d\n", rule, code);

                checksum = checksum + code;
                checksum = checksum ^ position;

                printf("Posicion: %d\n", position);
                printf("Checksum: %d\n", checksum);

                position++;
            }
        }
    }

    fclose(file);

    printf("CHECKSUM FINAL: %d\n", checksum);

    return 0;
}