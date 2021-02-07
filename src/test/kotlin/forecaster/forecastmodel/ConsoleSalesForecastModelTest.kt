package forecaster.forecastmodel

import forecaster.core.model.ProductFeatures
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import org.junit.jupiter.api.Assertions.*


@SpringBootTest
class ConsoleSalesForecastModelTest {

   @Autowired
   lateinit var salesForecastModel: ConsoleSalesForecastModel

    @Test
    fun predict_byCallingPythonScript() {
        val res = salesForecastModel.predict(
            productFeatures = ProductFeatures(123, mapOf("a" to 1, "v" to 3)),
            forDate = LocalDate.now()
        )
        assertEquals(42, res)
    }

}