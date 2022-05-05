import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {
	
	ArrayList<Node> nodeList = new ArrayList<Node>();
	
	public GraphPanel() {
		super();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draw my stuff
		for (int a = 0; a < nodeList.size(); a++) {
			g.drawOval(nodeList.get(a), i, i, i);
		}
	}
}
