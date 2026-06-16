[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/X0O7qRf4)
# 1DAW A - Programación. Recuperación de junio 2026

Se evaluarán los resultados de aprendizaje trabajados durante el curso mediante una solución completa al problema propuesto: estructura de programa, control de flujo, colecciones, orientación a objetos, herencia, entrada/salida de ficheros y, cuando corresponda, persistencia en base de datos.

---

- **Alumno**: [Nombre del alumno]      
- **Resultados de aprendizaje a recuperar**: [RAX, RAY, RAZ, ...]

---

## 1. Descripción del problema

A Enrique le encanta viajar con sus amigos. Le gusta descubrir nuevos lugares y tomar fotos todo el tiempo. Sin embargo, al volver a casa, tiene que clasificar y archivar todas las fotografías que él y sus amigos han tomado, y esa es una tarea muy larga y aburrida. Siempre hay dos tipos de archivos de fotos, los que sacan los smartphones y los que saca la cámara réflex de Enrique. Los archivos tienen una estructura de nombres diferente, que es la siguiente:

* Las **cámaras de los teléfonos inteligentes** usan el formato `IMG_YYYYMMDD_HHMMSS.jpg`, donde `YYYY` es el año, `MM` es el mes y `DD` es el día del mes. Por ejemplo: `IMG_20171203_213455.jpg` corresponde a una foto tomada el 3 de diciembre de 2017 a las 21:34:55.

* Los archivos de la **cámara réflex** se almacenan en el formato `PDDMMYY_HHMMSS.jpg`, donde `DD` es el día del mes, `MM` es el mes y `YY` son los dos últimos dígitos de un año, suponiendo que es un año del siglo XXI. Por ejemplo: `P031217_213455.jpg` corresponde a una foto tomada el 3 de diciembre de 2017 a las 21:34:55.

En ambos casos, `HHMMSS` es la marca de tiempo, con `HH` para la hora, `MM` para el minuto y `SS` para los segundos.

Dados estos dos formatos diferentes, es imposible ordenar los archivos automáticamente por nombre, ya que todos los `IMG_` se colocarían antes que los `P_`. A Enrique le gustaría obtener un **script de Linux** para cambiar el nombre de todos los archivos de manera que incluyan el nombre del lugar que visitaron, seguido de un contador de tres dígitos que comienza desde `000` para la imagen más antigua y se incrementa en uno para cada siguiente foto, ordenadas por fecha y hora en que fueron tomadas.

La sintaxis del comando `mv` es:

```bash
mv <origen> <destino>
```

### Entrada

La entrada constará de 3 líneas:

- El nombre del lugar visitado en el viaje. Siempre será una palabra.   
- Una lista de los archivos de fotos provenientes de las cámaras del smartphone, separados por un espacio.   
- Una lista de los archivos de fotos provenientes de la cámara réflex, separados por un espacio.

### Salida

El resultado será un fichero script, con nombre `<lugar>.sh`, donde `<lugar>` es el lugar visitado. El script contendrá la lista de comandos `mv` a ejecutar para cambiar el nombre de todos los archivos. Enrique también quiere que la lista esté **ordenada por el nuevo nombre del archivo**, por lo que el primer comando debe ser el que convierta la foto más antigua.

### Ejemplos

![](assets/example1.png)

![](assets/example2.png)

### Salida por consola

Un resumen del trabajo realizado. Ver más abajo.

## 2. ¿Qué se pide?

Se pide desarrollar una aplicación de consola que reciba por línea de comandos la ruta del fichero de entrada, procese su contenido y genere el fichero script de salida con los comandos `mv`.

La solución debe trabajar con clases propias, colecciones, validaciones, control de errores y operaciones de entrada/salida. También debe estar organizada de forma que puedan identificarse con claridad los elementos del programa, las estructuras de control, los tipos de datos utilizados y los principios de programación orientada a objetos aplicados.

### Requisitos de ejecución

El programa debe ejecutarse desde consola, recibiendo como argumento el fichero `.in` que se quiere procesar.

