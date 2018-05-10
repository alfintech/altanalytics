package io.altanalytics.domain.social;

public class GeneralStats {

    private String name;
    private long points;
    private long timeStampMillis;

    public GeneralStats(String name, long points, Long timeStampMillis) {
        this.name = name;
        this.points = points;
        this.timeStampMillis = timeStampMillis;
    }

    public String getName() {
        return name;
    }

    public long getPoints() {
        return points;
    }

    public long getTimeStampMillis() {
        return timeStampMillis;
    }
}
