package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

/***********************************************************************/
/*                                                                     */
/* Classe com métodos úteis para implementação de um jogo. Inclui:     */
/*                                                                     */
/* - Método para iniciar modo gráfico.                                 */
/*                                                                     */
/* - Métodos para desenhos de formas geométricas.                      */
/*                                                                     */
/* - Método para atualizar o display.                                  */
/*                                                                     */
/* - Método para verificar o estado (pressionada ou não pressionada)   */
/*   das teclas usadas no jogo:                                        */
/*                                                                     */
/*   	- up, down, left, right: movimentação do player.               */
/*		- control: disparo de projéteis.                               */
/*		- ESC: para sair do jogo.                                      */
/*                                                                     */
/***********************************************************************/

public class GameLib {
	
	public static final int WIDTH = 480;
	public static final int HEIGHT = 720;
	
	public static final int KEY_UP = 0;
	public static final int KEY_DOWN = 1;
	public static final int KEY_LEFT = 2;
	public static final int KEY_RIGHT = 3;
	public static final int KEY_CONTROL = 4;
	public static final int KEY_ESCAPE = 5;
	public static final int KEY_ENTER = 6;
	public static final int KEY_W = 7;
	public static final int KEY_A = 8;
	public static final int KEY_S = 9;
	public static final int KEY_D = 10;

	private static MyFrame frame = null;
	private static Graphics g = null;
	private static MyKeyAdapter keyboard = null;
	
	public static void initGraphics(){
		
		frame = new MyFrame("Projeto COO");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setVisible(true);
		
		keyboard = new MyKeyAdapter();
		frame.addKeyListener(keyboard);
		frame.requestFocus();
		
		frame.createBufferStrategy(2);		
		g = frame.getBufferStrategy().getDrawGraphics();
	}
	
	public static void setColor(Color c){
		
		g.setColor(c);
	}
	
	public static void drawLine(double x1, double y1, double x2, double y2){
		
		g.drawLine((int) Math.round(x1), (int) Math.round(y1), (int) Math.round(x2), (int) Math.round(y2));
	}
	
	public static void drawCircle(double cx, double cy, double radius){
		
		int x = (int) Math.round(cx - radius);
		int y = (int) Math.round(cy - radius);
		int width = (int) Math.round(2 * radius);
		int height = (int) Math.round(2 * radius);
		
		g.drawOval(x, y, width, height);
	}
	
	public static void drawDiamond(double x, double y, double radius){
		
		int x1 = (int) Math.round(x);
		int y1 = (int) Math.round(y - radius);
		
		int x2 = (int) Math.round(x + radius);
		int y2 = (int) Math.round(y);
		
		int x3 = (int) Math.round(x);
		int y3 = (int) Math.round(y + radius);
		
		int x4 = (int) Math.round(x - radius);
		int y4 = (int) Math.round(y);
		
		drawLine(x1, y1, x2, y2);
		drawLine(x2, y2, x3, y3);
		drawLine(x3, y3, x4, y4);
		drawLine(x4, y4, x1, y1);
	}
	
	public static void drawPlayer(double player_X, double player_Y, double player_size){
		
		GameLib.drawLine(player_X - player_size, player_Y + player_size, player_X, player_Y - player_size);
		GameLib.drawLine(player_X + player_size, player_Y + player_size, player_X, player_Y - player_size);
		GameLib.drawLine(player_X - player_size, player_Y + player_size, player_X, player_Y + player_size * 0.5);
		GameLib.drawLine(player_X + player_size, player_Y + player_size, player_X, player_Y + player_size * 0.5);
	}

	public static void fillPlayer(double player_X, double player_Y, double player_size) {
		int[] xPoints = {
				(int) Math.round(player_X - player_size),
				(int) Math.round(player_X),
				(int) Math.round(player_X + player_size),
				(int) Math.round(player_X + player_size / 4.0),
				(int) Math.round(player_X - player_size / 4.0)
		};

		int[] yPoints = {
				(int) Math.round(player_Y + player_size),
				(int) Math.round(player_Y - player_size),
				(int) Math.round(player_Y + player_size),
				(int) Math.round(player_Y + player_size * 0.65),
				(int) Math.round(player_Y + player_size * 0.65)
		};

		g.fillPolygon(xPoints, yPoints, 5);
	}
	
	public static void drawExplosion(double x, double y, double alpha){

		int p = 5;
		int r = (int) (255 - Math.pow(alpha, p) * 255);
		int g = (int) (128 - Math.pow(alpha, p) * 128);
		int b = 0;

		GameLib.setColor(new Color(r, g, b));
		GameLib.drawCircle(x, y, alpha * alpha * 40);
		GameLib.drawCircle(x, y, alpha * alpha * 40 + 1);
	}

