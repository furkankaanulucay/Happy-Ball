package AngryBirds;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import javax.swing.JPanel;

public class GameBoard extends JPanel implements ActionListener,MouseListener,MouseMotionListener {
		Timer timer = new Timer(1,this);
	    public double bubbleX = 200, bubbleY = 300;
	    public int bubbleW = 75, bubbleH = 75, xs = 0 , ys = 0, sayac = 0, minX = 800, maxX = 1100, minY = 100, maxY = 500, i = 0, heal = 3, point = 0;
	    public double powerX = 0, powerY = 0 , vi = 70, deg = 0 , del = 1 , dist = 0;
	    public float g = 9.8f;
	    public String sound = "C:\\Users\\pc\\eclipse-workspace\\Test\\src\\AngryBirds\\assets\\themesound.wav";
	    public Point[] location = new Point[5];
	    public float[] dash = { 2f, 0f, 2f };
	    public boolean isDrag = false, isReleased = false, isTop = false, drawPos = false, isHitX = false, isHitY = false, isExit = false, isFinish = false;;
        Image bg = Toolkit.getDefaultToolkit().createImage("C:\\Users\\pc\\eclipse-workspace\\Test\\src\\AngryBirds\\assets\\background.png");
        Image bird = Toolkit.getDefaultToolkit().createImage("C:\\Users\\pc\\eclipse-workspace\\Test\\src\\AngryBirds\\assets\\bird.png");
	    public GameBoard() {
	    	timer.start();
	    }
	    	    
	    private void doDrawing(Graphics g) {

	        Graphics2D g2d = (Graphics2D) g;
	        addMouseListener(this);
	        addMouseMotionListener(this);
	        soundEffect s = new soundEffect();
//          altta ki 2 satýrýn commentini kaldýrýrsak müzik efekti acýlýyor...
//	        s.setFile(sound); 
//	        s.play();



		    
	    }

