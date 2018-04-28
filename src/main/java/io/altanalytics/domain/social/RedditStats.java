package io.altanalytics.domain.social;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ismail on 23/04/2018.
 */
public class RedditStats {

    private long subscribers;
    private long activeUsers;
    private Date communityCreationDate;
    private double postsPerHour;
    private double postsPerDay;
    private double commentsPerHour;
    private double commentsPerDay;
    private long points;
    private String link;

    private DateFormat socialStatsDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public RedditStats(long subscribers, long activeUsers, Date communityCreationDate, double postsPerHour, double postsPerDay, double commentsPerHour, double commentsPerDay, String link, long points) {
        this.subscribers = subscribers;
        this.activeUsers = activeUsers;
        this.communityCreationDate = communityCreationDate;
        this.postsPerHour = postsPerHour;
        this.postsPerDay = postsPerDay;
        this.commentsPerHour = commentsPerHour;
        this.commentsPerDay = commentsPerDay;
        this.points = points;
        this.link = link;
    }

    public long getSubscribers() {
        return subscribers;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public Date getCommunityCreationDate() {
        return communityCreationDate;
    }

    public double getPostsPerHour() {
        return postsPerHour;
    }

    public double getPostsPerDay() {
        return postsPerDay;
    }

    public double getCommentsPerHour() {
        return commentsPerHour;
    }

    public double getCommentsPerDay() {
        return commentsPerDay;
    }

    public String getLink(){
        return link;
    }

    public long getPoints() {
        return points;
    }

    public String getCommunityCreationDateString() {
        if(communityCreationDate!=null){
            return socialStatsDateFormat.format(communityCreationDate);
        }
        return null;
    }
}

