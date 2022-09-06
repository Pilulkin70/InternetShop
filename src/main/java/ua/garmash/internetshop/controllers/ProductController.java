package ua.garmash.internetshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public String list(Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        model.addAttribute("products", productService.getAll(pageable));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("page", productService.getPage().getNumber());
        List<String> pageNumbers = IntStream.range(0, productService.getPage().getTotalPages())
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        model.addAttribute("pages", pageNumbers);
        return "products";
    }

    @GetMapping("/{id}/cart")
    public String addBucket(@PathVariable Long id, Principal principal, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        productService.addToUserCart(id);
        return "redirect:" + referer;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/products";
        }
        productService.delProductById(id);
        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/invert-available")
    public String invertProductAvailability(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/products";
        }
        productService.invertAvailabilityById(id);
        return "redirect:/products";
    }

    @PostMapping
    public String addProduct(ProductDto dto) {
        dto.setAvailable(true);
        productService.addProduct(dto);
        return "redirect:/products";
    }


    @GetMapping("/{id}")
    @ResponseBody
    public ProductDto getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @RequestMapping("/filterByCategory")
    public String filterCategory(@RequestParam("categoryId") long categoryId, Model model) {
        model.addAttribute("products", productService.findAllByCategoryId(categoryId));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("page", 0);
        model.addAttribute("pages", new ArrayList<String>(1));
        return "products";
    }

    @RequestMapping("/filterByBrand")
    public String filterBrand(@RequestParam("brandId") long brandId, Model model) {
        model.addAttribute("products", productService.findAllByBrandId(brandId));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("page", 0);
        model.addAttribute("pages", new ArrayList<String>(1));
        return "products";
    }

    @RequestMapping(path = {"/search"})
    public String search(Model model, String keyword,
                         @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        if (keyword == null) {
            model.addAttribute("products", productService.getAll(pageable));
        } else {
            model.addAttribute("products", productService.getByKeyword(keyword));
        }
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("page", 0);
        model.addAttribute("pages", new ArrayList<String>(1));
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
        if (bindingResult.hasErrors()) {
            logger.error(String.valueOf(bindingResult.getFieldError()));
            model.addAttribute("method", "new");
            return "product";
        }
        productService.addProduct(productForm);
        logger.debug(String.format("Product with id: %s successfully created.", productForm.getId()));

        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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
            throw new RuntimeException("Product Id exception.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/{id}/edit", params = "submit")
    public String editProduct(@PathVariable("id") long productId, @ModelAttribute("productForm") ProductDto productForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.error(String.valueOf(bindingResult.getFieldError()));
            model.addAttribute("method", "edit");
            return "product";
        }
        productService.updateById(productId, productForm);
        logger.debug(String.format("Product with id: %s has been successfully edited.", productId));

        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/{id}/edit", params = "cancel")
    public String cancel(@PathVariable("id") long productId) {
        logger.debug(String.format("Product with id: %s. Edit canceled.", productId));
        return "redirect:/products";
    }
}
