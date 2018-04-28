package io.altanalytics.data.external.cryptocompare.client;

import io.altanalytics.data.external.common.HttpConnectionManager;
import io.altanalytics.domain.social.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Ismail on 19/04/2018.
 */
public class CryptoCompareSocialMediaClient {

    @Autowired
    public HttpConnectionManager connectionManager;

    private static final String REST_URL_TEMPLATE = "https://www.cryptocompare.com/api/data/socialstats/?id=";

    private static final String JSON_FIELD_DATA = "Data";
    private static final String JSON_FIELD_DATA_ID = "Id";

    private static final String JSON_FIELD_DATA_GENERAL = "General";
    private static final String JSON_FIELD_DATA_GENERAL_NAME = "Name";
    private static final String JSON_FIELD_DATA_GENERAL_POINTS = "Points";

    private static final String JSON_FIELD_DATA_TWITTER = "Twitter";
    private static final String JSON_FIELD_DATA_TWITTER_FOLLOWERS = "followers";
    private static final String JSON_FIELD_DATA_TWITTER_FOLLOWING = "following";
    private static final String JSON_FIELD_DATA_TWITTER_LISTS = "lists";
    private static final String JSON_FIELD_DATA_TWITTER_FAVOURITES = "favourites";
    private static final String JSON_FIELD_DATA_TWITTER_STATUSES = "statuses";
    private static final String JSON_FIELD_DATA_TWITTER_ACCOUNT_CREATION = "account_creation";
    private static final String JSON_FIELD_DATA_TWITTER_LINK = "link";
    private static final String JSON_FIELD_DATA_TWITTER_POINTS = "Points";

    private static final String JSON_FIELD_DATA_REDDIT = "Reddit";
    private static final String JSON_FIELD_DATA_REDDIT_SUBSCRIBERS = "subscribers";
    private static final String JSON_FIELD_DATA_REDDIT_ACTIVE_USERS = "active_users";
    private static final String JSON_FIELD_DATA_REDDIT_COMMUNITY_CREATION = "community_creation";
    private static final String JSON_FIELD_DATA_REDDIT_POSTS_PER_HOUR = "posts_per_hour";
    private static final String JSON_FIELD_DATA_REDDIT_POSTS_PER_DAY = "posts_per_day";
    private static final String JSON_FIELD_DATA_REDDIT_COMMENTS_PER_HOUR = "comments_per_hour";
    private static final String JSON_FIELD_DATA_REDDIT_COMMENTS_PER_DAY = "comments_per_day";
    private static final String JSON_FIELD_DATA_REDDIT_LINK = "link";
    private static final String JSON_FIELD_DATA_REDDIT_POINTS = "Points";

    private static final String JSON_FIELD_DATA_FACEBOOK = "Facebook";
    private static final String JSON_FIELD_DATA_FACEBOOK_LIKES = "likes";
    private static final String JSON_FIELD_DATA_FACEBOOK_IS_CLOSED = "is_closed";
    private static final String JSON_FIELD_DATA_FACEBOOK_TALKING_ABOUT = "talking_about";
    private static final String JSON_FIELD_DATA_FACEBOOK_LINK = "link";
    private static final String JSON_FIELD_DATA_FACEBOOK_POINTS = "Points";

    private static final String JSON_FIELD_DATA_CODEREPOSITORY = "CodeRepository";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_LIST = "List";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_STARS = "stars";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_LANGUAGE = "language";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_FORKS = "forks";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_OPEN_TOTAL_ISSUES = "open_total_issues";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_SUBSCRIBERS = "subscribers";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_SIZE = "size";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_URL = "url";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_LAST_UPDATE = "last_update";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_LAST_PUSH = "last_push";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_CREATED_AT = "created_at";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_FORK = "fork";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_OPEN_PULL_ISSUES = "open_pull_issues";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_CLOSED_PULL_ISSUES = "closed_pull_issues";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_CLOSED_TOTAL_ISSUES = "closed_total_issues";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_OPEN_ISSUES = "open_issues";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_CLOSED_ISSUES = "closed_issues";
    private static final String JSON_FIELD_DATA_CODEREPOSITORY_POINTS = "Points";


    private static final Logger LOG = Logger.getLogger(CryptoCompareSocialMediaClient.class);

    private int numberOfRetryAttempts = 3;


