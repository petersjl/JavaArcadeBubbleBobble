import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * @author petersjl
 *
 * This object is created by the main and constructs the DataPanel and GamePanel then sets the size and title
 * of the frame.
 * 
 * TODO: create a main menu to be created before displaying the levels
 *
 */
@SuppressWarnings("serial")
public class BubbleFrame extends JFrame {
	File temp = new File("./Levels/");
	int directorySize = temp.list().length-1;
	
	private GamePanel game;
	private DataPanel data;
	private MainMenu menu;
	
	public BubbleFrame() {
		
		this.data = new DataPanel();
		this.game = new GamePanel(this, data);
		data.addGame(game);
//		this.menu = new MainMenu(this);
//		add(menu);
		
		add(data, BorderLayout.NORTH);
		add(game, BorderLayout.CENTER);
		
		setTitle("A Sub-Par Platformer");
		setSize(825, 1007);
		setLocationRelativeTo(null);
		setResizable(false);
		addKeyListener(new gameListener(game));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		this.menu = new MainMenu(this);

		
	}
	
	//broken method
//	public void startGame() {
//		remove(menu);
//		
//		add(data, BorderLayout.NORTH);
//		add(game, BorderLayout.CENTER);
//		addKeyListener(new gameListener(game));
//		data.reset();
//		game.createLevel(1);
//		this.levelNumber = 1;
//		game.world.togglePause();
//		
//	}
	
	/*
	 * This creates a key listener to control the character in the game
	 */
	
	public class gameListener implements KeyListener{
		
		private GamePanel game;
		
		public gameListener(GamePanel game) {
			this.game = game;
		}
		/*
		 * 39 = right arrow
		 * 37 = left arrow
		 * 38 = up arrow
		 */
		@Override
		/**
		 * events trigger depending on keys
		 */
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			
			case 32:
				this.game.sprint();
				break;
			case 39 :
				this.game.moveRight();
				break;
			
			case 37 :
				this.game.moveLeft();
				break;
			case 38 :
				this.game.jump();
				break;
			
			case 40:
				this.game.drop();
				break;
			default : break;
			}
			
			
		}

		@Override
		/**
		 * Stops events from running when keys are released
		 */
		public void keyReleased(KeyEvent e) {
			switch(e.getKeyCode()) {
			
			case 32:
				this.game.stopSprinting();
				break;
			case 39 :
				this.game.stopRight();
				break;
			
			case 37 :
				this.game.stopLeft();
				break;
			}
			
		}

		@Override
		/**
		 * events trigger depending on key pressed
		 */
		public void keyTyped(KeyEvent arg0) {
			switch((int)arg0.getKeyChar()) {
			
			case 'u' :	
				if(game.world.level == directorySize) {createErrorMessage(1);break;}
				else {
					game.levelUp();
					break;
				}
			case 'd' :	
				if(game.world.level == 1) {createErrorMessage(0);break;}
				else {
					game.levelDown();
					break;
				}
			case 'U' :
				if(game.world.level == directorySize) {createErrorMessage(1);break;}
				else {
					game.levelUp();
					break;
				}
			case 'D' :
				if(game.world.level == 1) {createErrorMessage(0);break;}
				else {
					game.levelDown();
					break;
				}
			case 'a':
				game.addBubble(true);
				break;
			case 'A':
				game.addBubble(true);
				break;
				
			case 27:
				game.pause();
				break;
			case 'm':
				game.world.mute();
				break;
			case 'M':
				game.world.mute();
				break;
			}
			
		}
		
	}
	
	//creates and displays an error message if the user attempts to go to a level that doesn't exist
	private void createErrorMessage(int i) {
		if(i == 0) {
			JOptionPane optionPane = new JOptionPane("A lower level does not exist.", JOptionPane.ERROR_MESSAGE);    
			JDialog dialog = optionPane.createDialog("Missing Level");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
		if(i == 1) {
			JOptionPane optionPane = new JOptionPane("A higher level does not exist.", JOptionPane.ERROR_MESSAGE);    
			JDialog dialog = optionPane.createDialog("Missing Level");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
	}
	

	public boolean checkLevel(int oof) {
		if(oof > directorySize)
			return false;
		return true;
	}
}
