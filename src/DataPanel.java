import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author petersjl
 *
 *         creates a panel for the top of the screen to hold and display game
 *         data
 * 
 *         TODO: implement data holders and pass to game world
 */

@SuppressWarnings("serial")
public class DataPanel extends JPanel {

	private final int HEIGHT = 60;
	private final int WIDTH = 810;
	private final Color BACKGROUND_COLOR = Color.PINK;

	private GamePanel game;
	private JPanel lives;
	private JPanel powerups;
	private JPanel scorePanel;

	private JLabel livesLabel;
	private JLabel powerupsLabel;
	private JLabel scoreLabel;

	private String[] powerup = { "None", "Shield", "Freeze" };
	private int currentPower = 0;
	private int time;
	private DecimalFormat f = new DecimalFormat("000000");

	public DataPanel() {
		this.setSize(WIDTH, HEIGHT);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setBackground(BACKGROUND_COLOR);

		lives = new JPanel();
		powerups = new JPanel();
		scorePanel = new JPanel();

		livesLabel = new JLabel();
		powerupsLabel = new JLabel();
		scoreLabel = new JLabel();

		livesLabel.setBounds(0, 0, 200, 50);

		lives.add(livesLabel);
		powerups.add(powerupsLabel);
		scorePanel.add(scoreLabel);

		lives.setSize(270, 60);
		powerups.setSize(270, 60);
		scorePanel.setSize(270, 60);

		lives.setBackground(BACKGROUND_COLOR);
		powerups.setBackground(BACKGROUND_COLOR);
		scorePanel.setBackground(BACKGROUND_COLOR);

		add(lives);
		add(powerups);
		add(scorePanel);

//		score = 0;

	}

	public void reset() {
		livesLabel.setText("<html><h1>Lives: " + game.world.hero.getHealth() +"</h1></html>");
		powerupsLabel.setText("<html><h1>Level: 1</h1></html>");
		scoreLabel.setText("<html><h1>Score: 00000</h1></html>");

	}

	/**
	 * Updates the upper dashboard depending on the score, number of lives, and powerup
	 */
	public void update() {
		try {
//<<<<<<< HEAD
//		livesLabel.setText("<html><h1>Lives: " + game.world.hero.getHealth() +"</h1></html>");
//		powerupsLabel.setText("<html><h1>Level :" + game.world.level + "</h1></html>");
//		scoreLabel.setText("<html><h1>Score: " + f.format(GameInfo.getScore()) + "</h1></html>");
//		}catch(NullPointerException e) {
			
//=======
			currentPower = game.getPowerNum()[0];
			time = game.getPowerNum()[1];
			livesLabel.setText("<html><h1>Lives: " + game.world.hero.getHealth() + "</h1></html>");
			if (time > 0) {
				powerupsLabel.setText("<html><h1>" + powerup[currentPower] + " " + ((int)time / (int)20) + "</h1></html>");
			} else {
				powerupsLabel.setText("<html><h1>" + powerup[currentPower] + "</h1></html>");
			}
			scoreLabel.setText("<html><h1>Score: " + f.format(GameInfo.getScore()) + "</h1></html>");
		} catch (NullPointerException e) {

		}
	}

	public void addScore(int s) {
//		score += s;
	}

	public void addGame(GamePanel g) {
		this.game = g;
	}
	
	public class MuteButton extends JButton{
		
		public MuteButton(GamePanel g) {
			this.addActionListener(new MuteListener(this, g));
			this.setText("Mute");
		}
		
		public class MuteListener implements ActionListener{
			
			MuteButton button;
			GamePanel game;
			Color backMuted = new Color(214, 77, 127);
			Color backNot;
			boolean muted = false;
			
			public MuteListener(MuteButton b, GamePanel g) {
				button = b;
				game = g;
				backNot = button.getBackground();
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if(muted) button.setBackground(backNot);
				else button.setBackground(backMuted);
				muted = !muted;
				game.world.mute();
				
			}
			
		}
	}
	
	public class ResetButton extends JButton{
		
		public ResetButton(DataPanel p, GamePanel g) {
			addActionListener(new ResetListener(p,g));
			setText("Restart");
		}
		
		public class ResetListener implements ActionListener{
			
			private DataPanel data;
			private GamePanel game;
			
			public ResetListener(DataPanel p, GamePanel g) {
				data = p;
				game = g;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				data.reset();
				game.world.createLevel(1);
				game.world.level = 0;
				
			}
			
		}
	}

}
