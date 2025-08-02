# GitHubUserActivity

Ein kleines Java-CLI-Tool, um die öffentlichen Aktivitäten eines GitHub-Nutzers auszulesen und übersichtlich darzustellen.


## Features

- Zeigt Push-, Create-, Delete- und Watch-Events aus der GitHub API für beliebige Nutzer an
- Erkennt und listet Commits, Repos, Branches, Tags und "Stars"
- Ausgabe im Konsolenformat, inklusive Debug-JSON


## Installation

1. Repository klonen:
    ```bash
    git clone https://github.com/Heavykeiler07/GitHubUserActivity.git
    ```
2. Mit Eclipse oder einer anderen IDE öffnen, oder via Kommandozeile kompilieren:
    ```bash
    javac -d bin src/com/example/githubUserActivity/GitHubUserActivity.java
    ```

    
## Nutzung

Das Tool wird mit einem GitHub-Benutzernamen als Argument gestartet

    
## Fehlerbehandlung

- Gibt bei ungültigem Benutzernamen eine Fehlermeldung aus
- Zeigt bei Netzwerk- oder API-Fehlern entsprechende Hinweise
- Zeigt die rohen JSON-Daten zur Analyse an


## Hinweise

- Es werden nur die letzten 30 öffentlichen Aktivitäten angezeigt (GitHub API-Limit).
- Das Tool ist ein Lernprojekt und kann leicht erweitert werden (z.B. für weitere Event-Typen).


## Lizenz

MIT License
