import json
import requests
from datetime import date as datelib
from statics import ErrorMessages, Regions
from model import BusinessException, WeatherCondition

use_real_data = False


async def get_forecast(city: str):
    lat, lon = await get_coordinates(city)

    if lat is None or lon is None:
        raise BusinessException(404, ErrorMessages.COORDINATES_NOT_FOUND)

    received_data = await send_request(lat, lon)

    region = await get_region(lat, lon)

    forecast = await parse_data(region, received_data)

    for item in forecast:
        print(item)

    return forecast


async def get_coordinates(city):
    with open("./data/rs.json", "r", encoding="utf-8") as file:
        data = json.load(file)

    for city_data in data:
        if city_data["city"].lower() == city.lower():
            return city_data["lat"], city_data["lng"]

    return None, None


async def send_request(lat: str, lon: str):
    api_key = "0c6dde9d6c79cc7846ec4c1c53d53a14"

    url = f"https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={api_key}&units=metric"

    if use_real_data:
        print("REQUEST SENT")
        try:
            response = requests.get(url)
        except:
            raise BusinessException(503, ErrorMessages.GETTING_RESPONSE_FAILED)

        try:
            data = json.loads(response.text)
        except:
            raise BusinessException(503, ErrorMessages.READING_RESPONSE_FAILED)
    else:
        with open("./data/example_response.json", "r", encoding="utf-8") as test_data:
            data = json.load(test_data)

    return data


async def parse_data(region, received_data):
    try:
        forecast_data = received_data["list"]

    except:
        raise BusinessException(503, ErrorMessages.PARSING_DATA_FAILED)

    conditions = []

    for hourly_data in forecast_data[::8]:
        try:
            date = datelib.fromtimestamp(int(hourly_data["dt"]))
            weather = hourly_data["weather"][0]["main"]
            icon = await map_icon_url(hourly_data["weather"][0]["icon"])
            temp = hourly_data["main"]["temp"]
            min_temp = hourly_data["main"]["temp_min"]
            max_temp = hourly_data["main"]["temp_max"]

        except:
            raise BusinessException(503, ErrorMessages.PARSING_DATA_FAILED)

        if any([date, weather, icon, temp, min_temp, max_temp]) is None:
            raise BusinessException(503, ErrorMessages.PARSING_DATA_FAILED)

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


async def map_icon_url(iconcode):
    return f"http://openweathermap.org/img/w/{iconcode}.png"
