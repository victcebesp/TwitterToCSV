package twitter;

import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static twitter.TweetType.*;

public class TweetsToCSVConverter {

    private final TwitterRetriever twitterRetriever;

    public TweetsToCSVConverter() {
        twitterRetriever = new TwitterRetriever(new TwitterLogger());
    }

    public static void main(String[] args) throws TwitterException, IOException {
        final TweetsToCSVConverter tweetsToCSVConverter = new TweetsToCSVConverter();
        String themes = "#Serverless #Kubernetes #Apple #Google #Microsoft";
        for (String theme : themes.split(" "))
            tweetsToCSVConverter.convertToCSV("tweets.csv", theme);
    }

    public void convertToCSV(String path, String hashtag) throws TwitterException, IOException {

        List<Status> tweets = twitterRetriever.retrieveTweets(hashtag, 200);

        String header = "ID,TimeStamp,CountryCode,TweetType,Size,RetweetCount,LikeCount";

        List<String> tweetsAsStringList = tweets.stream()
                .filter(s -> s.getPlace() != null)
                .map(this::tweetToString)
                .collect(Collectors.toList());

        if (Files.exists(Paths.get(path))) Files.write(Paths.get(path), tweetsAsStringList, StandardOpenOption.APPEND);
        else {
            List<String> output = new ArrayList<>();
            output.add(header);
            output.addAll(tweetsAsStringList);
            Files.write(Paths.get(path), output);
        }


    }

    private String tweetToString(Status tweet) {
        StringBuilder tweetString = new StringBuilder();
        Date createdAt = tweet.getCreatedAt();
        String countryCode = tweet.getPlace().getCountryCode();
        TweetType tweetType = getTweetType(tweet);
        int tweetSize = tweet.getText().length();
        int retweetCount = tweet.getRetweetCount();
        int favouriteCount = tweet.getFavoriteCount();
        long id = tweet.getId();

        tweetString.append(id).append(",")
                .append(createdAt).append(",")
                .append(countryCode).append(",")
                .append(tweetType).append(",")
                .append(tweetSize).append(",")
                .append(retweetCount).append(",")
                .append(favouriteCount);
        return tweetString.toString();
    }

    private TweetType getTweetType(Status tweet) {
        if (tweet.isRetweet()) return RETWEET;
        if (tweet.getInReplyToStatusId() != -1L) return REPLY;
        return ORIGINAL;
    }

}
