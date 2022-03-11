import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Shooter extends AbstractEnemy {

	private int shootTimer;

	public Shooter(GameWorld world, Point2D center) {
		super(world, center);
		this.color = Color.orange;
		shootTimer = 100;

		try {
			left1 = ImageIO.read(new File("./EntityFiles/Shooter Left.png"));
			right1 = ImageIO.read(new File("./EntityFiles/Shooter Right.png"));
			image = right1;
		} catch (IOException ex) {
			// handle exception...
		}
	}

	public void shoot() {
		if (!bubbled) {
			if (shootTimer <= 0) {
				this.getWorld().addBubble(false, this.getCenterPoint(), isFacingRight);
				shootTimer = new Random().nextInt(100) + 100;
			} else {
				shootTimer--;
			}
		}
	}

	public void updatePosition() {
		super.updatePosition();
		shoot();
	}

	public void drawOn(Graphics g, GameComponent t) {
		g.drawImage(image, (int) (this.getCenterPoint().getX()) - 30, (int) (this.getCenterPoint().getY()) - 30, 60, 60,
				t);
	}
}
