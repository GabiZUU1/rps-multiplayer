from datetime import datetime


class Utils:
    @staticmethod
    def fTime():
        current_time = datetime.now().strftime("%H:%M:%S")
        return f"[{current_time}]"