import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class GameWorld implements Drawable, Temporal {
	
	private final Point2D heroStart = new Point2D.Double(45, 30*26-2);

	private BubbleFrame frame;
	private DataPanel data;

//	private static final int UPDATE_INTERVAL_MS = 10;
	private final int HEIGHT = 900;
	private final int WIDTH = 810;
//	private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private int winTimeBuffer;
	public int level;

	// LevelReader specific to each new level
	private LevelReader r;

	// saves data to display and check the map
	// TODO implement ceiling
	private ArrayList<ArrayList<Integer>> map;
	protected ArrayList<Point2D> ground;
	protected ArrayList<Point2D> ceiling;

	// saves data for different types of entities
	public Hero hero;
	private ArrayList<Entity> enemies;
	private ArrayList<Entity> fruit;
	private ArrayList<Entity> powerups;
	private ArrayList<Entity> bubbles;
	private ArrayList<Entity> hittables;
	private ArrayList<ArrayList<Entity>> totalLists;
	private ArrayList<Block> blocks;

	// arraylists to hold all entities on screen
	private List<Entity> entities = new ArrayList<Entity>();
	private final List<Entity> entitiesToAdd = new ArrayList<Entity>();
	private final List<Entity> entitiesToRemove = new ArrayList<Entity>();
	private final ArrayList<Entity> bubblesToAdd = new ArrayList<Entity>();
//	private final ArrayList<Entity> bubblesToRemove = new ArrayList<Entity>();
	private final ArrayList<Entity> fruitToAdd = new ArrayList<Entity>();
//	private final ArrayList<Entity> fruitToRemove = new ArrayList<Entity>();

	private boolean isPaused;

	private Music music;
	private boolean musicStarted;
	private boolean muted;

	/**
	 * Creates the game base
	 * 
	 * @param frame
	 * @param panel
	 */
	public GameWorld(BubbleFrame frame, DataPanel panel) {
		// Sets up the sounds for the game
		music = new Music();
		this.musicStarted = false;
		this.muted = false;

		this.isPaused = false;
		this.frame = frame;
		this.data = panel;

		// Creates the first level
		level = 1;
		createLevel(1);

		// Creates the timer that will control the game
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				timePassed();
			}
		}, 10, 10);

	}

	public int getLevel() {
		return this.level;
	}

	/**
	 * This creates the level It resets all the arraylists and reads the level data
	 * from the text files. It also starts the music
	 * 
	 * @param levelNumber
	 */
	public void createLevel(int levelNumber) {
		
		//creates starting game hero
		if(levelNumber == 1) {
			this.hero = new Hero(this);
		}
		hero.setCenterPoint(heroStart);

		// Determines how much time after the destruction of the last enemy that the
		// player is notified on winning and moves on to the next level.
		winTimeBuffer = 200;

		// clears the entities fields
		this.entities.clear();
		this.entitiesToAdd.clear();
		this.entitiesToRemove.clear();

		// creates the new level reader based on the levelNumber
		this.r = new LevelReader(levelNumber);

		// creates level
		this.map = r.getLevelDat();
		this.ground = createGround(this.map);
//		this.ceiling = createCeiling(this.map);

		// creates the map
		this.enemies = r.readEnemies(this);
		this.fruit = r.readFruit(this);
		this.powerups = r.readPowerups(this);
		this.bubbles = new ArrayList<Entity>();
		this.hittables = new ArrayList<Entity>();
		this.totalLists = new ArrayList<ArrayList<Entity>>();
		this.blocks = new ArrayList<Block>();

		// Creates block objects for texturing the level. These represent the walls and
		// platforms
		for (int r = 0; r < map.size(); r++) {
			for (int c = 0; c < map.get(0).size(); c++) {
				if (map.get(r).get(c) != 0) {
					this.blocks.add(new Block(this, new Point2D.Double(c * 30 + 14, r * 30 + 14)));
				}
			}
		}

		// adds all entities to the entities ArrayList
		// Organizes the data into respected ArrayLists
		this.entities.add(hero);
		this.entities.addAll(enemies);
		this.entities.addAll(fruit);
		this.entities.addAll(powerups);
		this.entities.addAll(bubbles);
		this.entities.addAll(hittables);
		this.totalLists.add(this.enemies);
		this.totalLists.add(this.powerups);
		this.totalLists.add(this.bubbles);
		this.totalLists.add(this.hittables);
		this.totalLists.add(this.powerups);

		// Starts the music if it wasn't started already.
		if (!this.musicStarted) {
			music.music();
			this.musicStarted = true;
		}
	}

	/**
	 * creates an ArrayList of coordinates for the standable blocks
	 * 
	 * @param map
	 * @return arraylist of standable block coords
	 */
	private ArrayList<Point2D> createGround(ArrayList<ArrayList<Integer>> map) {

		ArrayList<Point2D> ground = new ArrayList<Point2D>();
		for (int r = 0; r < map.size(); r++) {
			for (int c = 0; c < map.get(0).size(); c++) {
				if (map.get(r).get(c) == 3) {
					ground.add(new Point2D.Double(c, r));
				}
			}
		}
		return ground;
	}

	public void addEntity(Entity e) {
		this.entitiesToAdd.add(e);
	}

	public void addEntityToRemove(Entity a) {
		entitiesToRemove.add(a);
	}

	public synchronized List<Drawable> getDrawableParts() {
		return new ArrayList<Drawable>(this.entities);
	}

	public Dimension getSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	public ArrayList<ArrayList<Integer>> getMap() {
		return map;
	}

	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}

	// ------------------------------------------------------------------------------------------
	// Temporal Interface
	/**
	 * Updates game stuff every passing moment
	 */
	@Override
	public void timePassed() {
		if (!this.isPaused) {

			for (Temporal t : this.entities) {
				t.timePassed();
			}
			for (Entity t : this.enemies) {

				((AbstractEnemy) t).enemyMove();
				((AbstractEnemy) t).updatePosition();
			}

			for (Entity t : this.enemies) {
				hero.checkCollision(t, null);
			}

			for (Entity t : this.bubbles) {
				if (!((Bubble) t).isFriendBubble())
					hero.checkCollision(t, null);
			}

			for (Entity t : this.powerups) {
				t.checkCollision(hero, null);
			}

		}
		checkFruitCollision();
		checkIfBubbled();
		checkWinStatus();

		// Remove all entities that need to be removed
		this.entities.removeAll(this.entitiesToRemove);
		try {
			for (ArrayList<Entity> t : this.totalLists) {
				t.removeAll(this.entitiesToRemove);
			}
		} catch (ArrayIndexOutOfBoundsException r) {
			System.out.println("O");
		}
		this.entitiesToRemove.clear();

		// Adds all entities that need to be added
		this.entities.addAll(this.entitiesToAdd);
		this.entitiesToAdd.clear();

		this.bubbles.addAll(this.bubblesToAdd);
		this.bubblesToAdd.clear();

		this.fruit.addAll(this.fruitToAdd);
		this.fruitToAdd.clear();

		this.music.decreaseTime();

		data.update();

		music.update();
	}

	public void die() {
	} // Ignore

	@Override
	public void setIsPaused(boolean isPaused) {
		this.isPaused = !this.isPaused;
	}

	@Override
	public boolean getIsPaused() {
		return this.isPaused;
	}

	/**
	 * Pauses / resumes the game and music
	 */
	public void pause() {
		this.isPaused = !this.isPaused;
		if(!muted)
		music.pauseMusic();
	}
	
	public void mute() {
		this.muted = !this.muted;
		if(!isPaused) music.pauseMusic();
	}

	// ------------------------------------------------------------------------------------------
	// Drawable Interface

	@Override
	public Color getColor() {
		return null;
	}

	@Override
	public Shape getShape() {
		return null;
	}

	// ------------------------------------------------------------------------------------------
	// pass on methods

	public void heroMoveRight() {
		hero.moveRight();
	}

	public double getSprint() {
		return this.hero.getSprint();
	}

	public void heroStopRight() {
		this.hero.stopRight();
	}

	public void sprint() {
		this.hero.sprinting();
	}

	public void stopSprint() {
		this.hero.stopSprint();
	}

	public void heroMoveLeft() {
		hero.moveLeft();
	}

	public void heroStopLeft() {
		this.hero.stopLeft();
	}

	public void heroJump() {
		hero.jump();
	}

	public void heroDrop() {
		hero.drop();
	}

	// ------------------------------------------------------------------------------------------
	// Game Win Stuff
	/**
	 * Check win conditions. Based on enemies remaining and lives left
	 */
	public void checkWinStatus() {
		if (this.enemies.isEmpty()) {
			if (this.winTimeBuffer == 0) {
				createWinMessage();
				levelUp();
			}
			this.winTimeBuffer--;
		} else if (hero.getHealth() <= 0) {
			if (this.winTimeBuffer == 0) {
				createLostMessage();
				this.level = 1;
				createLevel(level);
				GameInfo.resetScore();
				GameInfo.resetEnemyKilled();
			}
			this.winTimeBuffer--;
		}
	}

	public void createWinMessage() {
		JOptionPane optionPane = new JOptionPane("Winner Winner Chicken Door Finner.", JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("Good job, you won mate.");
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}

	public void createLostMessage() {
		JOptionPane optionPane = new JOptionPane("Sorry Chief. Better luck next time.", JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("Darn");
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}

	// ------------------------------------------------------------------------------------------
	// Bubble Method
	public void addBubble(boolean a) {
		if (hero.getCooldown() == 0) {
			hero.startCooldown();
			addBubble(a, hero.getCenterPoint());
		}
	}

	public void addBubble(boolean a, Point2D center) {
		addBubble(a, center, hero.isFacingRight());
	}

	public void addBubble(boolean a, Point2D center, boolean fR) {
		Entity a2 = (new Bubble(this, center, fR, a));
		entitiesToAdd.add(a2);
		bubblesToAdd.add(a2);
	}

	public boolean checkIfBubbled() {
		for (Entity t : enemies) {
			for (Entity y : bubbles) {
				if (((Bubble) y).checkBounds(t)) {
					t.velocity = y.velocity;
					((AbstractEnemy) t).setBubbled(y);
//					System.out.println(((AbstractEnemy) t).number + " Bubbled");
				}
			}
		}
		return isPaused;
	}

	public Point2D returnHeroCenter() {
		return this.hero.getCenterPoint();
	}

	public int getHeroHealth() {
		return this.hero.getHealth();
	}

	// ------------------------------------------------------------------------------------------
	// Fruit Stuff
	public void addFruit(Point2D center) {
		Entity a = (new Fruit(this, center));
		entitiesToAdd.add(a);
		fruitToAdd.add(a);
	}

	public void checkFruitCollision() {
		for (Entity t : fruit) {
			t.checkCollision(hero, null);
		}
	}

	// ------------------------------------------------------------------------------------------
	// Level control methods
	public void levelUp() {
		if (frame.checkLevel(level + 1))
			level++;
		createLevel(level);
	}

	public void levelDown() {
		level--;
		createLevel(level);
	}

	// ------------------------------------------------------------------------------------------
	// Sound Methods
	public void playCollision() {
		music.contact();
	}

	public void playPowerUp() {
		music.powerUp();
	}

	public void playDing() {
		music.ding();
	}

	public void playPop() {
		music.pop();
	}

	public void jumpWhoosh() {
		music.whoosh();
	}

	public void fireStart() {
		music.fireBallStart();
	}

	public void fireEnd() {
		music.fireBallEnd();
	}

	//
	// DataPanel Relays
	public int[] getPowerUpNumber() {
		return hero.getPowerNumber();
	}

	public void setPowerNum(int a) {
		hero.setPowerNumber(a);
	}
}
