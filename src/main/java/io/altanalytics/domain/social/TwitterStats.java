package io.altanalytics.domain.social;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ismail on 23/04/2018.
 */
public class TwitterStats {

    private long followers;
    private long following;
    private long lists;
    private long favourites;
    private long statuses;
    private Date accountCreation;
    private long points;
    private String link;

    private DateFormat socialStatsDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public TwitterStats(long followers, long following, long lists, long favourites, long statuses, Date accountCreation, long points, String link) {
        this.followers = followers;
        this.following = following;
        this.lists = lists;
        this.favourites = favourites;
        this.statuses = statuses;
        this.accountCreation = accountCreation;
        this.points = points;
        this.link = link;
    }

    public long getFollowers() {
        return followers;
    }

    public long getFollowing() {
        return following;
    }

    public long getLists() {
        return lists;
    }

    public long getFavourites() {
        return favourites;
    }

    public long getStatuses() {
        return statuses;
    }

    public Date getAccountCreation() {
        return accountCreation;
    }

    public String getAccountCreationDateString() {
        if(accountCreation!=null){
            return socialStatsDateFormat.format(accountCreation);
        }
        return socialStatsDateFormat.format(new Date(0));
    }

    public long getPoints() {
        return points;
    }

    public String getLink() {
        return link;
    }


}
