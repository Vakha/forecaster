package forecaster.core

import forecaster.core.model.ProductFeatures
import java.time.LocalDate

interface SalesForecastModel {

    fun predict(productFeatures: ProductFeatures, forDate: LocalDate): Long

}