package forecaster.storage

import forecaster.core.ProductFeaturesStorage
import forecaster.core.model.ProductFeatures
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.random.Random

@Component
class RandomProductFeaturesStorage : ProductFeaturesStorage {

    override fun getProductFeatures(productId: Long): ProductFeatures? =
        ProductFeatures(
            productId = productId,
            features = mapOf(
                "name" to UUID.randomUUID().toString(),
                "onPromotion" to Random.nextBoolean(),
                "price" to Random.nextDouble(1.0, 10000.0)
            )
        )

}