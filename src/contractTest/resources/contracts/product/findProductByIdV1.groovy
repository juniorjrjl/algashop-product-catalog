package contracts.product

import org.springframework.cloud.contract.spec.Contract
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

Contract.make {
    request {
        method GET()
        headers {
            accept APPLICATION_JSON_VALUE
        }
        url("/api/v1/products/6e148bd5-47f6-4022-b9da-07cfaa294f7a")
    }
    response {
        status OK()
        headers {
            contentType APPLICATION_JSON_VALUE
        }
        body([
                id: fromRequest().path(3),
                addedAt: anyIso8601WithOffset(),
                name: "Notebook X11",
                brand: "Deep Driver",
                regularPrice: 1500.00,
                salePrice: 1000.00,
                inStock: true,
                enabled: true,
                category:[
                        id: anyUuid(),
                        name: "Notebook"
                ],
                description: "A gamer Notebook"
        ])
    }
}
