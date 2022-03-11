import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class Entity implements Drawable, Temporal {

	protected Point2D velocity;
	private GameWorld world;
	private Point2D centerPoint;
	protected boolean isOnGround;
	protected Color color;
	protected Shape shape;
	protected boolean bubbled;
	protected boolean isFacingRight;
	protected boolean willDie;
	protected boolean djump;

	protected BufferedImage image;

	public Entity(GameWorld world, Point2D center) {
		this.world = world;
		this.centerPoint = center;
		this.velocity = new Point2D.Double(0, 5);

	}

	// gives the entity an upwards velocity if it is on the ground
	public void jump() {
		if (this.isOnGround) {
			this.velocity = new Point2D.Double(this.velocity.getX(), -22);
		}
	}

	// should be called by each entity and then overridden
	@Override
	public void timePassed() {
		this.checkGround();
		this.checkCeiling();
		this.checkWall();
		this.checkTeleport();
	}

	// causes the hero to disappear
	@Override
	public void die() {
		willDie = true;
		world.addEntityToRemove(this);
//		world.addEntityToRemove(this);

	}

	@Override
	public void setIsPaused(boolean isPaused) {
		// null

	}

	@Override
	public boolean getIsPaused() {
		// null
		return false;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	//Old method used when textures didn't exist
	public Shape getShape() {
		double x = getCenterPoint().getX();
		double y = getCenterPoint().getY();
		if (Bubble.class.isInstance(this)) {
			return new Ellipse2D.Double((int) x - 15, (int) y - 15, 30, 30);
		}
		return new Rectangle((int) x - 15, (int) y - 15, 30, 30);
	}

	public Point2D getCenterPoint() {
		return this.centerPoint;
	}

	public void setCenterPoint(Point2D center) {
		this.centerPoint = center;
	}

	protected GameWorld getWorld() {
		return this.world;
	}

	// checks to see if there is ground below the entity and sets isOnGround to
	// agree
	public void checkGround() {

		int xl = (int) this.getCenterPoint().getX() - 15;
		int xr = (int) this.getCenterPoint().getX() + 15;
		int y = (int) this.getCenterPoint().getY() + 15;

		// checks to see if a falling entity has reached a standable surface
		if (!isOnGround && this.velocity.getY() > 0 && !bubbled) {
			for (Point2D p : world.ground) {
				int px = (int) p.getX() * 30;
				int py = (int) p.getY() * 30;
				if ((xl > px - 1 && xl < px + 31) || (xr > px - 1 && xr < px + 31)) {
					if (y > py && y < py + 30) {
						this.setCenterPoint(new Point2D.Double(this.getCenterPoint().getX(), py - 15));
						this.velocity = new Point2D.Double(this.velocity.getX(), 0);
						this.isOnGround = true;
						this.djump = true;
					}
				}
			}
		}
		// checks to see if a grounded entity is still grounded
		else {
			this.isOnGround = false;
			for (Point2D p : world.ground) {
				int px = (int) p.getX() * 30;
				int py = (int) p.getY() * 30;
				if ((xl > px - 1 && xl < px + 31) || (xr - 1 > px && xr < px + 31)) {
					if (y == py) {

						this.isOnGround = true;
						break;
					}
				}
			}

		}
	}

	// checks to see if the entity is jumping into the ceiling
	public boolean checkCeiling() {
		int y = (int) this.getCenterPoint().getY();
		if (y <= 40) {
			this.velocity = new Point2D.Double(0, 1);
			return true;
		}
		return false;
	}

	// checks to see if the entity is running into a wall
	public void checkWall() {
		int xl = (int) this.getCenterPoint().getX() - 15;
		int xr = (int) this.getCenterPoint().getX() + 15;
		if (xl < 30) {
			this.setCenterPoint(new Point2D.Double(45, this.getCenterPoint().getY()));
		}
		if (xr > 30 * 26) {
			this.setCenterPoint(new Point2D.Double(30 * 26 - 15, this.getCenterPoint().getY()));
		}

	}

	public void checkTeleport() {
		if (this.getCenterPoint().getY() > 30 * 30) {
			this.setCenterPoint(new Point2D.Double(this.getCenterPoint().getX(), -15));
		}
	}

	public boolean checkCollision(Entity a, Entity b) {
		Double xMin = a.getCenterPoint().getX();
		Double yMin = a.getCenterPoint().getY();
		Double xMinB = this.getCenterPoint().getX();
		Double yMinB = this.getCenterPoint().getY();

		if (Math.sqrt(Math.pow((xMin - xMinB), 2) + Math.pow(yMin - yMinB, 2)) < 20) {
			this.handleCollision(a);

		}

		return false;
	}

	public void updatePosition() {
		double x = this.getCenterPoint().getX() + this.velocity.getX();
		double y = this.getCenterPoint().getY() + this.velocity.getY();
		this.setCenterPoint(new Point2D.Double(x, y));
	}

	public void handleCollision(Entity a) {

	}

	public boolean willDie() {
		return this.willDie;
	}

	public boolean isBubbled() {
		return this.bubbled;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}

	public void drawOn(Graphics g, GameComponent t) {
		g.drawImage(image, (int)(this.getCenterPoint().getX())-15, (int)(this.getCenterPoint().getY())-15, 30 , 30 ,t);
	}
	
	
}
