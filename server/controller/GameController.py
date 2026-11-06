from threading import Lock

from Utils import Utils
from exceptions.AlreadyExistsException import AlreadyExistsException
from net.Protocol import Protocol


class GameController:
    def __init__(self, repo):
        self.repo = repo
        self.protocol = Protocol()
        self._lock = Lock()

    def parse(self, handler, line):
        with self._lock:
            cmd = self.protocol.parse(line)
            match cmd.name:
                case "JOIN":
                    try:
                        self.repo.addPlayer(cmd.arg, handler)
                        handler.send(self.protocol.ok())
                    except AlreadyExistsException as e:
                        print(f"{Utils.fTime()} {e}")
                        handler.send(self.protocol.error("ALREADY_EXISTS"))