	public static void drawShieldItem(double x, double y, double size) {
		double thirtiethSize = size / 3.0;

		int[] shieldXPoints = {
				(int) (x - thirtiethSize * 1.5),
				(int) (x),
				(int) (x + thirtiethSize * 1.5),
				(int) (x + thirtiethSize * 1.2),
				(int) (x),
				(int) (x - thirtiethSize * 1.2),
				(int) (x - thirtiethSize * 1.5)};
		int[] shieldYPoints = {(int) (y - thirtiethSize), (int) (y - thirtiethSize), (int) (y - thirtiethSize),
				(int) (y), (int) (y + size / 3 * 2), (int) (y),
				(int) (y - thirtiethSize)};

		g.setColor(Color.YELLOW);

		g.fillPolygon(shieldXPoints, shieldYPoints, shieldXPoints.length);

		g.setColor(Color.WHITE);

		g.drawPolygon(shieldXPoints, shieldYPoints, shieldXPoints.length);

		g.drawLine((int) (x), (int) (y - thirtiethSize), (int) (x), (int) (y));
	}
	public static void drawSpaceship(double x, double y, double size) {
		double halfSize = size / 2.0;
		double quarterSize = size / 4.0;

		double[] spaceshipX = {
				x - halfSize, x + halfSize, x + quarterSize, x, x - quarterSize
		};
		double[] spaceshipY = {
				y - quarterSize, y - quarterSize, y + quarterSize, y + halfSize, y + quarterSize
		};

		drawLine(spaceshipX[0], spaceshipY[0], spaceshipX[1], spaceshipY[1]);
		drawLine(spaceshipX[1], spaceshipY[1], spaceshipX[2], spaceshipY[2]);
		drawLine(spaceshipX[2], spaceshipY[2], spaceshipX[3], spaceshipY[3]);
		drawLine(spaceshipX[3], spaceshipY[3], spaceshipX[4], spaceshipY[4]);
		drawLine(spaceshipX[4], spaceshipY[4], spaceshipX[0], spaceshipY[0]);

		drawCircle(x, y, quarterSize / 2);
	}

	public static void drawGameOver() {
		String[] lines = {"Game", "Over"};
		String restartText = "Press Enter to Restart";
		int font_size_gameover = 60;
		int font_size_restart = 20;
		int font_margin = 20;
		Color text_color = Color.YELLOW;

		Font fontGameOver = new Font("Arial", Font.BOLD, font_size_gameover);
		g.setFont(fontGameOver);

		FontMetrics fmGameOver = g.getFontMetrics();
		int maxHeightGameOver = 0;
		for (String line : lines) {
			int textHeight = fmGameOver.getAscent();
			maxHeightGameOver = Math.max(maxHeightGameOver, textHeight);
		}

		int totalHeightGameOver = lines.length * (maxHeightGameOver + font_margin);
		int startYGameOver = (GameLib.HEIGHT - totalHeightGameOver) / 2;

		g.setColor(text_color);
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			int textWidth = fmGameOver.stringWidth(line);

			int x = (GameLib.WIDTH - textWidth) / 2;

			int y = startYGameOver + i * (maxHeightGameOver + font_margin);

			g.drawString(line, x, y);
		}

		Font fontRestart = new Font("Arial", Font.PLAIN, font_size_restart);
		g.setFont(fontRestart);

		FontMetrics fmRestart = g.getFontMetrics();
		int textWidthRestart = fmRestart.stringWidth(restartText);
		int textHeightRestart = fmRestart.getAscent();

		int xRestart = (GameLib.WIDTH - textWidthRestart) / 2;

		int yRestart = startYGameOver + totalHeightGameOver + font_margin + textHeightRestart;

