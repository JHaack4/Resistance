package com.jerhis.resist;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;
import java.util.Arrays;

public class CheckBoxScroller {

    public boolean showing;
    String  title;
    int maxHeight = -1, current = 0;
    TextureAtlas.AtlasRegion bar[], exit;
    BitmapFont font;
    float lastSize;
    ButtonSet exitButton;
    ArrayList<CheckBox> checkBoxes;
    public int top = 1000, bottom = 330;
    public float pad = 1.20f;

    public CheckBoxScroller(ArrayList<CheckBox> checkBoxes, String title, TextureAtlas.AtlasRegion bar[], TextureAtlas.AtlasRegion exit, SpriteBatch batch, BitmapFont font) {
        this.bar = bar;
        this.exit = exit;
        this.title = title;
        this.checkBoxes = checkBoxes;
        showing = false;
        this.font = font;
        lastSize = font.getScaleX();
        exitButton = new ButtonSet(batch);
        exitButton.addButton(new Button(exit, 56,1920-56) {
            public void onClick() {
                exit();
            } });

    }

    public void draw(SpriteBatch batch) {

        if (showing) {
            //exitButton.showing = true;
            font.draw(batch,title, 1080/2 - font.getBounds(title).width/2, top+135);
            exitButton.draw();

            //  System.out.println(current + " " + maxHeight + " " + numLines);

            for (CheckBox c: checkBoxes) c.showing = false;
            for (int k = 0; k + current < checkBoxes.size() && (k < maxHeight+1||maxHeight==-1); k++) {
                CheckBox c = checkBoxes.get(k + current);
                c.showing = true;
                int heightdiff = c.box[0].originalHeight;

                c.x = 68;
                c.y = (int)(top -heightdiff/2- pad*heightdiff*k);
                if (!c.dead) c.draw(batch, font);
                if (maxHeight == -1 && (top-bottom) - pad*heightdiff*k < pad*heightdiff)
                    maxHeight = k;
            }

            // System.out.println(numLines + " " + maxHeight + " " + current + "  " + Arrays.toString(lines));
            if (checkBoxes.size() > maxHeight && maxHeight != -1) {
                //batch.draw(bar[0], 480-bar[0].originalWidth, 0, 0, 0, bar[0].originalWidth, bar[0].originalHeight, 1, 4.0f/7, 0);
                //batch.draw(bar[1], 480-bar[0].originalWidth, 220 - current*1.0f/(checkBoxes.size()-maxHeight)*195);
                batch.draw(bar[1], 1080-bar[1].originalWidth/2, top -bar[1].originalHeight - current*1.0f/(checkBoxes.size()-maxHeight)*(top-bottom-bar[1].originalHeight));

            }
        }

    }

    public void touchDown(int x, int y) {
        if (!showing) return;
        //if (x > 430 && y < 50 && current < checkBoxes.size() - maxHeight)
        //    current++;
        //if (x > 430 && y < 400 && y > 350 && current > 0)
        //    current--;
        lastY = y;

        for (CheckBox c: checkBoxes) {
            if (c.showing && !c.dead) c.touchDown(x,y);
        }
        exitButton.touchDown(x,y);
    }
    int lastY = 1920;
    public void touchDragged(int x, int y) {
        if (!showing) return;
        if (y > 40+lastY && current < checkBoxes.size() - maxHeight && y < top+30 && maxHeight != -1) {
            current++;
            lastY = y;
        }
        if (y < -20+lastY && current > 0  && y < top+20 && maxHeight != -1) {
            current--;
            lastY = y;
        }

        exitButton.touchDragged(x,y);
    }
    public void touchUp(int x, int y) {
        if (!showing) return;
        exitButton.touchUp(x,y);
    }

    public void exit() {
        showing = false;
        exitButton.showing = false;
    }

}