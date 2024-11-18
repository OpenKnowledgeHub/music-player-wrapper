# Music Player Wrapper

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=OpenKnowledgeHub_music-player-wrapper&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=OpenKnowledgeHub_music-player-wrapper)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=OpenKnowledgeHub_music-player-wrapper&metric=coverage)](https://sonarcloud.io/summary/new_code?id=OpenKnowledgeHub_music-player-wrapper)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=OpenKnowledgeHub_music-player-wrapper&metric=bugs)](https://sonarcloud.io/summary/new_code?id=OpenKnowledgeHub_music-player-wrapper)

The **Music Player Wrapper** project is a lightweight wrapper designed to interact with music streaming services.

## Requirements

- Java SE 21
- Maven >= 3.x

## Structure

The project follows a [hexagonal architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)) style.  
It is divided into four Maven submodules, where each layer builds upon the previous one:

```
.
├── model
├── application
├── adapter
└── bootstrap
```

## Usage

When running `mvn clean package`, all four modules are compiled and packed in their `module/target` folder. The
`bootstrap` module is responsable to combine all underlying modules.

### Profiles

Currently, there is one input port implementation for a command line usage (Maven profile `cli`) and one as a rest
server (Maven profile `rest`). After packaging the project, the packaged jar can be found under `bootstrap/target`.
