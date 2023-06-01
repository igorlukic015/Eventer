from fastapi import APIRouter, responses
from service import get_forecast

router = APIRouter()
version = 1


@router.get(f"/api/v{version}/healthcheck")
async def healthcheck():
    return {"Health": "Good"}


@router.get(f"/api/v{version}/forecast")
async def get(city: str):
    result = await get_forecast(city)

    return responses.JSONResponse(result.serialize())
