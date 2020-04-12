package com.jerhis.resist;

import android.view.KeyEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;

public class RLobby {

    Resistance r;
    MyGdxGame game;
    ButtonSet lobbyButtons, keyboardButton;
    boolean keyboardUp = false;
    String keyString = "";
    char currentChar = 'A';
    CheckBoxScroller lobbyNamesScroller;

    RLobby(Resistance r) {
        this.r = r;
        game = r.game;
    }

    void initialize() {

        keyboardButton = new ButtonSet(r.game.batch);
        keyboardButton.addButton(new Button(r.iBack,r.iBackP, 56,1920-56) {
            public void onClick() {
                if (keyboardUp)
                    keyDown(KeyEvent.KEYCODE_BACK);
            } });
        keyboardButton.addButton(new Button(r.buttonImages, r.game.font, "A", 540, 500, 180, 180) {
            public void onClick() {
                if (keyString.length() < 16) keyString += currentChar;
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, ">", 740, 500, 180, 180) {
            public void onClick() {
                currentChar++;
                if (currentChar == 'Z' + 1) currentChar = 'A';
                if (currentChar == 'z' + 1) currentChar = 'a';
                keyboardButton.buttons.get(1).s = currentChar + "";
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, "<", 340, 500, 180, 180) {
            public void onClick() {
                currentChar--;
                if (currentChar == 'A' - 1) currentChar = 'Z';
                if (currentChar == 'a' - 1) currentChar = 'z';
                keyboardButton.buttons.get(1).s = currentChar + "";
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, "Space", 340, 340, 330, 110) {
            public void onClick() {
                if (keyString.length() < 16) keyString += ' ';
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, "Enter", 740, 220, 330, 110) {
            public void onClick() {
                if (keyString.equals("")) {
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    keyboardUp = false;
                    //Gdx.input.setCatchBackKey(false);
                    lobbyNamesScroller.checkBoxes.get(0).checked = false;
                    currentChar = 'A';
                }
                else {
                    game.addName(keyString);
                    CheckBox c = new CheckBox(r.checkBox, keyString, 0, -100);
                    c.checked = true;
                    lobbyNamesScroller.checkBoxes.add(1, c);
                    keyString = "";
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    keyboardUp = false;
                    //Gdx.input.setCatchBackKey(false);
                    lobbyNamesScroller.checkBoxes.get(0).checked = false;
                    currentChar = 'A';
                }
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, "Delete", 740, 340, 330, 110) {
            public void onClick() {
                if (keyString.length() > 0) {
                    keyString = keyString.substring(0,keyString.length() - 1);
                }
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, "Caps", 340, 220, 330, 110) {
            public void onClick() {
                if (currentChar > 'Z') currentChar += -'a'+'A';
                else currentChar += 'a'-'A';
                keyboardButton.buttons.get(1).s = currentChar + "";
            } });

        lobbyButtons = new ButtonSet(game.batch);
        lobbyButtons.addButton(new Button(r.iBack,r.iBackP, 56,1920-56) {
            public void onClick() {
                if (r.sLobby)
                    r.leaveMain = true;
            } });
        lobbyButtons.addButton(new Button(r.iHelp,r.iHelpP, 1080-56,1920-56) {
            public void onClick() {
                r.helpScroll.showing = true;
                //game.font.setScale(0.8f);
                r.quitButton.showing = true;
                r.helpScroll.setText(r.helpLobby);
            } });
        lobbyButtons.addButton(new Button(r.buttonImages, game.font, "Play with this Group", 1080/2, 1680, 0, 110) {
            public void onClick() {
                lobbyButtons.showing = false;
                r.sLobby = false;
                lobbyNamesScroller.showing = false;
                r.generateGame(false);
                r.sLearn = true;
                r.learn.learnIndex = 0;
                r.learn.learnButtons.showing = true;
                r.learn.learnButtons.buttons.get(2).showing = false;
                r.learn.orderNames.clear();
                for (int k = 1; k <= r.players.size(); k++) {
                    r.learn.orderNames.add(new CheckBox(r.iswap, k + ": " + r.players.get(k-1).name, 0, 0));
                }
            } });

        ArrayList<CheckBox> lobbyNames = new ArrayList<CheckBox>();
        lobbyNames.add(new CheckBox(new TextureAtlas.AtlasRegion[]{r.iAdd,r.iAdd}, "Add New Name", 0, 0));
        for (int k = 0; k < game.names.size(); k++) {
            lobbyNames.add(new CheckBox(r.checkBox, game.names.get(k), 0, 0));
        }
        if (lobbyNames.size() < 17) {
            for (int k = lobbyNames.size() + 1; k < 17; k++)
                lobbyNames.add(new CheckBox(r.checkBox, "Player #" + (k-1), 0, 0));
        }
        lobbyNames.add(new CheckBox(r.checkBox, "never show this", 0 ,0 ));
        lobbyNames.get(lobbyNames.size()-1).dead = true;
        lobbyNames.add(new CheckBox(r.checkBox, "never show this2", 0 ,0 ));
        lobbyNames.get(lobbyNames.size()-1).dead = true;
        lobbyNames.add(new CheckBox(new TextureAtlas.AtlasRegion[] {r.iDel,r.iDel}, "Delete Marked Names", 0 ,0 ));
        lobbyNames.add(new CheckBox(r.checkBox, "never show this3", 0 ,0 ));
        lobbyNames.get(lobbyNames.size()-1).dead = true;
        lobbyNamesScroller = new CheckBoxScroller(lobbyNames, "", r.scroll, r.iBack, game.batch, game.font);
        lobbyNamesScroller.top = 1600;
        lobbyNamesScroller.bottom = 0;
        lobbyNamesScroller.exitButton.showing = false;
        lobbyNamesScroller.showing = true;
    }

    void update() {
        if (keyboardUp) {
            r.drawC("Enter a new name:", 540, 1420);
            r.drawC("-" + keyString + "-", 540, 1350);
            keyboardButton.buttons.get(1).s = currentChar + "";
            keyboardButton.draw();
        }
        else {
            int numPlayers = 0;
            lobbyNamesScroller.draw(game.batch);
            for (CheckBox c: lobbyNamesScroller.checkBoxes) {
                if (c.checked) numPlayers++;
            }
            //drawC("Who is playing? (5-10)", 240, 610);
            r.drawC("Choose Players", 1080/2, 1920-130);
            lobbyButtons.buttons.get(2).showing = (numPlayers > 4 || (numPlayers > 2 && r.game.debug)) && numPlayers <21;
            lobbyButtons.draw();
        }
    }

    void touchDown(int x, int y) {
        if (!keyboardUp) {
            lobbyNamesScroller.touchDown(x,y);
            lobbyButtons.touchDown(x,y);
            if (lobbyNamesScroller.checkBoxes.get(0).checked) {
                Gdx.input.setOnscreenKeyboardVisible(true);
                keyboardUp = true;
                Gdx.input.setCatchBackKey(true);
                lobbyNamesScroller.checkBoxes.get(0).checked = false;
            }
            if (lobbyNamesScroller.checkBoxes.get(lobbyNamesScroller.checkBoxes.size() - 2).checked) {
                lobbyNamesScroller.checkBoxes.get(lobbyNamesScroller.checkBoxes.size() - 2).checked = false;
                int r = 0;
                for (int k = 1; k < lobbyNamesScroller.checkBoxes.size() - 3; k++) {
                    if (lobbyNamesScroller.checkBoxes.get(k).checked) {
                        game.deleteName(lobbyNamesScroller.checkBoxes.get(k).text);
                        lobbyNamesScroller.checkBoxes.remove(k);
                        k--;
                        r++;
                    }
                }
                lobbyNamesScroller.current -= r;
                if (lobbyNamesScroller.current < 0) lobbyNamesScroller.current = 0;
            }
        }
        else {
            keyboardButton.touchDown(x, y);
        }
    }

    void touchUp(int x, int y) {
        if (!keyboardUp) {
            lobbyNamesScroller.touchUp(x,y);
            lobbyButtons.touchUp(x,y);
        }
        else {
            keyboardButton.touchUp(x, y);
        }
    }

    void touchDragged(int x, int y) {
        if (!keyboardUp) {
            lobbyButtons.touchDragged(x,y);
            lobbyNamesScroller.touchDragged(x,y);
        }
        else {
            keyboardButton.touchDragged(x,y);
        }
    }

    public boolean keyDown(int keycode) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            Gdx.input.setOnscreenKeyboardVisible(false);
            keyboardUp = false;
            //Gdx.input.setCatchBackKey(false);
            lobbyNamesScroller.checkBoxes.get(0).checked = false;
        }
        if (keycode == KeyEvent.KEYCODE_ENTER) {
            if (keyString.equals("")) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                r.lobby.keyboardUp = false;
                //Gdx.input.setCatchBackKey(false);
                lobbyNamesScroller.checkBoxes.get(0).checked = false;
            }
            else {
                game.addName(keyString);
                CheckBox c = new CheckBox(r.checkBox, keyString, 0, -100);
                c.checked = true;
                lobbyNamesScroller.checkBoxes.add(1, c);
                keyString = "";
                Gdx.input.setOnscreenKeyboardVisible(false);
                r.lobby.keyboardUp = false;
                //Gdx.input.setCatchBackKey(false);
                lobbyNamesScroller.checkBoxes.get(0).checked = false;
            }
        }
        if (keycode == KeyEvent.KEYCODE_DEL) {
            if (keyString.length() > 0) {
                keyString = keyString.substring(0,keyString.length() - 1);
            }
        }

        return false;
    }

    public boolean keyUp(int keycode) {return false;}

    public boolean keyTyped(char character) {
        if ((character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z'
                || character == ' '|| character == '.') && keyString.length() < 16)
            keyString = keyString + character;

        return false;
    }
}
