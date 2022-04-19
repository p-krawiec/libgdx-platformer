package com.dapssenpai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    static int IDLE = 0;
    static int WALK = 1;
    static int JUMP = 2;
    static int FALL = 3;

    Rectangle hitbox;
    Rectangle bounds;
    int gridSize;

    Vector2 velocity = new Vector2();
    float acceleration = 10f;
    float maxSpeed = 4f;
    float damp = 0.8f;
    float gravity = -10f;
    float jumpForce = 5f;
    boolean canJump;

    boolean isRight = true;
    int state = IDLE;

    Player(float x, float y, int gridSize) {
        hitbox = new Rectangle(x, y, gridSize*0.5f, gridSize*0.7f);
        bounds = new Rectangle(x, y, gridSize, gridSize);
        this.gridSize = gridSize;
    }

    public void update(int[][] world) {
        handleGravity(world);
        handleInput();

        if(!canMoveSideways(world))
            velocity.x = 0;

        // UPDATE STATE FOR SPRITE
        updateState();
        if(velocity.x < 0)
            isRight = false;
        else if(velocity.x > 0)
            isRight = true;

        System.out.println(velocity.x);

        hitbox.y += velocity.y;
        hitbox.x += velocity.x;

        bounds.y = hitbox.y;
        bounds.x = (hitbox.x + hitbox.width/2)-(bounds.width/2);
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

    public void handleGravity(int[][] world) {
        if (canMoveDown(world)) {
            velocity.y += gravity * Gdx.graphics.getDeltaTime();
        } else {
            velocity.y = 0;
            canJump = true;
        }
    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x += -acceleration * Gdx.graphics.getDeltaTime();
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x += acceleration * Gdx.graphics.getDeltaTime();
        } else {
            if(Math.abs(velocity.x) < 0.1)
                velocity.x = 0;
            velocity.x *= damp;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && canJump) {
            velocity.y = jumpForce;
            canJump = false;
        }

        if(velocity.x > maxSpeed)
            velocity.x = maxSpeed;
        if(velocity.x < -maxSpeed)
            velocity.x = -maxSpeed;
    }

    public boolean canMoveDown(int[][] world) {
        int xLeft, xRight, y;

        if (hitbox.y < 0 || hitbox.x < 0 || hitbox.y >= 480 || hitbox.x >= 640)
            return true;

        xLeft = (int) (hitbox.x / gridSize);
        xRight = (int) ((hitbox.x + hitbox.width) / gridSize);
        y = (int)(hitbox.y + velocity.y) / gridSize;



        return world[y][xLeft] != 1 && world[y][xRight] != 1;

    }

    public boolean canMoveSideways(int[][] world) {
        int x, yDown, yUp;
        if (hitbox.y < 0 || hitbox.x < 0 || hitbox.y >= 480 || hitbox.x >= 640)
            return true;

        yDown = (int)(hitbox.y + 1) / gridSize;
        yUp = (int)(hitbox.y + hitbox.height) / gridSize;
        if (velocity.x > 0)
            x = (int)(hitbox.x + hitbox.width + velocity.x) / gridSize;
        else
            x = (int)(hitbox.x + velocity.x) / gridSize;

        return world[yDown][x] != 1 && world[yUp][x] != 1;
    }

}
