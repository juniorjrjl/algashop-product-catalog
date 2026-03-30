package contracts.category

import org.springframework.cloud.contract.spec.Contract
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

Contract.make {
    request {
        method PUT()
        urlPath("/api/v1/categories/f5ab7a1e-37da-41e1-892b-a1d38275c2f2")
        headers {
            contentType APPLICATION_JSON_VALUE
        }
        body([
                name: "Desktops",
                enabled: true
        ])
    }
    response {
        status 200
        headers {
            contentType APPLICATION_JSON_VALUE
        }
        body([
                id: fromRequest().path(3),
                name: anyNonBlankString(),
                enabled: anyBoolean()
        ])
    }
}