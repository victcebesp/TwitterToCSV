package twitter;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TwitterRetriever {

    private final TwitterLogger twitterLogger;

    public TwitterRetriever(TwitterLogger tweeterLogger) {
        this.twitterLogger = tweeterLogger;
    }

    public List<Status> retrieveTweets(String searchTerm, int quantity) {
        
        Query query = new Query(searchTerm).count(200);

        List<Status> tweets = new ArrayList<>();
        try {
            tweets = twitterLogger.login()
                                  .twitter()
                                  .search(query)
                                  .getTweets();

            while(tweets.size() < quantity) {
                query.setMaxId(getMaxId(tweets));
                tweets.addAll(twitterLogger.login()
                        .twitter()
                        .search(query)
                        .getTweets());
            }
        } catch (TwitterException e) {
            System.out.println(tweets.size());
            return tweets;
        }

        System.out.println(tweets.size());
        return tweets;
    }

    private static Long getMaxId(List<Status> tweets) {
        List<Long> collection = tweets.stream()
                .map(Status::getId)
                .sorted()
                .collect(Collectors.toList());

        return collection.get(0);
    }

}
