package ua.garmash.internetshop.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ua.garmash.internetshop.model.Product;

@Component
public class ProductValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Product product = (Product) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "error.not_empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.not_empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "error.not_empty");

        final int minTitleLength = 2;
        final int maxTitleLength = 32;
        if (product.getTitle().length() < minTitleLength) {
            errors.rejectValue("title", "product.error.name.less_2");
        }
        if (product.getTitle().length() > maxTitleLength) {
            errors.rejectValue("title", "product.error.name.over_32");
        }
    }
}
