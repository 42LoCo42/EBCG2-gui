package ebcg2gui.panels;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.json.JSONArray;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
	private JSONArray boardArray;
	private int colCount;
	private int rowCount;
	
	public void setBoard(JSONArray _boardArray) {
		boardArray = _boardArray;
		colCount = boardArray.length();
		rowCount = boardArray.getJSONArray(0).length();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		for(int x=0; x<colCount; x++) {
			g.drawLine(getWidth()/colCount * (x+1), 0, getWidth()/colCount * (x+1), getHeight());
			
			for(int y=0; y<rowCount; y++) {
				g.drawLine(0, getHeight()/rowCount * (y+1), getWidth(), getHeight()/rowCount * (y+1));
			}
		}
	}
}
