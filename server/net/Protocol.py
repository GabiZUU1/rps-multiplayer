class Protocol:
    def __init__(self):
        self.PROTOCOL_VERSION = 1

    def hello(self):
        return f"HELLO RPS {self.PROTOCOL_VERSION}"