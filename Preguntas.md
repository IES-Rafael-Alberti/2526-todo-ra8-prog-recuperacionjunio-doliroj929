# Preguntas de evaluación por RA

Estas preguntas sirven para que expliques y defiendas la solución que has desarrollado, relacionando el código con los resultados de aprendizaje.

**IMPORTANTE**: Utiliza enlaces permanentes al código fuente. También puedes copiarlo y pegarlo en tu respuesta entre

```Kotlin
aquí el código
```

para que se vea mejor. No copies y pegues capturas de pantalla.

## UD1, RA1: Estructura de un programa informático

1. Identifica los bloques principales de tu solución (`Main`, parser de argumentos, aplicación, lector, procesador, escritor y modelos). Explica qué responsabilidad tiene cada bloque y por qué no debe estar todo el código dentro de `main`.

Respuesta a pregunta 1:

2. Localiza en tu solución ejemplos de variables, constantes o literales, operadores y conversiones de tipos. Explica para qué se usan en el procesamiento de nombres, fechas, contadores y rutas de fichero.

Respuesta a pregunta 2:

## UD2, RA3: Estructuras de control, depuración y excepciones

3. Explica qué estructuras de selección y repetición aparecen en tu solución para validar argumentos, recorrer listas de fotos, detectar formatos incorrectos y generar los comandos `mv`.

Respuesta a pregunta 3:

4. Describe qué excepciones o errores controla tu programa cuando el fichero de entrada no existe, tiene menos de tres líneas o contiene nombres de foto con formato incorrecto. Indica en qué clase gestionas cada caso.

Respuesta a pregunta 4:

## UD3, RA6: Tipos avanzados de datos y colecciones

5. Explica qué colecciones utilizas para almacenar las fotos de smartphone, las fotos réflex y la lista final de comandos. Justifica por qué usas la que usas y no otra.

Respuesta a pregunta 5:

6. Tu solución puede usar expresiones regulares para validar nombres como `IMG_20210613_104512.jpg` o `P130621_083827.jpg`. Escribe qué comprueba cada patrón y explica cómo se relaciona con la creación de objetos `SmartphonePhotoFile` y `ReflexPhotoFile`.

Respuesta a pregunta 6:

## UD4, RA2: Fundamentos de programación orientada a objetos

7. Explica qué objetos instancias durante la ejecución del programa y por qué usas un objeto y no un tipo primitivo/básico. Por ejemplo: `TripInput`, `PhotoFile`, `RenameCommand`, etc.

Respuesta a pregunta 7:

8. Elige una clase de tu solución y explica sus propiedades, sus métodos, su constructor y un ejemplo de llamada a uno de sus métodos desde otra clase.

Respuesta a pregunta 8:

## UD5, RA4: Programas organizados en clases

9. Justifica por qué tu solución está dividida en varias clases en lugar de resolverse con funciones sueltas. Relaciona tu respuesta con cohesión, separación de responsabilidades y mantenimiento.

Respuesta a pregunta 9:

10. Explica qué modificadores de visibilidad usas en las clases de tu solución. Indica qué miembros deben ser públicos y cuáles deben ser privados, por ejemplo en `PhotoProcessor`, `InputFileReader` o `ScriptWriter`.

Respuesta a pregunta 10:

## UD6, RA7: Herencia, polimorfismo e interfaces

11. Explica la jerarquía formada por `PhotoFile`, `SmartphonePhotoFile` y `ReflexPhotoFile`. Indica cuál es la superclase, cuáles son las subclases y qué comportamiento sobrescribe o especializa cada una.

Respuesta a pregunta 11:

12. Justifica por qué el procesador trabaja con una lista de `PhotoFile` y no con dos listas separadas todo el tiempo. Relaciona tu respuesta con polimorfismo y con la ordenación por la clave `YYYYMMDDHHMMSS`.

Respuesta a pregunta 12:

## UD7, RA5: Entrada y salida de información

13. Describe el flujo completo de entrada y salida: cómo recibes la ruta por consola, cómo lees el fichero `.in`, qué comprobaciones haces, cómo interpretas sus tres líneas, qué mensajes de resumen o error muestras por consola, cómo generas el fichero `<lugar>.sh` y qué tipos de ficheros usas.

Respuesta a pregunta 13:

14. Indica qué alternativas ofrece Kotlin para leer y escribir ficheros. Compara opciones como `File`, `Path`, `readLines`, `bufferedReader`, `writeText` y `bufferedWriter`, y justifica cuál usarías en esta solución.

Respuesta a pregunta 14:

## UD9, RA9: Bases de datos relacionales

15. Explica qué objetos importantes de la librería JDBC usas en el `insert` de esta ampliación y para qué sirve cada uno, por ejemplo `DriverManager`, `Connection` y `PreparedStatement`. Indica también para qué serviría `ResultSet` en una consulta.

Respuesta a pregunta 15:

16. Propón cómo completarías el resto del CRUD sobre la tabla `resumen_procesamiento`, sin implementarlo: consulta de resúmenes, modificación de un registro incorrecto y borrado de un resumen almacenado.

Respuesta a pregunta 16:
