package game.asteroids.input;

public class Input {

	//<editor-fold desc="KEY SETUP">
	public static final Key LEFT = new Key(com.badlogic.gdx.Input.Keys.LEFT);
	public static final Key RIGHT = new Key(com.badlogic.gdx.Input.Keys.RIGHT);
	public static final Key UP = new Key(com.badlogic.gdx.Input.Keys.UP);
	public static final Key DOWN = new Key(com.badlogic.gdx.Input.Keys.DOWN);

	public static final Key SPACE = new Key(com.badlogic.gdx.Input.Keys.SPACE);
	public static final Key ESCAPE = new Key(com.badlogic.gdx.Input.Keys.ESCAPE);
	public static final Key LSHIFT = new Key(com.badlogic.gdx.Input.Keys.SHIFT_LEFT);
	public static final Key LCTRL = new Key(com.badlogic.gdx.Input.Keys.CONTROL_LEFT);

	public static final Key W = new Key(com.badlogic.gdx.Input.Keys.W);
	public static final Key A = new Key(com.badlogic.gdx.Input.Keys.A);
	public static final Key S = new Key(com.badlogic.gdx.Input.Keys.S);
	public static final Key D = new Key(com.badlogic.gdx.Input.Keys.D);

	private static final Key[] keys = new Key[]{LEFT, RIGHT, UP, DOWN, SPACE, ESCAPE, LSHIFT, LCTRL, W, A, S, D};
	//</editor-fold>

	public static void update(){
		for (Key key : keys)
			key.updateStatus();
	}

	public static boolean keyDown(Key key){
		return key.getStatus() == KeyStatus.down;
	}
	public static boolean keyPressed(Key key){
		return key.getStatus() == KeyStatus.pressed;
	}
	public static boolean keyReleased(Key key){
		return key.getStatus() == KeyStatus.released;
	}
}
