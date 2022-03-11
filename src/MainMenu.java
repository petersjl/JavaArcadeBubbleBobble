import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainMenu extends JPanel {
	
	private final int WIDTH = 825;
	private final int HEIGHT = 1007;
	
	private BubbleFrame frame;
	
	public MainMenu(BubbleFrame frame) {
		this.frame = frame;
		
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setMaximumSize(new Dimension(WIDTH,HEIGHT));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(new JLabel("<html><h1> A SUB-PAR</h1></html>"));
		add(new JLabel("<html><h1> PLATFORMER</h1></html>"));
		
		add(Box.createVerticalStrut(100));
		
		JButton play = new JButton("<html><h3> Play Game </h3></html>");
		JButton howTo = new JButton("<html><h3> How To Play </h3></html>");
		JButton quit = new JButton("<html><h3> Quit </h3></html>");
		
		play.setMaximumSize(new Dimension(200, 30));
		howTo.setMaximumSize(new Dimension(200, 30));
		quit.setMaximumSize(new Dimension(200, 30));
		
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//frame.startGame();
				
			}
			
		});
		
		howTo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane optionPane = new JOptionPane("This button isn't implemented yet.", JOptionPane.ERROR_MESSAGE);    
				JDialog dialog = optionPane.createDialog("ERROR: LAZY PROGRAMMERS");
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
				
			}
			
		});
		
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				
			}
			
		});
		
		add(play);
		
		add(Box.createVerticalStrut(20));
		
		add(howTo);
		
		add(Box.createVerticalStrut(20));
		
		add(quit);
			
	}
}
