package ua.garmash.internetshop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ua.garmash.internetshop.model.User;
import ua.garmash.internetshop.service.UserService;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.not_empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.not_empty");

        final int minUsernameLength = 3;
        final int maxUsernameLength = 32;
        if (user.getUsername().length() < minUsernameLength) {
            errors.rejectValue("username", "register.error.username.less_3");
        }
        if (user.getUsername().length() > maxUsernameLength) {
            errors.rejectValue("username", "register.error.username.over_32");
        }
        //Username can't be duplicated
        if (userService.findByName(user.getUsername()) != null) {
            errors.rejectValue("username", "register.error.duplicated.username");
        }
        final int minPasswordLength = 8;
        final int maxPasswordLength = 32;
        if (user.getPassword().length() < minPasswordLength) {
            errors.rejectValue("password", "register.error.password.less_8");
        }
        if (user.getPassword().length() > maxPasswordLength) {
            errors.rejectValue("password", "register.error.password.over_32");
        }
        final int minimalAge = 18;
        if (user.getAge() <= minimalAge) {
            errors.rejectValue("age", "register.error.age_size");
        }
    }
}
