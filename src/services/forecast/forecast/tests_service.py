from re import S
import unittest
from model import BusinessException
import service


class ServiceTests(unittest.IsolatedAsyncioTestCase):

    # async def test_send_request_invalid_url_fail_exception(self):
    #     self.assertRaises(BusinessException, await service.send_request(None, None))

    async def test_parse_data_invalid_type_empty_parsing_failed(self):
        invalid_data = """
            list: [
                {
                    "dt": a,
                    "main": {
                    "temp": 295.09,
                    "feels_like": 295.04,
                    "temp_min": 293.99,
                    "temp_max": 295.09,
                    "pressure": 1009,
                    "sea_level": 1009,
                    "grnd_level": 999,
                    "humidity": 65,
                    "temp_kf": 1.1
                    },
                    "weather": [
                    {
                        "id": 802,
                        "main": "Clouds",
                        "description": "scattered clouds",
                        "icon": "03n"
                    }]
                }
            ],
        """
        with self.assertRaises(
            BusinessException,
        ):
            await service.parse_data(invalid_data)

    async def test_parse_data_missing_data_parsing_failed(self):
        invalid_data = """
            list: [
                {
                    "dt": 1660413600,
                    "main": {
                    "feels_like": 295.04,
                    "temp_min": 293.99,
                    "temp_max": 295.09,
                    "pressure": 1009,
                    "sea_level": 1009,
                    "grnd_level": 999,
                    "humidity": 65,
                    "temp_kf": 1.1
                    },
                    "weather": [
                    {
                        "id": 802,
                        "main": "Clouds",
                        "description": "scattered clouds",
                        "icon": "03n"
                    }]
                }
            ],
        
        """

        with self.assertRaises(BusinessException):
            await service.parse_data(invalid_data)

    async def test_parse_data_received_data_missing_value_parsing_failed(self):
        invalid_data = """
            list: [
                {
                    "dt": 1660413600,
                    "main": {
                    "temp": 295.09,
                    "feels_like": 295.04,
                    "temp_min": "",
                    "temp_max": 295.09,
                    "pressure": 1009,
                    "sea_level": 1009,
                    "grnd_level": 999,
                    "humidity": 65,
                    "temp_kf": 1.1
                    },
                    "weather": [
                    {
                        "id": 802,
                        "main": "Clouds",
                        "description": "scattered clouds",
                        "icon": "03n"
                    }]
                }
            ],
        """

        with self.assertRaises(BusinessException):
            await service.parse_data(invalid_data)

    async def test_parse_data_received_data_none_parsing_failed(self):
        with self.assertRaises(BusinessException):
            await service.parse_data(None)

    async def test_parse_data_received_data_empty_parsing_failed(self):
        invalid_data = ""

        with self.assertRaises(BusinessException):
            await service.parse_data(invalid_data)


if __name__ == "__main__":
    unittest.main()
