class AlreadyExistsException(Exception):
    def __init__(self, *args: object):
        super(*args)