# Propuesta de solución orientada a objetos

En esta solución quiero que modeles el problema con clases propias, herencia y polimorfismo. No se trata solo de que el programa funcione; también debes demostrar que sabes separar responsabilidades y que cada clase tiene un motivo claro para existir.

## Decisión principal

Tu programa se ejecutará por línea de comandos recibiendo el fichero de entrada:

```bash
./gradlew run --args="examples/sample-1.in"
```

A partir de ese fichero, tu aplicación debe leer las tres líneas del problema, validar los datos, convertir cada nombre de archivo en un objeto de dominio, ordenar las fotos por fecha y hora, y generar un script de salida llamado `<lugar>.sh`, por ejemplo `Japan.sh` o `Paris.sh`.

## Arquitectura que debes construir

| Clase | Responsabilidad |
|-------|-----------------|
| `Main` | Será el punto de entrada. Llamará al parser de argumentos y arrancará la aplicación. |
| `ArgumentParser` | Validará los argumentos de línea de comandos y devolverá la ruta del fichero de entrada. |
| `PhotoRenamerApp` | Coordinará el caso de uso completo: leer, procesar y escribir. |
| `InputFileReader` | Leerá el fichero de entrada, comprobará que tiene el formato esperado y construirá un objeto `TripInput`. |
| `TripInput` | Guardará el lugar, los nombres de fotos de smartphone y los nombres de fotos réflex. |
| `PhotoFile` | Será la superclase abstracta que representa una foto con nombre original y clave de ordenación. |
| `SmartphonePhotoFile` | Interpretará nombres con formato `IMG_YYYYMMDD_HHMMSS.jpg`. |
| `ReflexPhotoFile` | Interpretará nombres con formato `PDDMMYY_HHMMSS.jpg`. |
| `PhotoProcessor` | Convertirá nombres originales en objetos `PhotoFile`, registrará errores, ordenará fotos válidas y generará destinos finales. |
| `PhotoTypeStats` | Guardará las fotos leídas, correctas y erróneas de un tipo de cámara. |
| `ProcessingResult` | Agrupará los comandos generados y las estadísticas necesarias para mostrar el resumen por consola. |
| `RenameCommand` | Representará un comando `mv origen destino`. |
| `ScriptWriter` | Generará el fichero `.sh` a partir de los comandos calculados. |

## Flujo de ejecución

Tu programa debe seguir este flujo:

1. `Main` recibe los argumentos del programa.
2. `ArgumentParser` comprueba que se ha recibido exactamente una ruta de fichero.
3. `PhotoRenamerApp` recibe la ruta validada.
4. `InputFileReader` lee el fichero y devuelve un `TripInput`.
5. `PhotoProcessor` crea objetos `SmartphonePhotoFile` y `ReflexPhotoFile` solo para los nombres válidos.
6. Cada foto calcula su propia clave de ordenación `YYYYMMDDHHMMSS` gracias al polimorfismo.
7. `PhotoProcessor` cuenta fotos leídas, correctas y erróneas por tipo.
8. `PhotoProcessor` genera nombres destino con el formato `<lugar>_000.jpg`, `<lugar>_001.jpg`, etc.
9. `ScriptWriter` crea el fichero `<lugar>.sh` con los comandos `mv`.
10. `PhotoRenamerApp` muestra por consola el resumen incluido en `ProcessingResult`.

## Jerarquía de fotos

Aquí es donde debes demostrar la parte de herencia y polimorfismo. Evita resolver todos los formatos con `if` repartidos por el código. La idea es que tengas una superclase abstracta `PhotoFile` y dos subclases concretas `SmartphonePhotoFile` y `ReflexPhotoFile`. Cada subclase debe implementar su propio método de validación y de cálculo de clave de ordenación (`orderKey`).

Con esta estructura, `PhotoProcessor` no necesita saber cómo se interpreta cada formato. Trabaja con una lista de `PhotoFile`:

```kotlin
val orderedPhotos = photos.sortedBy { it.orderKey }
```

