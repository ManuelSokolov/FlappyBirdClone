package flappyBird;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Renderer extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g) { //ctrl+space paintC..
		super.paintComponent(g);//chama o codigo do Jpanel
		FlappyBird.flappyBird.repaint(g);//passar os graficos para o objeto flapp
		
	}
}
