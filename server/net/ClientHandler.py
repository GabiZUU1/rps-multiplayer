from Utils import Utils
from net.Protocol import Protocol


class ClientHandler:
    def __init__(self, nr, conn, addr, controller):
        self.nr = nr
        self.name = None
        self.controller = controller
        self.conn = conn
        self.addr = addr
        self.protocol = Protocol()

    def start(self):
        self.send(self.protocol.hello())

        try:
            with self.conn.makefile("r", encoding = "utf-8", newline = "\n") as reader:
                for line in reader:
                    print(f"{Utils.fTime()} {self.addr}({self.nr}) says: {line!r}")
                    self.controller.parse(self, line)
        except OSError:
            pass
        finally:
            print(f"{Utils.fTime()} {self.addr}({self.nr}) disconnected")
            self.controller.onDisconnect(self)
            self.conn.close()

    def send(self, message):
        self.conn.sendall((message + "\n").encode("utf-8"))