import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Block{
	
	protected Point2D velocity;
	private Point2D centerPoint;

	public static Image image; 
	
	/**
	 * Makes up the walls and platforms of the game. Can apply any image
	 * @param world
	 * @param center
	 */
	public Block(GameWorld world, Point2D center) {
		this.centerPoint = center;
		this.velocity = new Point2D.Double(0, 5);
		if (image == null)
			try {
				image = ImageIO.read(new File("./Images/craete.jpg"));
				image =  image.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
			} catch (IOException ex) {
				// handle exception...
			}
		
	}

	/**
	 * draws the block when called
	 * @param g
	 * @param t
	 */
	public void drawOn(Graphics g, GameComponent t) {
		g.drawImage(image, (int)(this.centerPoint.getX())-15, (int)(this.centerPoint.getY())-15, 30 , 30 ,t);
	}
}
