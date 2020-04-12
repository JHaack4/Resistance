package com.jerhis.resist;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;


public class MainMenu implements Screen, InputProcessor {

    final MyGdxGame game;
    OrthographicCamera camera;
    int state = -2;
    boolean playing[] = new boolean[20];
    boolean merlin = false;
    boolean show = false;
    boolean goodWin = true;
    int numPlayers = 0;
    int numSpies = 0;
    long showTime;
    ArrayList<Player> players = new ArrayList<Player>();
    int failCount = 0;
    int[] missions = new int[5];
    String missionNums[][] = new String[][]{{"2","3","2","3","3"},{"2","3","3","3","4"},{"2","3","3","4*","4"},
                                            {"3","4","4","5*","5"},{"3","4","4","5*","5"},{"3","4","4","5*","5"}};
    String[] names = new String[]{"Josh","Sam","Jordan","Zeke","Tyler","Derek","Tanner","Gavin","Ryan","Joe",
                                "Patrick","Jace","12","13","14","15","16","17","18","19"};



    public MainMenu(final MyGdxGame gam) {
        game = gam;

        Gdx.input.setInputProcessor(this);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        for (int k = 0 ; k < 20; k ++)
            playing [k] = false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);


        for (int x = 0; x < 800; x+=100)
            for (int y = 0; y < 480; y+=100)
                game.shapeRenderer.rect(x,y,100,100,new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY),new Color(Color.DARK_GRAY));

        if (state == -2) {
            game.shapeRenderer.rect(10,10,20,20);
            game.shapeRenderer.rect(10,40,20,20);
            game.shapeRenderer.rect(10,70,20,20);
            game.shapeRenderer.rect(10,100,20,20);
            game.shapeRenderer.rect(10,130,20,20);
            game.shapeRenderer.rect(10,160,20,20);
            game.shapeRenderer.rect(10,190,20,20);
            game.shapeRenderer.rect(10,220,20,20);
            game.shapeRenderer.rect(10,250,20,20);
            game.shapeRenderer.rect(10,280,20,20);

            game.shapeRenderer.rect(210,10,20,20);
            game.shapeRenderer.rect(210,40,20,20);
            game.shapeRenderer.rect(210,70,20,20);
            game.shapeRenderer.rect(210,100,20,20);
            game.shapeRenderer.rect(210,130,20,20);
            game.shapeRenderer.rect(210,160,20,20);
            game.shapeRenderer.rect(210,190,20,20);
            game.shapeRenderer.rect(210,220,20,20);
            game.shapeRenderer.rect(210,250,20,20);
            game.shapeRenderer.rect(210,280,20,20);

            game.shapeRenderer.rect(550,120,200,100);
            game.shapeRenderer.rect(460,320,320,100);

            for (int k = 0; k < 10; k ++)
                if (playing[k])
                    game.shapeRenderer.rect(15,k*30 + 15,10,10);

            for (int k = 0; k < 10; k ++)
                if (playing[k + 10])
                    game.shapeRenderer.rect(215,k*30 + 15,10,10);


        }

        if (state >= -1) {
            game.shapeRenderer.rect(700, 0, 100, 100);
        }
        if (state == -4) {
            game.shapeRenderer.rect(200, 160, 400, 70);
            game.shapeRenderer.rect(700, 0, 100, 100);
        }
        if (state == -3) {
            game.shapeRenderer.rect(350, 160, 400, 70);
            game.shapeRenderer.rect(350, 260, 400, 70);
            game.shapeRenderer.rect(350, 360, 400, 70);
        }
        if (state == -5) game.shapeRenderer.rect(700, 0, 100, 100);
        game.shapeRenderer.end();

        if (state == -4) {
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (int k = 0; k < 5; k++) {
                if (missions[k] == 0) {
                    game.shapeRenderer.rect(360 + 100*(k-2), 300, 80, 80);
                }
                if (missions[k] == 1) {
                    game.shapeRenderer.setColor(0,0,1,1);
                    game.shapeRenderer.rect(360 + 100*(k-2), 300, 80, 80);
                    game.shapeRenderer.setColor(1,1,1,1);
                }
                if (missions[k] == 2) {
                    game.shapeRenderer.setColor(1,0,0,1);
                    game.shapeRenderer.rect(360 + 100*(k-2), 300, 80, 80);
                    game.shapeRenderer.setColor(1,1,1,1);
                }
            }
            game.shapeRenderer.end();
        }

        game.batch.begin();
        if (state == -2) {
            game.font.draw(game.batch, "Start", 600, 200);

            game.font.draw(game.batch, "Merlin: " + merlin, 500, 400);

            for (int k = 0; k < 10; k++)
                game.font.draw(game.batch, names[k], 40, 50+30*k);
            for (int k = 10; k < 20; k++)
                game.font.draw(game.batch, names[k], 240, 50+30*(k-10));

        }

        if (state == -1) {
            for (int k = 0; k < numPlayers; k++) {
                game.font.draw(game.batch, (k+1)+". " + players.get(k).name, 10, 50+30*k);
            }
            drawC("First leader is " + players.get(0).name, 400, 400);
        }

        if (state >= 0) {
           drawC("Player #" + (state+1) + ": " + players.get(state).name, 400, 400);
            if (show) {
                switch (players.get(state).type) {
                case 0:  drawC("You are a good guy.", 400, 300);
                    drawC("There are " +numSpies + " spies.", 400, 250); break;
                case 1:  drawC("You are a SPY. The spies are:", 400, 300);
                    String spies = "";
                    for (int k = 0; k < numPlayers; k++) {
                        if (players.get(k).type == 1)
                            spies += players.get(k).name + ", ";
                    }
                    drawC(spies.substring(0, spies.length() -2) + ".", 400, 250);
                    break;
                    case 2:  drawC("You are the MERLIN. The spies are:", 400, 300);
                        String spies2 = "";
                        for (int k = 0; k < numPlayers; k++) {
                            if (players.get(k).type == 1)
                                spies2 += players.get(k).name + ", ";
                        }
                        drawC(spies2.substring(0, spies2.length()-2) + ".", 400, 250);
                        break;
                }
                int countDown = (int)(2999-(System.currentTimeMillis() - showTime))/1000 + 1;
                if (countDown > 0)drawC(countDown + "", 750, 100);
            }
        }
        if (state == -4) {
            drawC("Fails: " + failCount, 400, 250);
            drawC("End", 750, 100);
            for (int k = 0; k < 5; k++)
                game.font.draw(game.batch, missionNums[numPlayers-5][k], 390+100*(k-2), 440);
        }
        if (state == -3) {
            drawC("Merlin: " + merlin, 550, 350);
            drawC("Same People", 550, 450);
            drawC("Reset", 550, 250);

            //if (goodWin) drawC("Good wins!", 550, 100);
            //else drawC("Spies win!", 550, 100);

            for (int k = 0; k < numPlayers; k++) {
                Player p = players.get(k);
                String t = "";
                if (p.type == 1) t = "Spy";
                else if (p.type == 2) t = "Merlin";
                else t = "Good";
                game.font.draw(game.batch, p.name, 10, 50+30*k);
                game.font.draw(game.batch, t, 210, 50+30*k);
            }
        }
        if (state == -5) {
            drawC("Guess the Merlin" , 400, 300);
            for (int k = 0; k < numPlayers; k++) {
                Player p = players.get(k);
                String t = "";
                if (p.type == 1) t = "Spy";
                else if (p.type == 2) t = "Good";
                else t = "Good";
                game.font.draw(game.batch, p.name, 10, 50+30*k);
                game.font.draw(game.batch, t, 210, 50+30*k);
            }
        }
        game.batch.end();
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

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        int x = (int) pos.x, y = (int) pos.y;

        if (state > -1 && show && x > 700 && y < 100 && System.currentTimeMillis() > showTime + 3000) {
            state++;
            show = false;
            if (state == numPlayers) state = -4;
        }
        else if (state > -1 && x > 700 && y < 100 && !show) {
            show = true;
            showTime = System.currentTimeMillis();
        }

        else if (state == -2) {
            if (x > 10 && x < 70 && y < 300)
                playing[y/30] = !playing[y/30];

            if (x > 210 && x < 270 && y < 300)
                playing[y/30 + 10] = !playing[y/30 + 10];

           if (x > 550 && x < 750 && y < 220 && y > 120) {
               state ++;
                for (int k = 0; k < 20; k++)
                    if (playing[k]) {
                        String name = names[k];
                        numPlayers++;
                        players.add(new Player(name));
                    }

               if (numPlayers <5 || numPlayers > 10) {
                   players.clear();
                   numPlayers = 0;
                   numSpies = 0;
                   state = -2;
                   return false;
               }

               for (int k = 0; k < 10000; k++) {
                   int a = (int)(Math.random()*numPlayers);

                   players.add(players.remove(a));
               }

               if (numPlayers == 5 || numPlayers == 6)
                   numSpies = 2;
               if (numPlayers == 7 || numPlayers == 8 || numPlayers == 9)
                   numSpies = 3;
               if (numPlayers == 10)
                   numSpies = 4;

               for (int k = 1; k <= numSpies; k++) {
                   players.get(k).type = 1;
               }
               if (merlin) players.get(0).type = 2;

               for (int k = 0; k < 10000; k++) {
                   int a = (int)(Math.random()*numPlayers);

                   players.add(players.remove(a));
               }
           }

            if (x > 460 && x < 780 && y < 420 && y > 320)
               merlin = !merlin;
        }

        else if (state == -1 && x > 700 && y < 100) state = 0;
        else if (state == -4) {
            if (x > 700 && y < 100) {
                int a = 0, b = 0;
                for (int k = 0; k < 5; k++) {
                    if (missions[k] ==1) a++;
                    else if (missions[k]==2) b++;
                }
                if (b > 2 || failCount >= 5) {
                    goodWin = false;
                    state = -3;
                }
                else if (a > 2) {
                    if (merlin) {
                        state = -5;
                    }
                    else {
                        goodWin = true;
                        state = -3;
                    }
                }
            }
            if (x > 200 && x < 600 && y > 160 && y < 240) failCount ++;
            for (int k = 0; k < 5; k ++)
                if (x > 350 + 100*(k-2) && x < 450 + 100*(k-2) && y > 300 && y < 400) {
                    missions[k]++;
                    missions[k] = missions[k]%3;
                    failCount = 0;
                }
        }
        else if (state == -3 && x > 350 && x < 750) {
            if (y > 160 && y < 230) {
                players.clear();
                numPlayers = 0;
                numSpies = 0;
                state = -2;
                showTime = 0;
                failCount = 0;
                missions = new int[5];
                show = false;
            }
            if (y > 260 && y < 330) {
                merlin = !merlin;
            }
            if (y > 360 && y < 430) {
                state = 0;
                showTime = 0;
                failCount = 0;
                missions = new int[5];
                show = false;

                for (Player p: players)
                    p.type = 0;

                if (merlin)
                    players.get((int)(Math.random()*numPlayers)).type = 2;

                int b = 0;
                while (b < numSpies) {
                    int a = (int)(Math.random()*numPlayers);
                    if (players.get(a).type==0) {
                        players.get(a).type = 1;
                        b++;
                    }
                }

            }
        }
        else if (state == -5 && x > 700 && y < 100) state = -3;


        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        int x = (int) pos.x, y = (int) pos.y;

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        int x = (int) pos.x, y = (int) pos.y;

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void onBackPressed() {

    }

    void drawC(String s, int x, int y) {
        game.font.draw(game.batch, s, x - game.font.getBounds(s).width/2, y - game.font.getBounds(s).height/2);
    }
}
