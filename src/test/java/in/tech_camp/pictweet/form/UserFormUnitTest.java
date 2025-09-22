package in.tech_camp.pictweet.form;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import in.tech_camp.pictweet.factory.UserFormFactory;
import in.tech_camp.pictweet.validation.ValidationPriority1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
public class UserFormUnitTest {
    private UserForm userForm;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        userForm = UserFormFactory.createUser();
    }

    @Test
    public void nicknameが空の場合バリデーションエラーが発生する() {
        userForm.setNickname("");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Nickname can't be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void emailが空の場合バリデーションエラーが発生する() {
        userForm.setEmail("");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Email can't be blank", violations.iterator().next().getMessage());
    }
}