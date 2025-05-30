package com.e2e.tests.product;

import com.e2e.tests.util.E2ESuite;
import com.e2e.tests.util.TestRestFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.awaitility.Awaitility.await;
import static org.testcontainers.shaded.org.awaitility.Durations.FIVE_SECONDS;


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
@SpringBootTest(classes = E2ESuite.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ProductServiceE2ETest extends E2ESuite {

    @Autowired
    private TestRestFacade restFacade;

    @Value("${product.service.url}")
    private String productServiceUrl;


    @Test
    void shouldGetAllProducts() {
        ResponseEntity<String> response = restFacade.get(productServiceUrl + "/product-service/api/categories", String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + response.getStatusCode());
    }
}
