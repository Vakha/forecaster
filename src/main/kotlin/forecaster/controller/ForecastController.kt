package forecaster.controller

import forecaster.core.SalesForecaster
import forecaster.dto.SalesForecastRequest
import forecaster.dto.SalesForecastResponse
import org.springframework.web.bind.annotation.*

@RestController
class ForecastController(val salesForecaster: SalesForecaster) {

    @PostMapping("/forecast/sales")
    fun forecastSales(@RequestBody salesForecastRequest: SalesForecastRequest): SalesForecastResponse {
        val productId = salesForecastRequest.productId
        val forecastForDate = salesForecastRequest.forecastForDate
        val forecastedAmount = salesForecaster.forecast(
            productId = productId,
            forecastForDate = forecastForDate,
            scenarioFeatures = salesForecastRequest.scenarioFeatures
        )
        return SalesForecastResponse(
            productId = productId,
            forecastForDate = forecastForDate,
            forecastedAmount = forecastedAmount
        )
    }
}