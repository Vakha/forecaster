package forecaster.controller

import forecaster.core.SalesForecaster
import forecaster.core.model.ForecastModelException
import forecaster.core.model.ProductNotFoundException
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate


@WebMvcTest(ForecastController::class)
class ForecastControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var salesForecaster: SalesForecaster

    @Test
    fun salesForecast_okResponse() {
        val productId = 1234L
        val forecastForDate = LocalDate.of(2020, 12, 1)
        val scenarioFeatures = mapOf("onPromotion" to true, "price" to 123.45)
        `when`(salesForecaster.forecast(productId, forecastForDate, scenarioFeatures))
            .thenReturn(42)
        val request = "{\n" +
                "    \"productId\": $productId,\n" +
                "    \"forecastForDate\": \"$forecastForDate\",\n" +
                "    \"scenarioFeatures\": {\n" +
                "        \"onPromotion\": true,\n" +
                "        \"price\": 123.45\n" +
                "    }\n" +
                "}"
        val response =
            "{\"productId\":$productId,\"forecastForDate\":\"$forecastForDate\",\"forecastedAmount\":42}"

        mockMvc.perform(
            post("/forecast/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andDo(print())
            .andExpect { status().isOk }
            .andExpect { content().json(response) }

        verify(salesForecaster).forecast(productId, forecastForDate, scenarioFeatures)
    }

    @Test
    fun salesForecast_notFound() {
        val productId = 1234L
        val forecastForDate = LocalDate.of(2020, 12, 1)
        val scenarioFeatures = mapOf("onPromotion" to true, "price" to 123.45)
        val request = "{\n" +
                "    \"productId\": $productId,\n" +
                "    \"forecastForDate\": \"$forecastForDate\",\n" +
                "    \"scenarioFeatures\": {\n" +
                "        \"onPromotion\": true,\n" +
                "        \"price\": 123.45\n" +
                "    }\n" +
                "}"

        `when`(salesForecaster.forecast(productId, forecastForDate, scenarioFeatures))
            .then { throw ProductNotFoundException(productId) }

        mockMvc.perform(
            post("/forecast/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andDo(print())
            .andExpect { status().isNotFound }

        verify(salesForecaster).forecast(productId, forecastForDate, scenarioFeatures)
    }

    @Test
    fun salesForecast_modelException() {
        val productId = 1234L
        val forecastForDate = LocalDate.of(2020, 12, 1)
        val scenarioFeatures = mapOf("onPromotion" to true, "price" to 123.45)
        val request = "{\n" +
                "    \"productId\": $productId,\n" +
                "    \"forecastForDate\": \"$forecastForDate\",\n" +
                "    \"scenarioFeatures\": {\n" +
                "        \"onPromotion\": true,\n" +
                "        \"price\": 123.45\n" +
                "    }\n" +
                "}"

        `when`(salesForecaster.forecast(productId, forecastForDate, scenarioFeatures))
            .then { throw ForecastModelException(null) }

        mockMvc.perform(
            post("/forecast/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andDo(print())
            .andExpect { status().isInternalServerError }

        verify(salesForecaster).forecast(productId, forecastForDate, scenarioFeatures)
    }

}