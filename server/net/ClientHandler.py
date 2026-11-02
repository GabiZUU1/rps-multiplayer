import socket
from threading import Thread


class ClientHandler:
    def __init__(self):
        self._s = None
        self._ip = "0.0.0.0"
        self._port = 7777

    def startServer(self):
        print("Starting server...")
        self._s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._s.bind((self._ip, self._port))
        self._s.listen(5)
        print("Server started. Waiting for clients...")

        i = 0
        while True :
            i += 1
            cs, addr = self._s.accept()
            t = Thread(target = self.test, args = (cs, i))
            t.start()

    def createHandshake(self, cs, i):
        print(f"Client nr. {i} connected!")
        cs.sendall("HELLO RPS 1\n".encode("utf-8"))

        with cs, cs.makefile("r", encoding="utf-8", newline="\n") as reader:
            for line in reader:
                pass # wait for the JOIN message

    # def test(self, cs, i):
    #     print(f"Client nr {i} connected!")
    #     with cs, cs.makefile("r", encoding="utf-8", newline="\n") as reader:
    #         for line in reader:
    #             print(f"Client {i} says: {line.rstrip()}")
    #     print(f"Client {i} disconnected!")