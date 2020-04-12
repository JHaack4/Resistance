package com.jerhis.resist;

public class MResults {

    Mafia m;
    MyGdxGame game;
    ButtonSet buttons;

    MResults(Mafia mafia) {
        m = mafia;
        game = m.game;
    }

    void initialize() {

    }

    void update() {

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
