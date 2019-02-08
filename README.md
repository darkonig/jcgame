# Java Game - "La Casa de Ossos"
## Bones' House

This is a game console written in purely with Java 8, only uses libraries for testing.

## Building

```cmd
mvn clean compile
```

## Packaging (jar)

```cmd
mvn clean package
```

## Running Tests Only

```cmd
mvn clean test
```

## Running Coverage Only

```cmd
mvn clean verify
```

## Running

To running it is necessary a ANSI terminal enabled.

You can directly over maven:

```cmd
mvn clean compile exec:java
```

Or create a package (jar), and run directly the jar file:

```cmd
java -jar target/jcgame-1.0-SNAPSHOT.jar
```


## Game Commands

```
 __________________________________________________
| Navigation Commands                              |
| A -> Move left                                   |
| S -> Move right                                  |
| W -> Move up                                     |
| Z -> Move down                                   |
| Q -> Save and exit                               |
|                                                  |
| STATUS -> Open player status                     |
| H -> Open Help                                   |
|                                                  |
| Dialog                                           |
| > -> next text                                   |
|                                                  |
|                                                  |
| < Back                                           |
 --------------------------------------------------
```

