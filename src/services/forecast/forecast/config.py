from decouple import config as decouple_config


class Configuration:

    def __init__(self):
        self.host = decouple_config('HOST', default='')
        self.port = decouple_config('PORT', default='-1', cast=int)
        self.environment = decouple_config('ENVIRONMENT', default='unknown')
        self.shouldReload = self.environment == 'debug'
        self.log_level = decouple_config("LOG_LEVEL", default="NOTSET")
        self.is_debug = self.environment == "debug"

        self.cache_host = decouple_config('CACHE_HOST', default='')
        self.cache_port = decouple_config('CACHE_PORT', default='-1', cast=int)
        self.cache_username = decouple_config('CACHE_USERNAME', default='')
        self.cache_password = decouple_config('CACHE_PASSWORD', default='')

        self.forecast_api_key = decouple_config('FORECAST_API_KEY', default='')


config = Configuration()
