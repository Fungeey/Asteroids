package game.asteroids.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GUI {
	private static BitmapFont font = new BitmapFont(Gdx.files.internal("consolas.fnt"), Gdx.files.internal("consolas.png"), false);
	private static GlyphLayout textLayout = new GlyphLayout();

	public static void draw(SpriteBatch batch){
		textLayout.setText(font, Player.score + "");
		font.draw(batch, textLayout, -500, 300);
	}

	private static void drawText(SpriteBatch batch, GlyphLayout text, float x, float y){
		font.draw(batch, text, x, y);
	}

	public static void dispose(){
		font.dispose();
	}
}
