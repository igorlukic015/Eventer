from dotenv import dotenv_values


class Configuration:

    def __init__(self):
        values = dotenv_values('.env')
        self.host = values.get('HOST', '')
        self.port = int(values.get('PORT', '-1'))
        self.environment = values.get('ENVIRONMENT', 'unknown')
        self.shouldReload = self.environment == 'debug'
        self.log_level = self.environment
        self.is_debug = self.environment == 'debug'

        self.cache_host = values.get('CACHE_HOST', '')
        self.cache_port = int(values.get('CACHE_PORT', '-1'))
        self.cache_username = values.get('CACHE_USERNAME', '')
        self.cache_password = values.get('CACHE_PASSWORD', '')

        self.forecast_api_key = values.get('FORECAST_API_KEY', '')


config = Configuration()
