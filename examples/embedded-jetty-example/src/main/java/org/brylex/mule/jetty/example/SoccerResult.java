package org.brylex.mule.jetty.example;

public class SoccerResult {

    private SoccerMatch soccerMatch;
    private String home;
    private String away;
    private String time;

    public SoccerMatch getSoccerMatch() {
        return soccerMatch;
    }

    public void setSoccerMatch(SoccerMatch soccerMatch) {
        this.soccerMatch = soccerMatch;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SoccerResult{" +
                "soccerMatch=" + soccerMatch +
                ", home='" + home + '\'' +
                ", away='" + away + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SoccerResult)) return false;

        SoccerResult that = (SoccerResult) o;

        if (!away.equals(that.away)) return false;
        if (!home.equals(that.home)) return false;
        if (!soccerMatch.equals(that.soccerMatch)) return false;
        if (!time.equals(that.time)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = soccerMatch.hashCode();
        result = 31 * result + home.hashCode();
        result = 31 * result + away.hashCode();
        result = 31 * result + time.hashCode();
        return result;
    }
}
