package ebcg2gui.panels;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InserterPanel extends JPanel {
	private int colCount, selected, currentNum;
	
	public void setData(int _colCount, int _selected, int _currentNum) {
		colCount = _colCount;
		selected = _selected;
		currentNum = _currentNum;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(colCount <= 0) return;
		
		g.setColor(Color.BLACK);
		for(int i=0; i<colCount; i++) {
			g.drawLine(getWidth()/colCount * (i+1), 0, getWidth()/colCount * (i+1), getHeight());
		}
		
		int cellWidth = getWidth()/(2*colCount);
		g.drawString(Integer.toString(currentNum),  cellWidth + (getWidth()*selected)/colCount, getHeight()/2);
	}
}
