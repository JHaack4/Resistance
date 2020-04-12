package com.jerhis.resist;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class Button {

    int x, y, w, h;
    String s;
    boolean pushed;
    TextureAtlas.AtlasRegion open = null, push = null;
    boolean showing;
    TextureAtlas.AtlasRegion[] buttonImages = null;
    BitmapFont font = null;

    public Button(TextureAtlas.AtlasRegion[] buttonImages, BitmapFont font, String s, int x, int y, int w, int h) {
        pushed = false;
        this.x = x;
        this.y = y;
        this.s = s;
        this.font = font;
        this.w = w;
        if (w == 0)
            this.w = (int)(font.getBounds(s).width + buttonImages[0].originalWidth*15*((float)h)/buttonImages[1].originalHeight);
        this.h = h;
        showing = true;
        this.buttonImages = buttonImages;
    }

    public Button(TextureAtlas.AtlasRegion open, TextureAtlas.AtlasRegion push, int x, int y) {
        pushed = false;
        this.x = x;
        this.y = y;
        this.open = open;
        this.push = push;
        w = open.originalWidth;
        h = open.originalHeight;
        showing = true;
    }

    public Button(TextureAtlas.AtlasRegion open , int x, int y) {
        pushed = false;
        this.x = x;
        this.y = y;
        this.open = open;
        w = open.originalWidth;
        h = open.originalHeight;
        showing = true;
    }

    public abstract void onClick();

    public void draw(SpriteBatch batch) {

        if (!showing) return;
        if (open == null) {
          Button b = this;
          if (b.pushed) {
              batch.draw(buttonImages[4], b.x-(b.w/2-buttonImages[0].originalWidth*((float)b.h)/buttonImages[1].originalHeight),
                      b.y-b.h/2, 0, 0, buttonImages[1].originalWidth, buttonImages[1].originalHeight,
                      ((float)b.w - ((float)b.h)/buttonImages[1].originalHeight*(buttonImages[2].originalWidth + buttonImages[0].originalWidth))/buttonImages[0].originalWidth,
                      ((float)b.h)/buttonImages[1].originalHeight, 0);
              batch.draw(buttonImages[3], b.x - b.w/2, b.y - b.h/2, 0, 0, buttonImages[0].originalWidth, buttonImages[0].originalHeight,
                      ((float)b.h)/buttonImages[1].originalHeight,((float)b.h)/buttonImages[1].originalHeight, 0);
              batch.draw(buttonImages[5], b.x + b.w/2 - buttonImages[2].originalWidth*((float)b.h)/buttonImages[1].originalHeight,
                      b.y - b.h/2, 0, 0, buttonImages[2].originalWidth, buttonImages[2].originalHeight,
                      ((float)b.h)/buttonImages[1].originalHeight,((float)b.h)/buttonImages[1].originalHeight, 0);
              if (font.equals(MyGdxGame.fontD))
                MyGdxGame.fontGS.draw(batch, b.s, b.x - font.getBounds(b.s).width/2, b.y + font.getBounds(b.s).height- font.getBounds(b.s).height/3);
              else
                MyGdxGame.fontG.draw(batch, b.s, b.x - font.getBounds(b.s).width/2, b.y + font.getBounds(b.s).height- font.getBounds(b.s).height/3);

          }
          else {
            batch.draw(buttonImages[1], b.x-(b.w/2-buttonImages[0].originalWidth*((float)b.h)/buttonImages[1].originalHeight),
                    b.y-b.h/2, 0, 0, buttonImages[1].originalWidth, buttonImages[1].originalHeight,
                    ((float)b.w - ((float)b.h)/buttonImages[1].originalHeight*(buttonImages[2].originalWidth + buttonImages[0].originalWidth))/buttonImages[0].originalWidth,
                    ((float)b.h)/buttonImages[1].originalHeight, 0);
            batch.draw(buttonImages[0], b.x - b.w/2, b.y - b.h/2, 0, 0, buttonImages[0].originalWidth, buttonImages[0].originalHeight,
                    ((float)b.h)/buttonImages[1].originalHeight,((float)b.h)/buttonImages[1].originalHeight, 0);
            batch.draw(buttonImages[2], b.x + b.w/2 - buttonImages[2].originalWidth*((float)b.h)/buttonImages[1].originalHeight,
                    b.y - b.h/2, 0, 0, buttonImages[2].originalWidth, buttonImages[2].originalHeight,
                    ((float)b.h)/buttonImages[1].originalHeight,((float)b.h)/buttonImages[1].originalHeight, 0);
            font.draw(batch, b.s, b.x - font.getBounds(b.s).width/2, b.y + font.getBounds(b.s).height- font.getBounds(b.s).height/3);
          }
        }
        else if (push == null) {
            if (pushed)
                batch.draw(open, x - (w/2)*0.8f, y - (h/2)*0.8f, 0,0,w,h,0.8f,0.8f,0);
            else batch.draw(open, x - w/2, y - h/2, 0,0,w,h,1,1,0);
        }
        else {
            if (pushed)
                batch.draw(push, x - push.originalWidth/2, y - push.originalHeight/2, 0,0,w,h,1,1,0);
            else batch.draw(open, x - w/2, y - h/2, 0,0,w,h,1,1,0);
        }
    }

    public void checkWidth() {
        this.w = (int)(font.getBounds(s).width + buttonImages[0].originalWidth*15*((float)h)/buttonImages[1].originalHeight);
    }

}