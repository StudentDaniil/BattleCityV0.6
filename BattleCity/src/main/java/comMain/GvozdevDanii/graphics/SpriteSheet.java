package comMain.GvozdevDanii.graphics;

import java.awt.image.BufferedImage;

public class  SpriteSheet {

	private final BufferedImage	sheet;
	private final int				spriteCount;
	private final int				scale;
	private final int				spritesInWidth;

	public SpriteSheet(BufferedImage sheet, int spriteCount, int scale) {
		this.sheet = sheet;
		this.spriteCount = spriteCount;
		this.scale = scale;

		this.spritesInWidth = sheet.getWidth() / scale;

	}

	public BufferedImage getSprite(int index) {

		index = index % spriteCount;

		int x = index % spritesInWidth * scale;
		int y = index / spritesInWidth * scale;

		return sheet.getSubimage(x, y, scale, scale);

	}

}