	    @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        int x = (this.getWidth() - bg.getWidth(null)) / 2;
	        int y = (this.getHeight() - bg.getHeight(null)) / 2;
	        g.drawImage(bg, x, y, null);
	        g.drawImage(bird,(int) bubbleX,(int) bubbleY,bubbleW,bubbleH,null);
		    g.setColor(new Color(168, 50, 50));
		    g.setFont(new Font("ARIAL", Font.BOLD, 22));
		    g.drawString("Kalan Can: " + heal, 10, 550);
		    g.drawString("Puan: " + point, 1075, 550);
		    this.createBoxes(g);
		    this.drawPath(g);
	        g.dispose();
	        doDrawing(g);
	    }

	    public void actionPerformed(ActionEvent e) {
	    	
	 	       if(isReleased) {
	 	    	  this.isHit((int)(bubbleX + bubbleW / 2),(int)(bubbleY + bubbleH / 2));
	 	    	   	sayac += 1;
	 	    	   	del += 0.35;
	 	    	   	if(bubbleX >= 1100 || this.isHitX) { /* top x düzleminde duvara çarpar ise */
	 	    	   		this.isHitX = true;
	 	    	   		for(int i=0; i <= sayac; i++) {
	 	    	   			bubbleX -= 0.3;
	 	    	   		}
	 	    	   	}
	 	    	   	else if(!this.isHitX) {
	 	    	   		bubbleX = (powerX + (vi * Math.cos(deg) * del));
	 	    	   	}
	 	    	   		if((powerY + (vi * Math.sin(deg) * del + (0.5 * g * del * del))) > 525) {
	 	    	   			bubbleY = 525;
	 	    	   			try {
								Thread.sleep(1000);
								isFinish = true;
								this.resetGame();
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
	 	    	   			
	 	    	   		}
	 	    	   		else {
	 	    	   			bubbleY = (powerY + (vi * Math.sin(deg) * del + (0.5 * g * del * del)));	
	 	    	   		}
	 	        }
	        repaint();
	    }
        public void mouseDragged(MouseEvent e) 
        { 
            // update the label to show the point 
            // through which point mouse is dragged 
        	if(isDrag) {
        			powerX = e.getX();
        			powerY = e.getY();
        			bubbleX = e.getX() - 25;
            		bubbleY = e.getY() - 25;
            		dist = Math.sqrt((Math.pow((e.getY() - 300),2)) + (Math.pow((e.getX() - 200), 2)));
            		vi = dist / 2;
            		deg = (Math.atan2(300 - e.getY(), 200 - e.getX()));
            		drawPos = true;
            		repaint();
        	}
        }
        
        public void mouseMoved(MouseEvent e) 
        { 
            // update the label to show the point to which the cursor moved 
//        	System.out.println(e.getX() + " " + e.getY());
        	if(e.getX() >= bubbleX && e.getX() <= bubbleX + bubbleW  && e.getY() >= bubbleY && e.getY() <= bubbleY + bubbleH ) {
        		this.isDrag = true;
        	}
        	else {
        		this.isDrag = false;        		
        	}
        } 
        public void mouseReleased(MouseEvent e) {
        	if(isDrag) {
        		if(bubbleX < 0) {
        			isFinish = true;
        			this.resetGame();
        		}
        		isReleased = true;
        	}	
        }
        
        public void isHit(int x,int y) { /* fýrlatýlan topun kutulara çarpýp çarpmadýðýný kontrol eden fonksiyon */
        	for(int i = 0; i < 5; i++) {
        		if(x >= (this.location[i].x) && x <= (this.location[i].x + 50) && y >= (this.location[i].y) && y <= (this.location[i].y + 50)
        		|| x <= (this.location[i].x) && x >= (this.location[i].x + 50) && y <= (this.location[i].y) && y >= (this.location[i].y + 50)
        				) {	
        			this.location[i].x = -50;
        			this.location[i].y = -50;
        			point += 50;
        		}
        	}
        }
        
        public void createBoxes(Graphics g) { /* kutularýn oluþturulmasý */
	        Random r = new Random();
		    while(i < 5) {
		    	this.location[i] = new Point((r.nextInt(this.maxX - this.minX) + this.minX),(r.nextInt(this.maxY - this.minY) + this.minY));
		    	i++;
		    }
        	for(int a = 0; a < 5; a++) {
        		g.fill3DRect(this.location[a].x,this.location[a].y, 50, 50, false);
		    }
        }
        public void drawPath(Graphics g) {
        	Graphics2D g2d = (Graphics2D) g;
        	BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT,
			    	 BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);
			    	 g2d.setStroke(bs);
        	if(drawPos && !isReleased && bubbleX > 0) {
        		g.drawLine(200, 300, (int)bubbleX + (bubbleW / 2), (int)bubbleY + (bubbleH / 2));
		         for(double t=0.5; t<= 50; t+=0.1) {
		        	 
		        	 xs = (int)(powerX + (vi * Math.cos(deg) * t));
				     ys = (int)(powerY + (vi * Math.sin(deg) * t + (0.5 * this.g * t * t)));
				     g.fillOval(xs, ys, 3,3);
				     
		         }
		         
		         
		    }
        }
        public void resetGame() {
        	if(heal == 0) {
                    JOptionPane.showMessageDialog(null, "Tebrikler.\nPuanýnýz : "+ point +"." , "Oyun Bitti", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
        	} else {
    			if(point == 250 && heal > 0) {
    					point = 300;
                        JOptionPane.showMessageDialog(null, "Tebrikler can hakkýnýz bitmeden oyunu bitirdiniz.\nPuanýnýz : "+ point +"." , "Oyun Bitti", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
    			}
            	heal--;
            	bubbleX = 200;
            	bubbleY = 300;
            	xs = 0;
            	ys = 0;
            	dist = 0;
            	del = 1;
            	deg = 0;
            	sayac = 0;
            	vi = 70;
            	isHitX = false;
            	isFinish = false;
            	isReleased = false;
            	isDrag = false;
            	drawPos = false;
        	}
        }
        
        /* müzik eklenmesi */
	    public class soundEffect {
	    	Clip clip;
	    	public void setFile(String soundFileName) {
	    		
	    		try {
	    			File file = new File(soundFileName);
	    			AudioInputStream sound = AudioSystem.getAudioInputStream(file);
	    			clip = AudioSystem.getClip();
	    			clip.open(sound);
	    		}
	    		catch(Exception e) {
	    		}
	    	}
	    	
	    	public void play() {
	    		clip.setFramePosition(0);
	    		clip.start();
	    	}
	    }

		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
	}