La clave `orderKey` puede ser una cadena con el formato `YYYYMMDDHHMMSS`. Como todos los componentes tienen longitud fija, ordenar alfabéticamente esas cadenas produce el mismo resultado que ordenar cronológicamente. Por ejemplo, `"20210613083827"` va antes que `"20210613104512"`.

## Validaciones mínimas

Como mínimo, tu solución debe controlar estos casos:

| Caso | Comportamiento esperado |
|------|-------------------------|
| No se pasa argumento | Muestra un error indicando el uso correcto. |
| Se pasan demasiados argumentos | Muestra un error indicando el uso correcto. |
| El fichero no existe | Muestra un error de lectura. |
| El fichero no tiene tres líneas | Muestra un error de formato. |
| El lugar está vacío o contiene espacios | Muestra un error de formato. |
| Un nombre de smartphone no cumple el patrón | Muestra un error indicando el fichero problemático. |
| Un nombre réflex no cumple el patrón | Muestra un error indicando el fichero problemático. |
| Fecha u hora inválida | Muestra un error de formato. |

## Estructura de paquetes sugerida

Puedes organizar el proyecto así:

```text
src/main/kotlin/
└── org/iesra/
    ├── Main.kt
    ├── app/
    │   └── PhotoRenamerApp.kt
    ├── cli/
    │   └── ArgumentParser.kt
    ├── io/
    │   ├── InputFileReader.kt
    │   └── ScriptWriter.kt
    ├── model/
    │   ├── PhotoFile.kt
    │   ├── SmartphonePhotoFile.kt
    │   ├── ReflexPhotoFile.kt
    │   ├── PhotoTypeStats.kt
    │   ├── ProcessingResult.kt
    │   ├── RenameCommand.kt
    │   └── TripInput.kt
    ├── database/
    │   ├── DatabaseConnectionFactory.kt
    │   ├── ProcessingSummaryDao.kt
    │   └── JdbcProcessingSummaryDao.kt
    └── service/
        ├── PhotoProcessor.kt
        └── ProcessingSummaryService.kt
```

Si no tienes que recuperar RA9, puedes omitir el paquete `database` y la clase `ProcessingSummaryService`.

## Ampliación RA9

Esta parte solo debes hacerla si tienes que recuperar RA9. Después de procesar el fichero y generar el script, tu aplicación debe guardar en base de datos un resumen con el lugar y el número de fotos correctas.

La tabla mínima es `resumen_procesamiento`:

```sql
CREATE TABLE resumen_procesamiento (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lugar VARCHAR(100) NOT NULL,
    procesadas INTEGER NOT NULL
);
```

Solo tienes que implementar el alta del CRUD:

```sql
INSERT INTO resumen_procesamiento (lugar, procesadas)
VALUES ('Paris', 10);
```

Para mantener la solución orientada a objetos, separa esta parte en un servicio y un DAO:

- `ProcessingSummary`: modelo con el lugar y el número de fotos correctas.
- `ProcessingSummaryService`: crea el resumen después del procesamiento.
- `ProcessingSummaryDao`: interfaz con la operación `insert`.
- `JdbcProcessingSummaryDao`: implementación JDBC del `insert`.
- `DatabaseConnectionFactory`: clase que crea la conexión con la base de datos.

## Criterios que debes cumplir

- Tu programa debe ejecutarse pasando el fichero de entrada por argumentos.
- Debe existir una clase responsable de parsear argumentos.
- Debe existir una clase aplicación que coordine el flujo.
- Debes separar la lectura del fichero y la escritura del script en clases diferentes.
- Debe existir una jerarquía de clases para representar los tipos de foto.
- El código que ordena y genera comandos debe trabajar contra la superclase `PhotoFile`.
- Si recuperas RA9, la inserción en base de datos debe quedar separada en un servicio y un DAO.
- No debes resolver todo el problema dentro de `main`.
- No debes mezclar lectura de fichero, parseo de nombres, ordenación y escritura en una única clase.
