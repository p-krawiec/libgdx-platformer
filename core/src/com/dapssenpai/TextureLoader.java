package com.dapssenpai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureLoader {

    Texture playerTexture;
    Texture blockTexture;

    TextureRegion playerSprite;
    boolean lookingRight = true;
    TextureRegion blockSprite;

    TextureLoader() {
        playerTexture = new Texture(Gdx.files.internal("FrogSprites.png"));
        blockTexture = new Texture(Gdx.files.internal("tileset.png"));
        playerSprite = new TextureRegion(playerTexture);
        playerSprite.setRegion(0,0,32,32);
        blockSprite = new TextureRegion(blockTexture);
        blockSprite.setRegion(32,32,32,32);
    }

    public TextureRegion getPlayerTexture(boolean lR, int state) {
        setPlayerState(state);
        if(lookingRight) {
            if (!lR) {
                lookingRight = false;
                playerSprite.flip(true,false);
            }
        } else {
            if (lR) {
                lookingRight = true;
                playerSprite.flip(true, false);
            }
        }
        return playerSprite;
    }

    public void setPlayerState(int state) {
        playerSprite.setRegion(0,state*32,32,32);
        lookingRight = true;
    }

    public TextureRegion getBlockSprite() { return blockSprite; }

    public void dispose() {
        playerTexture.dispose();
        blockTexture.dispose();
    }
}
