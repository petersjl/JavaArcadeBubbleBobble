import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Walker extends AbstractEnemy {

	private int pictureChange, deltaTime;
	private BufferedImage left2, right2;

	public Walker(GameWorld world, Point2D center) {
		super(world, center);
		this.color = Color.red;
		pictureChange = 10;
		deltaTime = 10;

		if (right1 == null)
			try {
				left1 = ImageIO.read(new File("./EntityFiles/Knight Left 2 (2).png"));
				left2 = ImageIO.read(new File("./EntityFiles/Knight Left 2 (1).png"));
				right1 = ImageIO.read(new File("./EntityFiles/Knight Right 2.png"));
				right2 = ImageIO.read(new File("./EntityFiles/Knight Right 1.png"));
				image = right1;
			} catch (IOException ex) {
				// handle exception...
				System.out.println("O");
			}
	}

	public void enemyMove() {

		super.enemyMove();

		if (pictureChange <= 0) {
			pictureChange = deltaTime;
			if (isFacingRight) {
				if (image.equals(right1)) {
					image = right2;
				} else {
					image = right1;
				}
			} else {
				if (image.equals(left1)) {
					image = left2;
				} else {
					image = left1;
				}
			}
		} else {
			pictureChange--;
		}
	}

	public void drawOn(Graphics g, GameComponent t) {

		g.drawImage(image, (int) (this.getCenterPoint().getX()) - 15 - 7,
				(int) (this.getCenterPoint().getY()) - 15 - 10, 40, 40, t);
	}

}
