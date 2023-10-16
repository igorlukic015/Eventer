from redis import Redis

from fastapi import APIRouter, Depends

from forecast.model import WeatherResponse
from forecast.cache import get_cache
from forecast.service import ForecastService
from forecast.statics import API_VERSION


router = APIRouter()


@router.get(f"/api/v{API_VERSION}/healthcheck")
async def healthcheck():
    return {"Health": "Good"}


@router.get(f"/api/v{API_VERSION}/forecast", response_model=WeatherResponse)
async def get_forecast_for_city(city: str, cache: Redis = Depends(get_cache, use_cache=False)):
    result = await ForecastService(cache).get_forecast(city)

    return result.serialize()
