from threading import Lock

from Utils import Utils
from exceptions.AlreadyExistsException import AlreadyExistsException
from exceptions.DoesNotExistException import DoesNotExistException
from net.Broadcaster import Broadcaster
from net.Protocol import Protocol


class GameController:
    def __init__(self, repo, broadcaster):
        self.repo = repo
        self.broadcaster = broadcaster
        self.protocol = Protocol()
        self._lock = Lock()

    def handleLine(self, handler, line):
        with self._lock:
            cmd = self.protocol.parse(line)
            match cmd.name:
                case "JOIN":
                    try:
                        self.repo.addPlayer(cmd.arg, handler)
                        handler.name = cmd.arg
                        handler.send(self.protocol.ok())
                        self.broadcaster.sendAll(self.protocol.players(self.repo.getPlayers()))
                    except AlreadyExistsException as e:
                        print(f"{Utils.fTime()} {e}")
                        handler.send(self.protocol.error("ALREADY_EXISTS"))

    def onDisconnect(self, handler):
        if handler.name is None: return

        with self._lock:
            try:
                self.repo.removePlayer(handler.name)
                self.broadcaster.sendAll(self.protocol.players(self.repo.getPlayers()))
            except DoesNotExistException as e:
                print(f"{Utils.fTime()} {e}")