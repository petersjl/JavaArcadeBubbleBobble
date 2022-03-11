import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Fruit extends Entity {

	private int waitTime;
	private boolean deaded;

	/**
	 * Creates a fruit object depending on position
	 * @param world
	 * @param center
	 */
	public Fruit(GameWorld world, Point2D center) {
		super(world, center);
		this.color = Color.BLUE;
		this.velocity = new Point2D.Double(0, 4);
		waitTime = 50;
		deaded = false;

		try {
			image = ImageIO.read(new File("./Images/goldCoin.png"));
		} catch (IOException ex) {
			// handle exception...
		}
	}

	public void timePassed() {
		super.timePassed();
		updatePosition();
		if (waitTime > 0)
			waitTime--;
	}

	public boolean checkCeiling() {
		int y = (int) this.getCenterPoint().getY();
		if (y <= 40) {
			this.velocity = new Point2D.Double(0, 2);
			return true;
		}
		return false;
	}

	@Override
	public void updatePosition() {
		super.updatePosition();
	}

	public void handleCollision(Entity a) {
		if (waitTime <= 0 && !deaded) {
			this.getWorld().playDing();
			die();
			this.deaded = true;
			GameInfo.addScore(50);
		}
	}

}
