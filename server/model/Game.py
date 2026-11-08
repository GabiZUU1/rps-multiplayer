class Game:
    def __init__(self, players, rounds = 5):
        self.players = list(players)
        self.scores = {p: 0 for p in self.players}
        self.moves = {}
        self.round = 0
        self.maxRounds = rounds
        self.roundActive = False

    def startRound(self):
        self.round += 1
        self.moves = {}
        self.roundActive = True

    def submit(self, name, move):
        if not self.roundActive or name not in self.players or name in self.moves:
            return False
        self.moves[name] = move
        return True

    def allSubmitted(self):
        return len(self.moves) == len(self.players)

    def removePlayer(self, name):
        self.players.remove(name)
        self.moves.pop(name, None)
        self.scores.pop(name, None)