class ErrorMessages:
    COORDINATES_NOT_FOUND = "COORDINATES_NOT_FOUND"
    GETTING_RESPONSE_FAILED = "GETTING_RESPONSE_FAILED"
    READING_RESPONSE_FAILED = "READING_RESPONSE_FAILED"
    PARSING_DATA_FAILED = "PARSING_DATA_FAILED"
    PARSING_ARGUMENTS_FAILED = "PARSING_ARGUMENTS_FAILED"
    LOADING_COORDINATES_FAILED = "LOADING_COORDINATES_FAILED"
    SAVING_COORDINATES_FAILED = "SAVING_COORDINATES_FAILED"
    LOADING_WEATHER_FAILED = "LOADING_WEATHER_FAILED"
    SAVING_WEATHER_FAILED = "SAVING_WEATHER_FAILED"
    INVALID_DATE_RANGE = "INVALID_DATE_RANGE"


class Regions:
    NORTH = 'N'
    CENTRAL_UPPER_EAST = 'C1E'
    CENTRAL_UPPER_WEST = 'C1W'
    CENTRAL_LOWER_EAST = 'C2E'
    CENTRAL_LOWER_WEST = 'C2W'
    SOUTH = 'S'


class CacheIdentifiers:
    FORECAST = 'forecast'


WEATHER_API_URL = 'https://api.openweathermap.org'

TIME_TO_LIVE = 86400

LOG_FILE_PATH = './logs'
LOG_FILE_NAME = 'dump'

API_VERSION = 1
