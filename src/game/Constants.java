package game;

import java.awt.Dimension;

public class Constants {
	
	// Can be modified in the future in a settings class/gui
	public final static Dimension SIZE=new Dimension(800, 600);
	public final static int SPRITE_SCALE=3;
	
	// Always constant
	public final static String GAME_TITLE="Game Title!";
	public final static String FONT_RESOURCE="/resources/graphic/font/RetGanon.ttf";
	public final static String TEXTBOX_SKIN_RESOURCE="skin";
	
	public final static double TARGET_FPS=60.0;
	public final static double TARGET_TIME_BETWEEN_UPDATES=1E9/TARGET_FPS;
	public final static int MAX_UPDATES_BEFORE_RENDER = 5;
	
	// Tile constants
	public final static int TILE_WIDTH=16;
	public final static int TILE_HEIGHT=16;
	
	// Entity constants, useful to write them as a fraction of the tile width.
	public final static double DEFAULT_ENTITY_MAX_VEL=3.4; // tiles per second
	public final static double DEFAULT_ENTITY_ANIMATION_DELAY=0.067; // in seconds
	public final static double DEFAULT_ENTITY_WIDTH=14.0/16.0;
	public final static double DEFAULT_ENTITY_HEIGHT=14.0/16.0;
	
	// Mob constants
	public final static double MOB_HURT_TIME=0.75; // in seconds
	public final static double MOB_KB_TIME=0.15;
	
	// Player constants
	public final static String PLAYER_SPRITESET_NAME="hero3";
	public final static int PLAYER_SPRITE_WIDTH=46;
	public final static int PLAYER_SPRITE_HEIGHT=46;
	public final static double PLAYER_MAX_VELOCITY=DEFAULT_ENTITY_MAX_VEL;
	public final static int PLAYER_START_HEALTH=20;
}
