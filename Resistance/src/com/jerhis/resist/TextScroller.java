package com.jerhis.resist;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.Arrays;

public abstract class TextScroller {

    public boolean showing;
    String text, lines[], title;
    int numLines = 0, maxHeight = -1, current = 0;
    TextureAtlas.AtlasRegion bar[], exit, exitP;
    BitmapFont font, titleFont;
    float lastSize;
    ButtonSet exitButton;
    float pad = 1.8f;

    public TextScroller(String text, String title, TextureAtlas.AtlasRegion bar[], TextureAtlas.AtlasRegion exit,TextureAtlas.AtlasRegion exitp, SpriteBatch batch, BitmapFont font, BitmapFont titleFont) {
        this.text = text + " # # # # # # # #";
        this.bar = bar;
        this.exit = exit;
        this.exitP = exitp;
        this.title = title;
        lines = new String[10000];
        showing = false;
        this.font = font;
        this.titleFont = titleFont;
        lastSize = font.getScaleX();
        exitButton = new ButtonSet(batch);
        exitButton.addButton(new Button(exit, exitP, exit.originalWidth/2,1920-exit.originalHeight/2) {
            public void onClick() {
                exit();
            } });
    }

    public abstract void onExit();

    public void setFont(BitmapFont f) {
        font = f;
        numLines = 0;
        maxHeight = -1;
        current = 0;
        int b = 0;
        for (int k = 0; k < text.length(); k++) {
            String t = text.substring(b, k+1);
            if (text.charAt(k) == '#') {
                lines[numLines++] = t.substring(0,t.length()-1);
                b = k+1;
            }
            else if (font.getBounds(t).width > 1080-115) {
                int end = t.lastIndexOf(' ');
                if (end != -1) {
                    lines[numLines++] = t.substring(0,end);
                    b += end+1;
                }
                else {
                    lines[numLines++] = "-";
                    b = k+1;
                }
            }
            else if (k == text.length() -1) {
                lines[numLines++] = t;
            }
        }
        lastSize = font.getScaleX();
    }

    public void draw(SpriteBatch batch) {
        if (numLines == 0 || lastSize != font.getScaleX()) {
            numLines = 0;
            maxHeight = -1;
            current = 0;
            int b = 0;
            for (int k = 0; k < text.length(); k++) {
                String t = text.substring(b, k+1);
                if (text.charAt(k) == '#') {
                    lines[numLines++] = t.substring(0,t.length()-1);
                    b = k+1;
                }
                else if (font.getBounds(t).width > 1080-115) {
                    int end = t.lastIndexOf(' ');
                    if (end != -1) {
                        lines[numLines++] = t.substring(0,end);
                        b += end+1;
                    }
                    else {
                        lines[numLines++] = "-";
                        b = k+1;
                    }
                }
                else if (k == text.length() -1) {
                    lines[numLines++] = t;
                }
            }
            lastSize = font.getScaleX();
            //System.out.println(numLines + " " + maxHeight + " " + current + "  " + Arrays.toString(lines));
        }

        if (showing) {
            exitButton.showing = true;
            titleFont.draw(batch,title, 1080/2 - titleFont.getBounds(title).width/2, 1920-75);
            exitButton.draw();

          //  System.out.println(current + " " + maxHeight + " " + numLines);

            for (int k = 0; k + current < numLines && (k < maxHeight+1||maxHeight==-1); k++) {
                font.draw(batch, lines[k+current], 50, 1920-225 - pad*font.getBounds("P").height*k);
                if (maxHeight == -1 && 1920-225 - pad*font.getBounds("P").height*k < pad*font.getBounds("P").height)
                    maxHeight = k;
            }

           // System.out.println(numLines + " " + maxHeight + " " + current + "  " + Arrays.toString(lines));
            if (numLines > maxHeight && maxHeight != -1) {
                //batch.draw(bar[0], 480-bar[0].originalWidth, 0);
                batch.draw(bar[1], 1080-bar[1].originalWidth/2, 1920-225 -bar[1].originalHeight - current*1.0f/(numLines-maxHeight)*(1920-225-bar[1].originalHeight));
            }
        }

    }

    public void setText(String s) {
        text = s;
        numLines = 0;
        maxHeight = -1;
        current = 0;
        int b = 0;
        for (int k = 0; k < text.length(); k++) {
            String t = text.substring(b, k+1);
            if (text.charAt(k) == '#') {
                lines[numLines++] = t.substring(0,t.length()-1);
                b = k+1;
            }
            else if (font.getBounds(t).width > 1080-115) {
                int end = t.lastIndexOf(' ');
                if (end != -1) {
                    lines[numLines++] = t.substring(0,end);
                    b += end+1;
                }
                else {
                    lines[numLines++] = "-";
                    b = k+1;
                }
            }
            else if (k == text.length() -1) {
                lines[numLines++] = t;
            }
        }
        lastSize = font.getScaleX();
    }

    public void touchDown(int x, int y) {
        //if (x > 430 && y < 50 && current < numLines - maxHeight)
        //    current++;
        //if (x > 430 && y < 700 && y > 650 && current > 0)
        //    current--;
        lastY = y;

        exitButton.touchDown(x,y);
    }
    int lastY = 800;
    public void touchDragged(int x, int y) {
        if (y > 32+lastY && current < numLines - maxHeight  && y < 1920-225 && maxHeight != -1) {
            current++;
            lastY = y;
        }
        if (y < -32+lastY && current > 0 && y < 1920-225 && maxHeight != -1) {
            current--;
            lastY = y;
        }

        exitButton.touchDragged(x,y);
    }
    public void touchUp(int x, int y) {
        exitButton.touchUp(x,y);
    }

    public void exit() {
        showing = false;
        exitButton.showing = false;
        onExit();
    }

}
