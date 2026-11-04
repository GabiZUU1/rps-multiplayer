from net.Server import Server


def main():
    server = Server()
    server.start()

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        pass

     # rps-network-game/
     # ├── docs/protocol.md
     # ├── server/                      # Python
     # │   ├── CMakeLists.txt
     # │   ├── src/
     # │   │   ├── main.cpp
     # │   │   ├── net/        (ClientHandler, Protocol, Broadcaster)
     # │   │   ├── controller/ (GameController)
     # │   │   ├── model/      (Game, Player, Move, RoundResolver)
     # │   │   └── repo/       (PlayerRepository, InMemoryPlayerRepository)
     # │   └── tests/
     # └── client/                      # Java
     #     ├── pom.xml (or build.gradle)
     #     └── src/main/java/rps/
     #         ├── Main.java
     #         ├── net/        (Connection, Protocol)
     #         ├── controller/ (GameController)
     #         ├── model/      (GameModel)
     #         └── view/       (screens)