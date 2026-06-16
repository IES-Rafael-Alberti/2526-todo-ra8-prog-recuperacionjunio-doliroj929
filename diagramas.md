# Diagramas de la solución

Este documento recoge varios diagramas Mermaid para explicar la arquitectura propuesta, el flujo principal de ejecución y la ampliación de base de datos.

## Diagrama de clases de la solución base

Este diagrama representa las clases principales y sus relaciones.

```mermaid
classDiagram
    class Main {
        +main(args: Array)
    }

    class ArgumentParser {
        +parse(args: Array) Path
    }

    class PhotoRenamerApp {
        -inputFileReader: InputFileReader
        -photoProcessor: PhotoProcessor
        -scriptWriter: ScriptWriter
        +run(inputPath: Path)
    }

    class InputFileReader {
        +read(inputPath: Path) TripInput
    }

    class ScriptWriter {
        +write(place: String, commands: List)
    }

    class PhotoProcessor {
        +process(input: TripInput) ProcessingResult
        -buildValidSmartphonePhotos(names: List) List
        -buildValidReflexPhotos(names: List) List
    }

    class TripInput {
        +place: String
        +smartphoneFiles: List
        +reflexFiles: List
    }

    class RenameCommand {
        +source: String
        +target: String
        +toScriptLine() String
    }

    class ProcessingResult {
        +commands: List
        +reflexStats: PhotoTypeStats
        +smartphoneStats: PhotoTypeStats
        +totalCorrectPhotos: Int
    }

    class PhotoTypeStats {
        +read: Int
        +correct: Int
        +errors: Int
    }

    class PhotoFile {
        <<abstract>>
        +originalName: String
        +orderKey: String
    }

    class SmartphonePhotoFile {
        +orderKey: String
    }

    class ReflexPhotoFile {
        +orderKey: String
    }

    Main --> ArgumentParser : usa
    Main --> PhotoRenamerApp : crea
    PhotoRenamerApp --> InputFileReader : lee
    PhotoRenamerApp --> PhotoProcessor : procesa
    PhotoRenamerApp --> ScriptWriter : escribe
    InputFileReader --> TripInput : construye
    PhotoProcessor --> TripInput : recibe
    PhotoProcessor --> PhotoFile : usa
    PhotoProcessor --> RenameCommand : genera
    PhotoProcessor --> ProcessingResult : devuelve
    ProcessingResult --> PhotoTypeStats : contiene
    ProcessingResult --> RenameCommand : contiene
    ScriptWriter --> RenameCommand : escribe
    PhotoFile <|-- SmartphonePhotoFile
    PhotoFile <|-- ReflexPhotoFile
```

## Diagrama de secuencia de `PhotoRenamerApp`

```mermaid
sequenceDiagram
    participant Usuario
    participant Main
    participant ArgumentParser
    participant App
    participant Reader
    participant Processor
    participant Writer
    participant Console

    Usuario->>Main: Ejecuta programa con ruta .in
    Main->>ArgumentParser: parse(args)
    ArgumentParser-->>Main: inputPath validado
    Main->>App: run(inputPath)
    App->>Reader: read(inputPath)
    Reader-->>App: TripInput
    App->>Processor: process(tripInput)
    Processor->>Processor: Crear objetos PhotoFile
    Processor->>Processor: Ordenar por orderKey
    Processor->>Processor: Crear RenameCommand
    Processor-->>App: ProcessingResult
    App->>Writer: write(place, result.commands)
    Writer-->>App: Script generado
    App->>Console: Mostrar estadísticas del resultado
    App-->>Main: Fin de ejecución
```

## Diagrama de secuencia con errores de entrada

```mermaid
sequenceDiagram
    participant Usuario
    participant Main
    participant ArgumentParser
    participant App
    participant Reader
    participant Console

    Usuario->>Main: Ejecuta programa
    Main->>ArgumentParser: parse(args)
    alt Argumentos inválidos
        ArgumentParser-->>Main: Error de uso
        Main->>Console: Mostrar uso correcto
    else Argumentos válidos
        ArgumentParser-->>Main: inputPath validado
        Main->>App: run(inputPath)
        App->>Reader: read(inputPath)
        alt Error de lectura o formato general
            Reader-->>App: Error
            App->>Console: Mostrar error
        else Fichero leído
            Reader-->>App: TripInput
            App->>Console: Mostrar errores de formato de fotos
            App->>Console: Mostrar fotos leídas, correctas y errores
        end
    end
```

