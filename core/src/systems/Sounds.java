package systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

	public static final Sound PLAYER_SHOOT = Gdx.audio.newSound(Gdx.files.internal("sounds/player_shoot.wav"));
	public static final Sound PLAYER_JUMP = Gdx.audio.newSound(Gdx.files.internal("sounds/hyperjump.wav"));
	public static final Sound PLAYER_DEATH = Gdx.audio.newSound(Gdx.files.internal("sounds/player_death.wav"));
	public static final Sound PLAYER_THRUST = Gdx.audio.newSound(Gdx.files.internal("sounds/player_thrust.wav"));

	public static final Sound SAUCER_APPEAR = Gdx.audio.newSound(Gdx.files.internal("sounds/saucer_appear.wav"));
	public static final Sound SAUCER_EXPLOSION = Gdx.audio.newSound(Gdx.files.internal("sounds/saucer_explosion.wav"));
	public static final Sound SAUCER_SHOOT_1 = Gdx.audio.newSound(Gdx.files.internal("sounds/saucer_shoot_1.wav"));
	public static final Sound SAUCER_SHOOT_2 = Gdx.audio.newSound(Gdx.files.internal("sounds/saucer_shoot_2.wav"));
	public static final Sound SAUCER_CHANGE_1 = Gdx.audio.newSound(Gdx.files.internal("sounds/saucer_change_1.wav"));
	public static final Sound SAUCER_CHANGE_2 = Gdx.audio.newSound(Gdx.files.internal("sounds/saucer_change_2.wav"));

	public static final Sound EXPLOSION_1 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion_1.wav"));
	public static final Sound EXPLOSION_2 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion_2.wav"));
	public static final Sound EXPLOSION_3 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion_3.wav"));

	public static final Sound GAME_TRANSITION_1 = Gdx.audio.newSound(Gdx.files.internal("sounds/game_transition_1.wav"));
	public static final Sound GAME_TRANSITION_2 = Gdx.audio.newSound(Gdx.files.internal("sounds/game_transition_1.wav"));


	public static void play(Sound sound){
		sound.play(1.0f);
	}

	public static void dispose(){
		PLAYER_SHOOT.dispose();
		PLAYER_JUMP.dispose();
		PLAYER_DEATH.dispose();
		PLAYER_THRUST.dispose();

		SAUCER_EXPLOSION.dispose();
		SAUCER_SHOOT_1.dispose();
		SAUCER_SHOOT_2.dispose();
		SAUCER_CHANGE_1.dispose();
		SAUCER_CHANGE_2.dispose();

		EXPLOSION_1.dispose();
		EXPLOSION_2.dispose();
		EXPLOSION_3.dispose();

		GAME_TRANSITION_1.dispose();
		GAME_TRANSITION_2.dispose();
	}
}
