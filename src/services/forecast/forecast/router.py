from fastapi import APIRouter
from service import get_forecast

router = APIRouter()


@router.get("/healthcheck")
async def healthcheck():
    return {"Health": "Good"}


@router.get("/forecast")
async def get(city: str):
    return await get_forecast(city)
