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
                            name: anyNonBlankString(),
                            brand: anyNonBlankString(),
                            regularPrice: anyDouble(),
                            salePrice: anyDouble(),
                            inStock: anyBoolean(),
                            enabled: anyBoolean(),
                            category:[
                                    id: anyUuid(),
                                    name: anyNonBlankString(),
                            ],
                            description: anyNonBlankString()
                        ],
                        [
                            id: anyUuid(),
                            addedAt: anyIso8601WithOffset(),
                            name: anyNonBlankString(),
                            brand: anyNonBlankString(),
                            regularPrice: anyDouble(),
                            salePrice: anyDouble(),
                            inStock: anyBoolean(),
                            enabled: anyBoolean(),
                            category:[
                                    id: anyUuid(),
                                    name: anyNonBlankString(),
                            ],
                            description: anyNonBlankString()
                        ]
                    ]
            ])
        }
    }
}