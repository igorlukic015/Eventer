import redis
from redis.commands.json.path import Path
from redis.commands.search.query import Query

cache = redis.Redis(host='localhost', port=9001, username='default', password='a123456!')
index = cache.ft("idx:forecast")


async def save_weather(weather):
    cache.json().set(f"forecast:{weather.region}%{weather.date}", Path.root_path(), weather.serialize())


async def get_weather(region, date):
    return index.search(Query(f"@region:({region}) @date:({date.replace('-', 'x')})"))
