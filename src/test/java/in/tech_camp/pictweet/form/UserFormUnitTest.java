package in.tech_camp.pictweet.form;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;

import in.tech_camp.pictweet.factory.UserFormFactory;
import in.tech_camp.pictweet.factory.TweetFormFactory;
import in.tech_camp.pictweet.validation.ValidationPriority1;
import in.tech_camp.pictweet.validation.ValidationPriority2;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import in.tech_camp.pictweet.form.TweetForm;

@ActiveProfiles("test")
public class UserFormUnitTest {
    private UserForm userForm;
    private TweetForm tweetForm;
    private Validator validator;
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        userForm = UserFormFactory.createUser();
        tweetForm = TweetFormFactory.createTweet();
        bindingResult = mock(BindingResult.class);
    }

    @Nested
    class ユーザー作成ができる場合 {
        @Test
        public void nicknameとemailとpasswordとpasswordConfirmationが存在すれば登録できる () {
            Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
            assertEquals(0, violations.size());
        }

        @Test
        public void テキストと画像が存在していれば投稿できる () {
            // テキストと画像の両方が設定されているTweetFormを作成
            Set<ConstraintViolation<TweetForm>> violations = validator.validate(tweetForm, ValidationPriority1.class);
            assertEquals(0, violations.size());
        }

        @Test
        public void テキストが存在し画像が空でも投稿できる() {
            // テキストは存在するが、画像が空の場合でも投稿できることを確認
            tweetForm.setImage("");
        
            Set<ConstraintViolation<TweetForm>> violations = validator.validate(tweetForm, ValidationPriority1.class);
            assertEquals(0, violations.size());
        }
    }

    @Nested
    class ユーザー作成ができない場合 {
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

        @Test
        public void passwordが空の場合バリデーションエラーが発生する() {
            // passwordを空文字に設定
            userForm.setPassword("");
            
            // ValidationPriority1グループでバリデーションを実行
            Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
            
            // バリデーションエラーが1つ発生することを確認
            assertEquals(1, violations.size());
            
            // エラーメッセージが正しいことを確認
            assertEquals("Password can't be blank", violations.iterator().next().getMessage());
        }

        @Test
        public void passwordとpasswordConfirmationが不一致ではバリデーションエラーが発生する() {
            userForm.setPasswordConfirmation("differentPassword");
            userForm.validatePasswordConfirmation(bindingResult);
            verify(bindingResult).rejectValue("passwordConfirmation", "error.user", "Password confirmation doesn't match Password");
        }

        @Test
        public void nicknameが7文字以上ではバリデーションエラーが発生する() {
            // nicknameを7文字に設定（制限の6文字を超える）
            userForm.setNickname("1234567");
            
            // ValidationPriority2グループでバリデーションを実行
            Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority2.class);
            
            // バリデーションエラーが1つ発生することを確認
            assertEquals(1, violations.size());
            
            // エラーメッセージが正しいことを確認
            assertEquals("Nickname is too long (maximum is 6 characters)", violations.iterator().next().getMessage());
        }

        @Test
        public void emailはアットマークを含まないとバリデーションエラーが発生する() {
            // emailをアットマークを含まない無効な形式に設定
            userForm.setEmail("invalidemail.com");
            
            // ValidationPriority2グループでバリデーションを実行
            Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority2.class);
            
            // バリデーションエラーが1つ発生することを確認
            assertEquals(1, violations.size());
            
            // エラーメッセージが正しいことを確認
            assertEquals("Email should be valid", violations.iterator().next().getMessage());
        }

        @Test
        public void passwordが5文字以下ではバリデーションエラーが発生する() {
            // passwordを5文字に設定（最小制限の6文字を下回る）
            String password = "a".repeat(5);
            userForm.setPassword(password); // 短すぎるパスワード
            
            // ValidationPriority2グループでバリデーションを実行
            Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority2.class);
            
            // バリデーションエラーが1つ発生することを確認
            assertEquals(1, violations.size());
            
            // エラーメッセージが正しいことを確認
            assertEquals("Password should be between 6 and 128 characters", violations.iterator().next().getMessage());
        }

        @Test
        public void passwordが129文字以上ではバリデーションエラーが発生する() {
            // passwordを129文字に設定（最大制限の128文字を超える）
            String longPassword = "a".repeat(129);
            userForm.setPassword(longPassword); // 長すぎるパスワード
            
            // ValidationPriority2グループでバリデーションを実行
            Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority2.class);
            
            // バリデーションエラーが1つ発生することを確認
            assertEquals(1, violations.size());
            
            // エラーメッセージが正しいことを確認
            assertEquals("Password should be between 6 and 128 characters", violations.iterator().next().getMessage());
        }

        @Test
        public void テキストが空では投稿できない() {
            // テキストを空にして、バリデーションエラーが発生することを確認
            tweetForm.setText("");
        
            Set<ConstraintViolation<TweetForm>> violations = validator.validate(tweetForm, ValidationPriority1.class);
            assertEquals(1, violations.size());
            assertEquals("Text can't be blank", violations.iterator().next().getMessage());
        }
    }
}