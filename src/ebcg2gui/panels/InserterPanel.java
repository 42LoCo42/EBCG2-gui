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
		
		g.setColor(Color.BLACK);
		for(int i=0; i<colCount; i++) {
			g.drawLine(getWidth()/colCount * (i+1), 0, getWidth()/colCount * (i+1), getHeight());
		}
		
		g.drawString(Integer.toString(currentNum), getWidth()/colCount * selected, getHeight()/2);
	}
}
