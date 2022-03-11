import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelReader {
	private FileReader reader;
	private Scanner scanner;
	private String totalText;

	/**
	 * Gets the map for the blocks in the level.
	 * Will be used in main game to create levels.
	 * @return
	 */
	public ArrayList<ArrayList<Integer>> getLevelDat() {
		String findText = "Leveldat:";
		int beg = totalText.indexOf(findText) + findText.length() + 1;
		String subbed = totalText.substring(beg);
		int end = subbed.indexOf("end") + beg;
		String s = totalText.substring(beg, end - 2);

//		s = s.replaceAll("\n", "");
//		s = s.replaceAll("\r", "");
		s = s.replaceAll(" ", "");
		s = s.replaceAll("\r", "");
		if (s.substring(0, 3).contains("\n")) {
			s = s.replaceFirst("\n", "");
		}

		@SuppressWarnings("resource")
		Scanner temp = new Scanner(s);
		temp.useDelimiter(",");
		ArrayList<ArrayList<Integer>> temp1 = new ArrayList<ArrayList<Integer>>();
		String persistent1 = null;
		while (temp.hasNext()) {
			ArrayList<Integer> temp2 = new ArrayList<Integer>();
			if (persistent1 != null)
				temp2.add(Integer.valueOf(persistent1));
			while (temp.hasNext()) {
				String str = temp.next();
				if (str.contains("\n")) {
//					persistent1 = str.replace("\r\n", "");
//					persistent1 = str.replace("\n\r", "");
					persistent1 = str.replace("\n", "");
//					persistent1 = str.replace(" ", "");
//					persistent1 = str.replace("\r", "");
					break;

				}
				int a = Integer.valueOf(str);
				temp2.add(a);
			}
			temp1.add(temp2);
		}
		return temp1;
	}

//	public ArrayList<ArrayList<Block>> makeBlock(ArrayList<ArrayList<Integer>> data) {
//		ArrayList<ArrayList<Block>> blocks = new ArrayList<ArrayList<Block>>();
//		System.out.println(data.size());
//		System.out.println(data.get(0).size());
//		int x = 0, y = 0;
//		for (ArrayList<Integer> ali : data) {
//			ArrayList<Block> tempBlock = new ArrayList<Block>();
//			for (Integer in : ali) {
//				tempBlock.add(new Block(in, x*30, y*30));
//				x++;
//			}
//			blocks.add(tempBlock);
//			System.out.println("RUN");
//			y++;
//			x=0;
//		}
//		System.out.println(blocks.size());
//		System.out.println(blocks.get(0).size());
//		for (ArrayList<Block> b1 : blocks) {
//			for (Block b2 : b1) {
//				System.out.println(b2.toString());
//			}
//		}

//		System.out.println(blocks.size());

//		return blocks;

//	}

	public ArrayList<Entity> readEnemies(GameWorld g) {
		return readSomething("Enemies", g);
	}

	public ArrayList<Entity> readFruit(GameWorld g) {
		return readSomething("Fruit", g);
	}

	public ArrayList<Entity> readPowerups(GameWorld g) {
		return readSomething("Powerups", g);
	}

	/**
	 * Given a item to search for, the scanner will search the document for a
	 * section that start with the keyword. From there, it will produce the
	 * respected objects.
	 * 
	 * @param name
	 * @param g
	 * @return
	 */
	public ArrayList<Entity> readSomething(String name, GameWorld g) {
		String findText = name + ":";
		int beg = totalText.indexOf(findText) + findText.length() + 1;
		String subbed = totalText.substring(beg);
		int end = subbed.indexOf("end") + beg;
		String s = totalText.substring(beg, end - 2);
		s = s.replaceAll("\n", "");
		s = s.replaceAll("\r", "");
		s = s.replaceAll(" ", "");
		@SuppressWarnings("resource")
		Scanner temp = new Scanner(s);
		temp.useDelimiter(",");
		ArrayList<Entity> en = new ArrayList<Entity>();

		while (temp.hasNext()) {
			int a = Integer.valueOf(temp.next());
			int b = Integer.valueOf(temp.next());
			int c = Integer.valueOf(temp.next());
			if (name == "Enemies")
				if (a == 0)
					en.add(new Walker(g, new Point2D.Double(30 * b, 30 * c + 30)));
				else
					en.add(new Shooter(g, new Point2D.Double(30 * b, 30 * c + 30)));
			else if (name == "Fruit")
				en.add(new Fruit(g, new Point2D.Double(30 * b, 30 * c)));
			else
				en.add(new PowerUp(g, new Point2D.Double(30 * b, 30 * c), a));
		}

//		for (AbstractEntity e : en) {
//			System.out.println(e.toString());
//		}

		return en;
	}

	public LevelReader(int levelNum) {
		try {
			reader = new FileReader("./Levels/Level" + levelNum + ".txt");
			scanner = new Scanner(reader);
			totalText = scanner.useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			System.out.println("Level doesn't exist, try again");
		}
	}

}
