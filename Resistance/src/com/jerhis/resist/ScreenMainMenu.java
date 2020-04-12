package com.jerhis.resist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class ScreenMainMenu implements Screen, InputProcessor {

    final MyGdxGame game;
    OrthographicCamera camera;
    int c = 1080/2;
    TextureAtlas textures;
    AtlasRegion buttonImages[], checkBox[], scroll[], iBack, iBackP, iHelp, iHelpP;
    ButtonSet mainButtons;
    TextScroller aboutScroll, mafRulesScroll, resRulesScroll;
    String debug = "d";

    boolean leaveRes = false, leaveMaf = false;


    public ScreenMainMenu(final MyGdxGame gam) {
        game = gam;

        Gdx.input.setInputProcessor(this);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1080, 1920);

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
        iBackP = textures.findRegion("backp");
        iHelp = textures.findRegion("help");
        iHelpP = textures.findRegion("helpp");

        game.platformHandler.wakeLock(0);

        mainButtons = new ButtonSet(game.batch);
        mainButtons.addButton(new Button(buttonImages, game.font, "Play Resistance", c, 1200, 800, 135) {
            public void onClick() {
                debug = "he";
                leaveRes = true;
            } });
        mainButtons.addButton(new Button(buttonImages, game.font, "Rules", c, 770, 0, 110) {
            public void onClick() {
                debug = "r1";
                resRulesScroll.showing = true;
                mainButtons.showing = false;
                //f//ont.setScale(0.8f);
            } });
        /*mainButtons.addButton(new Button(buttonImages, game.font, "Play Mafia", c, 350, 350, 60) {
            public void onClick() {
                debug = "me";
                leaveMaf = true;
            } });
        mainButtons.addButton(new Button(buttonImages, game.font, "Rules", c, 290, 0, 40) {
            public void onClick() {
                debug = "r2";
                mafRulesScroll.showing = true;
                mainButtons.showing = false;
               // font.setScale(0.8f);
            } });*/
        mainButtons.addButton(new Button(buttonImages, game.font, "About", c, 630, 0, 110) {
            public void onClick() {
                aboutScroll.showing = true;
                mainButtons.showing = false;
                //font.setScale(0.8f);
            } });

        aboutScroll = new TextScroller(aboutText, "ABOUT", scroll, iBack,iBackP, game.batch, game.fontD, game.font) {
            @Override
            public void onExit() {
                debug = "exited";
                mainButtons.showing = true;
                //font.setScale(1f);
            } };
        mafRulesScroll = new TextScroller(mafiaRules, "MAFIA RULES", scroll, iBack, iBackP, game.batch, game.fontD, game.font) {
            @Override
            public void onExit() {
                debug = "exited";
                mainButtons.showing = true;
                //font.setScale(1f);
            } };
        resRulesScroll = new TextScroller(resRules, "RESISTANCE RULES", scroll, iBack, iBackP,game.batch, game.fontD, game.font) {
            @Override
            public void onExit() {
                debug = "exited";
                mainButtons.showing = true;
                //font.setScale(1f);
            } };

        game.platformHandler.ad(1);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        if (MyGdxGame.debug) {
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (int x = 0; x < 1200; x+=100)
                for (int y = 0; y < 2000; y+=100)
                    game.shapeRenderer.rect(x,y,100,100,new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY));
            game.shapeRenderer.end();
        }

        game.batch.begin();
       // drawC("300,300",300,300);
       // if (MyGdxGame.debug) drawL(debug, 0,0);
        mainButtons.draw();
        aboutScroll.draw(game.batch);
        mafRulesScroll.draw(game.batch);
        resRulesScroll.draw(game.batch);
        game.batch.end();

        if (leaveRes) {
            game.setScreen(new Resistance(game));
            game.platformHandler.ad(2);
            dispose();
        }
        else if (leaveMaf) {
            //game.setScreen(new Mafia(game));
            //dispose();
        }

    }

    @Override
    public void resize(int i, int i2) {

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

    private void drawL(String s, int x, int y) {
        game.font.draw(game.batch, s,x,y+game.font.getBounds(s).height);
    }

    private void drawC(String s, int x, int y) {
        game.font.draw(game.batch, s,x-game.font.getBounds(s).width/2,y+game.font.getBounds(s).height);
    }

    @Override
    public void dispose() {
        textures.dispose();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        int x = (int) pos.x, y = (int) pos.y;

        mainButtons.touchDown(x,y);
        aboutScroll.touchDown(x,y);
        mafRulesScroll.touchDown(x,y);
        resRulesScroll.touchDown(x,y);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        int x = (int) pos.x, y = (int) pos.y;

        mainButtons.touchUp(x,y);
        aboutScroll.touchUp(x,y);
        mafRulesScroll.touchUp(x,y);
        resRulesScroll.touchUp(x,y);

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        int x = (int) pos.x, y = (int) pos.y;

        mainButtons.touchDragged(x,y);
        aboutScroll.touchDragged(x,y);
        mafRulesScroll.touchDragged(x,y);
        resRulesScroll.touchDragged(x,y);

        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }

    String resRules = "    Resistance is a party game where players try to figure out everyone else's identities." +
            " It is similar to Mafia and Werewolf, where a small, secret group of " +
            "informed Spies try to sabotage the larger group of Good Guys, while the " +
            "Good Guys attempt to identify the Spies. That is, the Good Guys don't know anyone else's roles, " +
            "but the Spies know who the other Spies are. At the start of the game, one third of the group is randomly " +
            "and secretly chosen to be Spies, while the rest of the group become Good Guys. " +
            "" +
            "The only thing the Good Guys know is how many spies there are, not who they are. " +
            "The game is best played in a circle, with no outside communication allowed (" +
            "i.e. two players cannot discuss something privately or text each other information). " +
            "Players are never allowed to show someone else their private role screen or their private pass/fail screen." +
            "#    At the start, one player is randomly chosen to be the Mission Leader. The Leader switches each mission. " +
            "The Leader selects a given " +
            "number of players to send out on a mission, and should discuss his choices " +
            "with everyone else. The Leader may choose to go out on" +
            " the mission himself. All of the players then discuss the Leader's choice " +
            "and simultaneously vote on whether to accept or reject the proposed team. To vote," +
            " players will signal thumbs up for approve, or a face-down palm for reject. (You can also " +
            "use coins or some other object for the voting.) " +
            "If a majority of players reject the proposal (or if it is a tie), leadership passes to the player " +
            "to the left, who then proposes their own mission. " +
            "This continues until a majority of players approve the current Leader's mission" +
            ". After five rejected mission proposals in a row, Spies automatically win the game. " +
            "Once a mission team is approved, the players will go on the mission. To do so, " +
            "players on the mission are " +
            "passed the phone, and asked to choose Pass or Fail. Good Guys can only pass missions, " +
            "while the Spies may either pass or " +
            "fail missions. The results are then revealed. If everyone selects pass, " +
            "the Good Guys earn one point. If even one card shows Fail, the spy team has sabotaged the " +
            "mission and earns one point (except for certain cases on Mission 4, where it may be necessary for " +
            "2 Fail cards to be played in order for the mission to fail. These are marked by an asterisk)." +
            "#" +
            "    The game continues until one team earns 3 points.##    There are also some other roles that can be " +
            "added to make the game more complex. #--Merlin: Merlin is a Good Guy who knows who all the spies are. " +
            "However, if the Spies can deduce who the Merlin is at the end of the game, then the Spies win instead." +
            "#--Assassin: The Assassin is a Spy who will attempt (along with the other Spies) to figure out who Merlin is at the end " +
            "of the game. If they guess correctly, the Spies steal the win." +
            "#--Perceval: Percival is a Good Guy who is shown Merlin and Morgana. However, he won't known which is which, " +
            "so he will try to figure this out to help the Good Guys win." +
            "#--Morgana: Morgana is a Spy who is revealed to Perceval. She should try to confuse Perceval by acting like Merlin." +
            "#--Mordred: Mordred is a Spy who is not revealed to Merlin." +
            "#--Oberon: Oberon is a Spy who doesn't know who the other Spies are." +
            "#--Blind Spies: None of the spies know who the other spies are." +
            "" +
            "##Tip: If one team is winning too often, try changing the roles a little bit. Adding Merlin and Perceval will strengthen" +
            " the Good Guys, while adding Mordred will strengthen the Spies. Also, turning on Oberon or Blind Spies will make it more difficult" +
            " for the Spies to coordinate.",
            mafiaRules = "sfewfmafia#3#333####3###3#3####333333333#3#3#3#333#3#3#3#3#3#3#3#3#3#3#3#3#3#3#3#3",
            aboutText = "    This app removes the need for playing cards and prevents cheating " +
                    "in Resistance and Avalon. It simplifies the learning phase of the game by removing the need for everyone to " +
                    "close their eyes. It also keeps track of who is the round leader, records the current mission passes and fails, " +
                    "and removes the possibility of players cheating by carefully watching " +
                    "other players select their pass and fail cards. ##    This app was created by Jerhis Studios.#Programmer: " +
                    "Jordan Haack#Graphics: Ike Schwab#     " +
                    "                          v1.0" +
                    "##    Dedicated to Tyler; he kept cheating so we had to make this app.";
}

/*
debug on main menu screen
countdown timer


 */