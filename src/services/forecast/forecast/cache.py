from redis import Redis
from redis.commands.json.path import Path

from forecast.statics import CacheIdentifiers, TIME_TO_LIVE, ErrorMessages
from forecast.config import config
from forecast.logger import logger


def get_cache() -> Redis:
    return Redis(host=config.cache_host,
                 port=config.cache_port,
                 username=config.cache_username,
                 password=config.cache_password,
                 decode_responses=True,
                 encoding="utf-8")


def save_weather(weather, cache: Redis):
    logger.info(f"Saving weather for {weather.region}@{weather.date}")

    try:
        cache.json().set(f"{CacheIdentifiers.FORECAST}:{weather.region}@{weather.date}",
                         Path.root_path(),
                         weather.serialize())

        cache.expire(f"{CacheIdentifiers.FORECAST}:{weather.region}@{weather.date}", TIME_TO_LIVE)
    except:
        logger.error(ErrorMessages.SAVING_WEATHER_FAILED)


def load_weather(region, date, cache: Redis):
    logger.info(f"Loading weather for {region}@{date}")

    result = None

    try:
        result = cache.json().get(f"{CacheIdentifiers.FORECAST}:{region}@{date}")
    except:
        logger.error(ErrorMessages.LOADING_WEATHER_FAILED)

    return result


def load_coordinates(city, cache: Redis):
    logger.info(f"Loading coordinates for {city}")
    try:
        result = cache.hgetall(city)
    except:
        logger.error(ErrorMessages.LOADING_COORDINATES_FAILED)
        return None, None

    return result.get('lat', None), result.get('lng', None)


def save_coordinates(city, mapping, cache: Redis):
    logger.info(f"Saving coordinates for {city}")
    try:
        cache.hset(city, mapping=mapping)
    except:
        logger.error(ErrorMessages.SAVING_COORDINATES_FAILED)
