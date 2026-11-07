from exceptions.AlreadyExistsException import AlreadyExistsException
from exceptions.DoesNotExistException import DoesNotExistException


class MemoryRepository:
    def __init__(self):
        self.players = {}

    def addPlayer(self, name, handler):
        if name in self.players.keys():
            raise AlreadyExistsException(f"Player with the name {name} already exists!")

        self.players[name] = handler

    def removePlayer(self, name):
        if name not in self.players.keys():
            raise DoesNotExistException(f"Player with the name {name} does not exist!")

        self.players.pop(name)

    def getPlayers(self):
        return list(self.players.keys())

    def getHandlers(self):
        return list(self.players.values())