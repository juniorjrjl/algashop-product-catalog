package contracts.product

import org.springframework.cloud.contract.spec.Contract
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

Contract.make {
    request {
        method GET()
        headers {
            accept APPLICATION_JSON_VALUE
        }
        url("/api/v1/products"){
            queryParameters {
                parameter('size', value(stub(optional(anyNumber())), test(10)))
                parameter('number', value(stub(optional(anyNumber())), test(0)))
            }
        }
        response {
            status OK()
            headers {
                contentType APPLICATION_JSON_VALUE
            }
            body([
                    size: fromRequest().query("size"),
                    number: 0,
                    totalElements: 2,
                    totalPages: 1,
                    content:[
                        [
                            id: anyUuid(),
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
                        ],
                        [
                            id: anyUuid(),
                            addedAt: anyIso8601WithOffset(),
                            name: "Desktop I9000",
                            brand: "Deep Driver",
                            regularPrice: 1500.00,
                            salePrice: 1000.00,
                            inStock: false,
                            enabled: true,
                            category:[
                                    id: anyUuid(),
                                    name: "Desktop"
                            ],
                            description: "A gamer Notebook"
                        ]
                    ]
            ])
        }
    }
}