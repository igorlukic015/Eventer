from uvicorn import run as uvicorn_run
from fastapi import FastAPI
from forecast.config import config
from router import router

app = FastAPI()

app.include_router(router)


if __name__ == "__main__":
    uvicorn_run("main:app",
                host=config.host,
                port=config.port,
                reload=config.shouldReload,
                log_level=config.log_level,
                debug=config.is_debug)
