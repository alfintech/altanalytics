package io.altanalytics.domain.social;

/**
 * Created by Ismail on 23/04/2018.
 */
public class FacebookStats {

    private long likes;
    private Boolean isClosed;
    private long talkingAbout;
    private String link;
    private long points;

    public FacebookStats(long likes, boolean isClosed, long talkingAbout, String link, long points) {
        this.likes = likes;
        this.isClosed = isClosed;
        this.talkingAbout = talkingAbout;
        this.link = link;
        this.points = points;
    }

    public long getLikes() {
        return likes;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public long getTalkingAbout() {
        return talkingAbout;
    }

    public String getLink() {
        return link;
    }

    public long getPoints() {
        return points;
    }
}
