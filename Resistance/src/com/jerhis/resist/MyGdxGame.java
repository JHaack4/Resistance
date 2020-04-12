package com.jerhis.resist;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class MyGdxGame extends Game implements ApplicationListener {

    String versionString = "v0.6.0";
    PlatformHandler platformHandler;
    public SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    static BitmapFont font, fontD, fontG, fontGS;
    Preferences prefs;
    public static boolean debug = false;
    public int numNames;
    public ArrayList<String> names;
    boolean adsEnabled = true;

    public MyGdxGame(PlatformInterface ph) {
        super();
        platformHandler = new PlatformHandler(ph);
    }

    @Override
    public void create() {

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        fontD = new BitmapFont(Gdx.files.internal("mono45white.fnt"));
        //fontD.setScale(2.25f, 2.25f);
        font = new BitmapFont(Gdx.files.internal("mono72white.fnt"));
        fontG = new BitmapFont(Gdx.files.internal("mono72green.fnt"));
        fontGS = new BitmapFont(Gdx.files.internal("mono45green.fnt"));
        //font.setScale(2.25f, 2.25f);
        //font.setColor(new Color(0,1,0,1));
        //font.setScale(0.5f);

        this.prefs = Gdx.app.getPreferences(".spymafgame2");
        names = new ArrayList<String>();
        numNames = prefs.getInteger("numNames", 0);
        for (int k = 0; k < numNames; k++) {
            String a = prefs.getString("name" + k, "%");
            if (!a.equals("%"))
                names.add(a);
        }

        adsEnabled = prefs.getBoolean("adsen", true);

        this.setScreen(new SplashScreen(this));
    }

    public void addName(String n) {
        names.add(n);
        prefs.putString("name" + numNames, n);
        numNames++;
        prefs.flush();
        prefs.putInteger("numNames", numNames);
        prefs.flush();
    }

    public void deleteName(String n) {
        for (int k = 0; k < numNames; k++) {
            String a = prefs.getString("name" + k, "%");
            if (a.equals(n)) {
                prefs.putString("name" + k, "%");
                prefs.flush();
                names.remove(a);
                return;
            }
        }
    }

    public void toggleAds() {
        adsEnabled = !adsEnabled;
        prefs.putBoolean("adsen", adsEnabled);
        prefs.flush();
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public void onBackPressed() {

    }

    /*
    public void highScore(int score) {
        //sets high score to current highscore
        if (score > highScore) {
            prefs.putString(fileNameHighScore, encrypt(score));
            prefs.flush();
            highScore = score;
        }
    }

    slider = prefs.getFloat(fileNameSlider, 0.8f);
     */
}
