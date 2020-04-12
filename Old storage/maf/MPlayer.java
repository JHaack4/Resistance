package com.jerhis.resist;

public class MPlayer {

    String name;
    int type;
    boolean living;
    String color = "";

    //0 : townie
    //1 : investigator/detective/cop
    //2 : doctor
    //3 : vigilante
    //4 : fool
    //-1 : mafia
    //-2 : role blocker/prostitute

    MPlayer(String n) {
        name = n;
        living = true;
    }

    void setType(int t, int c) {
        type = t;
        if (c==1)
            color = colorString() + " ";
        else color = "";
    }

    static int firstColorUse;
    static int colorUse;
    final static int maxLoadedColor = 20;
    String colorString() {
        colorUse++;
        if (colorUse == maxLoadedColor + 1) colorUse = 1;
        if (colorUse == firstColorUse) colorUse = maxLoadedColor + 2;
        switch (colorUse) {
            case 0: case -1: return "";
            case 1: return "blue";
            case 2: return "red";
            case 3: return "yellow";
            case 4: return "green";
            case 5: return "orange";
            case 6: return "purple";
            case 7: return "pink";
            case 8: return "brown";
            case 9: return "black";
            case 10: return "white";
            case 11: return "gray";
            case 12: return "cyan";
            case 13: return "magenta";
            case 14: return "lime";
            case 15: return "neon";
            case 16: return "teal";
            case 17: return "navy";
            case 18: return "gold";
            case 19: return "silver";
            case 20: return "bronze";
            default: return (colorUse-maxLoadedColor - 1) + " heart";
        }
    }

    public static String stringFromType(int type) {
        switch (type) {
            case 0: return "townie";
            case 1: return "investigator";
            case 2: return "doctor";
            case 3: return "vigilante";
            case 4: return "town fool";
            case -1: return "mafia";
            case -2: return "roleblocker";
            default: return "err";
        }
    }
}
