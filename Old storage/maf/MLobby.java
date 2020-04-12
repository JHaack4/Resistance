package com.jerhis.resist;

import android.view.KeyEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;

public class MLobby {

    Mafia r;
    MyGdxGame game;
    ButtonSet buttons, keyboardButton;

    boolean keyboardUp = false;
    String keyString = "";
    char currentChar = 'A';
    CheckBoxScroller lobbyNamesScroller;

    MLobby(Mafia mafia) {
        r = mafia;
        game = r.game;
    }

    void initialize() {
        keyboardButton = new ButtonSet(r.game.batch);
        keyboardButton.addButton(new Button(r.iBack, 25,775) {
            public void onClick() {
                if (keyboardUp)
                    keyDown(KeyEvent.KEYCODE_BACK);
            } });
        keyboardButton.addButton(new Button(r.buttonImages, r.game.font, "A", 240, 320, 80, 80) {
            public void onClick() {
                if (keyString.length() < 16) keyString += currentChar;
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, ">", 340, 320, 80, 80) {
            public void onClick() {
                currentChar++;
                if (currentChar == 'Z' + 1) currentChar = 'A';
                if (currentChar == 'z' + 1) currentChar = 'a';
                keyboardButton.buttons.get(1).s = currentChar + "";
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, "<", 140, 320, 80, 80) {
            public void onClick() {
                currentChar--;
                if (currentChar == 'A' - 1) currentChar = 'Z';
                if (currentChar == 'a' - 1) currentChar = 'z';
                keyboardButton.buttons.get(1).s = currentChar + "";
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, "Space", 240, 240, 200, 40) {
            public void onClick() {
                if (keyString.length() < 16) keyString += ' ';
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, "Enter", 240, 90, 200, 40) {
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
        keyboardButton.addButton(new Button(r.buttonImages, game.font, "Delete", 240, 190, 200, 40) {
            public void onClick() {
                if (keyString.length() > 0) {
                    keyString = keyString.substring(0,keyString.length() - 1);
                }
            } });
        keyboardButton.addButton(new Button(r.buttonImages, game.font, "Shift", 240, 140, 200, 40) {
            public void onClick() {
                if (currentChar > 'Z') currentChar += -'a'+'A';
                else currentChar += 'a'-'A';
                keyboardButton.buttons.get(1).s = currentChar + "";
            } });

        buttons = new ButtonSet(game.batch);
        buttons.addButton(new Button(r.iBack, 25,775) {
            public void onClick() {
                if (r.sLobby)
                    r.leaveMain = true;
            } });
        buttons.addButton(new Button(r.iHelp, 455,775) {
            public void onClick() {
                r.helpScroll.showing = true;
                //game.font.setScale(0.8f);
                r.quitButton.showing = true;
                r.helpScroll.setText(r.helpLobby);
            } });
        buttons.addButton(new Button(r.buttonImages, game.font, "Start", 240, 520, 300, 80) {
            public void onClick() {
                buttons.showing = false;
                r.sLobby = false;
                lobbyNamesScroller.showing = false;
                r.generateGame(false);
                r.sRules = true;
                r.rules.initFromLobby();
                //r.learn.learnIndex = 0;
                //r.learn.learnButtons.showing = true;
                //r.learn.learnButtons.buttons.get(2).showing = false;
            } });

        ArrayList<CheckBox> lobbyNames = new ArrayList<CheckBox>();
        lobbyNames.add(new CheckBox(new TextureAtlas.AtlasRegion[]{r.iAdd,r.iAdd}, "Enter New Name", 0, 0));
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
        lobbyNames.add(new CheckBox(new TextureAtlas.AtlasRegion[] {r.iDel,r.iDel}, "Delete Checked Names", 0 ,0 ));
        lobbyNamesScroller = new CheckBoxScroller(lobbyNames, "Who is playing?", r.scroll, r.iBack, game.batch, game.font);
        lobbyNamesScroller.exitButton.showing = false;
        lobbyNamesScroller.showing = true;
    }

    void update() {
        game.platformHandler.ad(2);
        if (keyboardUp) {
            r.drawC("Enter a new name:", 240, 650);
            r.drawC("-" + keyString + "-", 240, 600);
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
            r.drawC("MAFIA", 240, 750);
            buttons.buttons.get(2).showing = numPlayers > 3;
            buttons.draw();
        }
    }

    void touchDown(int x, int y) {
        if (!keyboardUp) {
            lobbyNamesScroller.touchDown(x,y);
            buttons.touchDown(x,y);
            if (lobbyNamesScroller.checkBoxes.get(0).checked) {
                Gdx.input.setOnscreenKeyboardVisible(true);
                keyboardUp = true;
                Gdx.input.setCatchBackKey(true);
                lobbyNamesScroller.checkBoxes.get(0).checked = false;
            }
            if (lobbyNamesScroller.checkBoxes.get(lobbyNamesScroller.checkBoxes.size() - 1).checked) {
                lobbyNamesScroller.checkBoxes.get(lobbyNamesScroller.checkBoxes.size() - 1).checked = false;
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
            }
        }
        else {
            keyboardButton.touchDown(x, y);
        }
    }

    void touchUp(int x, int y) {
        if (!keyboardUp) {
            lobbyNamesScroller.touchUp(x,y);
            buttons.touchUp(x,y);
        }
        else {
            keyboardButton.touchUp(x, y);
        }
    }

    void touchDragged(int x, int y) {
        if (!keyboardUp) {
            buttons.touchDragged(x,y);
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
