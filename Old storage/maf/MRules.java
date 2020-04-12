package com.jerhis.resist;

import java.util.ArrayList;

public class MRules {

    Mafia r, m;
    MyGdxGame game;
    ButtonSet buttons;
    ArrayList<PlusMinusBox> rulePMs = new ArrayList<PlusMinusBox>();
    PlusMinusBox pmTown, pmMafia, pmRoleblocker, pmDoctor, pmInvestigator, pmVigilante, pmFool;
    int townies = 0;

    MRules(Mafia mafia) {
        m = mafia;
        r = m;
        game = m.game;
    }

    void initFromLobby() {
        buttons.showing = true;
        r.lobby.buttons.showing = false;
        r.lobby.lobbyNamesScroller.showing = false;
        r.generateGame(false);
        pmMafia.value = (int)(Math.sqrt(r.numPlayers)-0.01);
        townies = r.numPlayers - pmMafia.value;
    }

    void initialize() {
        rulePMs = new ArrayList<PlusMinusBox>();

        pmTown = new PlusMinusBox(m.pm, "Townies: ", 25, 700);
        //rulePMs.add(pmTown);
        pmMafia = new PlusMinusBox(m.pm, "Mafia: ", 45, 660);
        rulePMs.add(pmMafia);
        pmRoleblocker = new PlusMinusBox(m.pm, "Roleblocker: ", 45, 620);
        rulePMs.add(pmRoleblocker);
        pmDoctor = new PlusMinusBox(m.pm, "Doctor: ", 45, 540);
        rulePMs.add(pmDoctor);
        pmInvestigator = new PlusMinusBox(m.pm, "Investigator: ", 45, 580);
        rulePMs.add(pmInvestigator);
        pmFool = new PlusMinusBox(m.pm, "Fool: ", 45, 500);
        rulePMs.add(pmFool);
        pmVigilante = new PlusMinusBox(m.pm, "Vigilante: ", 45, 460);
        rulePMs.add(pmVigilante);

        buttons = new ButtonSet(game.batch);
        buttons.addButton(new Button(m.iBack, 25,775) {
            public void onClick() {
                    buttons.showing = false;
                    r.sRules = false;
                    r.sLobby = true;
                    r.lobby.buttons.showing = true;
                    r.lobby.lobbyNamesScroller.showing = true;
                }
        });
        buttons.addButton(new Button(r.iHelp, 455,775) {
            public void onClick() {
                r.helpScroll.showing = true;
                r.helpScroll.setText(r.helpRules);
                //game.font.setScale(0.8f);
                r.quitButton.showing = true;
               // buttons.showing = false;
            } });
        buttons.addButton(new Button(r.buttonImages, game.font, "Start", 240, 250, 0, 60) {
            public void onClick() {
                r.learn.initFromRules();
            } });
    }

    void update() {
        buttons.draw();
        int sum = 0;
        for (PlusMinusBox pmb: rulePMs) {
            pmb.draw(game.batch, game.font);
            sum += pmb.value;
        }
        if (sum >= r.numPlayers)
            for (PlusMinusBox pmb: rulePMs)
                pmb.canIncrement = false;
        else for (PlusMinusBox pmb: rulePMs)
                pmb.canIncrement = true;
        townies = r.numPlayers - sum;
        if (townies < 0) {
            for (PlusMinusBox pmb: rulePMs)
                pmb.value = 0;
            pmMafia.value = (int)(Math.sqrt(r.numPlayers)-0.01);
            townies = r.numPlayers - pmMafia.value;
        }
        pmTown.value = townies;

        buttons.buttons.get(2).showing = pmMafia.value > 0 || pmRoleblocker.value > 0;
        game.font.draw(game.batch, "Townies: " + townies, 88, 727);
        r.drawC( "Players: " + r.numPlayers, 240, 760);

    }

    void touchDown(int x, int y) {
        for (PlusMinusBox pmb: rulePMs)
            pmb.touchDown(x,y);
        buttons.touchDown(x,y);
    }

    void touchUp(int x, int y) {
        buttons.touchUp(x,y);
    }

    void touchDragged(int x, int y) {
        buttons.touchDragged(x,y);
    }

}
