package ebcg2gui.tools;

import org.json.JSONArray;
import org.json.JSONObject;

import ebcg2gui.GUI;

public class SaveStateManager {
	private GUI instance;
	private int selected = 0;
	
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
		int currentNum = data.getInt("currentNum");
		
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
	}
}
