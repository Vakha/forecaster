package forecaster.core.model

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ForecastModelException(cause: Throwable?) : Exception("Error during model invocation", cause)