package forecaster.dto

import java.time.LocalDate

data class SalesForecastResponse(val productId: Long,
                                 val forecastForDate: LocalDate,
                                 val forecastedAmount: Long)
