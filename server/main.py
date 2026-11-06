from controller.GameController import GameController
from net.Server import Server
from repository.MemoryRepository import MemoryRepository


def main():
    repo = MemoryRepository()
    gameController = GameController(repo)
    server = Server(gameController)
    server.start()

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        pass