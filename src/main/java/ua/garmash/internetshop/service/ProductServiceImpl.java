package ua.garmash.internetshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.garmash.internetshop.dao.ProductRepository;
import ua.garmash.internetshop.dto.ProductDto;
import ua.garmash.internetshop.mapper.ProductMapper;
import ua.garmash.internetshop.model.Bucket;
import ua.garmash.internetshop.model.Product;
import ua.garmash.internetshop.model.User;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper = ProductMapper.MAPPER;

    private final ProductRepository productRepository;
    private final UserService userService;
    private final BucketService bucketService;
    private final CartService cartService;

    private static Page page;


    public ProductServiceImpl(ProductRepository productRepository,
                              UserService userService,
                              BucketService bucketService,
                              CartService cartService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.bucketService = bucketService;
        this.cartService = cartService;

    }

    @Override
    public List<ProductDto> getAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        page = productPage;
        List<Product> listOfProducts = productPage.getContent();
        return mapper.fromProductList(listOfProducts);
//        return mapper.fromProductList(productRepository.findAll(pageable));
    }

    public Page getPage(){
        return page;
    }

/*    public List<ProductDto> getFullTextSearch(String searchString) {
        return mapper.fromProductList(productRepository.findFullTextSearch(searchString));
    }*/

    @Override
    public List<ProductDto> getByKeyword(String searchString) {
        return mapper.fromProductList(productRepository.findByKeyword(searchString.toUpperCase()));
    }

    @Override
    public void addToUserCart(Long productId) {
        cartService.addToCart(productId);
    }

    @Override
    @Transactional
    public void addToUserBucket(Long productId, String username) {
        User user = userService.findByName(username);
        if (user == null) {
            throw new RuntimeException("User not found. " + username);
        }

        Bucket bucket = user.getBucket();
        if (bucket == null) {
            Bucket newBucket = bucketService.createBucket(user, productId);
            user.setBucket(newBucket);
            userService.save(user);
        } else {
//            bucketService.addProducts(bucket, Collections.singletonList(productId));
            bucketService.addItem(bucket, productId);
        }
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

    public List<ProductDto> findAllByCategoryId(Long categoryId) {
        List<Product> productList = productRepository.findAllByCategoryId(categoryId);
        return mapper.fromProductList(productList);
    }

    @Override
    @Transactional
    public void save(Long productId, ProductDto productDto) {
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
