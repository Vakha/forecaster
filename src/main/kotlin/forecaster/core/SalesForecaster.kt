package forecaster.core

import forecaster.core.model.ForecastModelException
import forecaster.core.model.ProductNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.Exception
import java.time.LocalDate

@Service
class SalesForecaster(private val productFeaturesStorage: ProductFeaturesStorage,
                      private val salesForecastModel: SalesForecastModel) {

    private val log: Logger = LoggerFactory.getLogger(SalesForecaster::class.java)

    fun forecast(productId: Long, forecastForDate: LocalDate, scenarioFeatures: Map<String, Any>): Long {
        val productFeatures = productFeaturesStorage.getProductFeatures(productId)
            ?: throw ProductNotFoundException(productId)
        val updatedFeatures = productFeatures.copy(
            features = productFeatures.features + scenarioFeatures
        )
        try {
            return salesForecastModel.predict(updatedFeatures, forecastForDate)
        } catch (e: Exception) {
            log.error("Failed to execute model", e)
            throw ForecastModelException(e)
        }
    }

}