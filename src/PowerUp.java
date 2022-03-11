import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PowerUp extends Entity {

	private boolean deaded;
	// 1=freeze | 2=invincibility
	private int type;
	private int time;

	public PowerUp(GameWorld world, Point2D center, int type) {
		super(world, center);
		deaded = false;
		this.type = type;
		if (type == 0) {
			try {
				image = ImageIO.read(new File("./Images/shield.png"));
			} catch (IOException ex) {
				// handle exception...
			}
		} else {
			try {
				image = ImageIO.read(new File("./Images/shield.png"));
			} catch (IOException ex) {
				// handle exception...
			}
		}
	}

	public void timePassed() {
		super.timePassed();
		updatePosition();
	}

	@Override
	public void updatePosition() {
		super.updatePosition();
	}

	public void handleCollision(Entity a) {
		if (!deaded) {
			this.getWorld().addEntityToRemove(this);
			GameInfo.addScore(50);
			this.getWorld().setPowerNum(type+1);
			deaded = false;
			this.getWorld().playPowerUp();
		}
	}
}
