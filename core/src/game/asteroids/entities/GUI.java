package game.asteroids.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GUI {
	private static BitmapFont font = new BitmapFont(Gdx.files.internal("consolas.fnt"), Gdx.files.internal("consolas.png"), false);
	private static GlyphLayout textLayout = new GlyphLayout();

	public static void draw(SpriteBatch batch){
		textLayout.setText(font, "Hello");
		// gdx.graphics.getwidth() is not accurate
		//
		drawText(batch, textLayout, Gdx.graphics.getWidth()/2, 0);
	}

	private static void drawText(SpriteBatch batch, GlyphLayout text, float x, float y){
		drawText(batch, text, x, y, TextAlign.CENTER);
	}

	private static void drawText(SpriteBatch batch, GlyphLayout text, float x, float y, TextAlign align){
		font.draw(batch, text, x, y);
	}

	private static enum TextAlign{
		TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT, CENTER
	}

	public static void dispose(){
		font.dispose();
	}
}
