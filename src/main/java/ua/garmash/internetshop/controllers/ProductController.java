package ua.garmash.internetshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.garmash.internetshop.dto.ProductDto;
import ua.garmash.internetshop.service.BrandService;
import ua.garmash.internetshop.service.CategoryService;
import ua.garmash.internetshop.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Controller
@RequestMapping("/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public ProductController(ProductService productService, CategoryService categoryService, BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAll());
        model.addAttribute("categories", categoryService.findAll());
        return "products";
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        productService.addToUserCart(id);
        if (principal != null) {
            productService.addToUserBucket(id, principal.getName());
        }
//		return "redirect:/products";
        return "redirect:" + referer;
    }

    @GetMapping("/{id}/delete")
    public String delProduct(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/products";
        }
        productService.delProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/{id}/invert-available")
    public String invertAvailabilityProduct(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/products";
        }
        productService.invertAvailabilityById(id);
        return "redirect:/products";
    }

    @PostMapping
//	public ResponseEntity<Void> addProduct(ProductDto dto){
    public String addProduct(ProductDto dto) {
        dto.setAvailable(true);
        productService.addProduct(dto);
//		return ResponseEntity.status(HttpStatus.CREATED).build();
        return "redirect:/products";
    }


    @GetMapping("/{id}")
    @ResponseBody
    public ProductDto getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @RequestMapping("/filterByCategory")
    public String homePost(@RequestParam("categoryId") long categoryId, Model model) {
        model.addAttribute("products", productService.findAllByCategoryId(categoryId));
        model.addAttribute("categories", categoryService.findAll());
        return "products";
    }

    @RequestMapping(path = {"/search"})
    public String search(Model model, String keyword) {
        if (keyword == null) {
            model.addAttribute("products", productService.getAll());
        } else {
            model.addAttribute("products", productService.getByKeyword(keyword));
        }
        model.addAttribute("categories", categoryService.findAll());
        return "products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("productForm", new ProductDto());
        model.addAttribute("method", "new");
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        return "product";
    }

    @PostMapping("/new")
    public String newProduct(@ModelAttribute("productForm") ProductDto productForm, BindingResult bindingResult, Model model) {
//		productValidator.validate(productForm, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.error(String.valueOf(bindingResult.getFieldError()));
            model.addAttribute("method", "new");
            return "product";
        }
        productService.addProduct(productForm);
        logger.debug(String.format("Product with id: %s successfully created.", productForm.getId()));

        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editProduct(@PathVariable("id") long productId, Model model) {
        ProductDto productDto = productService.getById(productId);
        if (productDto != null) {
            model.addAttribute("productForm", productDto);
            model.addAttribute("method", "edit");
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("brands", brandService.findAll());
            return "product";
        } else {
            return "error/404";
        }
    }

    @PostMapping(value = "/{id}/edit", params = "submit")
    public String editProduct(@PathVariable("id") long productId, @ModelAttribute("productForm") ProductDto productForm, BindingResult bindingResult, Model model) {
//		productValidator.validate(productForm, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.error(String.valueOf(bindingResult.getFieldError()));
            model.addAttribute("method", "edit");
            return "product";
        }
        productService.save(productId, productForm);
        logger.debug(String.format("Product with id: %s has been successfully edited.", productId));

        return "redirect:/products";
    }

    @PostMapping(value = "/{id}/edit", params = "cancel")
    public String cancelProduct(@PathVariable("id") long productId){
        logger.debug(String.format("Product with id: %s. Edit canceled.", productId));
        return "redirect:/products";
    }
}
