package in.tech_camp.pictweet.form;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import in.tech_camp.pictweet.factory.TweetFormFactory;
import in.tech_camp.pictweet.validation.ValidationPriority1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


@ActiveProfiles("test")
public class TweetFormUnitTest {

    private Validator validator;

    private TweetForm tweetForm;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        tweetForm = TweetFormFactory.createTweet();
    }
    @Nested
    class ツイート投稿ができる場合 {
        @Test
        public void テキストと画像が存在していれば投稿できる () {
          // テキストと画像の両方が設定されているTweetFormを作成
          Set<ConstraintViolation<TweetForm>> violations = validator.validate(tweetForm, ValidationPriority1.class);
          assertEquals(0, violations.size());
        }

        @Test
        public void テキストのみで投稿できる() {
            // 画像を空にして、テキストのみでツイート投稿できることを確認
            tweetForm.setImage("");

            Set<ConstraintViolation<TweetForm>> violations = validator.validate(tweetForm, ValidationPriority1.class);
            assertEquals(0, violations.size());
        }

        @Test
        public void テキストが存在し画像が空でも投稿できる() {
            // テキストは存在するが、画像が空の場合でも投稿できることを確認
            tweetForm.setText("テストメッセージ");
            tweetForm.setImage(null);

            Set<ConstraintViolation<TweetForm>> violations = validator.validate(tweetForm, ValidationPriority1.class);
            assertEquals(0, violations.size());
        }
    }
    @Nested
    class ツイート投稿ができない場合 {
        @Test
        public void テキストが空では投稿できない() {
            // テキストを空にして、バリデーションエラーが発生することを確認
            tweetForm.setText("");

            Set<ConstraintViolation<TweetForm>> violations = validator.validate(tweetForm, ValidationPriority1.class);
            assertEquals(1, violations.size());
            assertEquals("Text can't be blank", violations.iterator().next().getMessage());
        }

        @Test
        public void テキストがnullでは投稿できない() {
            // テキストをnullにして、バリデーションエラーが発生することを確認
            tweetForm.setText(null);

            Set<ConstraintViolation<TweetForm>> violations = validator.validate(tweetForm, ValidationPriority1.class);
            assertEquals(1, violations.size());
            assertEquals("Text can't be blank", violations.iterator().next().getMessage());
        }

        @Test
        public void テキストが空白文字のみでは投稿できない() {
            // テキストを空白文字のみにして、バリデーションエラーが発生することを確認
            tweetForm.setText("   ");

            Set<ConstraintViolation<TweetForm>> violations = validator.validate(tweetForm, ValidationPriority1.class);
            assertEquals(1, violations.size());
            assertEquals("Text can't be blank", violations.iterator().next().getMessage());
        }
    }
}