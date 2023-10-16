from logging import Formatter, FileHandler, getLogger, StreamHandler
from statics import LOG_FILE_NAME, LOG_FILE_PATH
from config import config

logging_levels = {"critical": 50,
                  "error": 40,
                  "warning": 30,
                  "info": 20,
                  "debug": 10,
                  "notset": 0}

log_formatter = Formatter("%(levelname)s: %(asctime)s %(message)s")

file_handler = FileHandler("{0}/{1}.log".format(LOG_FILE_PATH, LOG_FILE_NAME), mode="a", encoding="utf-8")
file_handler.setFormatter(log_formatter)

console_handler = StreamHandler()
console_handler.setFormatter(log_formatter)

logger = getLogger()

logger.setLevel(logging_levels[config.log_level])

logger.addHandler(file_handler)
logger.addHandler(console_handler)
