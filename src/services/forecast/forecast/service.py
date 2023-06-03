from http import HTTPStatus
from requests import get
from json import load, loads
from datetime import date as datelib
from forecast.config import config
from statics import ErrorMessages, Regions, WEATHER_API_URL
from model import BusinessException, WeatherCondition
from cache import save_weather, load_weather, load_coordinates, save_coordinates

use_real_data = False


async def get_forecast(city: str):
    date = datelib.today().isoformat()
    lat, lon = load_coordinates(city.lower())

    if lat is None or lon is None:
        raise BusinessException(HTTPStatus.NOT_FOUND, ErrorMessages.COORDINATES_NOT_FOUND)

    region = await get_region(lat, lon)

    return await get_conditions(region, date, lat, lon)


async def get_conditions(region, date, lat, lon):
    cached_weather = load_weather(region, date)

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

    for weather in forecast:
        if weather.date == date:
            searched_weather = weather

        save_weather(weather)

    if not use_real_data:
        searched_weather = forecast[0]

    return searched_weather


async def send_request(lat: str, lon: str):
    url = f"{WEATHER_API_URL}/data/2.5/forecast?lat={lat}&lon={lon}&appid={config.forecast_api_key}&units=metric"

    if use_real_data:
        print("REQUEST SENT")
        try:
            response = get(url)

        except:
            raise BusinessException(503, ErrorMessages.GETTING_RESPONSE_FAILED)

        try:
            data = loads(response.text)
        except:
            raise BusinessException(503, ErrorMessages.READING_RESPONSE_FAILED)
    else:
        with open("./data/example_response.json", "r", encoding="utf-8") as test_data:
            data = load(test_data)

    return data


async def parse_data(region, received_data):
    try:
        forecast_data = received_data["list"]

    except:
        raise BusinessException(HTTPStatus.SERVICE_UNAVAILABLE, ErrorMessages.PARSING_DATA_FAILED)

    conditions = []

    for hourly_data in forecast_data[::8]:
        try:
            date = datelib.fromtimestamp(int(hourly_data["dt"])).isoformat()
            weather = hourly_data["weather"][0]["main"]
            icon = await map_icon_url(hourly_data["weather"][0]["icon"])
            temp = hourly_data["main"]["temp"]
            min_temp = hourly_data["main"]["temp_min"]
            max_temp = hourly_data["main"]["temp_max"]

        except:
            raise BusinessException(HTTPStatus.SERVICE_UNAVAILABLE, ErrorMessages.PARSING_DATA_FAILED)

        if any([date, weather, icon, temp, min_temp, max_temp]) is None:
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


async def map_icon_url(icon_code):
    return f"https://openweathermap.org/img/w/{icon_code}.png"


def seed_city_cache():
    with open("./data/rs.json", "r", encoding="utf-8") as file:
        data = load(file)

    for city_data in data:
        save_coordinates(city_data['city'].lower(), mapping={'lat': city_data['lat'], 'lng': city_data['lng']})
