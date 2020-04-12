package com.jerhis.resist;

import java.util.ArrayList;

public class MGame {

    Mafia m, r;
    MyGdxGame game;
    ButtonSet gameButtons;
    int dayCount, nightCount;
    long cycleStartTime;
    int state;
    final int START = -1, DAY = -2, NIGHT = -3;

    MGame(Mafia mafia) {
        m = r = mafia;
        game = m.game;
    }

    public void initialize() {

        gameButtons = new ButtonSet(game.batch);
        gameButtons.addButton(new Button(r.iBack, 25,775) { //0
            public void onClick() {
                //nothing
            } });
        gameButtons.addButton(new Button(r.iHelp, 455,775) { //1
            public void onClick() {
                r.helpScroll.showing = true;
                //game.font.setScale(0.8f);
                r.quitButton.showing = true;
                if (state == START) r.helpScroll.setText(r.helpGame0);
                else if (state == DAY) r.helpScroll.setText(r.helpGame2);
                else if (state == NIGHT) r.helpScroll.setText(r.helpGame1);
            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "DAY", 240, 40, 0, 60) {//2
            public void onClick() {
                dayCount++;
                state = DAY;
                cycleStartTime = System.currentTimeMillis();
            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "NIGHT", 240, 120, 200, 60) {//3
            public void onClick() {
                nightCount++;
                state = NIGHT;
                cycleStartTime = System.currentTimeMillis();
            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Lynch Selected", 240, 120, 200, 60) {//4
            public void onClick() {

            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Target Selected", 240, 420, 0, 60) {//5
            public void onClick() {

            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Action Menu", 240, 350, 0, 60) {//6
            public void onClick() {

            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Revive Player", 240, 120, 0, 60) {//7
            public void onClick() {

            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Kill Player", 240, 70, 0, 60) {//8
            public void onClick() {

            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Modify Role", 240, 70, 0, 60) {//9
            public void onClick() {

            } });

    }

    void initFromLearn() {
        state = START;
        dayCount = 0;
        nightCount = 0;
        cycleStartTime = System.currentTimeMillis();
    }

    void update() {
        m.game.batch.draw(m.iDel, 10, 10);

        if (state == START) {

        }
        else if (state == DAY) {

        }
        else if (state == NIGHT) {

        }
    }

    void touchDown(int x, int y) {
        gameButtons.touchDown(x,y);
    }

    void touchUp(int x, int y) {
        gameButtons.touchUp(x,y);
    }

    void touchDragged(int x, int y) {
        gameButtons.touchDragged(x,y);
    }


}
