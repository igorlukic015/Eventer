from redis import Redis
from redis.commands.json.path import Path
from redis.commands.search.query import Query
from forecast.statics import CacheIdentifiers

cache = Redis(host='localhost', port=9001, username='default', password='a123456!')
forecast_index = cache.ft(f"idx:{CacheIdentifiers.FORECAST}")


async def save_weather(weather):
    cache.json().set(f"{CacheIdentifiers.FORECAST}:{weather.region}%{weather.date}", Path.root_path(), weather.serialize())


async def get_weather(region, date):
    return forecast_index.search(Query(f"@region:({region}) @date:({date.replace('-', 'x')})"))
