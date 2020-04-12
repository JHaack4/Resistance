package com.jerhis.resist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Mafia implements Screen, InputProcessor {

    final MyGdxGame game;
    OrthographicCamera camera;

    MLobby lobby;
    MRules rules;
    MLearn learn;
    MGame mgame;
    MResults results;
    TextureAtlas textures;

    AtlasRegion buttonImages[], checkBox[], scroll[], iBack, iHelp, iPass, iFail, iNeither, iAdd, iDel, iPlus, iMinus, pm[];
    ButtonSet quitButton;
    TextScroller helpScroll;

    ArrayList<MPlayer> players;
    int  numPlayers, numMafia, numGood, numGoodAlive, numMafiaAlive;
    boolean sLobby = true, sRules = false, sLearn = false, sGame = false, sResults = false;
    boolean leaving = false, leaveMain = false, showState = false;

    public Mafia(final MyGdxGame gam) {
        game = gam;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        game.font.setScale(1);

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
        scroll = new AtlasRegion[2];
        scroll[0] = textures.findRegion("scroll");
        scroll[1] = textures.findRegion("scrollbar");
        iBack = textures.findRegion("back");
        iHelp = textures.findRegion("help");
        iPass = textures.findRegion("pass");
        iFail = textures.findRegion("fail");
        iNeither = textures.findRegion("upcoming");
        iAdd = textures.findRegion("addname");
        iDel = textures.findRegion("delname");
        iPlus = textures.findRegion("plus");
        iMinus = textures.findRegion("minus");
        pm = new AtlasRegion[2];
        pm[0] = iPlus;
        pm[1] = iMinus;

        lobby = new MLobby(this);
        lobby.initialize();
        rules = new MRules(this);
        rules.initialize();
        learn = new MLearn(this);
        learn.initialize();
        mgame = new MGame(this);
        mgame.initialize();
        results = new MResults(this);
        results.initialize();

        helpScroll = new TextScroller("Replace this", "HELP", scroll, iBack, game.batch, game.fontD, game.font) {
            @Override
            public void onExit() {
                //font.setScale(1f);
                quitButton.showing = false;
            } };

        quitButton = new ButtonSet(game.batch);
        quitButton.addButton(new Button(buttonImages, game.font, "Quit", 420, 760, 0, 60) {
            @Override
            public void onClick() {
                leaveMain = true;
            }
        });

        //sound = Gdx.audio.newSound(Gdx.files.internal("sound.mp3"));
        //music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        //sound.play();
        //music.play();

        //int stored = game.prefs.getInteger("best", -1); //stored = -1 if there is nothing stored at "best"
        //game.prefs.putInteger("best", stored);
        //game.prefs.flush();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        if (MyGdxGame.debug) {
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (int x = 0; x < 800; x+=100)
                for (int y = 0; y < 800; y+=100)
                    game.shapeRenderer.rect(x,y,100,100,new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY));
            game.shapeRenderer.end();
        }

        game.batch.begin();

        if (helpScroll.showing) {
            helpScroll.draw(game.batch);
            quitButton.draw();
        }
        else if (sLobby) lobby.update();
        else if (sRules) rules.update();
        else if (sLearn) learn.update();
        else if (sGame) mgame.update();
        else if (sResults) results.update();

        game.batch.end();

        if (leaveMain) {
            game.setScreen(new ScreenMainMenu(game));
            dispose();
        }
    }


    public void generateGame(boolean repeat) {
        if (!repeat) {
            players = new ArrayList<MPlayer>();
            for (CheckBox c : lobby.lobbyNamesScroller.checkBoxes)
                if (c.checked) {
                    players.add(new MPlayer(c.text));
                }
        }

        numPlayers = players.size();
        numMafia = 1;
        numGood = 1;
        numGoodAlive = 1;
        numMafiaAlive = 1;

        if (!repeat) {
            for (int k = 0; k < 10000; k++) {
                int a = (int)(Math.random()*numPlayers);
                players.add(players.remove(a));
            }
        }

        for (MPlayer p: players)
            p.type = 0;

    }

    public void generateRoles() {
        //rgame.failCount = 0;
        //fail3 = false;
        //pass3 = false;
        //rgame.mission = 0;
        //rgame.missionResults = new int[5];
       // learn.learnIndex = 0;
        for (MPlayer p: players) {
            p.living = true;
            p.type = 0;
        }
        //TODO: reset everything

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

        MPlayer.firstColorUse = (1+(int)(MPlayer.maxLoadedColor*Math.random()));
        MPlayer.colorUse = MPlayer.firstColorUse + 1;
        if (MPlayer.colorUse == MPlayer.maxLoadedColor+1) MPlayer.colorUse = 1;

        int uses = 0;
        for (int k = 0; k < rules.pmMafia.value; k++)
            players.get(randomValues.get(uses++)).setType(-1, -1);
        for (int k = 0; k < rules.pmRoleblocker.value; k++)
            players.get(randomValues.get(uses++)).setType(-2, rules.pmRoleblocker.value > 1 ? 1 : -1);
        for (int k = 0; k < rules.pmInvestigator.value; k++)
            players.get(randomValues.get(uses++)).setType(1, rules.pmInvestigator.value > 1 ? 1 : -1);
        for (int k = 0; k < rules.pmDoctor.value; k++)
            players.get(randomValues.get(uses++)).setType(2, rules.pmDoctor.value > 1 ? 1 : -1);
        for (int k = 0; k < rules.pmVigilante.value; k++)
            players.get(randomValues.get(uses++)).setType(3, rules.pmVigilante.value > 1 ? 1 : -1);
        for (int k = 0; k < rules.pmFool.value; k++)
            players.get(randomValues.get(uses++)).setType(4, rules.pmFool.value > 1 ? 1 : -1);


        numPlayers = players.size();
        numMafia = rules.pmRoleblocker.value + rules.pmMafia.value;
        numGood = numPlayers - numMafia;
        numGoodAlive = numGood;
        numMafiaAlive = numMafia;

    }

    public boolean isMafia(int type) {
        return type < 0;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override public void dispose() {
        textures.dispose();
        Gdx.input.setCatchBackKey(false);
    }

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
        else if (sRules) rules.touchDown(x,y);
        else if (sLearn) learn.touchDown(x,y);
        else if (sGame) mgame.touchDown(x,y);
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
        else if (sRules) rules.touchUp(x,y);
        else if (sLearn) learn.touchUp(x,y);
        else if (sGame) mgame.touchUp(x,y);
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
        else if (sRules) rules.touchDragged(x,y);
        else if (sLearn) learn.touchDragged(x,y);
        else if (sGame)  mgame.touchDragged(x,y);
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
                if (lobby.buttons.buttons.get(0).showing)
                    lobby.buttons.buttons.get(0).onClick();
            }
            else if (sRules) {
                if (rules.buttons.buttons.get(0).showing)
                    rules.buttons.buttons.get(0).onClick();
            }
            else if (sLearn) {
                if (learn.buttons.buttons.get(0).showing) {
                    learn.buttons.buttons.get(0).onClick();
                    // Log.d("mytag", "here2");
                }
            }
            else if (sGame) {
                if (mgame.gameButtons.buttons.get(0).showing)
                    mgame.gameButtons.buttons.get(0).onClick();
            }
            else if (sResults) {
                if (results.buttons.buttons.get(0).showing)
                    results.buttons.buttons.get(0).onClick();
            }
            return false;
        }

        lobby.keyDown(keycode);

        return false;
    }
    @Override
    public boolean keyUp(int keycode) {return false;}
    @Override
    public boolean keyTyped(char character) {
        lobby.keyTyped(character);

        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {return false;}
    @Override
    public boolean scrolled(int amount) {return false;}

    String helpLobby = "explain the maf lobby",
            helpRules = "explain maf rules",
            helpLearn = "explain the learning of maf roles",
            helpGame0 = "start",
            helpGame1 = "night",
            helpGame2 = "day";

    public void drawL(String s, int x, int y) {
        game.font.draw(game.batch, s,x,y+game.font.getBounds(s).height);
    }

    public void drawC(String s, int x, int y) {
        game.font.draw(game.batch, s,x-game.font.getBounds(s).width/2,y+game.font.getBounds(s).height);
    }

}
