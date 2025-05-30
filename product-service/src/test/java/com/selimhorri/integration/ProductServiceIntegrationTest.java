package com.selimhorri.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.selimhorri.app.domain.Category;
import com.selimhorri.app.domain.Product;
import com.selimhorri.app.dto.ProductDto;
import com.selimhorri.app.dto.response.collection.DtoCollectionResponse;
import com.selimhorri.app.helper.ProductMappingHelper;


@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = com.selimhorri.app.ProductServiceApplication.class
)
@Testcontainers
@ContextConfiguration(initializers = KubernetesIntegrationTest.Initializer.class)
public class ProductServiceIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void createProductAndRetrieve() {
        // Crear producto
        Category category = new Category(1, "Computers",
                "https://intelcorp.scene7.com/is/image/intelcorp/mini-pc-product-image-transparent-background:1920-1080?wid=480&hei=270&fmt=webp-alpha",
                null, null, null);

        Product product1 = new Product(1, "Super PC Gamer",
                "https://media.falabella.com/falabellaCO/127344182_01/w=1500,h=1500,fit=pad", "gamepc123", 1000.0, 9,
                category);

        ProductDto newProduct = ProductMappingHelper.map(product1);

        ResponseEntity<ProductDto> createResponse = restTemplate.postForEntity(
            "/api/products",
            newProduct,
            ProductDto.class
        );
        
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody().getProductId());

        int productId = createResponse.getBody().getProductId();

        // Consultar producto
        ResponseEntity<Object> getResponse = restTemplate.getForEntity(
            "/api/products/" + productId,
            Object.class
        );
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        // assertEquals("bobsmith@email.com", getResponse.getBody().getEmail());
    }

    @Test
    public void testGetAllProducts() {
        String url = "http://localhost:8500/product-service/api/products";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetProductById() {
        String url = "http://localhost:8500/product-service/api/products/1";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
