package com.jerhis.resist;

import java.util.ArrayList;

public class RResults {

    Resistance r;
    MyGdxGame game;
    ButtonSet resultButtons;

    RResults(Resistance r) {
        this.r = r;
        game = r.game;
    }

    void initialize() {
        resultButtons = new ButtonSet(game.batch);
        resultButtons.addButton(new Button(r.buttonImages, game.font, "Same Players", 540, 1330, 0, 110) {
            public void onClick() {
                r.game.platformHandler.ad(4);
                for (CheckBox c: r.rgame.missionChecks) c.checked = false;
                r.generateGame(true);
                r.sLearn = true;
                r.sResults = false;
                r.learn.learnButtons.buttons.get(3).showing = true;
                r.learn.learnIndex = 0;
                r.learn.learnButtons.showing = true;
                //resultButtons.showing = false;
                r.rules.cheating = false;
                r.rules.cheatCode = "";
                r.rules.wtfName="";
                //r.leaveNew = true;
            } });
        resultButtons.addButton(new Button(r.buttonImages, game.font, "Main Menu", 540, 1180, 0, 110) {
            public void onClick() {
                r.leaveMain = true;
                r.game.platformHandler.ad(4);
            } });

    }

    void update() {
        resultButtons.draw();
        if (r.pass3) r.drawC("Good Prevails!", 540,1590);
        if (r.fail3) r.drawC("Spies Win!", 540,1590);
        for (int k = 0; k < r.numPlayers; k++) {
            String tp = "something went wrong";
            switch (r.players.get(k).type) {
                //TODO all roles
                case Resistance.GOOD: tp = "Good"; break;
                case Resistance.SPY: tp = "Spy"; break;
                case Resistance.MERLIN: tp = "Merlin"; break;
                case Resistance.ASSASSIN: tp = "Assassin"; break;
                case Resistance.MORGANA: tp = "Morgana"; break;
                case Resistance.PERCEVAL: tp = "Perceval"; break;
                case Resistance.OBERYN: tp = "Oberon"; break;
                case Resistance.MORDRED: tp = "Mordred"; break;
                case Resistance.TRICK_MERLIN: tp = "Merlin"; break;
            }
            //if (r.numPlayers > 10 )
                r.game.fontD.draw(r.game.batch, (k+1) + ": " + r.players.get(k).name + " - " + tp, 50, 1050 - 45*k);
            //else
               // r.game.font.draw(r.game.batch, (k+1) + ": " + r.players.get(k).name + "-" + tp, 50, 1050 - 70*k);
        }
    }

    void touchDown(int x, int y) {
        resultButtons.touchDown(x,y);
    }

    void touchUp(int x, int y) {
        resultButtons.touchUp(x,y);
    }

    void touchDragged(int x, int y) {
        resultButtons.touchDragged(x,y);
    }
}
