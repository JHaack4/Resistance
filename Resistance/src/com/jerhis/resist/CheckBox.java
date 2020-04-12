package com.jerhis.resist;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class CheckBox {

    boolean checked, showing, dead;
    String text;
    int x, y;
    TextureAtlas.AtlasRegion box[];
    boolean specialMerlin = false;

    public CheckBox(TextureAtlas.AtlasRegion[] box, String text, int x, int y) {
        checked = false;
        showing = true;
        this.text = text;
        this.x = x;
        this.y = y;
        this.box = box;
        dead = false;
    }

    public void draw(SpriteBatch batch, BitmapFont font) {
        if (showing) {
            batch.draw(box[checked ? 1 : 0], x - box[0].originalWidth/2, y - box[0].originalHeight/2);
            font.draw(batch, text, x + box[0].originalWidth/2 + 12, y + 2.0f/3*font.getBounds(text).height);
        }
    }

    public void touchDown(int x, int y) {
        if (x < this.x + box[0].originalWidth/2 +200 && x > this.x - box[0].originalWidth/2 -10&&
                y < this.y + box[0].originalHeight/2 && y > this.y - box[0].originalHeight/2 && showing && !dead) //
            checked = !checked;
    }


}
