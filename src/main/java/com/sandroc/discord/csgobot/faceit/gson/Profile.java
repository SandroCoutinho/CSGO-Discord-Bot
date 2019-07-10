package com.sandroc.discord.csgobot.faceit.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Profile {

    @SerializedName("items")
    public Items[] items;

    @Override
    public String toString() {
        return items[0].toString();
    }

    public class Items {
        @SerializedName("country")
        public String  country;
        @SerializedName("games")
        public Games[] games;

        @Override
        public String toString() {
            return country + "\n" + Arrays.toString(games);
        }

        public class Games {
            @SerializedName("name")
            public String name;
            @SerializedName("skill_level")
            public String skillLevel;

            @Override
            public String toString() {
                return name + " " + skillLevel;
            }
        }
    }
}
