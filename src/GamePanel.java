import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * 
 * @author petersjl
 *
 *Creates the playable area for the map and entities
 *
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	//constants for height and color
	//TODO create varying background color based on level file
	private final int HEIGHT = 900;
	private final int WIDTH = 810;
	private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	
	public GameWorld world;
	private DataPanel dataPanel;
	
	//creates the panel and adds a GameWorld environment
	public GamePanel(BubbleFrame frame, DataPanel data) {
		this.dataPanel = data;
		
		setSize(WIDTH, HEIGHT);
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH,HEIGHT));
		setBackground(BACKGROUND_COLOR);
		setLocation(0,60);
		this.world = new GameWorld(frame, dataPanel);
		GameComponent component = new GameComponent(world);
		add(component);
	}
	
	/*
	 * keyboard pass on method to pass on the value of levelNumber
	 * 
	 * @param levelNumber
	 */
	public void createLevel(int levelNumber) {
		world.createLevel(levelNumber);
	}
	
	//keyboard pass on methods
	public void moveRight() {
		world.heroMoveRight();
	}
	
	public void moveLeft() {
		world.heroMoveLeft();
	}
	public void sprint() {
		world.sprint();
	}
	public void stopSprinting() {
		world.stopSprint();
	}

	public void jump() {
		world.heroJump();
		
	}
	
	public void drop() {
		world.heroDrop();
	}
	
	public void addBubble(boolean a) {
		world.addBubble(a);
	}

	public void stopRight() {
		world.heroStopRight();
		
	}

	public void stopLeft() {
		world.heroStopLeft();
		
	}

	public void levelUp() {
		world.levelUp();
		
	}

	public void levelDown() {
		world.levelDown();
		
	}
	
	public void pause() {
		world.pause();
	}
	
	public int[] getPowerNum() {
		return world.getPowerUpNumber();
	}
}
