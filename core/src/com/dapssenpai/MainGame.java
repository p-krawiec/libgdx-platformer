package com.dapssenpai;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainGame extends ApplicationAdapter {
	static int WIDTH = 640;
	static int HEIGHT = 480;
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

	@Override
	public void create () {
		batch = new SpriteBatch();
		debugRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		fitViewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);

		textures = new TextureLoader();

		player = new Player(150,150, GRID_SIZE, 32);
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
	}

	@Override
	public void resize(int width, int height) {
		fitViewport.update(width, height);
	}

	@Override
	public void render () {
		ScreenUtils.clear(Color.SKY);
		camera.update();
		handleInput();

		player.update(world);
//		camera.position.x = player.hitbox.x;

		drawBatch();

		if(debug)
			renderDebug();
	}

	public void drawBatch() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for (int col = 0; col < WORLD_HEIGHT; col++) {
			for (int row = 0; row < WORLD_WIDTH; row++) {
				if (world[col][row] == 1) {
					batch.draw(textures.getBlockSprite(),row*32,col*32);
				}
			}
		}


		player.drawSprite(batch, textures.getPlayerTexture(player.isRight, player.state));

		batch.end();
	}

	public void renderDebug() {
		debugRenderer.setProjectionMatrix(camera.combined);
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
		textures.dispose();
	}


}
