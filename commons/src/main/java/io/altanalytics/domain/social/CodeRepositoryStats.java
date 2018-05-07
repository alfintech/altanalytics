package io.altanalytics.domain.social;

import java.util.List;

/**
 * Created by Ismail on 23/04/2018.
 */
public class CodeRepositoryStats {

    private List<CodeRespositoryProjectStats> projects;
    private long points;

    public CodeRepositoryStats(List<CodeRespositoryProjectStats> projects, long points) {
        this.projects = projects;
        this.points = points;
    }

    public List<CodeRespositoryProjectStats> getProjects() {
        return projects;
    }

    public long getPoints() {
        return points;
    }
}