    public SocialStats fetch(Integer coinId) throws Exception {

        String requestURL = REST_URL_TEMPLATE + coinId;
        HttpGet httpRequest = new HttpGet(requestURL);
        RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH).setExpectContinueEnabled(true).setStaleConnectionCheckEnabled(true).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST)).setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
        RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();
        httpRequest.setConfig(requestConfig);

        LOG.debug("fetching stats for coin: " + coinId);
        String response = null;
        for (int i = 0; i < numberOfRetryAttempts; i++) {
            CloseableHttpClient httpClient;
            CloseableHttpResponse httpResponse = null;
            LOG.debug("attempt number: " + i + " for coin id: " + coinId);
            try {
                httpClient = connectionManager.getHttpConnection();
                httpResponse = httpClient.execute(httpRequest);
                response = read(httpResponse);
                httpResponse.close();
                break;
            } catch (SocketTimeoutException e) {
                LOG.debug("Time out happened for coin with id: " + coinId);
                continue;
            } finally {
                if (httpResponse != null)
                    httpResponse.close();
            }
        }
        SocialStats socialStats = parseResponse(response);
        LOG.debug("fetched stats for coin: " + coinId);
        return socialStats;
    }

    private String read(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + read(response));
        }

        return IOUtils.toString(new InputStreamReader(response.getEntity().getContent()));
    }

    private SocialStats parseResponse(String response) throws IOException, ParseException {

        if (response == null || response.isEmpty()) {
            throw new RuntimeException("Failed : Received empty response");
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
        JSONObject data = (JSONObject) jsonObject.get(JSON_FIELD_DATA);

        GeneralStats generalStats = getGeneralStats(data);
        FacebookStats facebookStats = getFacebookStats(data);
        TwitterStats twitterStats = getTwitterStats(data);
        RedditStats redditStats = getRedditStats(data);
        CodeRepositoryStats codeRepositoryStats = getCodeRepositoryStats(data);
        SocialStats socialStats = new SocialStats(redditStats, generalStats, twitterStats, facebookStats, codeRepositoryStats);
        return socialStats;
    }

    private GeneralStats getGeneralStats(JSONObject data) {
        JSONObject general = (JSONObject) data.get(JSON_FIELD_DATA_GENERAL);
        String generalName = (String) general.get(JSON_FIELD_DATA_GENERAL_NAME);
        long generalPoints = (long) general.get(JSON_FIELD_DATA_GENERAL_POINTS);
        Long timeStampMillis = System.currentTimeMillis();

        return new GeneralStats(generalName, generalPoints, timeStampMillis);
    }

    private FacebookStats getFacebookStats(JSONObject data) {
        JSONObject facebook = (JSONObject) data.get(JSON_FIELD_DATA_FACEBOOK);
        long facebookPoints = (long) facebook.get(JSON_FIELD_DATA_FACEBOOK_POINTS);

        //Not all coins will have a facebook page, return null if they don't.
        if (facebook.isEmpty() || facebook.size() <= 1 || facebookPoints == 0) {
            return null;
        }
        long facebookLikes = (long) facebook.get(JSON_FIELD_DATA_FACEBOOK_LIKES);
        String facebookIsClosed = (String) facebook.get(JSON_FIELD_DATA_FACEBOOK_IS_CLOSED);
        String facebookTalkingAbout = (String) facebook.get(JSON_FIELD_DATA_FACEBOOK_TALKING_ABOUT);
        String facebookLink = (String) facebook.get(JSON_FIELD_DATA_FACEBOOK_LINK);

        long facebookTalkingAboutLong = NumberUtils.isParsable(facebookTalkingAbout) ? Long.parseLong(facebookTalkingAbout) : 0;
        Boolean facebookIsClosedBool = BooleanUtils.toBooleanObject(facebookIsClosed);

        return new FacebookStats(facebookLikes, facebookIsClosedBool, facebookTalkingAboutLong, facebookLink, facebookPoints);
    }

    private TwitterStats getTwitterStats(JSONObject data) {
        JSONObject twitter = (JSONObject) data.get(JSON_FIELD_DATA_TWITTER);
        long twitterPoints = (long) twitter.get(JSON_FIELD_DATA_TWITTER_POINTS);

        //Not all coins will have a twitter page, return null if they don't.
        if (twitter.isEmpty() || twitter.size() <= 1 || twitterPoints==0) {
            return null;
        }

        long twitterFollowers = (long) twitter.get(JSON_FIELD_DATA_TWITTER_FOLLOWERS);
        String twitterFollowing = (String) twitter.get(JSON_FIELD_DATA_TWITTER_FOLLOWING);
        long twitterLists = (long) twitter.get(JSON_FIELD_DATA_TWITTER_LISTS);
        String twitterFavourites = (String) twitter.get(JSON_FIELD_DATA_TWITTER_FAVOURITES);
        long twitterStatuses = (long) twitter.get(JSON_FIELD_DATA_TWITTER_STATUSES);
        String twitterLink = (String) twitter.get(JSON_FIELD_DATA_TWITTER_LINK);

        String twitterAccountCreation = (String) twitter.get(JSON_FIELD_DATA_TWITTER_ACCOUNT_CREATION);

        Date twitterAccountCreateionDate = NumberUtils.isParsable(twitterAccountCreation) ? new Date(Long.parseLong(twitterAccountCreation)) : null;
        long twitterFollowingLong = NumberUtils.isParsable(twitterFollowing) ? Long.parseLong(twitterFollowing) : 0;
        long twitterFavouritesLong = NumberUtils.isParsable(twitterFavourites) ? Long.parseLong(twitterFavourites) : 0;

        return new TwitterStats(twitterFollowers, twitterFollowingLong, twitterLists, twitterFavouritesLong, twitterStatuses, twitterAccountCreateionDate, twitterPoints, twitterLink);
    }

    private RedditStats getRedditStats(JSONObject data) {
        JSONObject reddit = (JSONObject) data.get(JSON_FIELD_DATA_REDDIT);
        long redditPoints = (long) reddit.get(JSON_FIELD_DATA_REDDIT_POINTS);

        //Not all coins will have a reddit page, return null if they don't.
        if (reddit.isEmpty() || reddit.size() <= 1 || redditPoints == 0) {
            return null;
        }

        long redditSubscribers = (long) reddit.get(JSON_FIELD_DATA_REDDIT_SUBSCRIBERS);
        long redditActiveUsers = (long) reddit.get(JSON_FIELD_DATA_REDDIT_ACTIVE_USERS);
        String redditCommunityCreation = (String) reddit.get(JSON_FIELD_DATA_REDDIT_COMMUNITY_CREATION);
        String redditPostsPerHour = (String) reddit.get(JSON_FIELD_DATA_REDDIT_POSTS_PER_HOUR);
        String redditPostsPerDay = (String) reddit.get(JSON_FIELD_DATA_REDDIT_POSTS_PER_DAY);
        String redditCommentsPerHour = (String) reddit.get(JSON_FIELD_DATA_REDDIT_COMMENTS_PER_HOUR);
        double redditCommentsPerDay = reddit.containsKey(JSON_FIELD_DATA_REDDIT_COMMENTS_PER_DAY) ? (double) reddit.get(JSON_FIELD_DATA_REDDIT_COMMENTS_PER_DAY) : 0;
        String redditLink = (String) reddit.get(JSON_FIELD_DATA_REDDIT_LINK);

        Date redditCommunityCreationDate = redditCommunityCreation.equals("undefined") ? null : new Date(Long.parseLong(redditCommunityCreation));
        double redditPostsPerHourDouble = redditPostsPerHour == null ? 0 : Double.parseDouble(redditPostsPerHour);
        double redditPostsPerDayDouble = redditPostsPerDay == null ? 0 : Double.parseDouble(redditPostsPerDay);
        double redditCommentsPerHourDouble = redditCommentsPerHour == null ? 0 : Double.parseDouble(redditCommentsPerHour);

        return new RedditStats(redditSubscribers, redditActiveUsers, redditCommunityCreationDate, redditPostsPerHourDouble, redditPostsPerDayDouble, redditCommentsPerHourDouble, redditCommentsPerDay, redditLink, redditPoints);
    }

    private CodeRepositoryStats getCodeRepositoryStats(JSONObject data) {
        JSONObject codeRepository = (JSONObject) data.get(JSON_FIELD_DATA_CODEREPOSITORY);
        JSONArray codeRepositoryList = (JSONArray) codeRepository.get(JSON_FIELD_DATA_CODEREPOSITORY_LIST);
        long codeRespositoryPoints = (long) codeRepository.get(JSON_FIELD_DATA_CODEREPOSITORY_POINTS);

        if (codeRepositoryList.size() == 0) {
            return null;
        }

        List<CodeRespositoryProjectStats> codeRespositoryProjectStatsList = new ArrayList<>();

        for (Object codeRepoChild : codeRepositoryList) {
            JSONObject coinRepo = (JSONObject) codeRepoChild;
            long codeRespositoryStars = (long) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_STARS);
            String codeRespositoryLanguage = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_LANGUAGE);
            long codeRespositoryForks = (long) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_FORKS);
            long codeRespositorySubscribers = (long) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_SUBSCRIBERS);
            String codeRespositorySize = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_SIZE);
            String codeRespositoryUrl = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_URL);

            String codeRespositoryLastUpdate = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_LAST_UPDATE);
            String codeRespositoryLastPush = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_LAST_PUSH);
            String codeRespositoryCreatedAt = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_CREATED_AT);

            String codeRespositoryFork = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_FORK);

            String codeRespositoryOpenIssues = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_OPEN_ISSUES);
            String codeRespositoryOpenPullIssues = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_OPEN_PULL_ISSUES);
            String codeRespositoryOpenTotalIssues = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_OPEN_TOTAL_ISSUES);

            String codeRespositoryClosedIssues = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_CLOSED_ISSUES);
            String codeRespositoryClosedPullIssues = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_CLOSED_PULL_ISSUES);
            String codeRespositoryClosedTotalIssues = (String) coinRepo.get(JSON_FIELD_DATA_CODEREPOSITORY_CLOSED_TOTAL_ISSUES);

            long codeRespositorySizeLong = NumberUtils.isParsable(codeRespositorySize) ? Long.parseLong(codeRespositorySize) : 0;

            long codeRespositoryOpenIssuesLong = NumberUtils.isParsable(codeRespositoryOpenIssues) ? Long.parseLong(codeRespositoryOpenIssues) : 0;
            long codeRespositoryOpenPullIssuesLong = NumberUtils.isParsable(codeRespositoryOpenPullIssues) ? Long.parseLong(codeRespositoryOpenPullIssues) : 0;
            long codeRespositoryOpenTotalIssuesLong = NumberUtils.isParsable(codeRespositoryOpenTotalIssues) ? Long.parseLong(codeRespositoryOpenTotalIssues) : 0;

            long codeRespositoryClosedIssuesLong = NumberUtils.isParsable(codeRespositoryClosedIssues) ? Long.parseLong(codeRespositoryClosedIssues) : 0;
            long codeRespositoryClosedPullIssuesLong = NumberUtils.isParsable(codeRespositoryClosedPullIssues) ? Long.parseLong(codeRespositoryClosedPullIssues) : 0;
            long codeRespositoryClosedTotalIssuesLong = NumberUtils.isParsable(codeRespositoryClosedTotalIssues) ? Long.parseLong(codeRespositoryClosedTotalIssues) : 0;


            Date codeRespositoryLastUpdateDate = NumberUtils.isParsable(codeRespositoryLastUpdate) ? new Date(Long.parseLong(codeRespositoryLastUpdate)) : null;
            Date codeRespositoryLastPushDate = NumberUtils.isParsable(codeRespositoryLastPush) ? new Date(Long.parseLong(codeRespositoryLastPush)) : null;
            Date codeRespositoryCreatedAtDate = NumberUtils.isParsable(codeRespositoryCreatedAt) ? new Date(Long.parseLong(codeRespositoryCreatedAt)) : null;

            boolean isCodeRespositoryFork = Boolean.parseBoolean(codeRespositoryFork);


            CodeRespositoryProjectStats codeRespositoryProjectStats = new CodeRespositoryProjectStats(codeRespositorySizeLong, codeRespositoryUrl, codeRespositoryStars, codeRespositoryForks, isCodeRespositoryFork, codeRespositoryLanguage, codeRespositorySubscribers, codeRespositoryLastPushDate, codeRespositoryLastUpdateDate, codeRespositoryCreatedAtDate, codeRespositoryOpenIssuesLong, codeRespositoryOpenPullIssuesLong, codeRespositoryOpenTotalIssuesLong, codeRespositoryClosedIssuesLong, codeRespositoryClosedPullIssuesLong, codeRespositoryClosedTotalIssuesLong);

            codeRespositoryProjectStatsList.add(codeRespositoryProjectStats);
        }

        return new CodeRepositoryStats(codeRespositoryProjectStatsList, codeRespositoryPoints);
    }
}
