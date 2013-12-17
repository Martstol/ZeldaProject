package game.graphic;

import game.GameComponent;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class SpriteLoader {
	
	public static final String DEFAULT_RESOURCE_FOLDER="resources";
	public static final String DEFAULT_GRAPHIC_FOLDER="graphic";
	public static final String DEFAULT_SPRITESET_FOLDER="spriteset";
	public static final String DEFAULT_IMAGE_FOLDER="image";
	public static final String DEFAULT_IMAGE_EXTENSION="png";
	
	public static BufferedImage load(String name) {
		String path="/"+DEFAULT_RESOURCE_FOLDER+"/"+DEFAULT_GRAPHIC_FOLDER+"/"+DEFAULT_IMAGE_FOLDER+"/"+name+"."+DEFAULT_IMAGE_EXTENSION;
		InputStream input=GameComponent.class.getResourceAsStream(path);
		BufferedImage img=null;
		try {
			img=ImageIO.read(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return createCompatibleImage(img);
	}
	
	public static BufferedImage[][] cut(String name, int spriteWidth, int spriteHeight, int scale) {
		String path="/"+DEFAULT_RESOURCE_FOLDER+"/"+DEFAULT_GRAPHIC_FOLDER+"/"+DEFAULT_SPRITESET_FOLDER+"/"+name+"."+DEFAULT_IMAGE_EXTENSION;
		InputStream input=GameComponent.class.getResourceAsStream(path);
		BufferedImage img=null;
		try {
			img=ImageIO.read(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int rows=img.getHeight()/spriteHeight;
		int columns=img.getWidth()/spriteWidth;
		BufferedImage[][] spriteset=new BufferedImage[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				spriteset[i][j]=createCompatibleImage(spriteWidth*scale, spriteHeight*scale, img.getTransparency());
				Graphics2D g = spriteset[i][j].createGraphics();
				g.drawImage(img, 0, 0, spriteWidth*scale, spriteHeight*scale,  j*spriteWidth, i*spriteHeight, (j+1)*spriteWidth, (i+1)*spriteHeight, null);
				g.dispose();
			}
		}
		return spriteset;
	}
	
	public static BufferedImage createCompatibleImage(int width, int height) {
        return createCompatibleImage(width,height,Transparency.TRANSLUCENT);
	}
	
	public static BufferedImage createCompatibleImage(int width, int height, int translucency) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width,height,translucency);
	}
	
	public static BufferedImage createCompatibleImage(Image image) {
        return createCompatibleImage(image,(image instanceof BufferedImage) ? ((BufferedImage)image).getTransparency() : Transparency.TRANSLUCENT);
	}
	
	public static BufferedImage createCompatibleImage(Image image, int transparency) {
        BufferedImage newImage = createCompatibleImage(image.getWidth(null),image.getHeight(null),transparency);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image,0,0,null);
        g.dispose();
        return newImage;
	}

}
