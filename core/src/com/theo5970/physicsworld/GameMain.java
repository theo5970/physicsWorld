package com.theo5970.physicsworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.theo5970.physicsworld.builder.box2d.BodyBuilder;
import com.theo5970.physicsworld.builder.box2d.FixtureDefBuilder;
import com.theo5970.physicsworld.builder.box2d.ShapeBuilder;
import com.theo5970.physicsworld.component.*;
import com.theo5970.physicsworld.controller.PlayerController;
import com.theo5970.physicsworld.controller.RenderController;
import com.theo5970.physicsworld.controller.UpdateController;
import com.theo5970.physicsworld.manager.*;

import java.util.ArrayList;
import java.util.List;


public class GameMain extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	Texture img2;
	Texture img3;
	Texture img4;

	FPSLogger fpsLogger;
	GameObject playerObject;

	World world;
	Box2DDebugRenderer debugRenderer;

	public void makeGround () {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0, 0);
		bodyDef.type = BodyDef.BodyType.StaticBody;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10000, 5);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0.1f;
		fixtureDef.friction = 0.8f;
		fixtureDef.shape = shape;
		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		shape.dispose();

		GameObject object = new GameObject();
		object.addComponent(new BodyComponent(body));
		body.setUserData(object);
	}

	public void makeWall () {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0, 5000);
		bodyDef.type = BodyDef.BodyType.StaticBody;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(5, 10000);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0.1f;
		fixtureDef.friction = 0.8f;
		fixtureDef.shape = shape;
		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		shape.dispose();

		GameObject object = new GameObject();
		object.addComponent(new BodyComponent(body));
		body.setUserData(object);
	}

	public Body makeBlock (BodyDef.BodyType bodyType, float x, float y, float w, float h) {
		PolygonShape shape = ShapeBuilder.buildAsBox(w, h);
		FixtureDef fixtureDef = FixtureDefBuilder.build(shape, false, 0.1f, 0, 0.3f);
		BodyBuilder bodyBuilder = new BodyBuilder();
		bodyBuilder.setPosition(x, y)
				.setBodyType(bodyType)
				.addFixtureDef(fixtureDef);
		return bodyBuilder.build(world);
	}

	public Body makeIncline (float x, float y, float w, float h, int rotation) {
		PolygonShape shape = ShapeBuilder.buildIncline(w, h, rotation);
		FixtureDef fixtureDef = FixtureDefBuilder.build(shape, false, 0.1f, 0, 0.3f);
		BodyBuilder bodyBuilder = new BodyBuilder();
		bodyBuilder.setPosition(x, y)
				.setBodyType(BodyDef.BodyType.StaticBody)
				.addFixtureDef(fixtureDef);
		return bodyBuilder.build(world);
	}

	public Body buildPlayerBody (float x, float y) {
		PolygonShape shape1 = ShapeBuilder.buildAsBox(4f, 4f);
		FixtureDef fixtureDef1 = FixtureDefBuilder.build(shape1, false, 0.1f, 0, 0);
		PolygonShape shape2 = ShapeBuilder.buildAsBox(1f, 0.5f, new Vector2(0, -2.0f));
		FixtureDef fixtureDef2 = FixtureDefBuilder.build(shape2, true, 0.1f, 0, 0);

		BodyBuilder bodyBuilder = new BodyBuilder();
		bodyBuilder.setPosition(x, y)
				.setBodyType(BodyDef.BodyType.DynamicBody)
				.setLinearDamping(0.5f)
				.setFixedRotation(true)
				.addFixtureDef(fixtureDef1)
				.addFixtureDef(fixtureDef2);
		return bodyBuilder.build(world);
	}

	public GameObject buildPlayer (float x, float y) {
		Body body = buildPlayerBody(x, y);
		GameObject object = new GameObject();
		object.addComponent(new TextureComponent(img));
		TransformComponent transformComponent = new TransformComponent();
		transformComponent.size.set(4, 4);
		object.addComponent(transformComponent);
		object.addComponent(new ColorComponent(Color.RED));
		object.addComponent(new BodyComponent(body));
		object.addComponent(new PlayerComponent());
		ParticleComponent particleComponent = new ParticleComponent(ParticleManager.getPool("jump"));
		particleComponent.enabled = false;
		object.addComponent(particleComponent);
		body.setUserData(object);
		return object;
	}

	public GameObject buildBullet (Vector2 position, Vector2 target, float lifetime) {
		CircleShape shape = ShapeBuilder.buildCircle(2f);
		FixtureDef fixtureDef = FixtureDefBuilder.build(shape, true, 0.1f, 0, 0.3f);
		BodyBuilder bodyBuilder = new BodyBuilder();
		bodyBuilder.setPosition(position)
				.setBodyType(BodyDef.BodyType.KinematicBody)
				.addFixtureDef(fixtureDef);
		Body body = bodyBuilder.build(world);
		GameObject gameObject = new GameObject();
		gameObject.addComponent(new TextureComponent(img3));
		TransformComponent transformComponent = new TransformComponent();
		transformComponent.position.add(0, 0, 1);
		transformComponent.size.set(4, 4);
		gameObject.addComponent(transformComponent);
		gameObject.addComponent(new ColorComponent(Color.ORANGE));
		gameObject.addComponent(new BodyComponent(body));
		gameObject.addComponent(new BulletComponent(target, MathUtils.random(0.5f, 1.0f), lifetime));
		gameObject.addComponent(new DamageComponent());
		gameObject.addComponent(new ParticleComponent(ParticleManager.getPool("bullet")));
		body.setUserData(gameObject);
		return gameObject;
	}

	public GameObject buildTestItem (float x, float y) {
		CircleShape shape = ShapeBuilder.buildCircle(2f);
		FixtureDef fixtureDef = FixtureDefBuilder.build(shape, true, 0.1f, 0, 0.3f);
		BodyBuilder bodyBuilder = new BodyBuilder();
		bodyBuilder.setPosition(x, y)
				.setBodyType(BodyDef.BodyType.StaticBody)
				.addFixtureDef(fixtureDef);
		Body body = bodyBuilder.build(world);
		GameObject gameObject = new GameObject();
		TransformComponent transformComponent = new TransformComponent();
		transformComponent.position.add(0, 0, 1);
		transformComponent.size.set(4, 4);

		gameObject.addComponent(transformComponent, new ColorComponent(Color.WHITE),
				new BodyComponent(body), new ItemComponent(), new TextureComponent(img4),
				new SmoothMoveComponent(2f), new ParticleComponent(ParticleManager.getPool("test")));
		body.setUserData(gameObject);
		return gameObject;
	}

	public Color randomColor () {
		return new Color(
				MathUtils.random(0.3f, 1.0f),
				MathUtils.random(0.3f, 1.0f),
				MathUtils.random(0.3f, 1.0f),
				1.0f);
	}

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		world = new World(new Vector2(0, -9.8f * 5), false);
		world.setContactListener(new ContactManager());
		debugRenderer = new Box2DDebugRenderer();

		CameraManager.init(8f);

		SoundManager.register("damage", "assets/damage.mp3");

		ParticleManager.register("test", "assets/test1.pack");
		ParticleManager.register("bullet", "assets/bullet.pack");
		ParticleManager.register("jump", "assets/player.pack");
		batch = new SpriteBatch();
		img = new Texture("assets/box.png");
		img2 = new Texture("assets/stair.png");
		img3 = new Texture("assets/bullet.png");
		img4 = new Texture("assets/circle.png");

		fpsLogger = new FPSLogger();
		makeWall();
		makeGround();

		playerObject = buildPlayer(80, 10);

		PlayerController.setPlayerObject(playerObject);
		ObjectManager.register(playerObject);

		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 100; y++) {

				float s = 8;
				float px = x * (s * 4) + 50;
				float py = y * (s * 2) + 30;
				Body body = makeBlock(BodyDef.BodyType.KinematicBody,
						px, py, s * 2, s);
				GameObject object = new GameObject();
				object.addComponent(new TextureComponent(img));
				object.addComponent(new ColorComponent(randomColor()));
				object.addComponent(new BodyComponent(body));
				TransformComponent transformComponent = new TransformComponent();
				transformComponent.position.add(0, 0, 2);
				transformComponent.size.set(s * 2, s);
				object.addComponent(transformComponent);
				body.setUserData(object);
				ObjectManager.register(object);

			}
		}
	}

	private static final Vector2 jumpImpulse = new Vector2(0, 100f);

	private float secTime = 0;

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(Gdx.graphics.getDeltaTime(), 3, 8);
		ParticleManager.update(Gdx.graphics.getDeltaTime());
		if (secTime >= 0.5f) {
			secTime = 0;

			Vector2 position = PlayerController.getPlayerPosition();
			ObjectManager.register(
					buildBullet(new Vector2(position).add(MathUtils.random(-200, 200), MathUtils.random(-200, 200)), position, MathUtils.random(7.0f, 10.0f)));

		} else {
			secTime += Gdx.graphics.getDeltaTime();
		}
		fpsLogger.log();

		ObjectManager.updateBeRemoved();
		CameraManager.updateAutomatic();
		PlayerController.update();

		for (GameObject object : ObjectManager.getList()) {
			UpdateController.update(object);
			if (object.hasComponent(TextureComponent.class)) {
				RenderController.addObject(object);
			}
		}
		batch.begin();
		batch.setProjectionMatrix(CameraManager.getCombined());
		RenderController.render(batch);
		batch.end();

	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public boolean keyDown (int keycode) {
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		return false;
	}

	private static final Vector2 impulseVec = new Vector2(0, 100);

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		for (GameObject object : ObjectManager.getList()) {
			if (object.hasComponent(BodyComponent.class)) {
				BodyComponent bodyComponent = object.getComponent(BodyComponent.class);
				Body body = bodyComponent.getBody();
				body.applyLinearImpulse(impulseVec, body.getPosition(), false);
			}
		}
		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		if (amount > 0) {
			CameraManager.zoomOut();
		} else {
			CameraManager.zoomIn();
		}
		return false;
	}
}
