package forecaster.dto

import java.time.LocalDate

data class SalesForecastRequest(val productId: Long,
                                val forecastForDate: LocalDate,
                                val scenarioFeatures: Map<String, Any>)
