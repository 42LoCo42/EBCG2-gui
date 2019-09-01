package ebcg2gui;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import org.json.JSONArray;
import org.json.JSONObject;

public class SaveStateManager {
	private GUI instance;
	
	private int score, maxScore, currentNum, selected, colCount;
	
	private static final String mergeInfoFormat = 
			" %d%n"
			+ "-------------%n"
			+ " Merges:%n"
			+ " x2 = %d%n"
			+ " x3 = %d%n"
			+ " x4 = %d%n"
			+ "-------------%n"
			+ " Points:%n"
			+ " x2 = %d%n"
			+ " x3 = %d%n"
			+ " x4 = %d%n"
			;
	
	public SaveStateManager(GUI _instance) {
		instance = _instance;
	}
	
	public void display(String rawData) {
		JSONObject data = new JSONObject(rawData);
		score = data.getInt("score");
		maxScore = data.getInt("maxScore");
		currentNum = data.getInt("currentNum");
		
		// update labels
		instance.getLblPoints().setText("Score: " + Integer.toString(data.getInt("score")));
		instance.getLblMaxpoints().setText("Total score: " + Integer.toString(data.getInt("maxScore")));
		
		// update MergeInfoText
		instance.getMergeInfoText().setText(String.format(mergeInfoFormat, 
				currentNum,
				currentNum + 1,
				currentNum + 2,
				currentNum + 3,
				(int) Math.pow(2, currentNum + 1),
				(int) Math.pow(2, currentNum + 2),
				(int) Math.pow(2, currentNum + 3)
				));
		
		// update board & inserter
		JSONArray board = data.getJSONArray("board");
		instance.getBoardPanel().setBoard(board);
		instance.getInserterPanel().setData(board.length(), selected, currentNum);
		instance.getContentPane().validate();
		instance.getContentPane().repaint();
		
		colCount = board.length();
	}
	
	public KeyEventDispatcher inserterAdapter = new KeyEventDispatcher() {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if(e.getID() != KeyEvent.KEY_RELEASED) return false;
			
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				selected--;
				if(selected < 0) {
					selected += colCount;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				selected = (selected+1) % colCount;
			}
			else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				instance.getCon().send("insert " + selected + " ");
			}
			
			instance.getInserterPanel().setData(colCount, selected, currentNum);
			instance.getContentPane().validate();
			instance.getContentPane().repaint();
			return false;
		}
	};
	
	public int getScore() {
		return score;
	}
	public int getMaxScore() {
		return maxScore;
	}
}
