package twitter;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.List;
import java.util.stream.Collectors;

public class TwitterRetriever {

    private final TwitterLogger twitterLogger;

    public TwitterRetriever(TwitterLogger tweeterLogger) {
        this.twitterLogger = tweeterLogger;
    }

    public List<Status> retrieveTweets(String searchTerm, int quantity) throws TwitterException {
        
        Query query = new Query(searchTerm).count(100);
        
        List<Status> tweets = twitterLogger.login()
                                           .twitter()
                                           .search(query)
                                           .getTweets();
        
        while(tweets.size() < quantity) {
            query.setMaxId(getMaxId(tweets));
            tweets.addAll(twitterLogger.login().twitter().search(query).getTweets());
        }

        return tweets.subList(0, quantity);
    }

    private static Long getMaxId(List<Status> tweets) {
        List<Long> collection = tweets.stream()
                .map(Status::getId)
                .sorted()
                .collect(Collectors.toList());

        return collection.get(0);
    }

    public Status retrieveTweetById(long tweetID) {
        Status tweet = null;
        try {
            tweet = twitterLogger.login()
                                .twitter()
                                .showStatus(tweetID);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return tweet;
    }
}
