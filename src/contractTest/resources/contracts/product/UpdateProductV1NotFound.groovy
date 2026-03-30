package contracts.product

import org.springframework.cloud.contract.spec.Contract
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE

Contract.make {
    request {
        method PUT()
        headers {
            accept APPLICATION_JSON_VALUE
            contentType APPLICATION_JSON_VALUE
        }
        urlPath("/api/v1/products/019d3f21-af98-7db1-8bb7-b248dc05a4a4"){
            body([
                    name: value(
                            test("NotFound"),
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
        status NOT_FOUND()
        headers {
            contentType APPLICATION_PROBLEM_JSON_VALUE
        }
        body([
                instance: fromRequest().path(),
                type: "/errors/not-found",
                title: "Not Found"
        ])
    }
}