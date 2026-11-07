from controller.GameController import GameController
from net.Server import Server
from net.Broadcaster import Broadcaster
from repository.MemoryRepository import MemoryRepository


def main():
    repo = MemoryRepository()
    broadcaster = Broadcaster(repo)
    gameController = GameController(repo, broadcaster)
    server = Server(gameController)
    server.start()

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        pass