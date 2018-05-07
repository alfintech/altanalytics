package io.altanalytics.domain.social;

/**
 * Created by Ismail on 23/04/2018.
 */
public class SocialStats {

    private RedditStats redditStats;
    private GeneralStats generalStats;
    private TwitterStats twitterStats;
    private FacebookStats facebookStats;
    private CodeRepositoryStats codeRepositoryStats;

    public SocialStats(RedditStats redditStats, GeneralStats generalStats, TwitterStats twitterStats, FacebookStats facebookStats, CodeRepositoryStats codeRepositoryStats) {
        this.redditStats = redditStats;
        this.generalStats = generalStats;
        this.twitterStats = twitterStats;
        this.facebookStats = facebookStats;
        this.codeRepositoryStats = codeRepositoryStats;
    }

    public RedditStats getRedditStats() {
        return redditStats;
    }

    public GeneralStats getGeneralStats() {
        return generalStats;
    }

    public TwitterStats getTwitterStats() {
        return twitterStats;
    }

    public FacebookStats getFacebookStats() {
        return facebookStats;
    }

    public CodeRepositoryStats getCodeRepositoryStats() {
        return codeRepositoryStats;
    }
}
