package contracts.product

import org.springframework.cloud.contract.spec.Contract
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

Contract.make {
    request {
        method POST()
        headers {
            accept APPLICATION_JSON_VALUE
            contentType APPLICATION_JSON_VALUE
        }
        urlPath("/api/v1/products"){
            body([
                    name: value(
                            test("Created"),
                            stub(nonBlank())
                    ),
                    brand: value(
                            test("Deep Driver"),
                            stub(nonBlank())
                    ),
                    regularPrice: value(
                            test(1500.00),
                            stub(number())
                    ),
                    salePrice: value(
                            test(1000.00),
                            stub(number())
                    ),
                    enabled: value(
                            test(true),
                            stub(anyBoolean())
                    ),
                    categoryId: value(
                            test("6e148bd5-47f6-4022-b9da-07cfaa294f7a"),
                            stub(anyUuid())
                    ),
                    description: value(
                            test("A gamer Notebook"),
                            stub(optional(nonBlank()))
                    )
            ])
        }
    }
    response {
        status CREATED()
        headers {
            contentType APPLICATION_JSON_VALUE
        }
        body([
                id : anyUuid(),
                name: anyNonBlankString(),
                brand: anyNonBlankString(),
                regularPrice: anyDouble(),
                salePrice: anyDouble(),
                inStock: false,
                enabled: anyBoolean(),
                category: [
                    id: anyUuid(),
                    name: anyNonBlankString()
                ],
                description: anyNonBlankString(),
                addedAt: anyIso8601WithOffset(),
        ])
    }
}

