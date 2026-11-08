from model.Message import Message

class Protocol:
    def __init__(self):
        self.PROTOCOL_VERSION = 1

    def parse(self, line: str):
        mm = line.strip().split(" ")
        msg = Message(mm[0], mm[1] if len(mm) > 1 else "")
        return msg

    def hello(self):
        return f"HELLO RPS {self.PROTOCOL_VERSION}"

    def ok(self):
        return "OK"

    def players(self, players):
        return "PLAYERS " + ",".join(players)

    def gameStart(self):
        return "GAME_START"

    def round(self, time):
        return f"ROUND {time}"

    def error(self, message):
        return f"ERROR {message}"