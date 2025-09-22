package in.tech_camp.pictweet.form;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import in.tech_camp.pictweet.validation.ValidationPriority1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
public class UserFormUnitTest {
    @Test
    public void nicknameが空の場合バリデーションエラーが発生する() {
        // UserFormのインスタンスを初期化
        UserForm userForm = new UserForm();
        userForm.setNickname(""); // 空のニックネーム
        userForm.setEmail("test@test.com"); // メールアドレス
        userForm.setPassword("techcamp123"); // パスワード
        userForm.setPasswordConfirmation("techcamp123");// 確認用パスワード
        // バリデーションの実行
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
        // アサーション
        assertEquals(1, violations.size());
        assertEquals("Nickname can't be blank", violations.iterator().next().getMessage());
    }
}