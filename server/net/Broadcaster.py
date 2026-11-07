class Broadcaster:
    def __init__(self, repo):
        self.repo = repo

    def sendAll(self, line):
        for handler in self.repo.getHandlers():
            handler.send(line)