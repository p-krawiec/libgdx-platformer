package com.dapssenpai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    static int IDLE = 0;
    static int WALK = 1;
    static int JUMP = 2;
    static int FALL = 3;

    boolean left, right, jump;

    Rectangle hitbox;
    Rectangle bounds; // SPRITE BOUNDS
    int gridSize; // TILESET SIZE

    Vector2 velocity = new Vector2();
    float moveSpeed = 4f;
    float gravity = -10f;
    float jumpForce = 5f;
    boolean grounded;

    boolean isRight = true;
    int state = IDLE;

    Player(float x, float y, int gridSize, int spriteSize) {
        hitbox = new Rectangle(x, y, spriteSize*0.5f, spriteSize*0.7f);
        bounds = new Rectangle(x, y, spriteSize, spriteSize);
        this.gridSize = gridSize;
    }

    public void update(int[][] world) {
        velocity.x = 0;
        handleInput();

        if(jump) // PLAYER IS JUMPING
            jump();
        if(!left && !right && grounded) {
            updateState();
            return; // IF PLAYER IS NOT MOVING AT ALL
        }


        if (left && !right)
            velocity.x = -moveSpeed;
        if (right && !left)
            velocity.x = moveSpeed;

        if (grounded) {
            moveHorizontal(world);
            if (!wouldBeTouchingSolid(0,-1,world))
                grounded = false;
        } else {
            moveVertical(world);
            moveHorizontal(world);
        }

        updateState();

        bounds.y = hitbox.y;
        bounds.x = (hitbox.x + hitbox.width/2)-(bounds.width/2); // OFFSET FROM CENTER OF HITBOX
    }

    public void updateState() {
        if (velocity.y > 0)
            state = JUMP;
        else if (velocity.y < 0)
            state = FALL;
        else if (velocity.x != 0)
            state = WALK;
        else
            state = IDLE;

        if(velocity.x < 0)
            isRight = false;
        else if(velocity.x > 0)
            isRight = true;
    }

    public void drawDebug(ShapeRenderer renderer) {
        renderer.setColor(Color.RED);
        renderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

        renderer.setColor(Color.BLACK);
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void drawSprite(SpriteBatch batch, TextureRegion texture) {
        batch.draw(texture, bounds.x, bounds.y);
    }

    public void handleInput() {
        left = (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT));
        right = (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT));
        jump = (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP));
    }

    private void jump() {
        if(!grounded)
            return;
        grounded = false;
        velocity.y = jumpForce;
    }

    public void moveVertical(int[][] world) {
        if (!grounded) {
            velocity.y += gravity * Gdx.graphics.getDeltaTime();
            if (wouldBeTouchingSolid(0, velocity.y, world)) {
                if(velocity.y < 0) {
                    grounded = true;
                    hitbox.y = ((int)hitbox.y/gridSize)*gridSize;
                }
                velocity.y = 0;
            } else {
                hitbox.y += velocity.y;
            }
        } else {
            velocity.y = 0;
        }
    }

    public void moveHorizontal(int[][] world) {
        if (wouldBeTouchingSolid(velocity.x, 0, world)) {
            int xUnitPos = ((int) hitbox.x/gridSize)*gridSize;
            if (velocity.x > 0) // GOING RIGHT
                hitbox.x = xUnitPos + (gridSize-hitbox.width)-1;
            else
                hitbox.x = xUnitPos;
            velocity.x = 0;

        } else {
            hitbox.x += velocity.x;
        }
    }

    public boolean wouldBeTouchingSolid(float velX, float velY, int[][] world) {
        Rectangle tmp = new Rectangle(hitbox);
        tmp.x += velX;
        tmp.y += velY;

        if (!isSolid(tmp.x, tmp.y, world))
            if (!isSolid(tmp.x, tmp.y+tmp.height, world))
                if (!isSolid(tmp.x+tmp.width, tmp.y, world))
                    if (!isSolid(tmp.x+tmp.width, tmp.y+tmp.height, world))
                        return false;
        return true;
    }

    public boolean isSolid(float xPos, float yPos, int[][] world) {
        int x = (int) xPos / 32;
        int y = (int) yPos / 32;

        return world[y][x] == 1;
    }

}
