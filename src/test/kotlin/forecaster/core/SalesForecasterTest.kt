package forecaster.core

import forecaster.core.model.ForecastModelException
import forecaster.core.model.ProductFeatures
import forecaster.core.model.ProductNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import org.mockito.Mockito.*
import java.time.LocalDate


class SalesForecasterTest {

    private val productFeaturesStorageMock = mock(ProductFeaturesStorage::class.java)
    private val salesForecastModelMock = mock(SalesForecastModel::class.java)

    private val forecaster = SalesForecaster(productFeaturesStorageMock, salesForecastModelMock)

    @Test
    fun forecast_successfully() {
        val productId = 600L
        val forecastForDate = LocalDate.now()
        val scenarioFeatures = mapOf("onPromotion" to true)
        val storedFeatures = mapOf("onPromotion" to false, "price" to 12.2)
        val storedProductFeatures = ProductFeatures(productId, storedFeatures)
        val featuresToForecast = mapOf("onPromotion" to true, "price" to 12.2)
        val productFeaturesToForecast = ProductFeatures(productId, featuresToForecast)
        val expectedPredictedAmount = 42L


        `when`(productFeaturesStorageMock.getProductFeatures(productId))
            .thenReturn(storedProductFeatures)
        `when`(salesForecastModelMock.predict(productFeaturesToForecast, forecastForDate))
            .thenReturn(expectedPredictedAmount)

        val actualPredictedAmount = forecaster.forecast(productId, forecastForDate, scenarioFeatures)

        assertEquals(expectedPredictedAmount, actualPredictedAmount)

        verify(productFeaturesStorageMock).getProductFeatures(productId)
        verify(salesForecastModelMock).predict(productFeaturesToForecast, forecastForDate)
    }

    @Test
    fun forecast_failIfNotProductFound() {
        val productId = 600L
        val forecastForDate = LocalDate.now()

        `when`(productFeaturesStorageMock.getProductFeatures(productId))
            .thenReturn(null)

        assertThrows(ProductNotFoundException::class.java) {
            forecaster.forecast(productId, forecastForDate, mapOf())
        }
    }

    @Test
    fun forecast_handleExceptionFromModel() {
        val productId = 600L
        val forecastForDate = LocalDate.now()
        val scenarioFeatures = mapOf("onPromotion" to true)
        val storedFeatures = mapOf("onPromotion" to false, "price" to 12.2)
        val storedProductFeatures = ProductFeatures(productId, storedFeatures)
        val featuresToForecast = mapOf("onPromotion" to true, "price" to 12.2)
        val productFeaturesToForecast = ProductFeatures(productId, featuresToForecast)

        `when`(productFeaturesStorageMock.getProductFeatures(productId))
            .thenReturn(storedProductFeatures)
        `when`(salesForecastModelMock.predict(productFeaturesToForecast, forecastForDate))
            .thenThrow(RuntimeException("BOOM!"))

        assertThrows(ForecastModelException::class.java) {
            forecaster.forecast(productId, forecastForDate, scenarioFeatures)
        }

        verify(productFeaturesStorageMock).getProductFeatures(productId)
        verify(salesForecastModelMock).predict(productFeaturesToForecast, forecastForDate)
    }
}