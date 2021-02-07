package forecaster.core

import forecaster.core.model.ProductFeatures

interface ProductFeaturesStorage {

    fun getProductFeatures(productId: Long): ProductFeatures?

}