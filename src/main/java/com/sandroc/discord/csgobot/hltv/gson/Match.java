package com.sandroc.discord.csgobot.hltv.gson;

import com.google.gson.annotations.SerializedName;

public class Match {

    @SerializedName("event")
    public String event;
    @SerializedName("maps")
    public String maps;
    @SerializedName("team1")
    public Team   teamOne;
    @SerializedName("team2")
    public Team   teamTwo;
    @SerializedName("matchId")
    public String matchId;

    public class Team {

        @SerializedName("name")
        public String name;
        @SerializedName("result")
        public int    result;
    }
}
