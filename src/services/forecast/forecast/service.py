from datetime import date as datelib
import json
import requests

from model import BusinessException, WeatherCondition


use_real_data = False


async def get_forecast(city: str):
    lat, lon = await get_coordinates(city)

    if lat is None or lon is None:
        raise BusinessException(404, f"No coordinates found for city {city}")

    received_data = await send_request(lat, lon)

    forecast = await parse_data(received_data)

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


async def send_request(lat, lon):
    api_key = "0c6dde9d6c79cc7846ec4c1c53d53a14"

    url = f"https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={api_key}&units=metric"

    if use_real_data:
        print("REQUEST SENT")
        try:
            response = requests.get(url)
        except:
            raise BusinessException(503, "Failed getting response")

        try:
            data = json.loads(response.text)
        except:
            raise BusinessException(503, "Failed reading API response data")
    else:
        with open("./data/example_response.json", "r", encoding="utf-8") as test_data:
            data = json.load(test_data)

    return data


async def parse_data(received_data):
    forecast_data = received_data["list"]

    conditions = []

    for hourly_data in forecast_data[::8]:
        date = datelib.fromtimestamp(int(hourly_data["dt"]))
        weather = hourly_data["weather"][0]["main"]
        icon = map_icon_url(hourly_data["weather"][0]["icon"])
        temp = hourly_data["main"]["temp"]
        min_temp = hourly_data["main"]["temp_min"]
        max_temp = hourly_data["main"]["temp_max"]

        condition = WeatherCondition(
            date=date,
            icon=icon,
            temp=temp,
            weather=weather,
            max_temp=max_temp,
            min_temp=min_temp,
        )

        conditions.append(condition)

    return conditions


def map_icon_url(iconcode):
    return f"http://openweathermap.org/img/w/{iconcode}.png"


if __name__ == "__main__":
    print("started")
    get_forecast("Novi Sad")
    print("finished")
