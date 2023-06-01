from redis import Redis
from redis.commands.json.path import Path
from redis.commands.search.query import Query
from forecast.statics import CacheIdentifiers

cache = Redis(host='localhost', port=9001, username='default', password='a123456!')


async def save_weather(weather):
    cache.json().set(f"{CacheIdentifiers.FORECAST}:{weather.region}@{weather.date}", Path.root_path(), weather.serialize())
    cache.expire(f"{CacheIdentifiers.FORECAST}:{weather.region}@{weather.date}", 86400)


async def get_weather(region, date):
    return cache.json().get(f"{CacheIdentifiers.FORECAST}:{region}@{date}")
