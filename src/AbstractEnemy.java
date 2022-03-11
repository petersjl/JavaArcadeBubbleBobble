import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class AbstractEnemy extends Entity {

	private int moveTime, waitTime, jumpTime;
	private static Random rand = new Random();
	int number;
	static int num = 0;
	private Entity bubble;
	private boolean isOnWall;
	protected BufferedImage right1, left1;
	protected Point2D oldV;

	/**
	 * This is the class that store basic enemy data and fucntions Includes:
	 * - movement
	 * - position
	 * - automation
	 * @param world
	 * @param center
	 */
	public AbstractEnemy(GameWorld world, Point2D center) {
		super(world, center);
		this.isOnGround = false;
		moveTime = 0;
		waitTime = 0;
		jumpTime = 100;
		bubbled = false;
		num++;
		number = num;
		bubble = null;
		isOnWall = false;
		willDie = false;
	}

	/**
	 * if the enemy isn't bubbled, they either move, wait, or change their orientation.
	 * then, they decide if they're gonna jump
	 */
	public void enemyMove() {
		if (!bubbled) { 
			if (moveTime > 0) {
				// continue moving
				moveTime--;
				//turn if running into wall
				if(isOnWall) {
					this.velocity = new Point2D.Double((-1) * this.velocity.getX(), this.velocity.getY());
					turn();
					isOnWall = false;
				}
			} else if (moveTime <= 0 && waitTime > 0) {
				waitTime--;
			} else if (waitTime <= 0) {
				moveTime = rand.nextInt(100);
				waitTime = rand.nextInt(70);
				
				this.oldV = this.velocity;
				this.velocity = new Point2D.Double(rand.nextInt(5) - 2, this.velocity.getY());
				
				
				if(this.velocity.getX() > 0 && !isFacingRight) {
//					this.isFacingRight = true;
					turn();
				}
				else if(this.velocity.getX() < 0 && isFacingRight){
//					this.isFacingRight = false;
					turn();
				}
			} else {
				moveTime--;
				waitTime--;
			}

			if (jumpTime <= 0) {
				if ((int) this.getCenterPoint().getY() > 15 * 30) {
					jump();
					jumpTime = rand.nextInt(50) + 100;
				} else {

				}
			} else {
				jumpTime--;
			}
		} else {

		}
	}
	
	/**
	 * changes enemy orientation and corresponding picture
	 */
	public void turn() {
		isFacingRight = !isFacingRight;
		updatePicture();
	}
	
	/**
	 * updates pictures based on orientation
	 */
	public void updatePicture() {
		if(isFacingRight)
			image=right1;
		else
			image=left1;
	}
	
	/**
	 * checks wall bounds
	 */
	public void checkWall() {
		int xl = (int)this.getCenterPoint().getX()-15;
		int xr = (int)this.getCenterPoint().getX()+15;
		if(xl < 30) {
			this.setCenterPoint(new Point2D.Double(45, this.getCenterPoint().getY()));
			this.isOnWall = true;
		}
		if(xr > 30*26) {
			this.setCenterPoint(new Point2D.Double(30*26-15, this.getCenterPoint().getY()));
			this.isOnWall = true;
		}

	}

	/**
	 * updates position
	 */
	public void updatePosition() {
		if (!bubbled) {
			super.updatePosition();
		} else {
			this.setCenterPoint(bubble.getCenterPoint());
		}
//		System.out.println(x + "  " + y);
	}
	

	public void timePassed() {
		super.timePassed();
		// makes hero fall if not on ground
		if (!this.isOnGround && !bubbled) {
			if (this.velocity.getY() < 5) {
				this.velocity.setLocation(this.velocity.getX(), this.velocity.getY() + 2);
			}
		}


	}

	/**
	 * traps the enemy in a bubble
	 * @param bubble
	 */
	public void setBubbled(Entity bubble) {
		this.bubble = bubble;
		bubbled = true;
	}
	
	/**
	 * sets enemy free when bubble dies on its own
	 */
	public void setEnemyFree() {
		this.bubble = null;
		this.bubbled=false;
	}

}
