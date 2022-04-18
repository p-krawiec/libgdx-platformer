package com.dapssenpai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureLoader {

    Texture playerIdleTexture;
    TextureRegion playerIdle;

    TextureLoader() {
        playerIdleTexture = new Texture(Gdx.files.internal("Ninja Frog/Idle (32x32).png"));
        playerIdle = new TextureRegion(playerIdleTexture);
        playerIdle.setRegion(0,0,32,32);
    }

    public TextureRegion getPlayerTexture() {
        return playerIdle;
    }

    public void dispose() {
        playerIdleTexture.dispose();
    }
}
