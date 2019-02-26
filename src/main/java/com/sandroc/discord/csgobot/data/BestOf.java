package com.sandroc.discord.csgobot.data;

public enum BestOf {
    BEST_OF_1(new String[]{ "ban", "ban", "ban", "ban", "ban", "ban", "pick" }),
    BEST_OF_2(new String[]{ "ban", "ban", "ban", "ban", "pick", "pick" }),
    BEST_OF_3(new String[]{ "ban", "ban", "pick", "pick", "ban", "ban", "pick" }),
    BEST_OF_5(new String[]{ "ban", "ban", "pick", "pick", "pick", "pick", "pick" });

    private String[] mode;

    BestOf(String[] mode) {
        this.mode = mode;
    }

    public static String[] getBestOfByNumber(String number) {
        for (BestOf bestOf : BestOf.values()) {
            if (bestOf.toString().contains(number)) {
                return bestOf.getMode();
            }
        }

        return null;
    }

    public String[] getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
