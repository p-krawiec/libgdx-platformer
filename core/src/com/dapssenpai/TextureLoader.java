package com.dapssenpai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureLoader {

    Texture playerTexture;
    Texture blockTexture;

    TextureRegion blockSprite;

    Animation<TextureRegion> playerIdleLeft;
    Animation<TextureRegion> playerIdleRight;
    Animation<TextureRegion> playerWalkLeft;
    Animation<TextureRegion> playerWalkRight;
    Animation<TextureRegion> playerJumpLeft;
    Animation<TextureRegion> playerJumpRight;
    Animation<TextureRegion> playerFallLeft;
    Animation<TextureRegion> playerFallRight;

    TextureLoader() {
        playerTexture = new Texture(Gdx.files.internal("FrogSprites.png"));
        blockTexture = new Texture(Gdx.files.internal("tileset.png"));
        blockSprite = new TextureRegion(blockTexture);
        blockSprite.setRegion(32,32,32,32);

        createAnimations();
    }

    public Animation<TextureRegion> getPlayerAnimation(boolean lookingRight, int state) {
        Animation<TextureRegion> currentAnim = null;

        switch (state) {
            case 0: // IDLE
                if (lookingRight)
                    currentAnim = playerIdleRight;
                else
                    currentAnim = playerIdleLeft;
                break;
            case 1: // WALK
                if (lookingRight)
                    currentAnim = playerWalkRight;
                else
                    currentAnim = playerWalkLeft;
                break;
            case 2: // JUMP
                if (lookingRight)
                    currentAnim = playerJumpRight;
                else
                    currentAnim = playerJumpLeft;
                break;
            case 3: // FALL
                if (lookingRight)
                    currentAnim = playerFallRight;
                else
                    currentAnim = playerFallLeft;
                break;
        }

        return currentAnim;
    }

    private void createAnimations() {
        TextureRegion player = new TextureRegion(playerTexture);
        TextureRegion[][] split = player.split(32,32);
        TextureRegion[][] mirror = player.split(32,32); // MIRRORED VERSION OF SPRITE
        for (TextureRegion[] regionCol : mirror) // FLIP
            for (TextureRegion region : regionCol)
                region.flip(true, false);


        playerIdleRight = new Animation<>(0.1f, split[0][0], split[0][1], split[0][2], split[0][3], split[0][4], split[0][5], split[0][6], split[0][7], split[0][8], split[0][9], split[0][10]);
        playerIdleLeft = new Animation<>(0.1f, mirror[0][0], mirror[0][1], mirror[0][2], mirror[0][3], mirror[0][4], mirror[0][5], mirror[0][6], mirror[0][7], mirror[0][8], mirror[0][9], mirror[0][10]);
        playerWalkRight = new Animation<>(0.05f, split[1][0], split[1][1], split[1][2], split[1][3], split[1][4], split[1][5], split[1][6], split[1][7], split[1][8], split[1][9], split[1][10], split[1][11]);
        playerWalkLeft = new Animation<>(0.05f, mirror[1][0], mirror[1][1], mirror[1][2], mirror[1][3], mirror[1][4], mirror[1][5], mirror[1][6], mirror[1][7], mirror[1][8], mirror[1][9], mirror[1][10], mirror[1][11]);
        playerJumpRight = new Animation<>(0f, split[2][0]);
        playerJumpLeft = new Animation<>(0f, mirror[2][0]);
        playerFallRight = new Animation<>(0f, split[3][0]);
        playerFallLeft = new Animation<>(0f, mirror[3][0]);

    }

    public TextureRegion getBlockSprite() { return blockSprite; }

    public void dispose() {
        playerTexture.dispose();
        blockTexture.dispose();
    }
}
