package com.jerhis.resist;

import java.util.ArrayList;

public class RRules {

    Resistance r;
    MyGdxGame game;
    ArrayList<CheckBox> ruleChecks;
    CheckBox boxMerlin, boxPerceval, boxOberyn, boxMordred, boxBlindSpies, boxMissionSelect;

    RRules(Resistance r) {
        this.r = r;
        game = r.game;
    }

    void initialize() {
        //TODO all roles
        boxBlindSpies = new CheckBox(r.checkBox, "Blind Spies", 56, 1690);
        //boxMissionSelect = new CheckBox(r.checkBox, "Mission Quest", 25, 700);
        boxMerlin = new CheckBox(r.checkBox, "Merlin & Assassin", 56, 1510);
        boxPerceval = new CheckBox(r.checkBox, "Perceval & Morgana", 112, 1420);
        boxMordred = new CheckBox(r.checkBox, "Mordred (Deep Spy)", 112, 1330);
        boxOberyn = new CheckBox(r.checkBox, "Oberon (Solo Spy)", 56, 1600);

        ruleChecks = new ArrayList<CheckBox>();
        ruleChecks.add(boxMerlin);
        ruleChecks.add(boxPerceval);
        ruleChecks.add(boxMordred);
        ruleChecks.add(boxOberyn);
        //ruleChecks.add(boxMissionSelect);
        ruleChecks.add(boxBlindSpies);
    }

    void update() {
        if (boxMerlin.checked) {
            boxPerceval.showing = true;
            boxMordred.showing = true;
        }
        else {
            boxPerceval.showing = false;
            boxMordred.showing = false;
            boxPerceval.checked = false;
            boxMordred.checked = false;
        }
        if (boxPerceval.checked && boxMordred.checked && r.numSpies == 2) {
            boxOberyn.checked = false;
        }
        for (CheckBox c: ruleChecks) {
            c.draw(game.batch, game.font);
        }
        r.drawC("Choose Roles", 540, 1920-130);
        //if (r.numSpies == 2) r.drawC("Max 2 spy roles", 240, 450);
        //Todo too many roles / assassin = mordred/morgana
        //for (int k = 0; k < r.numPlayers; k++)
         //   r.drawL((r.numPlayers-k)+". " + r.players.get(r.numPlayers-k-1).name, 10, 50+30*k + (30*(10-r.numPlayers)));
    }

    String cheatString = "1010011110111001";
    int cheatIndex = 0;
    boolean cheating = false;
    String cheatCode = "";
    int trickMerlin[][];
    boolean soundPlayed = false;
    String wtfName = "";
    RPlayer cheatPlayer = null;
    //0101 trick merlin
    //1101 wtf at fails
    //1100 wtf at last player
    //0000 pl last good
    //0001 pl last merlin
    //0010 pl last perceval
    //0011 pl last spy
    //0100 pl last oberon
    //0110 pl last mordred
    //1011 ads disabled
    //1111 debug toggle

    void touchDown(int x, int y) {
        if (x > 540) {
            if (cheating && cheatCode.length() < 4) {
                cheatCode += "1";
            }
            else if (cheatString.charAt(cheatIndex) == '1')
                cheatIndex++;
            else cheatIndex = 0;
        }
        else {
            if (cheating && cheatCode.length() < 4) {
                cheatCode += "0";
            }
            else if (cheatString.charAt(cheatIndex) == '0')
                cheatIndex++;
            else cheatIndex = 0;
        }
        if (cheatIndex >= 16) {
            cheating = true;
            cheatIndex = 0;
        }
    }

    void touchUp(int x, int y) {

    }

    void touchDragged(int x, int y) {

    }

}
