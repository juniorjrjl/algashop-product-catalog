package contracts.product

import org.springframework.cloud.contract.spec.Contract
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

Contract.make {
    request {
        method DELETE()
        headers {
            accept APPLICATION_JSON_VALUE
        }
        url("/api/v1/products/6e148bd5-47f6-4022-b9da-07cfaa294f7b")
    }
    response {
        status NO_CONTENT()
    }
}
