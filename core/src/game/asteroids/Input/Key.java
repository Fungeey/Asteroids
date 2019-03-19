package game.asteroids.Input;

import com.badlogic.gdx.Gdx;

class Key {
	private int id;
	private KeyStatus status;

	Key(int id) {
		this.id = id;
		status = KeyStatus.up;
	}

	void updateStatus(){
		boolean pressed = Gdx.input.isKeyPressed(id);

		if (pressed) {
			if (status == KeyStatus.up)
				status = KeyStatus.pressed;
			if (status == KeyStatus.pressed)
				status = KeyStatus.down;
		} else {
			if (status == KeyStatus.down)
				status = KeyStatus.released;
			if (status == KeyStatus.released)
				status = KeyStatus.up;
		}
	}

	KeyStatus getStatus(){
		return this.status;
	}
}