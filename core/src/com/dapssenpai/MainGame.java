package com.dapssenpai;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainGame extends ApplicationAdapter {
	static int WIDTH = 640;
	static int HEIGHT = 360;
	static int GRID_SIZE = 32;
	static int WORLD_WIDTH = WIDTH / GRID_SIZE;
	static int WORLD_HEIGHT = HEIGHT / GRID_SIZE;

	SpriteBatch batch;
	ShapeRenderer debugRenderer;
	boolean debug;
	OrthographicCamera camera;
	FitViewport fitViewport;

	int[][] world = new int[WORLD_HEIGHT][WORLD_WIDTH];
	TextureLoader textures;

	Player player;
	float stateTime = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		debugRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(WIDTH,HEIGHT);
		camera.setToOrtho(false, WIDTH, HEIGHT);
		fitViewport = new FitViewport(WIDTH,HEIGHT, camera);

		textures = new TextureLoader();

		player = new Player(100,100, GRID_SIZE, 32);
		for (int col = 0; col < WORLD_HEIGHT; col++) {
			for (int row = 0; row < WORLD_WIDTH; row++) {
				if(row == 0 || row == WORLD_WIDTH-1 || col == 0 || col == WORLD_HEIGHT-1)
					world[col][row] = 1;
			}
		}

		world[1][7] = 1;
		world[1][8] = 1;
		world[1][9] = 1;
		world[2][8] = 1;

		world[3][11] = 1;
		world[3][12] = 1;
		world[4][13] = 1;

		world[7][3] = 1;
		world[7][4] = 1;
		world[7][5] = 1;
		world[6][6] = 1;
		world[6][7] = 1;
	}

	@Override
	public void resize(int width, int height) {
		fitViewport.update(width, height);
	}

	@Override
	public void render () {
		fitViewport.apply();
		ScreenUtils.clear(Color.SKY);
		handleInput();

		player.update(world);

		drawBatch();

		if(debug)
			renderDebug();

		stateTime += Gdx.graphics.getDeltaTime();
	}

	public void drawBatch() {
		batch.setProjectionMatrix(fitViewport.getCamera().combined);
		batch.begin();

		for (int col = 0; col < WORLD_HEIGHT; col++) {
			for (int row = 0; row < WORLD_WIDTH; row++) {
				if (world[col][row] == 1) {
					batch.draw(textures.getBlockSprite(),row*32,col*32);
				}
			}
		}

		Animation<TextureRegion> currentAnimation = textures.getPlayerAnimation(player.isRight, player.state);
		batch.draw(currentAnimation.getKeyFrame(stateTime, true), player.bounds.x ,player.bounds.y);

		batch.end();
	}

	public void renderDebug() {
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);

		debugRenderer.setColor(Color.BLACK);
		for (int col = 0; col < WORLD_HEIGHT; col++) {
			for (int row = 0; row < WORLD_WIDTH; row++) {
				if (world[col][row] == 1) {
					debugRenderer.rect(row*GRID_SIZE, col*GRID_SIZE, GRID_SIZE, GRID_SIZE);
				}
			}
		}

		player.drawDebug(debugRenderer);

		debugRenderer.end();
	}

	public void handleInput() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SEMICOLON))
			debug = !debug;
	}

	@Override
	public void dispose () {
		batch.dispose();
		debugRenderer.dispose();
		textures.dispose();
	}


}
