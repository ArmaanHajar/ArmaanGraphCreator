import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class GraphCreator implements ActionListener, MouseListener {
	
	JFrame frame = new JFrame();
	GraphPanel panel = new GraphPanel();
	JButton nodeB = new JButton("Node");
	JButton edgeB = new JButton("Edge");
	JTextField labelsTF = new JTextField("A");
	JTextField firstNode = new JTextField("First");
	JTextField secondNode = new JTextField("Second");
	JButton connectedB = new JButton("Test Connected");
	Container west = new Container();
	Container east = new Container();
	final int NODE_CREATE = 0;
	final int EDGE_FIRST = 1;
	final int EDGE_SECOND = 2;
	int state = NODE_CREATE;
	Node first = null;
	
	public GraphCreator() {
		frame.setSize(800,600);
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		west.setLayout(new GridLayout(3,1));
		west.add(nodeB);
		nodeB.addActionListener(this);
		nodeB.setBackground(Color.GREEN);
		west.add(edgeB);
		edgeB.addActionListener(this);
		edgeB.setBackground(Color.LIGHT_GRAY);
		west.add(labelsTF);
		frame.add(west, BorderLayout.WEST);
		east.setLayout(new GridLayout(3,1));
		east.add(firstNode);
		east.add(secondNode);
		east.add(connectedB);
		connectedB.addActionListener(this);
		frame.add(east, BorderLayout.EAST);
		panel.addMouseListener(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}

	public static void main(String[] args) {
		new GraphCreator();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (state == NODE_CREATE) {
			panel.addNode(e.getX(), e.getY(), labelsTF.getText());
		}
		else if (state == EDGE_FIRST) {
			Node n = panel.getNode(e.getX(), e.getY());
			if (n != null) {
				first = n;
				state = EDGE_SECOND;
				n.setHighlighted(true);
			}
		}
		else if (state == EDGE_SECOND) {
			Node n = panel.getNode(e.getX(), e.getY());
			if (n != null && !first.equals(n)) {
				first.setHighlighted(false);
				panel.addEdge(first, n, labelsTF.getText());
				first = null;
				state = EDGE_FIRST;
			}
		}
		frame.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(nodeB)) {
			nodeB.setBackground(Color.GREEN);
			edgeB.setBackground(Color.LIGHT_GRAY);
			state = NODE_CREATE;
		}
		if (e.getSource().equals(edgeB)) {
			edgeB.setBackground(Color.GREEN);
			nodeB.setBackground(Color.LIGHT_GRAY);
			state = EDGE_FIRST;
			panel.stopHighlighting();
			frame.repaint();
		}
		if (e.getSource().equals(connectedB)) {
			if (panel.nodeExists(firstNode.getText()) == false) {
				JOptionPane.showMessageDialog(frame, "First Node Is Not In Your Graph.");
			}
			else if (panel.nodeExists(secondNode.getText()) == false) {
				JOptionPane.showMessageDialog(frame, "Second Node Is Not In Your Graph.");
			}
			else {
				Queue queue = new Queue();
				ArrayList<String> connectedList = new ArrayList<String>();
				connectedList.add(panel.getNode(firstNode.getText()).getLabel());
				ArrayList<String> edges = panel.getConnectedLabels(firstNode.getText());
				for (int a = 0; a < edges.size(); a++) {
					queue.enqueue(edges.get(a));
				}
				while (queue.isEmpty() == false) {
					String currentNode = queue.dequeue();
					if (connectedList.contains(currentNode) == false) {
						connectedList.add(currentNode);
					}
					edges = panel.getConnectedLabels(currentNode);
					for (int a = 0; a < edges.size(); a++) {
						if (connectedList.contains(edges.get(a)) == false) {
							queue.enqueue(edges.get(a));
						}
					}
				}
				if (connectedList.contains(secondNode.getText())) {
					JOptionPane.showMessageDialog(frame, "Connected!");
				}
				else {
					JOptionPane.showMessageDialog(frame, "Not Connected.");

				}
			}
		}
	}
	
	/*
	 * Adjacency Matrix
	 * 
	 *		A	B	C
	 * A	1	1	1
	 * B	1	1	0
	 * C	1	0	1
	 *  
	 */
	
}
