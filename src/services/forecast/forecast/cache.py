import redis.asyncio as redis
from redis.commands.json.path import Path

from forecast.statics import CacheIdentifiers, TIME_TO_LIVE, ErrorMessages
from forecast.config import config
from forecast.logger import logger


async def get_cache() -> redis:
    return await redis.from_url(
        f"redis://{config.cache_username}:{config.cache_password}@{config.cache_host}:{config.cache_port}",
        encoding="utf-8",
        decode_responses=True)


async def save_weather(weather, cache: redis):
    logger.info(f"Saving weather for {weather.region}@{weather.date}")

    data = weather.serialize()

    try:
        # await cache.hset("proba", "{'jedan': 1}")

        await cache.json().set(f"{CacheIdentifiers.FORECAST}:{weather.region}@{data['date']}",
                               "$",
                               data)

        await cache.expire(f"{CacheIdentifiers.FORECAST}:{weather.region}@{data['date']}", TIME_TO_LIVE)
    #
    # except :
    #     logger.error(ErrorMessages.SAVING_WEATHER_FAILED)

    finally:
        await cache.close()


async def load_weather(region, date, cache: redis):
    logger.info(f"Loading weather for {region}@{date}")

    result = None

    try:
        result = await cache.json().get(f"{CacheIdentifiers.FORECAST}:{region}@{date}")
    except:
        logger.error(ErrorMessages.LOADING_WEATHER_FAILED)
    finally:
        await cache.close()

    return result


async def load_coordinates(city, cache: redis):
    logger.info(f"Loading coordinates for {city}")
    try:
        result = await cache.hgetall(city)
    except:
        logger.error(ErrorMessages.LOADING_COORDINATES_FAILED)
        return None, None
    finally:
        await cache.close()

    return result.get('lat', None), result.get('lng', None)


async def save_coordinates(city, mapping, cache: redis):
    logger.info(f"Saving coordinates for {city}")
    try:
        await cache.hset(city, mapping=mapping)
    except:
        logger.error(ErrorMessages.SAVING_COORDINATES_FAILED)
    finally:
        await cache.close()
