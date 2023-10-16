from http import HTTPStatus

from fastapi import Request
from fastapi.responses import JSONResponse

from forecast.model import BusinessException, ErrorResponse


async def business_exception_handler(request: Request, exc: BusinessException):
    return JSONResponse(
        status_code=exc.status_code,
        content=ErrorResponse(
            status_code=exc.status_code,
            detail=exc.detail,
            additional_info=exc.additional_info
        ).dict()
    )


async def general_exception_handler(request: Request, exc: Exception):
    return JSONResponse(
        status_code=HTTPStatus.INTERNAL_SERVER_ERROR,
        content=ErrorResponse(
            status_code=HTTPStatus.INTERNAL_SERVER_ERROR,
            detail=HTTPStatus.INTERNAL_SERVER_ERROR.name,
            additional_info=str(exc)
        ).dict()
    )
