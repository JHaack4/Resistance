package com.jerhis.resist;

import android.util.Log;
import android.view.KeyEvent;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Resistance implements Screen, InputProcessor {

    final MyGdxGame game;
    OrthographicCamera camera;

    RLobby lobby;
    RRules rules;
    RLearn learn;
    RGame rgame;
    RResults results;
    TextureAtlas textures;

    AtlasRegion buttonImages[], checkBox[], scroll[], iswap[], iBack, iHelp, iPass, iFail, iNeither, iAdd, iDel, iBackP, iHelpP;
    ButtonSet quitButton;
    TextScroller helpScroll;

    String missionNums[][] = new String[][]{{"0"},{"1"},{"2"},{"2","3","2","2","2"},{"3","2","2","3","3"}, // 0-4 1s
            {"2","3","2","3","3"} ,{"2","3","4","3","4"}, // 5-6 2s
            {"2","3","3","4*","4"}, {"3","4","4","5*","5"},{"3","4","4","5*","5"}, // 7-9 3s
            {"3","4","4","5*","5"}, {"3","4","5","5*","5"},{"3","4","5","6*","6"}, // 10-12 4s
            {"3","5*","6*","7*","6"},{"3","5*","6*","7*","6"}, {"3","5*","6*","7*","6"}, //13-15 5s
            {"5*","6*","6*","7*","7*"},{"5*","6*","6*","7*","7*"},{"5*","6*","6*","7*","7*"},{"5*","6*","6*","7*","7*"},{"5*","6*","6*","7*","7*"}}; //16-20 5s

    //TODO mission ordering variant
    ArrayList<RPlayer> players;
    int  numPlayers, numSpies, resultsDelay = MyGdxGame.debug ? 20 : 2000, addIndex;
    long  pfTime;
    boolean sLobby = true, sRules = false, sLearn = false, sGame = false, sResults = false;
    boolean leaving = false, leaveMain = false, showState = false, fail3 = false, pass3 = false, leaveNew = false;
    Sound tylerSound;

    public Resistance(final MyGdxGame gam) {
        game = gam;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1080, 1920);

        //game.font.setScale(1);
        game.platformHandler.wakeLock(0);

        textures = new TextureAtlas("images1.txt");
        buttonImages = new AtlasRegion[6];
        buttonImages[0] = textures.findRegion("bleftu");
        buttonImages[1] = textures.findRegion("bmidu");
        buttonImages[2] = textures.findRegion("brightu");
        buttonImages[3] = textures.findRegion("bleftp");
        buttonImages[4] = textures.findRegion("bmidp");
        buttonImages[5] = textures.findRegion("brightp");
        checkBox = new AtlasRegion[2];
        checkBox[0] = textures.findRegion("boxu");
        checkBox[1] = textures.findRegion("boxc");
        iswap = new AtlasRegion[2];
        iswap[0] = textures.findRegion("swapu");
        iswap[1] = textures.findRegion("swapc");
        scroll = new AtlasRegion[2];
        scroll[0] = textures.findRegion("scroll");
        scroll[1] = textures.findRegion("scrollbar");
        iBack = textures.findRegion("back");
        iHelp = textures.findRegion("help");
        iBackP = textures.findRegion("backp");
        iHelpP = textures.findRegion("helpp");
        iPass = textures.findRegion("pass");
        iFail = textures.findRegion("fail");
        iNeither = textures.findRegion("upcoming");
        iAdd = textures.findRegion("addname");
        iDel = textures.findRegion("delname");

        lobby = new RLobby(this);
        lobby.initialize();
        rules = new RRules(this);
        rules.initialize();
        learn = new RLearn(this);
        learn.initialize();
        rgame = new RGame(this);
        rgame.initialize();
        results = new RResults(this);
        results.initialize();

        helpScroll = new TextScroller("Replace this", "HELP", scroll, iBack,iBackP, game.batch, game.fontD, game.font) {
            @Override
            public void onExit() {
                //font.setScale(1f);
                quitButton.showing = false;
            } };

        quitButton = new ButtonSet(game.batch);
        quitButton.addButton(new Button(buttonImages, game.fontD, "Quit", 1080-56, 1920-56, 112, 112) {
            @Override
            public void onClick() {
                leaveMain = true;
            }
        });

        //yellow = new Texture(Gdx.files.internal("yellow0.png"));
        tylerSound = Gdx.audio.newSound(Gdx.files.internal("wtf.mp3"));
        //music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        //sound.play();
        //music.play();
        //int stored = game.prefs.getInteger("best", -1); //stored = -1 if there is nothing stored at "best"
        //game.prefs.putInteger("best", stored);
        //game.prefs.flush();
    }

    double roundTimer = 0.0;
    @Override
    public void render(float delta) {
        roundTimer += delta;
        int r = 0, b = 0, delay = resultsDelay;
        long time = System.currentTimeMillis() - pfTime - delay;
        if (pfTime != 0 && pfTime != -1 && time%2000 > 500 && time%2000 < 1500 && time > 0)
            if (rgame.passFail.get((int)(time/2000)) == 1)  b = 1;
            else r = 1;
        if (r == 1 && rules.cheatCode.equals("1101") && !rules.soundPlayed) {
            tylerSound.play(1.0f);
            rules.soundPlayed = true;
        }
        Gdx.gl.glClearColor(r,0,b,1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        if (MyGdxGame.debug) {
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (int x = 0; x < 1080; x+=100)
                for (int y = 0; y < 2000; y+=100)
                    game.shapeRenderer.rect(x,y,100,100,new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY));
            game.shapeRenderer.end();
        }

        game.batch.begin();

        if (helpScroll.showing) {
            helpScroll.draw(game.batch);
            quitButton.draw();
        }
        else if (sLobby) lobby.update();
        else if (sLearn) learn.update();
        else if (sGame) rgame.update(delay);
        else if (sResults) results.update();

        game.batch.end();

        if (leaveMain) {
            game.setScreen(new ScreenMainMenu(game));
            dispose();
        } else if (leaveNew) {
            Resistance resistance = new Resistance(game);
            //lobby names scroller check boxes
            for (int k = 0; k < lobby.lobbyNamesScroller.checkBoxes.size(); k++) {
                resistance.lobby.lobbyNamesScroller.checkBoxes.get(k).checked = lobby.lobbyNamesScroller.checkBoxes.get(k).checked;
            }

            //players
            resistance.players = new ArrayList<RPlayer>();
            for (int k = 0; k < players.size(); k++) {
                resistance.players.add(new RPlayer(players.get(k).name));
            }

            //rules
            resistance.rules.boxBlindSpies.checked = rules.boxBlindSpies.checked;
            resistance.rules.boxMerlin.checked = rules.boxMerlin.checked;
            resistance.rules.boxMordred.checked = rules.boxMordred.checked;
            resistance.rules.boxOberyn.checked = rules.boxOberyn.checked;
            resistance.rules.boxPerceval.checked = rules.boxPerceval.checked;

            resistance.lobby.lobbyButtons.showing = false;
            resistance.sLobby = false;
            resistance.lobby.lobbyNamesScroller.showing = false;
            resistance.generateGame(true);
            resistance.sLearn = true;
            resistance.learn.learnIndex = 0;
            resistance.learn.learnButtons.showing = true;
            resistance.learn.learnButtons.buttons.get(2).showing = false;
            resistance.learn.orderNames.clear();
            for (int k = 1; k <= resistance.players.size(); k++) {
                resistance.learn.orderNames.add(new CheckBox(resistance.iswap, k + ": " + resistance.players.get(k-1).name, 0, 0));
            }

            //screen
            game.setScreen(resistance);
            dispose();
        }
    }

    //--------------------------------------GAME GENERATION-------------------------------------------------
    public void generateGame(boolean repeat) {
        if (!repeat) {
            players = new ArrayList<RPlayer>();
            for (CheckBox c : lobby.lobbyNamesScroller.checkBoxes)
                if (c.checked) {
                    players.add(new RPlayer(c.text));
                }
        }

        rules.cheating = false;
        rules.cheatCode = "";
        numPlayers = players.size();
        numSpies = 0;
        roundTimer = 0.0;

        if (!repeat) {
            for (int k = 0; k < 10000; k++) {
                int a = (int)(Math.random()*numPlayers);
                players.add(players.remove(a));
            }
        }

        if (numPlayers == 3 || numPlayers == 4)
            numSpies = 1;
        if (numPlayers == 5 || numPlayers == 6)
            numSpies = 2;
        if (numPlayers == 7 || numPlayers == 8 || numPlayers == 9)
            numSpies = 3;
        if (numPlayers >= 10)
            numSpies = 4;
        if (numPlayers >= 13)
            numSpies = 5;
        learn.showTest = MyGdxGame.debug ? 99 : numSpies * 1000;

        for (RPlayer p: players)
            p.type = 0;
    }

    //Todo: all roles
    static final int GOOD = 0, MERLIN = 1, PERCEVAL = 2, TRICK_MERLIN = 31,
        SPY = -1, ASSASSIN = -2, MORGANA = -3, MORDRED = -4, OBERYN = -5;
    boolean assassinMordred = false, assassinMorgana = false;

    public void generateRoles() {
        rgame.failCount = 0;
        fail3 = false;
        pass3 = false;
        rgame.mission = 0;
        rgame.missionResults = new int[5];
        learn.learnIndex = 0;
        for (RPlayer p: players)
            p.type = GOOD;

        ArrayList<Integer> randomValues = new ArrayList<Integer>();
        int iVal = 0, useIndex = 0;
        while (iVal < numPlayers) {
            int iK = (int)(Math.random() * numPlayers);
            boolean once =false;
            for (Integer i: randomValues)
                if (i == iK) once = true;
            if (once) continue;

            randomValues.add(iK);
            iVal++;
        }

        if (rules.cheatCode.equals("0101")) {
            for (int iib: randomValues) {
                players.get(randomValues.get(useIndex++)).type = TRICK_MERLIN;
            }
            rules.trickMerlin = new int[numPlayers][numSpies];
            for (int a1 = 0; a1 < numPlayers; a1++) {
                for (int a2 = 0; a2 < numSpies; a2++) {
                    rules.trickMerlin[a1][a2] = -1;
                }
            }
            for (int zz = 0; zz < numPlayers; zz++) {
                int iVal2 = 0;
                while (iVal2 < numSpies) {
                    ///System.out.print(Arrays.toString(rules.trickMerlin[zz]));
                    //Scanner sc = new Scanner(System.in);
                    //sc.next();
                    int iK = (int)(Math.random() * numPlayers);
                    //System.out.print(iK +" z" + zz + " ");
                    boolean once = false;
                    for (Integer i: rules.trickMerlin[zz]) {
                        //System.out.print(i +" ");
                        if (i == iK || iK == zz) once = true;
                    }

                    if (once) continue;

                    rules.trickMerlin[zz][iVal2] = iK;
                    iVal2++;
                    //System.out.println("l"+iVal);
                }
            }
            return;
        }

        //Todo: all roles
        if (numSpies == 1) {
            int spyUses = 0;
            while (spyUses < numSpies) {
                players.get(randomValues.get(useIndex++)).type = SPY;
                spyUses++;
            }
        }
        if (numSpies >= 4) {
            int spyUses = 0;
            if (rules.boxMerlin.checked) {
                players.get(randomValues.get(useIndex++)).type = ASSASSIN;
                spyUses++;
                assassinMordred = false;
                assassinMorgana = false;
            }
            if (rules.boxMordred.checked) {
                players.get(randomValues.get(useIndex++)).type = MORDRED;
                spyUses++;
            }
            if (rules.boxPerceval.checked) {
                players.get(randomValues.get(useIndex++)).type = MORGANA;
                spyUses++;
            }
            if (rules.boxOberyn.checked) {
                players.get(randomValues.get(useIndex++)).type = OBERYN;
                spyUses++;
            }
            while (spyUses < numSpies) {
                players.get(randomValues.get(useIndex++)).type = SPY;
                spyUses++;
            }
        }
        else if (numSpies == 3) {
            int spyUses = 0;
            if (rules.boxMordred.checked) {
                players.get(randomValues.get(useIndex++)).type = MORDRED;
                spyUses++;
            }
            if (rules.boxPerceval.checked) {
                players.get(randomValues.get(useIndex++)).type = MORGANA;
                spyUses++;
            }
            if (rules.boxOberyn.checked) {
                players.get(randomValues.get(useIndex++)).type = OBERYN;
                spyUses++;
            }
            if (rules.boxMerlin.checked) {
                if (spyUses == numSpies){
                    assassinMordred = true;
                }
                else {
                    players.get(randomValues.get(useIndex++)).type = ASSASSIN;
                    spyUses++;
                    assassinMordred = false;
                    assassinMorgana = false;
                }
            }
            while (spyUses < numSpies) {
                players.get(randomValues.get(useIndex++)).type = SPY;
                spyUses++;
            }
        }
        else if (numSpies == 2) {
            int spyUses = 0;
            if (rules.boxMordred.checked) {
                players.get(randomValues.get(useIndex++)).type = MORDRED;
                spyUses++;
            }
            if (rules.boxPerceval.checked) {
                players.get(randomValues.get(useIndex++)).type = MORGANA;
                spyUses++;
            }
            if (rules.boxOberyn.checked && !(rules.boxPerceval.checked && rules.boxMordred.checked)) {
                players.get(randomValues.get(useIndex++)).type = OBERYN;
                spyUses++;
            }
            if (rules.boxMerlin.checked) {
                if (spyUses == numSpies){
                    if (rules.boxMordred.checked)
                        assassinMordred = true;
                    else if (rules.boxPerceval.checked)
                        assassinMorgana = true;
                }
                else {
                    players.get(randomValues.get(useIndex++)).type = ASSASSIN;
                    spyUses++;
                    assassinMordred = false;
                    assassinMorgana = false;
                }
            }
            while (spyUses < numSpies) {
                players.get(randomValues.get(useIndex++)).type = SPY;
                spyUses++;
            }
        }

        if (rules.boxMerlin.checked) players.get(randomValues.get(useIndex++)).type = MERLIN;
        if (rules.boxPerceval.checked) players.get(randomValues.get(useIndex++)).type = PERCEVAL;

        if (rules.cheatCode.equals("0000")) {
            if (rules.cheatPlayer.type != GOOD)
                generateRoles();
        }
        if (rules.cheatCode.equals("0001")) {
            if (rules.cheatPlayer.type != MERLIN && rules.boxMerlin.checked)
                generateRoles();
        }
        if (rules.cheatCode.equals("0010")) {
            if (rules.cheatPlayer.type != PERCEVAL && rules.boxPerceval.checked)
                generateRoles();
        }
        if (rules.cheatCode.equals("0011")) {
            if (rules.cheatPlayer.type >= 0)
                generateRoles();
        }
        if (rules.cheatCode.equals("0100")) {
            if (rules.cheatPlayer.type != OBERYN && rules.boxOberyn.checked)
                generateRoles();
        }
        if (rules.cheatCode.equals("0110")) {
            if (rules.cheatPlayer.type != MORDRED && rules.boxMordred.checked)
                generateRoles();
        }
        if (rules.cheatCode.equals("1011")) {
            game.platformHandler.ad(2);
            game.toggleAds();
        }
        if (rules.cheatCode.equals("1111")) {
            MyGdxGame.debug = !MyGdxGame.debug;
        }
    }

    public boolean canFail(int type) {
        return type < 0;
    }

    //--------------------------------------INPUT PROCESSING-------------------------------------------------
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        int x = (int) pos.x, y = (int) pos.y;

        if (helpScroll.showing) {
            helpScroll.touchDown(x,y);
            quitButton.touchDown(x,y);
        }
        else if (sLobby) lobby.touchDown(x,y);
        else if (sLearn) learn.touchDown(x,y);
        else if (sGame) rgame.touchDown(x,y);
        else if (sResults) results.touchDown(x,y);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        int x = (int) pos.x, y = (int) pos.y;

        if (helpScroll.showing) {
            helpScroll.touchUp(x,y);
            quitButton.touchUp(x,y);
        }
        else if (sLobby) lobby.touchUp(x,y);
        else if (sLearn) learn.touchUp(x,y);
        else if (sGame) rgame.touchUp(x,y);
        else if (sResults)  results.touchUp(x,y);

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        int x = (int) pos.x, y = (int) pos.y;

        if (helpScroll.showing) {
            helpScroll.touchDragged(x,y);
            quitButton.touchDragged(x,y);
        }
        else if (sLobby) lobby.touchDragged(x,y);
        else if (sLearn) learn.touchDragged(x,y);
        else if (sGame)  rgame.touchDragged(x,y);
        else if (sResults) results.touchDragged(x,y);

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            //Log.d("mytag", "here1");
            if (helpScroll.showing) {
                helpScroll.touchDown(10,790);
                helpScroll.touchUp(10,790);
                //quitButton.touchDragged(x,y);
            }
            else if (sLobby) {
                if (lobby.lobbyButtons.buttons.get(0).showing)
                    lobby.lobbyButtons.buttons.get(0).onClick();
            }
            else if (sLearn) {
                if (learn.learnButtons.buttons.get(0).showing) {
                    learn.learnButtons.buttons.get(0).onClick();
                   // Log.d("mytag", "here2");
                }
            }
            else if (sGame) {
                if (rgame.gameButtons.buttons.get(0).showing)
                    rgame.gameButtons.buttons.get(0).onClick();
            }
            else if (sResults) {
                //if (results.resultButtons.buttons.get(0).showing)
                    //results.resultButtons.buttons.get(0).onClick();
            }
            return false;
        }

        lobby.keyDown(keycode);

        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        lobby.keyTyped(character);

        return false;
    }

    //--------------------------------------UTILITIES-------------------------------------------------
    public void drawL(String s, int x, int y) {
        game.font.draw(game.batch, s,x,y+game.font.getBounds(s).height);
    }

    public void drawC(String s, int x, int y) {
        game.font.draw(game.batch, s,x-game.font.getBounds(s).width/2,y+game.font.getBounds(s).height);
    }

    String helpLobby = "    This is the lobby. Select the people who are playing using " +
            "the checkboxes. Enter a new name by clicking the pencil icon, or delete names with " +
            "the X button at the bottom. The recommended number of players is 5-10, though this " +
            "app allows for 5-20 players to play. Hit play to begin.",
            helpLearn = "    This is the learning phase of the game. When your name comes up, hit show to reveal your role." +
                    " Do not let anybody else see it! Hit next after you have memorized " +
                    "all of the information given to you, and then pass the phone to the next " +
                    "player." + "    ##Here are the Avalon roles in case you've forgotten: " +
                    " #--Merlin: Merlin is a Good Guy who knows who all the spies are. " +
                    "However, if the Spies can deduce who the Merlin is at the end of the game, then the Spies win instead." +
                    "#--Assassin: The Assassin is a Spy who will attempt (along with the other Spies) to figure out who Merlin is at the end " +
                    "of the game. If they guess correctly, the Spies steal the win." +
                    "#--Perceval: Percival is a Good Guy who is shown Merlin and Morgana. However, he won't known which is which, " +
                    "so he will try to figure this out to help the Good Guys win." +
                    "#--Morgana: Morgana is a Spy who is revealed to Perceval. She should try to confuse Perceval by acting like Merlin." +
                    "#--Mordred: Mordred is a Spy who is not revealed to Merlin." +
                    "#--Oberon: Oberon is a Spy who doesn't know who the other Spies are." +
                    "" +
                    "",
            helpLearn2 = "    This is the rule select screen. Use the checkboxes to add or remove roles" +
                    " from the game. You can also switch the order the players appear so that the " +
                    "names come up in clockwise order. Hit begin to initiate the learning phase of the game." + "  " +
                    "  ##Here are the Avalon roles in case you've forgotten: " +
                    " #--Merlin: Merlin is a Good Guy who knows who all the spies are. " +
                    "However, if the Spies can deduce who the Merlin is at the end of the game, then the Spies win instead." +
                    "#--Assassin: The Assassin is a Spy who will attempt (along with the other Spies) to figure out who Merlin is at the end " +
                    "of the game. If they guess correctly, the Spies steal the win." +
                    "#--Perceval: Percival is a Good Guy who is shown Merlin and Morgana. However, he won't known which is which, " +
                    "so he will try to figure this out to help the Good Guys win." +
                    "#--Morgana: Morgana is a Spy who is revealed to Perceval. She should try to confuse Perceval by acting like Merlin." +
                    "#--Mordred: Mordred is a Spy who is not revealed to Merlin." +
                    "#--Oberon: Oberon is a Spy who doesn't know who the other Spies are." +
                    "" +"#--Blind Spies: None of the spies know who the other spies are." +
                    "",
            helpGame0 = "    At this point of the game the current mission leader will recommend a team to go on a mission. " +
                    "Use the checkboxes to select this team, and feel free to discuss your choices with the other players. " +
                    "You are allowed to pick yourself to go on a mission. Hit finalize after you've selected the team. " +
                    "##    The results from the previous rounds are on the top of the screen, as well as the" +
                    " number of players to go on each mission. An asterisk (*) means that that mission takes two Spies" +
                    " to fail it. There is also a timer in case you want to set" +
                    " a time limit for each round, and a counter for the number of rejects for the " +
                    "current mission. Remember, if five proposals " +
                    "are rejected, the spies win!",
            helpGame1 = "    Here, all of the players will vote on the proposed mission, generally after " +
                    "discussing whether or not the players think it is a good team. When everyone is ready to vote, " +
                    "someone should count down from three. At the exact same time, all players will either " +
                    "give a thumbs up for Approve, or a flat palm down for Reject. (You can also use coins or other" +
                    " objects to vote.) The majority decides, and if there" +
                    " is a tie, the mission is rejected. After tallying the results, hit the corresponding button. Also, " +
                    "make sure every player keeps their votes up so everyone can see and discuss why people voted how they did!",
            helpGame2 = "NA",
            helpGame3 = "    When your name comes up, hit show to reveal the Pass/Fail options. Be careful, as they can be " +
                    "reversed. Good Guys will score a Pass regardless of which button they hit, and Spies can select Pass or Fail. " +
                    "Sometimes it is strategic for a Spy to pass a mission! ##    After everyone has voted, hold the phone so everyone " +
                    "can see it. Then the phone will flash blue for " +
                    "every Pass, and red for every Fail. The Fails will always come at the end.",
            helpGame30 = "    Here the Assassin will attempt to figure out who the Merlin is. He can discuss with the other Spies, " +
                    "but his decision will be the final one. If he guesses Merlin correctly, the Spies will steal the win!";

    //--------------------------------------UNUSED-------------------------------------------------
    @Override public boolean keyUp(int keycode) {return false;}
    @Override public boolean mouseMoved(int screenX, int screenY) {return false;}
    @Override public boolean scrolled(int amount) {return false;}
    @Override public void resize(int width, int height) {  }
    @Override public void show() { }
    @Override public void hide() {  }
    @Override public void pause() { }
    @Override public void resume() {  }
    @Override public void dispose() {
        textures.dispose();
        Gdx.input.setCatchBackKey(false);
        game.platformHandler.wakeLock(0);
    }
}
