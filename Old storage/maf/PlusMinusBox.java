package com.jerhis.resist;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PlusMinusBox {

    boolean showing, dead, canIncrement = true;
    String text;
    int value;
    int x, y;
    TextureAtlas.AtlasRegion pm[];

    public PlusMinusBox(TextureAtlas.AtlasRegion[] box, String text, int x, int y) {
        value = 0;
        showing = true;
        this.text = text;
        this.x = x;
        this.y = y;
        this.pm = box;
        dead = false;
    }

    public void draw(SpriteBatch batch, BitmapFont font) {
        if (showing) {
            batch.draw(pm[0], x - 3 - pm[0].originalWidth, y - pm[0].originalHeight/2);
            batch.draw(pm[1], x + 3, y - pm[0].originalHeight/2);
            font.draw(batch, text + value, x + pm[0].originalWidth + 5, y + 2.0f/3*font.getBounds(text).height);
        }
    }

    public void touchDown(int x, int y) {
        if (x < this.x && x > this.x - pm[0].originalWidth &&
                y < this.y + pm[0].originalHeight/2 && y > this.y - pm[0].originalHeight/2 && showing) {
            if (canIncrement) value ++;
            if (value < 0) value = 0;
        }
        if (x > this.x&& x < this.x + pm[0].originalWidth &&
                y < this.y + pm[0].originalHeight/2 && y > this.y - pm[0].originalHeight/2 && showing) {
            value --;
            if (value < 0) value = 0;
        }
    }

}
