package flappyBird;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener{
	public static FlappyBird flappyBird;
	public static int WIDTH=800,HEIGTH=800;
	public Renderer renderer;
	public Rectangle bird;
	public int ticks,yMotion, score;
	public ArrayList<Rectangle> columns;
	public Random r;
	public boolean gameOver, started;
	public ArrayList<Integer> record;
	 
	public FlappyBird() {
		JFrame jframe= new JFrame();
		Timer timer= new Timer(20,this);
		renderer= new Renderer();
		r=new Random();
		record= new ArrayList<>();
		jframe.add(renderer);
		jframe.setTitle("Flappy Bird");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//SAIR NO X
		jframe.setSize(WIDTH,HEIGTH);//DEFINIR O TAMANHO DA TELA
		jframe.addMouseListener(this);//OUVIR O RATO
		jframe.addKeyListener(this);//OUVIR UMA TECLA
		jframe.setResizable(false);
		jframe.setVisible(true);
		
		bird= new Rectangle(WIDTH / 2-10, HEIGTH /2-10,20,20);//para aparecer no centro do ecra
		columns= new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		//como e o jogo continua a correr e como e q ele entra nos outros metodos??
		//primeiro entra nos overrides e dps nos metodos normais do prog??
		
		timer.start();
		
	}
	public void addColumn(boolean start) {
		int space=320;//300
		int width=100;
		int heigth=50+r.nextInt(300);
		if(start) {
			columns.add(new Rectangle(WIDTH+ width+columns.size()*300,HEIGTH-heigth-120,width,heigth));//cima 
			columns.add(new Rectangle(WIDTH+ width+(columns.size()-1)*300,0,width,HEIGTH-heigth-space));//baixo
		}else {
			columns.add(new Rectangle(columns.get(columns.size()-1).x + 600,HEIGTH-heigth-120,width,heigth));//cima 
			columns.add(new Rectangle(columns.get(columns.size()-1).x,0,width,HEIGTH-heigth-space));//baixo
		}
		
	}
	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
		
	}
	public void jump() {
		if(gameOver) {
			bird= new Rectangle(WIDTH / 2-10, HEIGTH /2-10,20,20);//para aparecer no centro do ecra
			columns.clear();
			yMotion=0;
			score=0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			gameOver=false;
		}
		if(!started) {
			started=true;
		}
		else if(!gameOver) {
			if(yMotion>0)
				yMotion=0;
			yMotion-=10;//10
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		int speed=10;//10
		ticks++;
		if(started) {
			for(int i=0;i<columns.size();i++) {
				Rectangle column = columns.get(i);
				column.x-=speed;
			}
		//para o passaro cair constamente para baixo
			if(ticks%2==0 && yMotion < 15) {
				yMotion+=2; 
			}
			for(int i=0;i<columns.size();i++) {
				Rectangle column = columns.get(i);
				if(column.x+column.width < 0) {
					columns.remove(column);
					if(column.y==0) {
						addColumn(false);
					}
				}				
			}
		
		bird.y+=yMotion;
		for(Rectangle column : columns) {
			if(column.y==0 && bird.x +bird.width/2>column.x + column.width/2-10 && bird.x+bird.width/2
					<column.x+column.width/2+10) {//p add o score qdo passa no centros
				if(!gameOver)//PARA PARAR DE ADD O SCORE QUANDO SE PERDE
					score++;
			}
			if(column.intersects(bird)) {
				gameOver=true;
				bird.x=column.x - bird.width;
			}
		}
		
		if(bird.y>HEIGTH-120 || bird.y<0) {
			gameOver=true;
		}
		if(gameOver)
			bird.y=HEIGTH-120-bird.height;
		}
		renderer.repaint();
	}
	
	
	public void repaint(Graphics g) {
		g.setColor(Color.cyan);//definir cor
		g.fillRect(0, 0, WIDTH, HEIGTH);//encher o quadrado com esta cor
		
		g.setColor(Color.orange);//chao
		g.fillRect(0, HEIGTH-150,WIDTH, HEIGTH-150);
		
		g.setColor(Color.green);//relva
		g.fillRect(0, HEIGTH-150, WIDTH, 20);
		
		g.setColor(Color.red);//definir cor primeiro
		g.fillRect(bird.x,bird.y,bird.width,bird.height);//dps encher o ecra com o passaro
		
		for(Rectangle column:columns) {
			paintColumn(g,column);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial",1,100));
		if(!started) {
			g.drawString("Click to start!",100,HEIGTH/2-50);
		}
		
		if(gameOver) {
			bird.y=HEIGTH-120;
			g.drawString("GAME OVER",100,HEIGTH/2-50);
			g.drawString("Score: "+score,100,HEIGTH/2+100);
			record.add(score);
			int highScore=HighScore();
			g.drawString("High Score: "+highScore,100,HEIGTH/2+200);
		}
		
		if(!gameOver && started) {
			g.drawString(String.valueOf(score),WIDTH/2-25,100);
		}
				
	}
	public int HighScore() {
		int biggest=record.get(0);
		int i=0;
		for(int a: record) {
			int newBiggest=record.get(i);
			if(newBiggest>biggest)
				biggest=newBiggest;
			i++;
		}
		return biggest;		
	}
	
	public static void main(String[] args) {
		flappyBird=new FlappyBird();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
		
	}

	
	
	

}
