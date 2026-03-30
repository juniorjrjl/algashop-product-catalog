package contracts.category

import org.springframework.cloud.contract.spec.Contract
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

Contract.make {
    request {
        method POST()
        url "/api/v1/categories"
        headers {
            contentType APPLICATION_JSON_VALUE
        }
        body([
                name: value(
                        test("Created"),
                        stub(nonBlank())
                ),
                enabled: value(
                        test(true),
                        stub(anyBoolean())
                )
        ])
    }
    response {
        status CREATED()
        headers {
            contentType APPLICATION_JSON_VALUE
        }
        body([
                id: anyUuid(),
                name: anyNonBlankString(),
                enabled: anyBoolean()
        ])
    }
}