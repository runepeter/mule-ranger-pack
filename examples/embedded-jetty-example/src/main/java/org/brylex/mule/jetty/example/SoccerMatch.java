package org.brylex.mule.jetty.example;

import com.google.common.base.Joiner;

public class SoccerMatch {

    private String league;
    private String date;
    private String home;
    private String away;

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getKey() {
        return Joiner.on('.').join(league, date, home, away);
    }

    @Override
    public String toString() {
        return "SoccerMatch{" +
                "league='" + league + '\'' +
                ", date='" + date + '\'' +
                ", home='" + home + '\'' +
                ", away='" + away + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SoccerMatch)) return false;

        SoccerMatch that = (SoccerMatch) o;

        if (!away.equals(that.away)) return false;
        if (!date.equals(that.date)) return false;
        if (!home.equals(that.home)) return false;
        if (!league.equals(that.league)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = league.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + home.hashCode();
        result = 31 * result + away.hashCode();
        return result;
    }
}
