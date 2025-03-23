from http import HTTPStatus

from aiohttp import ClientSession

from json import load

from datetime import datetime, date as datelib, timedelta

from forecast.config import config
from forecast.statics import ErrorMessages, Regions, WEATHER_API_URL
from forecast.model import BusinessException, WeatherCondition
from forecast.cache import get_cache, save_weather, load_weather, load_coordinates, save_coordinates
from forecast.logger import logger


async def get_forecast(city: str, date: str):
    logger.info(f"Forecast requested for city {city}.")

    date_obj = datetime.strptime(date, '%Y-%m-%d')

    today = datetime.today()

    five_days_later = today + timedelta(days=5)

    if date_obj < today or date_obj > five_days_later:
        raise BusinessException(HTTPStatus.BAD_REQUEST, ErrorMessages.INVALID_DATE_RANGE)

    cache = await get_cache()

    lat, lon = await load_coordinates(city.lower(), cache)

    if lat is None or lon is None:
        logger.error(ErrorMessages.COORDINATES_NOT_FOUND)
        raise BusinessException(HTTPStatus.NOT_FOUND, ErrorMessages.COORDINATES_NOT_FOUND)

    region = await get_region(lat, lon)

    return await get_conditions(region, lat, lon, date)


async def get_conditions(region, lat, lon, date):
    cache = await get_cache()

    cached_weather = await load_weather(region, date, cache)

    if cached_weather is not None:
        return WeatherCondition(
            region=cached_weather['region'],
            date=cached_weather['date'],
            icon=cached_weather['icon'],
            temp=cached_weather['temp'],
            weather=cached_weather['weather'],
            max_temp=cached_weather['max_temp'],
            min_temp=cached_weather['min_temp'],
        )

    received_data = await send_request(lat, lon)

    forecast = await parse_data(region, received_data)

    searched_weather = None

    date_object = datetime.strptime(date, '%Y-%m-%d')

    for weather in forecast:
        weather_date_object = datetime.strptime(weather.date, '%Y-%m-%d')

        if date_object == weather_date_object:
            searched_weather = weather

        await save_weather(weather, cache)

    if not config.use_real_data:
        searched_weather = forecast[0]

    return searched_weather


async def get_region(lat: str, lon: str):
    latitude = float(lat)
    longitude = float(lon)

    if latitude > 44.5305:
        return Regions.NORTH
    elif latitude > 44.0291 and longitude < 20.9260:
        return Regions.CENTRAL_UPPER_WEST
    elif latitude > 44.0291 and longitude >= 20.9260:
        return Regions.CENTRAL_UPPER_EAST
    elif latitude > 43.3619 and longitude < 21.2778:
        return Regions.CENTRAL_LOWER_WEST
    elif latitude > 43.3619 and longitude >= 21.2778:
        return Regions.CENTRAL_LOWER_EAST
    else:
        return Regions.SOUTH


async def send_request(lat: str, lon: str):
    url = f"{WEATHER_API_URL}/data/2.5/forecast?lat={lat}&lon={lon}&appid={config.forecast_api_key}&units=metric"

    if config.use_real_data:
        logger.info("REQUEST_SENT")

        async with ClientSession() as session:
            async with session.get(url) as response:
                if response.status != 200:
                    logger.error(ErrorMessages.GETTING_RESPONSE_FAILED)
                    raise BusinessException(HTTPStatus.SERVICE_UNAVAILABLE, ErrorMessages.GETTING_RESPONSE_FAILED)
                try:
                    data = await response.json()
                except:
                    logger.error(ErrorMessages.READING_RESPONSE_FAILED)
                    raise BusinessException(HTTPStatus.SERVICE_UNAVAILABLE, ErrorMessages.READING_RESPONSE_FAILED)

                return data

    else:
        with open("./data/example_response.json", "r", encoding="utf-8") as test_data:
            data = load(test_data)
        return data


async def parse_data(region, received_data):
    try:
        forecast_data = received_data["list"]

    except:
        logger.error(ErrorMessages.PARSING_DATA_FAILED)
        raise BusinessException(HTTPStatus.SERVICE_UNAVAILABLE, ErrorMessages.PARSING_DATA_FAILED)

    conditions = []

    for hourly_data in forecast_data[::8]:
        try:
            date = datelib.fromtimestamp(int(hourly_data["dt"])).isoformat()
            weather = hourly_data["weather"][0]["id"]
            icon = await map_icon_url(hourly_data["weather"][0]["icon"])
            temp = hourly_data["main"]["temp"]
            min_temp = hourly_data["main"]["temp_min"]
            max_temp = hourly_data["main"]["temp_max"]

        except:
            logger.error(ErrorMessages.PARSING_DATA_FAILED)
            raise BusinessException(HTTPStatus.SERVICE_UNAVAILABLE, ErrorMessages.PARSING_DATA_FAILED)

        if any([date, weather, icon, temp, min_temp, max_temp]) is None:
            logger.error(ErrorMessages.PARSING_DATA_FAILED)
            raise BusinessException(HTTPStatus.SERVICE_UNAVAILABLE, ErrorMessages.PARSING_DATA_FAILED)

        condition = WeatherCondition(
            region=region,
            date=date,
            icon=icon,
            temp=temp,
            weather=weather,
            max_temp=max_temp,
            min_temp=min_temp,
        )

        conditions.append(condition)

    return conditions


async def map_icon_url(icon_code):
    return f"https://openweathermap.org/img/w/{icon_code}.png"


async def seed_city_cache():
    logger.info("Seeding cities")

    with open("./data/rs.json", "r", encoding="utf-8") as file:
        data = load(file)

    cache = await get_cache()

    for city_data in data:
        await save_coordinates(city=city_data['city'].lower(),
                               mapping={'lat': city_data['lat'], 'lng': city_data['lng']},
                               cache=cache)
