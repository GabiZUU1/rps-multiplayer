from exceptions.AlreadyExistsException import AlreadyExistsException


class MemoryRepository:
    def __init__(self):
        self.players = {}

    def addPlayer(self, name, handler):
        if name in self.players.keys():
            raise AlreadyExistsException(f"Player with the name {name} already exists!")

        self.players[name] = handler