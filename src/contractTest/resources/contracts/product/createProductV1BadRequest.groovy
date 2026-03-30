package contracts.product

import org.springframework.cloud.contract.spec.Contract

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE

Contract.make {
    request {
        method POST()
        headers {
            accept APPLICATION_JSON_VALUE
            contentType APPLICATION_JSON_VALUE
        }
        urlPath("/api/v1/products"){
            body([
                    name: ""
            ])
        }
    }
    response {
        status(BAD_REQUEST())
        headers {
            contentType APPLICATION_PROBLEM_JSON_VALUE
        }
        body([
                instance: fromRequest().path(),
                type: "/errors/invalid-fields",
                title: "Invalid fields",
                detail: "One or more fields are invalid",
                fields: [
                        name: anyNonBlankString(),
                        brand: anyNonBlankString(),
                        regularPrice: anyNonBlankString(),
                        salePrice: anyNonBlankString(),
                        enabled: anyNonBlankString(),
                        categoryId: anyNonBlankString(),
                ]
        ])
    }
}

