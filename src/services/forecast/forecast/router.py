from fastapi import APIRouter

from forecast.model import WeatherResponse
from forecast.service import get_forecast
from forecast.statics import API_VERSION


router = APIRouter()


@router.get(f"/api/v{API_VERSION}/healthcheck")
async def healthcheck():
    return {"Health": "Good"}


@router.get(f"/api/v{API_VERSION}/forecast", response_model=WeatherResponse)
async def get_forecast_for_city(city: str, date: str):
    result = await get_forecast(city, date)

    return result.serialize()
