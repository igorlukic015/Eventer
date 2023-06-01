from redis import Redis
from redis.commands.json.path import Path
from forecast.statics import CacheIdentifiers
from forecast.config import config

cache = Redis(host=config.cache_host,
              port=config.cache_port,
              username=config.cache_username,
              password=config.cache_password)


async def save_weather(weather):
    await cache.json().set(f"{CacheIdentifiers.FORECAST}:{weather.region}@{weather.date}",
                           Path.root_path(),
                           weather.serialize())

    await cache.expire(f"{CacheIdentifiers.FORECAST}:{weather.region}@{weather.date}", 86400)


async def get_weather(region, date):
    return await cache.json().get(f"{CacheIdentifiers.FORECAST}:{region}@{date}")
