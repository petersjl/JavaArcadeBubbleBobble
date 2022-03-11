
public class GameInfo {
	private static int score = 0;
	private static int enemyKilled = 0;
	private static boolean shouldAddLife = false;

	public static void addScore(int amount) {
		score += amount;
		if(score % 1000 == 0) shouldAddLife = true;
	}

	public static void addEnemyKilled() {
		enemyKilled++;
	}

	public static void resetScore() {
		score = 0;
	}

	public static void resetEnemyKilled() {
		enemyKilled = 0;
	}

	public static String toString1() {
		return "Score: " + score + "\nEnemies Killed: " + enemyKilled;

	}
	
	public static int getScore() {
		return score;
	}
	
	public static boolean checkLife() {
		if(shouldAddLife) {
			shouldAddLife = !shouldAddLife;
			return true;
		}
		return false;
	}
}