Durante la ejecución debe mostrar por consola:

- Si se ha producido algún error al procesar el archivo.   
- Cualquier error de formato detectado en el contenido del archivo.   
- Un resumen con el número de fotos procesadas de ambos tipos.   

El resumen debe indicar cuántas fotos se han leído, cuántas son correctas y cuántas tienen errores. Por ejemplo:

Ante la ejecución:

```bash
> java -jar procesaFotos.jar examples/sample-1.in
```

O de esta forma:

```bash
> ./gradlew run --args="examples/sample-1.in"
```

La salida por consola podría ser:

```text
Procesadas las fotos de Paris:
No se han detectado errores de formato.
Réflex:
=======
Fotos leídas: 2
Correctas:    2
Errores:      0
Smartphone:
===========
Fotos leídas: 3
Correctas:    3
Errores:      0
Generado el script Paris.sh con 5 comandos mv.
```

El fichero generado debe tener formato de script de Linux y llamarse `<lugar>.sh`, donde `<lugar>` es el nombre leído en la primera línea del fichero de entrada. Cada línea del script debe tener el formato:

```bash
mv <origen> <destino>
```

La aplicación debe leer correctamente el fichero de entrada, validar sus líneas, procesar únicamente las fotos correctas y generar correctamente el fichero de salida.

### Ampliación de base de datos (RA9)

Esta ampliación solo debe realizarla el alumnado que deba recuperar el RA9.

Tras ejecutar correctamente el programa, la aplicación debe insertar en una base de datos relacional un resumen del procesamiento realizado. Solo se deben almacenar las fotos correctas, no las fotos con errores de formato.

El modelo de datos mínimo será:

| Campo | Tipo sugerido | Descripción |
|-------|---------------|-------------|
| `id` | Entero autoincremental | Identificador interno del registro. |
| `lugar` | Texto | Nombre del lugar leído en la primera línea del fichero. |
| `procesadas` | Entero | Número de fotos válidas procesadas correctamente. |

La tabla puede crearse con una sentencia como esta:

```sql
CREATE TABLE resumen_procesamiento (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lugar VARCHAR(100) NOT NULL,
    procesadas INTEGER NOT NULL
);
```

La funcionalidad obligatoria para esta ampliación será únicamente la operación de alta del CRUD, implementada con `INSERT`. Por ejemplo, después de procesar el fichero de París, se insertaría un registro equivalente a:

```sql
INSERT INTO resumen_procesamiento (lugar, procesadas)
VALUES ('Paris', 10);
```

## 3. Ejecución y test

Para probar la práctica se utilizarán los archivos `.in` que hay en el directorio `examples` y otros archivos similares.

- Deberá cargar adecuadamente el archivo.
- Deberá procesar la entrada y generar el resultado correcto.
- Deberá exportar el resultado a un archivo.
- Deberá mostrar por consola los errores detectados y el resumen del procesamiento.

## 4. Evaluación

Se evaluarán los siguientes resultados de aprendizaje: RA1, RA2, RA3, RA4, RA5, RA6 y RA7. También se evaluará RA9 cuando el alumno o alumna tenga que recuperarlo.

Además del programa, debes responder las preguntas del documento [Preguntas.md](./Preguntas.md). Estas preguntas forman parte de la evaluación y sirven para justificar las decisiones tomadas en tu solución.

## 5. Condiciones de entrega

- Se entrega la URL al repositorio en el tiempo establecido, con el código fuente y los archivos de ejemplo.
- El repositorio debe contener un `README.md` con la información solicitada en este documento.
- El repositorio debe contener un archivo `Preguntas.md` con las respuestas a las preguntas planteadas (Solo los resultados de aprendizaje que recuperas).
- El programa debe compilar y ejecutarse correctamente, y debe incluir un fichero `build.gradle.kts` para compilarlo.

## 6. Más información

- [Diagramas aclaratorios](./diagramas.md)
- [Pistas para orientar la solución](./Pistas.md)
- [Preguntas que también debes responder](./Preguntas.md)
