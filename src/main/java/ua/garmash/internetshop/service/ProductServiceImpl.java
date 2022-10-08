package ua.garmash.internetshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.garmash.internetshop.repository.ProductRepository;
import ua.garmash.internetshop.dto.ProductDto;
import ua.garmash.internetshop.mapper.ProductMapper;
import ua.garmash.internetshop.model.Product;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;
    private final BasketService basketService;
    private final CartService cartService;

    private static Page page;

    public ProductServiceImpl(ProductRepository productRepository,
                              BasketService basketService,
                              CartService cartService) {
        this.productRepository = productRepository;
        this.basketService = basketService;
        this.cartService = cartService;
    }

    @Override
    public List<ProductDto> getAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        page = productPage;
        List<Product> listOfProducts = productPage.getContent();
        return mapper.fromProductList(listOfProducts);
    }

    @Override
    public Page getPage(){
        return page;
    }

    @Override
    public List<ProductDto> getByKeyword(String searchString) {
        return mapper.fromProductList(productRepository.findByKeyword(searchString.toUpperCase()));
    }

    @Override
    public void addToUserCart(Long productId) {
        cartService.addProductToCart(productId);
    }

    @Override
    @Transactional
    public void addProduct(ProductDto dto) {
        Product product = mapper.toProduct(dto);
        product.setAvailable(false);
        Product savedProduct = productRepository.save(product);
    }

    @Override
    @Transactional
    public void delProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.findById(id).orElse(new Product());
        return ProductMapper.MAPPER.fromProduct(product);
    }

    @Override
    @Transactional
    public void invertAvailabilityById(Long id) {
        if (productRepository.findById(id).isPresent()) {
            Product product = productRepository.findById(id).get();
            product.setAvailable(!product.getAvailable());
        }
    }

    @Override
    public List<ProductDto> findAllByCategoryId(Long categoryId) {
        List<Product> productList = productRepository.findAllByCategoryId(categoryId);
        return mapper.fromProductList(productList);
    }
    @Override
    public List<ProductDto> findAllByBrandId(Long brandId){
        List<Product> productList = productRepository.findAllByBrandId(brandId);
        return mapper.fromProductList(productList);
    }

    @Override
    @Transactional
    public void updateById(Long productId, ProductDto productDto) {
        Product product = productRepository.findById(productId).get();
        if (product == null) {
            throw new RuntimeException("Product not found. ID=" + productId);
        }
        final boolean availability = product.getAvailable();
        product = mapper.toProduct(productDto);
        product.setAvailable(availability);
        productRepository.save(product);
    }
}
