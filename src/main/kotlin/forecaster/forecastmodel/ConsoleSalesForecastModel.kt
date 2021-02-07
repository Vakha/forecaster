package forecaster.forecastmodel

import com.fasterxml.jackson.databind.ObjectMapper
import forecaster.core.SalesForecastModel
import forecaster.core.model.ProductFeatures
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import java.util.stream.Collectors

@Component
class ConsoleSalesForecastModel(private val mapper: ObjectMapper) : SalesForecastModel {

    private val resultPrefix = "predictedAmount:"

    override fun predict(productFeatures: ProductFeatures, forDate: LocalDate): Long {
        val process = Runtime.getRuntime().exec(
            arrayOf(
                "python3",
                "pymodel/predict.py",
                forDate.toString(),
                mapper.writeValueAsString(productFeatures.features),
            )
        )
        process.waitFor()
        if (process.exitValue() == 0) {
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val message = reader.lines().filter { s -> s.startsWith(resultPrefix) }.findFirst()
            return message.map { s ->
                s.replaceFirst(resultPrefix, "").toLong()
            }.orElseThrow { PyModelException("Not found results in command output") }
        } else {
            val reader = BufferedReader(InputStreamReader(process.errorStream))
            val errorMessage = reader.lines().collect(Collectors.joining("\n"))
            throw PyModelException(errorMessage)
        }
    }

}