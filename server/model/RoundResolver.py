BEATS = {"ROCK": "SCISSORS", "PAPER": "ROCK", "SCISSORS": "PAPER"}
class RoundResolver:
    @staticmethod
    def points(moves):
        results = {name: 0 for name in moves}
        for a, ma in moves.items():
            for b, mb in moves.items():
                if a != b and BEATS[ma] == mb:
                    results[a] += 1
        return results