/*
 * Author: Armaan Hajar
 * Date: 5/18/22
 * Description: A project where the user can draw nodes and connect them using edges. Each edge has
 *              a number and when the user clicks the "Travelling Salesman" button, the program finds
 *              the shortest path between all the nodes using the assigned edge values
 */
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
    // setters for the variables, buttons, text fields, array lists, and containers
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
	Container south = new Container();
	JTextField salesmanStartTF = new JTextField("A");
	JButton salesmanB = new JButton("Travelling Salesman");
	final int NODE_CREATE = 0;
	final int EDGE_FIRST = 1;
	final int EDGE_SECOND = 2;
	int state = NODE_CREATE;
	Node first = null;
	ArrayList<ArrayList<Node>> completed = new ArrayList<ArrayList<Node>>();
	
	public GraphCreator() {
        // assigns containers to places on the gui
        // puts buttons and text fields into the containers
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
		south.setLayout(new GridLayout(1,2));
		south.add(salesmanStartTF);
		south.add(salesmanB);
		salesmanB.addActionListener(this);
		frame.add(south, BorderLayout.SOUTH);
		
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
        // adds a node to the graph
        if (state == NODE_CREATE) {
			panel.addNode(e.getX(), e.getY(), labelsTF.getText());
		}
        // sets first edge for connecting two nodes using an edge
		else if (state == EDGE_FIRST) {
			Node n = panel.getNode(e.getX(), e.getY());
			if (n != null) {
				first = n;
				state = EDGE_SECOND;
				n.setHighlighted(true);
			}
		}
        // sets second edge for connecting two nodes using an edge
		else if (state == EDGE_SECOND) {
			Node n = panel.getNode(e.getX(), e.getY());
			if (n != null && !first.equals(n)) {
				String s = labelsTF.getText();
				boolean valid = true;
				for (int a = 0; a < s.length(); a++) {
					if (Character.isDigit(s.charAt(a)) == false) {
						valid = false;
					}
				}
                // if edge is named a number, allow for the edge to be placed
				if (valid == true) {
					first.setHighlighted(false);
					panel.addEdge(first, n, labelsTF.getText());
					first = null;
					state = EDGE_FIRST;
					System.out.println("valid");
				}
                // if edge is not named a number, dont allow for the edge to be placed 
				else {
					JOptionPane.showMessageDialog(frame, "Can Only Have Digits in Edge Labels.");
					System.out.println("invalid");
				}
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
		// color node button when selected
        if (e.getSource().equals(nodeB)) {
			nodeB.setBackground(Color.GREEN);
			nodeB.setOpaque(true);
			edgeB.setBackground(Color.LIGHT_GRAY);
			edgeB.setOpaque(true);
			state = NODE_CREATE;
		}
        // color edge button when selected
		if (e.getSource().equals(edgeB)) {
			edgeB.setBackground(Color.GREEN);
			edgeB.setOpaque(true);
			nodeB.setBackground(Color.LIGHT_GRAY);
			nodeB.setOpaque(true);
			state = EDGE_FIRST;
			panel.stopHighlighting();
			frame.repaint();
		}
        // code for test connection button
		if (e.getSource().equals(connectedB)) {
            // for when first node is not in graph
			if (panel.nodeExists(firstNode.getText()) == false) {
				JOptionPane.showMessageDialog(frame, "First Node Is Not In Your Graph.");
			}
            // for when second node is not in graph
			else if (panel.nodeExists(secondNode.getText()) == false) {
				JOptionPane.showMessageDialog(frame, "Second Node Is Not In Your Graph.");
			}
			else {
                // adds names of nodes to an array list
				Queue queue = new Queue();
				ArrayList<String> connectedList = new ArrayList<String>();
				connectedList.add(panel.getNode(firstNode.getText()).getLabel());
				ArrayList<String> edges = panel.getConnectedLabels(firstNode.getText());
                // adds edges to queue
				for (int a = 0; a < edges.size(); a++) {
					queue.enqueue(edges.get(a));
				}
				while (queue.isEmpty() == false) {
                    // adds nodes to queue
					String currentNode = queue.dequeue();
					if (connectedList.contains(currentNode) == false) {
						connectedList.add(currentNode);
					}
                    // adds edges to queue
					edges = panel.getConnectedLabels(currentNode);
					for (int a = 0; a < edges.size(); a++) {
						if (connectedList.contains(edges.get(a)) == false) {
							queue.enqueue(edges.get(a));
						}
					}
				}
                // for when two nodes are connected
				if (connectedList.contains(secondNode.getText())) {
					JOptionPane.showMessageDialog(frame, "Connected!");
				}
                // for when two nodes are not connected
				else {
					JOptionPane.showMessageDialog(frame, "Not Connected.");

				}
			}
		}
        // when travelling salesman button is pressed
		if (e.getSource().equals(salesmanB)) {
            // check if node exists
			if (panel.getNode(salesmanStartTF.getText()) != null) {
                // clears completed list
                completed.clear();
                // calls travelling
				travelling(panel.getNode(salesmanStartTF.getText()), new ArrayList<Node>(), 0);
                // goes through edges in completed and calculates a total
                if (completed.size() > 0) {
					int lowest = 0;
					int index = 0;
					ArrayList<Edge> edgeList = panel.getEdgeList();
					for (int a = 0; a < completed.size(); a++) {
						int total = 0;
						for (int b = 0; b < completed.get(a).size() - 1; b++) {
							Node first = completed.get(a).get(b);
							Node second = completed.get(a).get(b + 1);
							for (int c = 0; c < edgeList.size(); c++) {
								Edge edge = edgeList.get(c);
								if (first.equals(edge.getOtherEnd(second))) {
									total += Integer.parseInt(edge.getLabel());
								}
							}
						}
						// if the total is less than the lowest or if it's the first total, set lowest total to the total
						if (total < lowest || a == 0) {
							lowest = total;
							index = a;
						}
					}
					// creates output message
					String outputMessage = "";
					for (int a = 0; a < completed.get(index).size(); a++){
						outputMessage += completed.get(index).get(a).getLabel()+", ";
					}
                    // displays output message
					JOptionPane.showMessageDialog(frame, "The lowest cost path is " + outputMessage + "with a cost of " + lowest);
				}
                // if no paths are possible
                else {
					JOptionPane.showMessageDialog(frame, "No Possible Path!");
				}
			}
            // if starting node is not valid
			else {
				JOptionPane.showMessageDialog(frame, "Not a Valid Starting Node!");
			}
		}
	}
	
	public void travelling(Node n, ArrayList<Node> path, int total) {
        ArrayList<Node> newList = new ArrayList<Node>();
        // if the number of nodes in the path is equal to the number of nodes
        if (path.size() == panel.nodeList.size()) {
            // add this path to the completed list
            for (int a = 0; a < path.size(); a++) {
                newList.add(path.get(a));
            }
            completed.add(newList);
        }
        // else
		else {
            // for each edge
            for (int a = 0; a < panel.edgeList.size(); a++) {
                Edge e = panel.edgeList.get(a);
                // see if they are connected to the current node
                if (e.getOtherEnd(n) != null) {
                    // if they are not already in the path
                    if (path.contains(e.getOtherEnd(n)) == false) {
                        // add node to path
                        path.add(e.getOtherEnd(n));
                        // travelling(node, path, total + edge cost);
                        travelling(e.getOtherEnd(n), path, total + Integer.parseInt(e.getLabel()));
                    }
                }
            }
        }	
        if (path.size() > 0) {
            // remove the last thing in the path
            path.remove(path.size() - 1);
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