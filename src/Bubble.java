import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bubble extends Entity {

	private boolean hasEntity;
	private boolean isFriendBubble;
	private int time;
	private Entity hostage;
	private boolean deaded;
	private boolean wentUp;
	private boolean goingUp;

	/**
	 * Creates a bubble based on position, direction, and team (friend or foe)
	 * Friend and foe bubble have different images fuctionality
	 * @param world
	 * @param center
	 * @param facingRight
	 * @param isFriend
	 */
	public Bubble(GameWorld world, Point2D center, boolean facingRight, boolean isFriend) {
		super(world, center);
		if (isFriend)
			this.color = Color.blue;
		else
			this.color = Color.pink;
		this.hasEntity = false;
		this.isFriendBubble = isFriend;
		this.time = 100;
		if (facingRight) {
			this.velocity = new Point2D.Double(5.5, 0);
		} else {
			this.velocity = new Point2D.Double(-5.5, 0);
		}
		hostage = null;
		deaded = false;
		wentUp = false;
		goingUp = false;

		if (isFriendBubble)
			try {
				image = ImageIO.read(new File("./EntityFiles/bubble2.png"));
			} catch (IOException ex) {
				// handle exception...
			}
		else {
			try {
				image = ImageIO.read(new File("./EntityFiles/Projectile.png"));
			} catch (IOException ex) {
				// handle exception...
			}
			this.getWorld().fireStart();
		}
	}

	public boolean checkCeiling() {
		boolean temp = super.checkCeiling();
		if (temp) {
			double velocityX = (13.0 * 30 - this.getCenterPoint().getX()) / 70;
			this.velocity = new Point2D.Double(velocityX, 0);
		}
		return true;
	}

	@Override
	/**
	 * if time is 0, it either goes up or dies if it has a hostage
	 * then it checks if it has been popped by player if friendly
	 * or checks if it hits wall and die if enemy
	 */
	public void updatePosition() {
		if (time <= 0) {

			if (!wentUp) {
				wentUp = true;
				this.velocity = new Point2D.Double(0, -2);

			}
			if (hostage != null) {
				((AbstractEnemy) hostage).setEnemyFree();
				hostage = null;
				die();
			}
		}
		if (goingUp)
			time = 10;
		if (isFriendBubble) {

			if (this.checkIfHeroCapped() && !deaded && (time < 90 || hostage != null)) {
				if (this.hostage != null) {
					GameInfo.addEnemyKilled();
					GameInfo.addScore(100);
					try {
						hostage.die();
					} catch (NullPointerException e) {
					}
				}
//				System.out.println(GameInfo.toString1());
				deaded = true;
				this.die();

			}

			double x = this.getCenterPoint().getX() + this.velocity.getX();
			double y = this.getCenterPoint().getY() + this.velocity.getY();
			this.setCenterPoint(new Point2D.Double(x, y));
			time--;
			if (time % 100 == 0) {
//				System.out.println(time);
			}
		} else {
			int xl = (int) this.getCenterPoint().getX() - 15;
			int xr = (int) this.getCenterPoint().getX() + 15;
			if (xl < 31) {
				die();
			} else if (xr > 30 * 26 - 1) {
				die();
			} else {
				double x = this.getCenterPoint().getX() + this.velocity.getX();
				double y = this.getCenterPoint().getY() + this.velocity.getY();
				this.setCenterPoint(new Point2D.Double(x, y));
			}
		}
	}

	
	public boolean checkBounds(Entity e) {
		Double xMin = e.getCenterPoint().getX();
		Double yMin = e.getCenterPoint().getY();
		Double xMinB = this.getCenterPoint().getX();
		Double yMinB = this.getCenterPoint().getY();

		if (!hasEntity && isFriendBubble && !e.willDie() && !e.isBubbled() && !wentUp)
			if (Math.sqrt(Math.pow((xMin - xMinB), 2) + Math.pow(yMin - yMinB, 2)) < 20) {
//				System.out.println(Math.abs((int) (xMin - xMinB)));
//				System.out.println(Math.abs((int) (yMin - yMinB)));
				this.velocity = new Point2D.Double(0, -5);
				hasEntity = true;
				time = 1000;
				hostage = e;
				return true;
			}
		return false;
	}

	public void timePassed() {
		super.timePassed();
		updatePosition();
	}

	/**
	 * checks if hero popped the bubble
	 * @return
	 */
	public boolean checkIfHeroCapped() {
		Point2D temp = getWorld().returnHeroCenter();

		Double xMin = temp.getX();
		Double yMin = temp.getY();
		Double xMinB = this.getCenterPoint().getX();
		Double yMinB = this.getCenterPoint().getY();

		if (Math.sqrt(Math.pow((xMin - xMinB), 2) + Math.pow(yMin - yMinB, 2)) < 30) {
			if (hostage != null && !deaded)
				this.getWorld().addFruit(this.getCenterPoint());
			return true;
		}
		return false;
	}

	@Override
	public void handleCollision(Entity a) {

	}

	public boolean isFriendBubble() {
		return this.isFriendBubble;
	}

	public void die() {
		if(isFriendBubble)
			this.getWorld().playPop();
		else
			this.getWorld().fireEnd();
		super.die();
	}

	public void drawOn(Graphics g, GameComponent t) {
		if (isFriendBubble)
			g.drawImage(image, (int) (this.getCenterPoint().getX()) - 15, (int) (this.getCenterPoint().getY()) - 15, 30,
					30, t);
		else
			g.drawImage(image, (int) (this.getCenterPoint().getX()) - 30, (int) (this.getCenterPoint().getY()) - 30, 60,
					60, t);
	}

}
