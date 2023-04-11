from fastapi import HTTPException


class BusinessException(HTTPException):
    def __repr__(self) -> str:
        return f"Request failed with code {self.status_code} and message:\n{self.detail}"

class WeatherCondition:
    def __init__(self, **kwargs) -> None:
        self.region = kwargs['region']
        self.date = kwargs['date']
        self.weather = kwargs['weather']
        self.icon = kwargs['icon']
        self.temp = kwargs['temp']
        self.min_temp = kwargs['min_temp']
        self.max_temp = kwargs['max_temp']

    def __repr__(self) -> str:
        return f"For date {self.date}, weather is {self.weather} with temperature {self.temp} celsius"