from net.Protocol import Protocol


class ClientHandler:
    def __init__(self, conn, addr):
        self.conn = conn
        self.addr = addr
        self.protocol = Protocol()

    def start(self):
        self.send(self.protocol.hello())

    def send(self, message):
        self.conn.sendall((message + "\n").encode("utf-8"))