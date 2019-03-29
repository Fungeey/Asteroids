package game.asteroids.utility;

public class Sprites {
	private static final String ENTITIES = "entities/";
	private static final String ICONS = "icons/";
	private static final String FILE_FORMAT = ".png";

	public static final String PLAYER_SPRITE = ENTITIES + "ship" + FILE_FORMAT;
	public static final String PLAYER_BURN = ENTITIES + "ship_burn" + FILE_FORMAT;

	public static final String SAUCER_LARGE_SPRITE_1 = ENTITIES + "saucer_large_1" + FILE_FORMAT;
	public static final String SAUCER_LARGE_SPRITE_2 = ENTITIES + "saucer_large_2" + FILE_FORMAT;
	public static final String SAUCER_SMALL_SPRITE_1 = ENTITIES + "saucer_small_1" + FILE_FORMAT;
	public static final String SAUCER_SMALL_SPRITE_2 = ENTITIES + "saucer_small_2" + FILE_FORMAT;

	public static final String BULLET_PLAYER = ENTITIES + "bullet_player" + FILE_FORMAT;
	public static final String BULLET_SAUCER = ENTITIES + "bullet_saucer" + FILE_FORMAT;

	public static final String ASTEROID_LARGE = ENTITIES + "asteroid_large" + FILE_FORMAT;
	public static final String ASTEROID_MEDIUM = ENTITIES + "asteroid_medium" + FILE_FORMAT;
	public static final String ASTEROID_SMALL = ENTITIES + "asteroid_small" + FILE_FORMAT;

	public static final String ARROW_KEYS = ICONS + "arrow_keys" + FILE_FORMAT;
	public static final String UP_KEY = ICONS + "up_key" + FILE_FORMAT;
	public static final String SPACEBAR = ICONS + "spacebar" + FILE_FORMAT;
	
	public static final float PIXELS_PER_METER = 32;
}
