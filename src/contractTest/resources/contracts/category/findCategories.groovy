package contracts.category

import org.springframework.cloud.contract.spec.Contract
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/categories") {
            queryParameters {
                parameter("page", "0")
                parameter("size", "2")
            }
        }
    }
    response {
        status OK()
        headers {
            contentType APPLICATION_JSON_VALUE
        }
        body([
                number: fromRequest().query("page"),
                size: 2,
                totalPages: 1,
                totalElements: 2,
                content: [
                        [
                                id: anyUuid(),
                                name: anyNonBlankString(),
                                enabled: anyBoolean()
                        ],
                        [
                                id: anyUuid(),
                                name: anyNonBlankString(),
                                enabled: anyBoolean()
                        ]
                ]
        ])
    }
}