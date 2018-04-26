package io.altanalytics.domain.social;

public class GeneralStats {

    private String name;
    private long points;

    public GeneralStats(String name, long points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public long getPoints() {
        return points;
    }
}
