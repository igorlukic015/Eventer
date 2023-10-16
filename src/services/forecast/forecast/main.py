from uvicorn import run as uvicorn_run

from fastapi import FastAPI

from forecast.config import config
from forecast.model import BusinessException
from forecast.service import seed_city_cache
from forecast.statics import ErrorMessages
from forecast.router import router
from forecast.logger import logger
from forecast.exception_handlers import general_exception_handler, business_exception_handler

from sys import argv, exit

from getopt import getopt, GetoptError


app = FastAPI()

app.include_router(router)

app.add_exception_handler(Exception, general_exception_handler)
app.add_exception_handler(BusinessException, business_exception_handler)

def main(arguments):
    try:
        opts, args = getopt(arguments, "", ["seed="])

    except GetoptError:
        logger.error(ErrorMessages.PARSING_ARGUMENTS_FAILED)
        exit(1)

    should_seed = False

    for opt, arg in opts:
        if opt == "--seed":
            should_seed = bool(arg)

    if should_seed:
        seed_city_cache()
        exit(0)

    uvicorn_run("main:app",
                host=config.host,
                port=config.port,
                reload=config.shouldReload,
                log_level=config.log_level,
                debug=config.is_debug)


if __name__ == "__main__":
    main(argv[1:])