		g.drawString(restartText, xRestart, yRestart);
	}

	public static void drawTimer(long time) {
		Date date = new Date(time);
		String minutes = String.valueOf(date.getMinutes());
		String seconds = String.valueOf(date.getSeconds());
		String formattedTime = minutes + ":" + seconds;

		int x = GameLib.WIDTH - 50;
		int y = 40;
		int width = 40;
		int height = 20;

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawString(formattedTime, x + 10, y + 15);
	}
	public static void drawBossHealthBar(int currentHealth, int totalHealth) {
		int bar_width = 300;
		int bar_height = 25;
		int bar_margin = 25;
		Color BAR_BACKGROUND_COLOR = Color.GRAY;
		Color BAR_COLOR = Color.RED;

		int barX = (GameLib.WIDTH - bar_width) / 2;
		int barY = bar_margin;

		int barCurrentWidth = (int) ((double) currentHealth / totalHealth * bar_width);

		g.setColor(BAR_BACKGROUND_COLOR);
		g.fillRect(barX, barY, bar_width, bar_height);

		g.setColor(BAR_COLOR);
		g.fillRect(barX, barY, barCurrentWidth, bar_height);
	}

	public static void drawPlayerHealthBar(int currentHealth, int totalHealth) {
		int bar_height = 25;
		int heart_size = 20;
		int heart_padding = 10;

		Color HEART_COLOR = Color.RED;
		Color EMPTY_HEART_COLOR = Color.LIGHT_GRAY;

		int totalHeartWidth = totalHealth * (heart_size + heart_padding) - heart_padding;
		int barX = GameLib.WIDTH - totalHeartWidth - 10;
		int barY = GameLib.HEIGHT - bar_height - 10;

		for (int i = 0; i < totalHealth; i++) {
			int heartX = barX + i * (heart_size + heart_padding);
			int heartY = barY + (bar_height - heart_size) / 2;

			if (i < currentHealth) {
				g.setColor(HEART_COLOR);
			} else {
				g.setColor(EMPTY_HEART_COLOR);
			}
			drawHeart(g, heartX, heartY, heart_size, heart_size);
		}
	}

	public static void drawHeart(Graphics g, int x, int y, int width, int height) {
		int[] triangleX = {
				x - 2 * width / 18,
				x + width + 2 * width / 18,
				(x - 2 * width / 18 + x + width + 2 * width / 18) / 2};
		int[] triangleY = {
				y + height - 2 * height / 3,
				y + height - 2 * height / 3,
				y + height};
		g.fillOval(
				x - width / 12,
				y,
				width / 2 + width / 6,
				height / 2);
		g.fillOval(
				x + width / 2 - width / 12,
				y,
				width / 2 + width / 6,
				height / 2);
		g.fillPolygon(triangleX, triangleY, triangleX.length);
	}

	public static void fillRect(double cx, double cy, double width, double height){
		
		int x = (int) Math.round(cx - width/2);
		int y = (int) Math.round(cy - height/2);
		
		g.fillRect(x, y, (int) Math.round(width), (int) Math.round(height));
	}

	public static void fillOval(double cx, double cy, double width, double height){

		int x = (int) Math.round(cx - width/2);
		int y = (int) Math.round(cy - height/2);

		g.fillOval(x, y, (int) Math.round(width), (int) Math.round(height));
	}

	public static void fillSpaceship(double x, double y, double size){
		int halfSize = (int) (size / 2.0);
		int quarterSize = (int) (size / 4.0);

		int[] spaceshipX = {
				(int) x - halfSize,
				(int) x + halfSize,
				(int) x + quarterSize,
				(int) x,
				(int) x - quarterSize
		};
		int[] spaceshipY = {
				(int) y - quarterSize,
				(int) y - quarterSize,
				(int) y + quarterSize,
				(int) y + halfSize,
				(int) y + quarterSize
		};
		g.fillPolygon(spaceshipX, spaceshipY, 5);
	}
	
	public static void display(){
									
		g.dispose();
		frame.getBufferStrategy().show();
		Toolkit.getDefaultToolkit().sync();
		g = frame.getBufferStrategy().getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frame.getWidth() - 1, frame.getHeight() - 1);
		g.setColor(Color.WHITE);
	}
	
	public static boolean iskeyPressed(int index){
		
		return keyboard.isKeyPressed(index);
	}
	
	public static void debugKeys(){
		
		keyboard.debug();
	}
}

@SuppressWarnings("serial")
class MyFrame extends JFrame {
	
	public MyFrame(String title){
		
		super(title);
	}

	public void paint(Graphics g){ }
	
	public void update(Graphics g){ }
	
	public void repaint(){ }
}

class MyKeyAdapter extends KeyAdapter{
	
	private int [] codes = {
			KeyEvent.VK_UP,
			KeyEvent.VK_DOWN,
			KeyEvent.VK_LEFT,
			KeyEvent.VK_RIGHT,
			KeyEvent.VK_CONTROL,
			KeyEvent.VK_ESCAPE,
			KeyEvent.VK_ENTER,
			KeyEvent.VK_W,
			KeyEvent.VK_A,
			KeyEvent.VK_S,
			KeyEvent.VK_D,
	};
	
	private boolean [] keyStates = null;
	private long [] releaseTimeStamps = null;
	
	public MyKeyAdapter(){
		
		keyStates = new boolean[codes.length];
		releaseTimeStamps = new long[codes.length];
	}
	
	public int getIndexFromKeyCode(int keyCode){
		
		for(int i = 0; i < codes.length; i++){
			
			if(codes[i] == keyCode) return i;
		}
		
		return -1;
	}
	
	public void keyPressed(KeyEvent e){
		
		//System.out.println("KeyPressed " + e.getWhen() + " " + System.currentTimeMillis());
		
		int index = getIndexFromKeyCode(e.getKeyCode());
		
		if(index >= 0){
			
			keyStates[index] = true;
		}
	}
	
	public void keyReleased(KeyEvent e){
	
		//System.out.println("KeyReleased " + e.getWhen() + " " + System.currentTimeMillis());
		
		int index = getIndexFromKeyCode(e.getKeyCode());
		
		if(index >= 0){
			
			keyStates[index] = false;
			releaseTimeStamps[index] = System.currentTimeMillis();
		}
	}
	
	public boolean isKeyPressed(int index){
		
		boolean keyState = keyStates[index];
		long keyReleaseTime = releaseTimeStamps[index];
		
		if(keyState == false){

			if(System.currentTimeMillis() - keyReleaseTime > 5) return false;
		}
		
		return true;		
	}
	
	public void debug(){
		
		System.out.print("Key states = {");
		
		for(int i = 0; i < codes.length; i++){
			
			System.out.print(" " + keyStates[i] + (i < (codes.length - 1) ? "," : ""));
		}
		
		System.out.println(" }");
	}
}

