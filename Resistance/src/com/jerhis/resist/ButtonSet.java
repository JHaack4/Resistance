package com.jerhis.resist;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import java.util.ArrayList;

public class ButtonSet {

    /*
    1- create atlas regions and initialize buttonset
    2- add buttons
    3- instantiate touch methods
    4- add draw call
    5- use poll click for results - case switch
    6- clear for reset
     */

    public ArrayList<Button> buttons;
    //public int finalAnswer = -1,
    public int currentPushed = -1;
    SpriteBatch batch;
    public boolean showing;

    public ButtonSet(SpriteBatch batch) {
        buttons = new ArrayList<Button>();
        this.batch = batch;
        showing = true;
    }

    //public void addButton(String s, int x, int y, int w, int h) {
    //    buttons.add(new Button(s, x, y, w, h));
    //}

    public void addButton(Button b) {
        buttons.add(b);
    }

    /*public int pollClick() {
        if (finalAnswer == -1)
            return -1;
        else {
            int pushedButtonIndex = finalAnswer;
            finalAnswer = -1;
            return pushedButtonIndex;
        }

    }*/

    public void draw() {
        if (showing)
        for (Button b: buttons) {
            if (b.showing)
                b.draw(batch);
        }
    }

    public void touchDown(int x, int y) {
        openButtons();
        currentPushed = -1;

        if (showing)
        for (int k = 0; k < buttons.size(); k++) {
            if (inBounds(x, y, buttons.get(k)) && buttons.get(k).showing) {
                currentPushed = k;
                buttons.get(k).pushed = true;
            }
        }
    }

    public void touchDragged(int x, int y) {
        if (!showing) {
            openButtons();
            currentPushed = -1;
        }
        else if (currentPushed != -1 && !inBounds(x, y, buttons.get(currentPushed))) {
            buttons.get(currentPushed).pushed = false;
            currentPushed = -1;
        }
    }

    public void touchUp(int x, int y) {
        if (!showing) {
            openButtons();
            currentPushed = -1;
        }
        else if (currentPushed != -1 && inBounds(x, y, buttons.get(currentPushed))) {
            //finalAnswer = currentPushed;
            buttons.get(currentPushed).pushed = false;

            if (buttons.get(currentPushed).showing)
                buttons.get(currentPushed).onClick();
            currentPushed = -1;
            openButtons();
        }
    }

    private boolean inBounds(int touchX, int touchY, Button button) {
        return touchX > button.x - button.w/2 &&
                touchX < button.x + button.w/2 &&
                touchY > button.y - button.h/2 &&
                touchY < button.y + button.h/2;
    }

    public void clear() {
        //finalAnswer = -1;
        currentPushed = -1;
        openButtons();
    }

    private void openButtons() {
        for (Button b: buttons) {
            b.pushed = false;
        }
    }

}
