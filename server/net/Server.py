import socket
from threading import Thread

from net.ClientHandler import ClientHandler


class Server:
    def __init__(self):
        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.s.bind(("0.0.0.0", 7777))
        self.s.listen(5)

    def start(self):
        print("Server started on 0.0.0.0:7777")
        print("Waiting for clients...")
        i = 0
        while True:
            conn, addr = self.s.accept()

            i += 1
            print(f"Client {i} connected: {addr}")
            handler = ClientHandler(conn, addr)
            Thread(target = handler.start, daemon = True).start()