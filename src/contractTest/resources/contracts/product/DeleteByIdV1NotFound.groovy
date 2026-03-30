package contracts.product

import org.springframework.cloud.contract.spec.Contract
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE

Contract.make {
    request {
        method DELETE()
        headers {
            accept APPLICATION_JSON_VALUE
        }
        url("/api/v1/products/21651a12-b126-4213-ac21-19f66ff4642f")
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