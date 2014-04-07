package org.brylex.mule.jetty.example;

public class SoccerResultWithReceiver {

    private final String receiver;
    private final SoccerResult soccerResult;

    public SoccerResultWithReceiver(String receiver, SoccerResult soccerResult) {
        this.receiver = receiver;
        this.soccerResult = soccerResult;
    }

    public String getReceiver() {
        return receiver;
    }

    public SoccerResult getSoccerResult() {
        return soccerResult;
    }
}
