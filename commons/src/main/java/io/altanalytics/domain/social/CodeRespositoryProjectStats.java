package io.altanalytics.domain.social;

import java.util.Date;

/**
 * Created by Ismail on 23/04/2018.
 */
public class CodeRespositoryProjectStats {

    private long size;
    private String url;
    private long stars;
    private long forks;
    private boolean isFork;
    private String language;
    private long subscribers;

    private Date lastPush;
    private Date lastUpdate;
    private Date dateCreatedAt;

    private long openIssues;
    private long openPullIssues;
    private long openTotalIssues;

    private long closedIssues;
    private long closedPullIssues;
    private long closedTotalIssues;

    public CodeRespositoryProjectStats(long size, String url, long stars, long forks, boolean isFork, String language, long subscribers, Date lastPush, Date lastUpdate, Date dateCreatedAt, long openIssues, long openPullIssues, long openTotalIssues, long closedIssues, long closedPullIssues, long closedTotalIssues) {
        this.size = size;
        this.url = url;
        this.stars = stars;
        this.forks = forks;
        this.isFork = isFork;
        this.language = language;
        this.subscribers = subscribers;
        this.lastPush = lastPush;
        this.lastUpdate = lastUpdate;
        this.dateCreatedAt = dateCreatedAt;
        this.openIssues = openIssues;
        this.openPullIssues = openPullIssues;
        this.openTotalIssues = openTotalIssues;
        this.closedIssues = closedIssues;
        this.closedPullIssues = closedPullIssues;
        this.closedTotalIssues = closedTotalIssues;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public long getStars() {
        return stars;
    }

    public long getForks() {
        return forks;
    }

    public boolean isFork() {
        return isFork;
    }

    public String getLanguage() {
        return language;
    }

    public long getSubscribers() {
        return subscribers;
    }

    public Date getLastPush() {
        return lastPush;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public Date getDateCreatedAt() {
        return dateCreatedAt;
    }

    public long getOpenIssues() {
        return openIssues;
    }

    public long getOpenPullIssues() {
        return openPullIssues;
    }

    public long getOpenTotalIssues() {
        return openTotalIssues;
    }

    public long getClosedIssues() {
        return closedIssues;
    }

    public long getClosedPullIssues() {
        return closedPullIssues;
    }

    public long getClosedTotalIssues() {
        return closedTotalIssues;
    }
}
