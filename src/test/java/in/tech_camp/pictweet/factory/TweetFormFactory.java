package in.tech_camp.pictweet.factory;

import com.github.javafaker.Faker;

import in.tech_camp.pictweet.form.TweetForm;

public class TweetFormFactory {
  private static final Faker faker = new Faker();

  /**
   * テキストと画像の両方を含むTweetFormを作成
   */
  public static TweetForm createTweet() {
    TweetForm tweetForm = new TweetForm();
    tweetForm.setText(faker.lorem().sentence(10));
    tweetForm.setImage("sample_image_" + faker.number().digits(3) + ".jpg");
    return tweetForm;
  }

  /**
   * テキストのみのTweetFormを作成
   */
  public static TweetForm createTweetWithTextOnly() {
    TweetForm tweetForm = new TweetForm();
    tweetForm.setText(faker.lorem().sentence(10));
    tweetForm.setImage(null);
    return tweetForm;
  }

  /**
   * 画像のみのTweetFormを作成（テキストは空）
   */
  public static TweetForm createTweetWithImageOnly() {
    TweetForm tweetForm = new TweetForm();
    tweetForm.setText("");
    tweetForm.setImage("sample_image_" + faker.number().digits(3) + ".jpg");
    return tweetForm;
  }
}