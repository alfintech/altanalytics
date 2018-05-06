package io.altanalytics.domain.social;

public class GeneralStats {

    private String name;
    private long points;
    private long timestamp;

    public GeneralStats(String name, long points, Long timestamp) {
        this.name = name;
        this.points = points;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public long getPoints() {
        return points;
    }
    
    public long getTimestamp() {
    	return timestamp;
    }
}
