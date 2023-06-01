from uvicorn import run as uvicorn_run
from fastapi import FastAPI
from router import router

app = FastAPI()

app.include_router(router)


if __name__ == "__main__":
    uvicorn_run("main:app", host="127.0.0.1", port=9000, reload=True, log_level="debug", debug=True)
