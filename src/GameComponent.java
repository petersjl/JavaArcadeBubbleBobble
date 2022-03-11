import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * 
 * @author petersjl
 *
 *         draw component for the game world
 */
@SuppressWarnings("serial")
public class GameComponent extends JComponent {

	private static final int FRAMES_PER_SECOND = 30;
	private static final int REPAINT_INTERVAL_MS = 1000 / FRAMES_PER_SECOND;

	private BufferedImage image;
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

	private GameWorld world;

	public GameComponent(GameWorld world) {

		this.world = world;

		// Stores all level images on standby for level switching
		String[] list = { "level1.jpg", "level2.jpg", "level3.jpg", "level4.jpg", "level5.jpg" };
		for (String t : list) {
			try {
				images.add(ImageIO.read(new File("./Backgrounds/" + t)));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		setPreferredSize(world.getSize());
		setMaximumSize(world.getSize());

		// creates a timer to periodically repaint the screen
		Timer repaintTimer = new Timer(REPAINT_INTERVAL_MS, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}

		});

		try {
			image = ImageIO.read(new File("./Images/background.jpeg"));
		} catch (IOException ex) {
		}

		repaintTimer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// draws the map
		this.drawBackground(g2);
		drawWorld(g2, this.world.getMap());
		drawBlocks(g2);
		drawHealth(g2, this.world.getHeroHealth());
		drawSprint(g2, this.world.getSprint());

		// loop to draw each entity
		List<Drawable> drawableParts = this.world.getDrawableParts();
		for (Drawable d : drawableParts) {
			drawDrawable(g2, d);
		}
	}

	/**
	 * Draws every entity in the game
	 * @param g
	 * @param d
	 */
	private void drawDrawable(Graphics2D g, Drawable d) {
		((Entity) d).drawOn(g, this);
	}

	/**
	 * draws the blocks in the map
	 * old method
	 */
	private void drawWorld(Graphics2D g, ArrayList<ArrayList<Integer>> map) {

		g.setColor(Color.BLACK);

		for (int r = 0; r < map.size(); r++) {
			for (int c = 0; c < map.get(0).size(); c++) {
				if (map.get(r).get(c) != 0) {

					Rectangle rect = new Rectangle(c * 30, r * 30, 30, 30);
					g.fill(rect);
				}
			}
		}

	}

	/**
	 * Draws all the platform and walls blocks
	 * @param g
	 */
	private void drawBlocks(Graphics g) {
		for (Block t : this.world.getBlocks()) {
			t.drawOn(g, this);
		}

	}

	/**
	 * Draws the sprint meter at top right of screen in blue. Shows how much sprint
	 * the hero has remaining.
	 * 
	 * @param g
	 * @param sprintMeter
	 */
	private void drawSprint(Graphics2D g, double sprintMeter) {
		for (int i = 0; i < sprintMeter; i++) {
			g.setColor(Color.BLUE);
			Rectangle rect = new Rectangle(500 + i, 0, 1, 25);
			g.fill(rect);
			g.draw(rect);
		}
	}

	/**
	 * Draws green rectangles at the top left of screen. Represents number of lives
	 * remaining
	 * 
	 * @param g
	 * @param health
	 */
	private void drawHealth(Graphics2D g, int health) {
		for (int i = 0; i < health; i++) {
			g.setColor(Color.green);
			Rectangle rect = new Rectangle(30 + i * 30, 0, 25, 25);
			g.fill(rect);
			g.draw(rect);
		}
	}

	private void drawBackground(Graphics g) {
		g.drawImage(images.get(world.getLevel() - 1), 0, 0, 825, 1007, this);
	}

}
