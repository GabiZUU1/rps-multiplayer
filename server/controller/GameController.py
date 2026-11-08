import threading
from threading import Lock

from Utils import Utils
from exceptions.AlreadyExistsException import AlreadyExistsException
from exceptions.DoesNotExistException import DoesNotExistException
from model.Game import Game
from model.RoundResolver import RoundResolver
from net.Protocol import Protocol


class GameController:
    def __init__(self, repo, broadcaster):
        self.repo = repo
        self.broadcaster = broadcaster
        self.protocol = Protocol()
        self.game = None
        self.tmr = None
        self._lock = Lock()

    def handleLine(self, handler, line):
        with self._lock:
            cmd = self.protocol.parse(line)
            match cmd.name:
                case "JOIN":
                    try:
                        self.repo.addPlayer(cmd.arg, handler)
                        handler.name = cmd.arg
                        handler.send(self.protocol.ok())
                        self.broadcaster.sendAll(self.protocol.players(self.repo.getPlayers()))
                    except AlreadyExistsException as e:
                        print(f"{Utils.fTime()} {e}")
                        handler.send(self.protocol.error("ALREADY_EXISTS"))

                case "START":
                    if handler.name is None: pass
                    elif self.game is not None:
                        handler.send(self.protocol.error("GAME_RUNNING"))
                    elif len(self.repo.getPlayers()) < 2:
                        handler.send(self.protocol.error("NOT_ENOUGH_PLAYERS"))
                    else:
                        self._startRound()

                case "MOVE":
                    if self.game is None:
                        handler.send(self.protocol.error("BAD_MOVE"))
                    elif not self.game.submit(handler.name, cmd.arg):
                        handler.send(self.protocol.error("ALREADY_MOVED"))
                    elif self.game.allSubmitted():
                        self._endRound()

    def _startGame(self):
        self.game = Game(self.repo.getPlayers())
        self.broadcaster.sendAll(self.protocol.gameStart())
        self._startRound()

    def _startRound(self):
        self.game = Game(self.repo.getPlayers())
        self.game.startRound()
        self.broadcaster.sendAll(self.protocol.gameStart())
        self.broadcaster.sendAll(self.protocol.round(20))
        self.tmr = threading.Timer(21, self._onDeadLine, args = [self.game.round])
        self.tmr.daemon = True
        self.tmr.start()

    def _onDeadLine(self, roundNumber):
        with self._lock:
            if self.game is not None and self.game.round == roundNumber and self.game.roundActive:
                self._endRound()

    def _endRound(self):
        self.game.roundActive = False
        self.tmr.cancel()

        for name, p in RoundResolver.points(self.game.moves).items():
            self.game.scores[name] += p

        moves = {p: self.game.moves.get(p, "NONE") for p in self.game.players}
        self.broadcaster.sendAll(self.protocol.result(moves))
        self.broadcaster.sendAll(self.protocol.scores(self.game.scores))

        if self.game.round >= self.game.maxRounds:
            self._endGame()
        else:
            self.tmr = threading.Timer(4, self._onNextRound, args=[self.game.round])
            self.tmr.daemon = True
            self.tmr.start()

    def _onNextRound(self, roundNumber):
        with self._lock:
            if self.game is None or self.game.round != roundNumber or self.game.roundActive:
                return
            if len(self.game.players) < 2:
                self._endGame()
            else:
                self._startRound()

    def _endGame(self):
        if self.tmr is not None:
            self.tmr.cancel()

        scores = self.game.scores
        best = max(scores.values(), default=0)
        winners = [name for name, s in scores.items() if s == best]
        self.broadcaster.sendAll(self.protocol.gameOver(winners))
        self.game = None

    def onDisconnect(self, handler):
        if handler.name is None: return

        with self._lock:
            try:
                self.repo.removePlayer(handler.name)
                if self.game is not None:
                    self.game.removePlayer(handler.name)
                    if len(self.game.players) < 2:
                        pass
                    elif self.game.roundActive and self.game.allSubmitted():
                        self._endRound()

                self.broadcaster.sendAll(self.protocol.players(self.repo.getPlayers()))
            except DoesNotExistException as e:
                print(f"{Utils.fTime()} {e}")