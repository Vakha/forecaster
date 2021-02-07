package forecaster.core.model

data class ProductFeatures(val productId: Long,
                           val features: Map<String, Any>)
