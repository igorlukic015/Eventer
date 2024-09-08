from fastapi import HTTPException
from pydantic import BaseModel


class BusinessException(HTTPException):
    def __init__(self, status_code, detail: str, additional_info: str = None):
        super().__init__(status_code=status_code, detail=detail)
        self.additional_info = additional_info

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
        return f"For region {self.region} on date {self.date}, weather is {self.weather} with temperature {self.temp} celsius"

    def __iter__(self):
        yield from {
            "region": self.region,
            "date": self.date,
            "weather": self.weather,
            "icon": self.icon,
            "temp": self.temp,
            "min_temp": self.min_temp,
            "max_temp": self.max_temp
        }.items()

    def serialize(self):
        dictionary = dict(self)

        return dictionary


class WeatherResponse(BaseModel):
    region: str
    date: str
    weather: str
    icon: str
    temp: float
    min_temp: float
    max_temp: float

class ErrorResponse(BaseModel):
    detail: str
    status_code: int
    additional_info: str = None