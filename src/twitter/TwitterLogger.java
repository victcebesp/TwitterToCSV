package twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterLogger {

    private Twitter twitter;

    public TwitterLogger login() {
        ConfigurationBuilder cb = new ConfigurationBuilder().setJSONStoreEnabled(true);
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .setOAuthAccessToken("")
                .setOAuthAccessTokenSecret("");
        TwitterFactory tf = new TwitterFactory(cb.build());
        this.twitter = tf.getInstance();
        return this;
    }

    public Twitter twitter() {
        return twitter;
    }
}
