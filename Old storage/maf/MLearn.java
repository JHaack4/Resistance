package com.jerhis.resist;

import java.util.ArrayList;

public class MLearn {

    Mafia m, r;
    MRules rr;
    MyGdxGame game;
    ButtonSet buttons;
    boolean showState;
    int showTest = MyGdxGame.debug ? -500 : 2000;
    long showTime;
    Integer learnIndex;


    MLearn(Mafia mafia) {
        m = mafia;
        r = m;
        rr = m.rules;
        game = m.game;
    }

    void initFromRules(){
        m.sRules = false;
        m.sLearn = true;
        showState = false;
        learnIndex = 0;
        m.generateRoles();
        buttons.buttons.get(3).showing = false;
        buttons.buttons.get(2).showing = true;
    }


    void initialize() {
        final int SHOW = 2, NEXT = 3;
        buttons = new ButtonSet(game.batch);
        buttons.addButton(new Button(r.iBack, 25,775) {
            public void onClick() {
                if (showState) {
                    showState = false;
                    buttons.buttons.get(NEXT).showing = false;
                    buttons.buttons.get(SHOW).showing = true;
                }
                else if (learnIndex == 0) {
                    r.sLearn = false;
                    r.sRules = true;
                    showState = false;
                }
            } });
        buttons.addButton(new Button(r.iHelp, 455,775) {
            public void onClick() {
                r.helpScroll.showing = true;
                //game.font.setScale(0.8f);
                r.quitButton.showing = true;
                r.helpScroll.setText(r.helpLearn);
                showState = false;
                buttons.buttons.get(NEXT).showing = false;
                buttons.buttons.get(SHOW).showing = true;
            } });
        buttons.addButton(new Button(r.buttonImages, game.font, "Show", 240, 350, 0, 60) {
            public void onClick() {
                showState = true;
                buttons.buttons.get(SHOW).showing = false;
                buttons.buttons.get(NEXT).showing = true;
                buttons.buttons.get(3).s = "Wait";
                //buttons.buttons.get(3).y = 120;
                showTime = System.currentTimeMillis();
                //if (learnIndex > 0 && learnIndex <= r.numPlayers)
                 //   if (r.players.get(learnIndex-1).name.equals(r.rules.wtfName) && r.rules.cheatCode.equals("1100"))
                 //       r.tylerSound.play();
            } });
        buttons.addButton(new Button(r.buttonImages, game.font, "Next", 240, 125, 175, 60) {
            public void onClick() {
                int countDown = (int)(showTest-(System.currentTimeMillis() - showTime))/1000 + 1;
                if (countDown <= 0 || learnIndex == 0) {
                    learnIndex++;
                    if (learnIndex > r.numPlayers) {
                        r.sLearn = false;
                        r.sGame = true;
                        r.mgame.initFromLearn();
                    }
                    else {
                        showState = false;
                        buttons.buttons.get(NEXT).showing = false;
                        buttons.buttons.get(SHOW).showing = true;
                    }
                }
            } });
    }

    void update() {
        if (learnIndex == 0) {
            if (showState) {
                for (int k = 0; k < r.numPlayers; k++) {
                    game.font.draw(game.batch, (k+1) + ". " + r.players.get(k).name +
                            " " + MPlayer.stringFromType(r.players.get(k).type), 20, 740-40*k);
                }
                int countDown = (int)(showTest-(System.currentTimeMillis() - showTime))/1000 + 1;
                if (countDown > 0) buttons.buttons.get(3).s = "Wait";
                else  buttons.buttons.get(3).s = "Next";
            }
            else r.drawC("Moderator", 240, 550);
            buttons.buttons.get(0).showing = true;
        }
        if (learnIndex > 0) {
            r.drawC("Player #" + (learnIndex) + ": " + r.players.get(learnIndex-1).name, 240, 550);
            buttons.buttons.get(0).showing = false;
    }
        if (learnIndex > 0 && showState) { //TODO all roles
            MPlayer p = r.players.get(learnIndex-1);
            switch (p.type) {
                case 0:  r.drawC("You are a townie.", 240, 470);
                    r.drawC("There are " +r.numMafia + " mafia.", 240, 420); break;
                case 1:  r.drawC("You are " + p.color + "investigator.", 240, 470);
                    r.drawC("There are " +r.numMafia + " mafia.", 240, 420); break;
                case 2:  r.drawC("You are " + p.color + "doctor.", 240, 470);
                    r.drawC("There are " +r.numMafia + " mafia.", 240, 420); break;
                case 3:  r.drawC("You are " + p.color + "vigilante.", 240, 470);
                    r.drawC("There are " +r.numMafia + " mafia.", 240, 420); break;
                case 4:  r.drawC("You are " + p.color + "town fool.", 240, 470);
                    r.drawC("There are " +r.numMafia + " mafia.", 240, 420); break;

                case -1:  r.drawC("You are " + p.color + "mafia.", 240, 470);
                    r.drawC("There are " +r.numMafia + " mafia.", 240, 420); break;
                case -2:  r.drawC("You are " + p.color + "roleblocker.", 240, 470);
                    r.drawC("There are " +r.numMafia + " mafia.", 240, 420);
                    r.drawC("(You are part of the mafia)", 240, 380); break;
            }
            int countDown = (int)(showTest-(System.currentTimeMillis() - showTime))/1000 + 1;
            if (countDown > 0) buttons.buttons.get(3).s = "Wait";
            else  buttons.buttons.get(3).s = "Next";
        }

        //drawC("First leader is " + players.get(0).name, 240, 500);
        buttons.draw();
    }

    void touchDown(int x, int y) {
        buttons.touchDown(x,y);
    }

    void touchUp(int x, int y) {
        buttons.touchUp(x,y);
    }

    void touchDragged(int x, int y) {
        buttons.touchDragged(x,y);
    }

}
