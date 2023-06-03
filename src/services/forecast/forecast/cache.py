from redis import Redis
from redis.commands.json.path import Path
from forecast.statics import CacheIdentifiers, TIME_TO_LIVE
from forecast.config import config

cache = Redis(host=config.cache_host,
              port=config.cache_port,
              username=config.cache_username,
              password=config.cache_password,
              decode_responses=True)


def save_weather(weather):
    cache.json().set(f"{CacheIdentifiers.FORECAST}:{weather.region}@{weather.date}",
                     Path.root_path(),
                     weather.serialize())

    cache.expire(f"{CacheIdentifiers.FORECAST}:{weather.region}@{weather.date}", TIME_TO_LIVE)


def load_weather(region, date):
    return cache.json().get(f"{CacheIdentifiers.FORECAST}:{region}@{date}")


def load_coordinates(city):
    result = cache.hgetall(city)

    return result.get('lat', None), result.get('lng', None)


def save_coordinates(city, mapping):
    cache.hset(city, mapping=mapping)
