package com.selimhorri.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selimhorri.app.domain.Category;
import com.selimhorri.app.domain.Product;
import com.selimhorri.app.dto.ProductDto;
import com.selimhorri.app.exception.wrapper.ProductNotFoundException;
import com.selimhorri.app.helper.ProductMappingHelper;
import com.selimhorri.app.repository.ProductRepository;
import com.selimhorri.app.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    private ProductServiceImpl productService;

    private List<Product> products;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);

        Category category = new Category(1, "Computers",
                "https://intelcorp.scene7.com/is/image/intelcorp/mini-pc-product-image-transparent-background:1920-1080?wid=480&hei=270&fmt=webp-alpha",
                null, null, null);

        Product product1 = new Product(1, "Super PC Gamer",
                "https://media.falabella.com/falabellaCO/127344182_01/w=1500,h=1500,fit=pad", "gamepc123", 1000.0, 9,
                category);

        Product product2 = new Product(2, "Gaming Laptop",
                "https://m.media-amazon.com/images/I/71IsafDXnKL.jpg", "gamelaptop123", 1200.0, 5,
                category);

        Product product3 = new Product(3, "Gaming Monitor",
                "https://www.lg.com/content/dam/channel/wcms/co/images/monitores/34gp63a-b_awp_escb_co_c/gallery/DZ-01.jpg",
                "gamingmonitor123", 300.0, 7,
                category);

        products = List.of(product1, product2, product3);
    }

    @Test
    void testFindAllReturnsMappedProductDtos() {
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDto> result = productService.findAll();

        assertEquals(3, result.size());
    }

    @Test
    void testFindByIdReturnsProductDtoWhenFound() {
        when(productRepository.findById(1)).thenReturn(Optional.of(products.get(0)));

        ProductDto result = productService.findById(1);

        assertEquals(1, result.getProductId());
        assertEquals("Super PC Gamer", result.getProductTitle());
        assertEquals("gamepc123", result.getSku());
        assertEquals("Computers", result.getCategoryDto().getCategoryTitle());
        verify(productRepository).findById(1);
    }

    @Test
    void testFindByIdThrowsExceptionWhenNotFound() {
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(999));

        verify(productRepository).findById(999);
    }

    @Test
    void testSaveProductCallsRepositorySaveAndReturnsDto() {
        ProductDto productDto = Optional.of(products.get(0))
                .map(ProductMappingHelper::map)
                .orElseThrow();

        when(productRepository.save(ProductMappingHelper.map(productDto))).thenReturn(products.get(0));

        ProductDto result = productService.save(productDto);

        assertEquals(productDto, result);
        verify(productRepository).save(ProductMappingHelper.map(productDto));
    }

    @Test
    void testUpdateProductCallsRepositorySaveAndReturnsDto() {
        ProductDto productDto = Optional.of(products.get(0))
                .map(ProductMappingHelper::map)
                .orElseThrow();

        // when(productRepository.findById(1)).thenReturn(Optional.of(products.get(0)));
        when(productRepository.save(any(Product.class))).thenReturn(products.get(0));

        ProductDto result = productService.update(productDto);

        assertEquals(productDto, result);
        verify(productRepository).save(ProductMappingHelper.map(productDto));
    }

    @Test
    void testUpdateProductByIDCallsRepositorySaveAndReturnsDto() {
        when(productRepository.findById(1)).thenReturn(Optional.of(products.get(0)));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductDto newProductDto = Optional.of(products.get(0))
                .map(ProductMappingHelper::map)
                .orElseThrow();
        newProductDto.setProductTitle("Updated PC Gamer");
        newProductDto.setSku("updatedgamepc123");

        ProductDto result = productService.update(1, newProductDto);

        assertEquals(newProductDto, result);
        verify(productRepository).save(ProductMappingHelper.map(newProductDto));
    }

    @Test
    void testDeleteByIdCallsRepositoryDelete() {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(products.get(0)));
        productService.deleteById(1);

        verify(productRepository).delete(any(Product.class));
    }
}