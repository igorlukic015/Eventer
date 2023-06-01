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

        dictionary['date'] = dictionary['date'].replace('-', 'x')

        return dictionary
