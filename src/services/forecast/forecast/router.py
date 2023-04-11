from fastapi import APIRouter
from service import get_forecast

router = APIRouter()


import time

@router.get("/healthcheck")
async def healthcheck():
    return {"Health": "Good"}


@router.get("/forecast")
async def get(city: str):
    start = time.time()
    print(f"Funciton started at {start}")

    result = await get_forecast(city)

    end = time.time()
    print(f"Function ended at {end}")
    print("Elapsed time:")
    print(end - start)

    return result