## Diagrama de clases de la extensión de base de datos

Este diagrama muestra la ampliación para alumnado que tenga asociada la parte de base de datos. Solo se implementa el `insert`; el resto del CRUD queda para la pregunta teórica.

```mermaid
classDiagram
    class PhotoRenamerApp {
        -processingSummaryService: ProcessingSummaryService
        +run(inputPath: Path)
    }

    class ProcessingSummaryService {
        -processingSummaryDao: ProcessingSummaryDao
        +saveSummary(place: String, processedPhotos: Int)
    }

    class ProcessingSummary {
        +place: String
        +processedPhotos: Int
    }

    class ProcessingSummaryDao {
        <<interface>>
        +insert(summary: ProcessingSummary)
    }

    class JdbcProcessingSummaryDao {
        -connectionFactory: DatabaseConnectionFactory
        +insert(summary: ProcessingSummary)
    }

    class DatabaseConnectionFactory {
        +getConnection() Connection
    }

    class DriverManager {
        <<JDBC>>
        +getConnection(url: String) Connection
    }

    class Connection {
        <<JDBC>>
        +prepareStatement(sql: String) PreparedStatement
    }

    class PreparedStatement {
        <<JDBC>>
        +setString(index: Int, value: String)
        +setInt(index: Int, value: Int)
        +executeUpdate() Int
    }

    PhotoRenamerApp --> ProcessingSummaryService : usa
    ProcessingSummaryService --> ProcessingSummary : crea
    ProcessingSummaryService --> ProcessingSummaryDao : delega
    ProcessingSummaryDao <|.. JdbcProcessingSummaryDao
    JdbcProcessingSummaryDao --> DatabaseConnectionFactory : pide conexión
    DatabaseConnectionFactory --> DriverManager : usa
    DriverManager --> Connection : devuelve
    Connection --> PreparedStatement : crea
    JdbcProcessingSummaryDao --> PreparedStatement : ejecuta INSERT
```

## Diagrama de secuencia de la extensión JDBC

```mermaid
sequenceDiagram
    participant App
    participant Service
    participant JdbcDao
    participant Factory
    participant Connection
    participant Statement
    participant BD

    App->>Service: saveSummary(place, processedPhotos)
    Service->>Service: Crear ProcessingSummary
    Service->>JdbcDao: insert(summary)
    JdbcDao->>Factory: getConnection()
    Factory-->>JdbcDao: Connection
    JdbcDao->>Connection: prepareStatement(sql)
    Connection-->>JdbcDao: PreparedStatement
    JdbcDao->>Statement: setString(1, place)
    JdbcDao->>Statement: setInt(2, processedPhotos)
    JdbcDao->>Statement: executeUpdate()
    Statement->>BD: INSERT resumen_procesamiento(lugar, procesadas)
    BD-->>Statement: Filas insertadas
    JdbcDao-->>Service: Inserción completada
    Service-->>App: Resumen guardado
```

## Diagrama de flujo del procesamiento

```mermaid
graph TD
    A["Inicio"] --> B["Leer argumentos"]
    B --> C{"Argumentos válidos"}
    C -->|No| D["Mostrar error de uso"]
    C -->|Sí| E["Leer fichero de entrada"]
    E --> F{"Fichero válido"}
    F -->|No| G["Mostrar error de lectura o formato"]
    F -->|Sí| H["Separar lugar y listas de fotos"]
    H --> I["Validar nombres de fotos"]
    I --> J["Crear objetos PhotoFile válidos"]
    J --> K["Contar leídas, correctas y errores"]
    K --> L["Ordenar fotos por orderKey"]
    L --> M["Crear comandos RenameCommand"]
    M --> N["Escribir script lugar.sh"]
    N --> O["Mostrar resumen por consola"]
    O --> P{"Ampliación BD activada"}
    P -->|No| Q["Fin"]
    P -->|Sí| R["Insertar resumen en base de datos"]
    R --> Q
```

## Diagrama de estados de una foto durante el procesamiento

```mermaid
graph TD
    A["Foto leída"] --> B{"Formato correcto"}
    B -->|No| C["Error de formato"]
    B -->|Sí| D["Foto válida"]
    D --> E["Clave YYYYMMDDHHMMSS generada"]
    E --> F["Foto ordenada por orderKey"]
    F --> G["Nombre destino generado"]
    G --> H["Comando mv generado"]
    C --> I["No se procesa"]
    H --> J["Fin"]
    I --> J
```
