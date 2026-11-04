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