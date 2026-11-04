from net.Protocol import Protocol


class ClientHandler:
    def __init__(self, conn, addr):
        self.conn = conn
        self.addr = addr
        self.protocol = Protocol()

    def start(self):
        self.send(self.protocol.hello())

        try:
            with self.conn.makefile("r", encoding = "utf-8", newline = "\n") as reader:
                for line in reader:
                    print(f"{self.addr} says: {line!r}")
                    self.handleLine(line)
        except OSError:
            pass
        finally:
            print(f"{self.addr} disconnected")
            self.conn.close()

    def handleLine(self, line):
        pass # TODO: handle the command trough protocol then the command

    def send(self, message):
        self.conn.sendall((message + "\n").encode("utf-8"))