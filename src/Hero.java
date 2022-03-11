import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Hero extends Entity {

	private int health;
	private int healthCooldown;
	private boolean right;
	private boolean left;
	int drop;
	int cooldown;
	private Color baseColor;
	private boolean sprint;
	private boolean speedTime;
	private double sprintBar;
	private boolean tired;

	private int pictureChange, deltaTime;
	private BufferedImage right1, right2, left1, left2;

	private int powerNum, powerTime;

	public Hero(GameWorld world) {
		super(world, new Point2D.Double(45, 30 * 26 - 2));
		this.color = Color.GREEN;
		this.baseColor = this.color;
		isFacingRight = true;
		right = false;
		left = false;
		drop = 0;
		cooldown = 0;
		health = 3;
		healthCooldown = 0;
		djump = true;
		pictureChange = 10;
		deltaTime = 10;
		sprintBar = 100;
		tired = false;

		if (right1 == null)
			try {
				left1 = ImageIO.read(new File("./EntityFiles/pixil-frame-0 (1).png"));
				left2 = ImageIO.read(new File("./EntityFiles/pixil-frame-0 (2).png"));

				right1 = ImageIO.read(new File("./EntityFiles/pixil-frame-0 (3).png"));

				right2 = ImageIO.read(new File("./EntityFiles/pixil-frame-0 (4).png"));

				image = right1;
			} catch (IOException ex) {
				// handle exception...
				System.out.println("O");
			}
	}

	@Override
	public void timePassed() {
		checkWall();
		checkCeiling();
		checkTeleport();
		updateColor();
		checkHealth();
		if (cooldown > 0) {
			cooldown--;
		}
		if (healthCooldown > 0) {
			this.healthCooldown--;
		}

		if (drop > 0)
			drop--;
		else
			checkGround();
		// makes hero fall if not on ground
		if (!this.isOnGround || drop > 0) {
			if (this.velocity.getY() < 5) {
				this.velocity.setLocation(this.velocity.getX(), this.velocity.getY() + 2);
			}
			if (drop > 0)
				drop--;
		}
		if (this.right && this.left) {
			this.velocity.setLocation(0, this.velocity.getY());
		}
		// makes hero walk right if right arrow held
		else if (this.right && !this.left) {
			// sprinting
			if (sprint && sprintBar > 0 && !tired) {
				this.velocity.setLocation(8, this.velocity.getY());
				isFacingRight = true;
				sprintBar = sprintBar - 5;
				if (sprintBar <= 0)
					tired = true;
			} else {
				this.velocity.setLocation(4, this.velocity.getY());
				isFacingRight = true;
				if (sprintBar <= 100)
					sprintBar++;
				if (sprintBar == 100)
					tired = false;
			}
			if (pictureChange <= 0) {
				pictureChange = deltaTime;

				if (image.equals(right1)) {
					image = right2;
				} else {
					image = right1;
				}

			} else {
				pictureChange--;
				if (sprintBar <= 100)
					sprintBar++;
				if (sprintBar == 100)
					tired = false;
			}
		}
		// makes hero walk left if left arrow held
		else if (this.left && !this.right) {
			if (this.sprint && sprintBar > 0 && !tired) {
				this.velocity.setLocation(-8, this.velocity.getY());
				isFacingRight = false;
				sprintBar = sprintBar - 5;
				if (sprintBar <= 0)
					tired = true;
			} else {
				this.velocity.setLocation(-4, this.velocity.getY());
				isFacingRight = false;
				if (sprintBar <= 100)
					sprintBar++;
				if (sprintBar == 100)
					tired = false;

			}

			if (pictureChange <= 0) {
				pictureChange = deltaTime;

				if (image.equals(left1)) {
					image = left2;
				} else {
					image = left1;
				}

			} else {
				pictureChange--;
				if (sprintBar <= 100)
					sprintBar++;
				if (sprintBar == 100)
					tired = false;

			}
		}

		// clears X velocity if neither arrow held
		else {
			this.velocity.setLocation(0, this.velocity.getY());
		}
		if (sprintBar <= 100)
			sprintBar++;
		if (sprintBar == 100)
			tired = false;
		updatePosition();

		if (powerTime > 0)
			powerTime--;
		else
			powerNum = 0;

	}

	// sets a new center point by adding the current velocity
	@Override

	public void updatePosition() {
		super.updatePosition();
	}

	public void sprinting() {
		sprint = true;
	}

	public void stopSprint() {
		sprint = false;
	}
	// --------------------------------------------------------------------------
	// key press methods

	// makes the hero walk right
	public void moveRight() {
		this.right = true;

	}

	// makes the hero walk left
	public void moveLeft() {
		this.left = true;

	}

	// makes hero stop, facing right
	public void stopRight() {
		this.right = false;
		image = right1;
	}

	// makes hero stop, facing left
	public void stopLeft() {
		this.left = false;
		image = left1;
	}

	public boolean isFacingRight() {
		return this.isFacingRight;
	}

	public void drop() {
		if (this.isOnGround && this.getCenterPoint().getY() < 28 * 30 && drop == 0) {
			drop = 15;
		}
	}

	/**
	 * Controls single and double jumping in game
	 */
	public void jump() {
		if (this.isOnGround) {
			this.velocity = new Point2D.Double(this.velocity.getX(), -22);
			this.getWorld().jumpWhoosh();
		} else if (this.djump) {
			this.velocity = new Point2D.Double(this.velocity.getX(), -22);
			djump = false;
			this.getWorld().jumpWhoosh();
		}
	}

	// ----------------------------------------------------------------------------

	public int getCooldown() {
		return cooldown;
	}

	public void startCooldown() {
		this.cooldown = 30;
	}

	/**
	 * Checks if hero collided with enemy in captured bubble
	 */
	public void loseHealth() {
		this.health--;
		this.healthCooldown = 250;
	}

	/**
	 * Checks if hero collided with enemy without shield
	 */
	public void handleCollision(Entity a) {
		if (this.healthCooldown == 0 && !a.willDie() && !(powerNum == 1)) {
			loseHealth();
			getWorld().playCollision();
		}
	}
	

	public void checkHealth() {
		if(GameInfo.checkLife()) this.health++;
	}

	public void updateColor() {
		int r = this.baseColor.getRed();
		int g = this.baseColor.getGreen();
		int b = this.baseColor.getBlue();
		double percent = healthCooldown / 250.0;

		int newRed = (int) ((255 - r) * percent + r);
		int newGreen = (int) ((255 - g) * percent + g);
		int newBlue = (int) (((255 - b) * percent) + b);

		this.color = new Color(newRed, newGreen, newBlue);
	}

	public void drawOn(Graphics g, GameComponent t) {

		g.drawImage(image, (int) (this.getCenterPoint().getX()) - 15 - 7,
				(int) (this.getCenterPoint().getY()) - 15 - 10, 40, 40, t);
	}

	//-----------------------------------------------------------
	// Getter / Setters methods
	
	public int getHealth() {
		return this.health;
	}

	public double getSprint() {
		return this.sprintBar;
	}

	public void setTime() {
		this.pictureChange = 10;
	}

	public int[] getPowerNumber() {
		int[] a = { this.powerNum, this.powerTime };
		return a;
	}

	public void setPowerNumber(int a) {
		powerNum = a;
		powerTime = 400;
	}
}
