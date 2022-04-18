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
    Rectangle hitbox;
    Rectangle bounds;
    int size;

    Vector2 velocity = new Vector2();
    float moveSpeed = 5f;
    float gravity = -10f;
    float jumpForce = 5f;
    boolean canJump;

    Player(float x, float y, int size) {
        hitbox = new Rectangle(x, y, size, size);
        bounds = new Rectangle(x, y, size, size);
        this.size = size;
    }

    public void update(int[][] world) {
        handleGravity(world);
        handleInput();
        if(!canMoveSideways(world))
            velocity.x = 0;

        hitbox.y += velocity.y;
        hitbox.x += velocity.x;

        bounds.set(hitbox);
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
        velocity.x = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x = -moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x = moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && canJump) {
            velocity.y = jumpForce;
            canJump = false;
        }
    }

    public boolean canMoveDown(int[][] world) {
        int x, y;

        if (hitbox.y < 0 || hitbox.x < 0 || hitbox.y >= 480 || hitbox.x >= 640)
            return true;

        x = (int) hitbox.x / size;
        y = (int)(hitbox.y + velocity.y) / size;

        return world[y][x] != 1;

    }

    public boolean canMoveSideways(int[][] world) {
        if (hitbox.y < 0 || hitbox.x < 0 || hitbox.y >= 480 || hitbox.x >= 640)
            return true;

        int x = 0;
        int y = (int)(hitbox.y + 1) / size;
        if (velocity.x > 0)
            x = (int)(hitbox.x + size + velocity.x) / size;
        else if (velocity.x < 0)
            x = (int)(hitbox.x + velocity.x) / size;

        return world[y][x] != 1;
    }

}
