package Snake1v1;
// © 2018 Luke Jagg
// MIT License

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Group;
//import javafx.scene.Parent;
import javafx.scene.paint.*;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.stage.Stage;
import java.util.concurrent.*;

import javax.sound.sampled.AudioInputStream;

import java.io.File;
import java.util.Random;

public class Main extends Application {

	   	final int gridSize = 20;
	   	final int cellSize = 40;
	   	int score = 0;
	   	int score2 = 0;
	   	GraphicsContext gc;
	   	
	   	Vector2[] snakeFragments;
	   	Vector2[] snakeFragments2;
	   	Vector2[] fruits;
	   	int move = 0;
	   	int move2 = 0;
	   	// 0 Right -> goes clockwise
	   	Random rand;
	   	int debounce = -1;
		int debounce2 = -1;
	   	final long speed = 80;
	   	boolean dead = false;
	   	boolean dead2 = false;
	   	final int defaultLength = 2;
	   	final boolean diesOnWall = false;
	   	boolean eatOthers = false;
	   	final int numberOfFruit = 2;
	   	boolean noSmoothedCurves = false;
	   	double h = 0;
	   	double rainbowDelta = 340;
	   	double rainbowI = 20;
		static Color plr2Color = Color.rgb(255,255,255,1);
		final Color grid1 = Color.rgb(0,0,0);
		final Color grid2 = Color.rgb(255,255,250);
		final boolean customGrid = false;
		final boolean oddsOnly = false;
		final boolean twoPlayer = true;
		int frames = 0;
		float mult = 1;
		float mult2 = 0.5f;
		
		boolean customGrid2 = false;
		
	   	public void moveSnakes() {
	   	    		frames++;
	   	    		h += rainbowDelta;
	   	    		h %= 360;
	   	    		if (dead == false) {
		   	    		for (int i = snakeFragments.length-1; i > 0; i--) {
		   	    			if (!snakeFragments[i].equals(snakeFragments[i-1])) {
		   	    				snakeFragments[i] = new Vector2(snakeFragments[i-1].x, snakeFragments[i-1].y);
		   	    			}
		   	    		}
		   	    		if (move == 0) {
		   	    			snakeFragments[0].x++;
		   	    		}
		   	    		else if (move == 1) {
		   	    			snakeFragments[0].y++;
		   	    		}
		   	    		else if (move == 2) {
		   	    			snakeFragments[0].x--;
		   	    		}
		   	    		else if (move == 3) {
		   	    			snakeFragments[0].y--;
		   	    		}
		   	    		
		   	    		if (snakeFragments[0].x >= gridSize) {
		   	    			if (diesOnWall) {
		   	    				dead = true;
		   	    			}
		   	    			else {
		   	    				snakeFragments[0].x = 0;
		   	    			}
		   	    		}
		   	    		else if (snakeFragments[0].x < 0) {
		   	    			if (diesOnWall) {
		   	    				dead = true;
		   	    			}
		   	    			else {
		   	    				snakeFragments[0].x = gridSize - 1;
		   	    			}
		   	    		}
		   	    		if (snakeFragments[0].y >= gridSize) {
		   	    			if (diesOnWall) {
		   	    				dead = true;
		   	    			}
		   	    			else {
		   	    				snakeFragments[0].y = 0;
		   	    			}
		   	    		}
		   	    		else if (snakeFragments[0].y < 0) {
		   	    			if (diesOnWall) {
		   	    				dead = true;
		   	    			}
		   	    			else {
		   	    				snakeFragments[0].y = gridSize - 1;
		   	    			}
		   	    		}
		   	    		for (int i = 0; i < fruits.length; i++) {
			   	    		if (snakeFragments[0].x == fruits[i].x && snakeFragments[0].y == fruits[i].y) {
			   	    			score++;
			   	    			PlaceFruit(i);
			   	    		}
		   	    		}
		   	    		boolean worked = !dead;
		   	    		for (int i = 1; i < snakeFragments.length; i++) {
		    				if (snakeFragments[i].x == snakeFragments[0].x && snakeFragments[i].y == snakeFragments[0].y) {
		    					worked = false;
		    					System.out.println("DIED");
		    					break;
		    				}
		    			}
		   	    		for (int i = 0; i < snakeFragments2.length; i++) {
		    				if (snakeFragments2[i].x == snakeFragments[0].x && snakeFragments2[i].y == snakeFragments[0].y) {
		    					if (eatOthers && i != 0) {
		    						final int count = snakeFragments2.length - i;
		    						score2 -= count;
		    						score += count;
		    						
		    						Vector2[] temp = snakeFragments2;
				   	    			snakeFragments2 = new Vector2[snakeFragments2.length - count];
				   	    			
				   	    			for (int j = 0; j < snakeFragments2.length; j++) {
				   	    				snakeFragments2[j] = new Vector2(temp[j].x, temp[j].y);
				   	    			}
				   	    			break;
		    					}
		    					else {
		    						worked = false;
			    					System.out.println("DIED");
			    					break;
		    					}
		    				}
		    			}
		   	    		if (snakeFragments.length <= score + defaultLength) {
		   	    			Vector2[] temp = snakeFragments;
		   	    			snakeFragments = new Vector2[temp.length + 1];
		   	    			
		   	    			for (int i = 0; i < temp.length; i++) {
		   	    				snakeFragments[i] = new Vector2(temp[i].x, temp[i].y);
		   	    			}
	
		   	    			int x = snakeFragments[snakeFragments.length-2].x;
		   	    			int y = snakeFragments[snakeFragments.length-2].y;
		   	    			
		   	    			snakeFragments[snakeFragments.length-1] = new Vector2(x, y);
		   	    		}
		   	        if (worked == false) {
		   	        		dead = true;
		   	        }
		   	        debounce = -1;
		   	        if(dead) {
	   			    	//Media sound = new Media(new File(musicFile).toURI().toString());
	   			    	//MediaPlayer mediaPlayer = new MediaPlayer(sound);
	   	    		 	//mediaPlayer.play();
		   	        }
	   	    		}
	   	    		
	   	    		if (dead2 == false) {
		   	    		for (int i = snakeFragments2.length-1; i > 0; i--) {
		   	    			if (!snakeFragments2[i].equals(snakeFragments2[i-1])) {
		   	    				snakeFragments2[i] = new Vector2(snakeFragments2[i-1].x, snakeFragments2[i-1].y);
		   	    			}
		   	    		}
		   	    		if (move2 == 0) {
		   	    			snakeFragments2[0].x++;
		   	    		}
		   	    		else if (move2 == 1) {
		   	    			snakeFragments2[0].y++;
		   	    		}
		   	    		else if (move2 == 2) {
		   	    			snakeFragments2[0].x--;
		   	    		}
		   	    		else if (move2 == 3) {
		   	    			snakeFragments2[0].y--;
		   	    		}
		   	    		
		   	    		if (snakeFragments2[0].x >= gridSize) {
		   	    			if (diesOnWall) {
		   	    				dead2 = true;
		   	    			}
		   	    			else {
		   	    				snakeFragments2[0].x = 0;
		   	    			}
		   	    		}
		   	    		else if (snakeFragments2[0].x < 0) {
		   	    			if (diesOnWall) {
		   	    				dead2 = true;
		   	    			}
		   	    			else {
		   	    				snakeFragments2[0].x = gridSize - 1;
		   	    			}
		   	    		}
		   	    		if (snakeFragments2[0].y >= gridSize) {
		   	    			if (diesOnWall) {
		   	    				dead2 = true;
		   	    			}
		   	    			else {
		   	    				snakeFragments2[0].y = 0;
		   	    			}
		   	    		}
		   	    		else if (snakeFragments2[0].y < 0) {
		   	    			if (diesOnWall) {
		   	    				dead2 = true;
		   	    			}
		   	    			else {
		   	    				snakeFragments2[0].y = gridSize - 1;
		   	    			}
		   	    		}
		   	    		
		   	    		for (int i = 0; i < fruits.length; i++) {
			   	    		if (snakeFragments2[0].x == fruits[i].x && snakeFragments2[0].y == fruits[i].y) {
			   	    			score2++;
			   	    			PlaceFruit(i);
			   	    		}
		   	    		}
		   	    		boolean worked = !dead2;
		   	    		for (int i = 1; i < snakeFragments2.length; i++) {
		    				if (snakeFragments2[i].x == snakeFragments2[0].x && snakeFragments2[i].y == snakeFragments2[0].y) {
		    					worked = false;
		    					System.out.println("DIED2");
		    					break;
		    				}
		    			}
		   	    		for (int i = 0; i < snakeFragments.length; i++) {
		    				if (snakeFragments[i].x == snakeFragments2[0].x && snakeFragments[i].y == snakeFragments2[0].y) {
		    					if (eatOthers && i != 0) {
		    						final int count = snakeFragments.length - i;
		    						score -= count;
		    						score2 += count;
		    						
		    						Vector2[] temp = snakeFragments;
				   	    			snakeFragments = new Vector2[snakeFragments.length - count];
				   	    			
				   	    			for (int j = 0; j < snakeFragments.length; j++) {
				   	    				snakeFragments[j] = new Vector2(temp[j].x, temp[j].y);
				   	    			}
				   	    			break;
		    					}
		    					else {
			    					worked = false;
			    					System.out.println("DIED2");
			    					break;
		    					}
		    				}
		    			}
		   	    		if (snakeFragments2.length <= score2 + defaultLength) {
		   	    			Vector2[] temp = snakeFragments2;
		   	    			snakeFragments2 = new Vector2[temp.length + 1];
		   	    			
		   	    			for (int i = 0; i < temp.length; i++) {
		   	    				snakeFragments2[i] = new Vector2(temp[i].x, temp[i].y);
		   	    			}
	
		   	    			int x = snakeFragments2[snakeFragments2.length-2].x;
		   	    			int y = snakeFragments2[snakeFragments2.length-2].y;
		   	    			
		   	    			snakeFragments2[snakeFragments2.length-1] = new Vector2(x, y);
		   	    		}
		   	        if (worked == false) {
		   	        		dead2 = true;
		   	        }
		   	        debounce2 = -1;
		   	     if(dead2) {
	   			    	//Media sound = new Media(new File(musicFile).toURI().toString());
	   			    	//MediaPlayer mediaPlayer = new MediaPlayer(sound);
	   	    		 	//mediaPlayer.play();
		   	        }
	   	    		}
	   	    		if (!(dead && dead2)) Draw();
	   	    }
	   	
	    @Override
	    public void start(Stage primaryStage) 
	    {
		    Group root = new Group();
		    	Scene s = new Scene(root, gridSize * cellSize, gridSize * cellSize, Color.BLACK);
	
		    	final Canvas canvas = new Canvas(gridSize * cellSize, gridSize * cellSize);
		    	gc = canvas.getGraphicsContext2D();
		    	 
		    	root.getChildren().add(canvas);
		    
		    	primaryStage.setTitle("Snake 1v1");
		    	primaryStage.setScene(s);
		    	primaryStage.show();
		    	
		    	canvas.setFocusTraversable(true);
		    	canvas.setOnKeyPressed(e -> {
		    		switch(e.getCode()) {
		    			case D:
		    				if (move != 2 && debounce != 2) {
		    					if (debounce == -1) debounce = move;
		    					move = 0;
		    				}
		    				break;
		    			case L:
		    				if (move2 != 2 && debounce2 != 2)  {
		    					if (debounce2 == -1) debounce2 = move2;
		    					move2 = 0;
		    				}
		    				break;
		    			case S:
		    				if (move != 3 && debounce != 3) {
		    					if (debounce == -1) debounce = move;
		    					move = 1;
		    				}
		    				break;
		    			case K:
		    				if (move2 != 3 && debounce2 != 3) {
		    					if (debounce2 == -1) debounce2 = move2;
		    					move2 = 1;
		    				}
		    				break;
		    			case A:
		    				if (move != 0 && debounce != 0) {
		    					if (debounce == -1) debounce = move;
		    					move = 2;
		    				}
		    				break;
		    			case J:
		    				if (move2 != 0 && debounce2 != 0) {
		    					if (debounce2 == -1) debounce2 = move2;
		    					move2 = 2;
		    				}
		    				break;
		    			case W:
		    				if (move != 1 && debounce != 1) {
		    					if (debounce == -1) debounce = move;
		    					move = 3;
		    				}
		    				break;
		    			case I:
		    				if (move2 != 1 && debounce2 != 1) {
		    					if (debounce2 == -1) debounce2 = move2;
		    					move2 = 3;
		    				}
		    				break;
		    				
		    			case SPACE:
//		    				System.out.println(executor.isTerminated() + " TERMINATED");
//		    				System.out.println(executor.isShutdown());
		    				
		    				if (dead && dead2) {
		    					Start();
		    				}
		    				break;
		    				
		    			default:
		    				break;
		    		}
		    	});
					    	
		    	primaryStage.setOnCloseRequest(e -> System.exit(0));
		    	gc.setFill(Color.WHITE);
		    	gc.fillText("LOADING! :D", gridSize*cellSize/2.25, gridSize*cellSize/2.25);
		    	Start();
		    	
		    	final LongProperty lastUpdateTime = new SimpleLongProperty(0);
		    	// LOL I FINALLY FOUND OUT HOW TO SYNC THIS WITH THE DRAW THINGY LOL!!!
		    	// <3 AnimationTimer
		    	AnimationTimer timer = new AnimationTimer() {
		    		
		    		long delta = 0;
		    		
		    	    @Override
		    	    public void handle(long dt) {
		    	    	
		    	        if (lastUpdateTime.get() > 0) {
		    	        	
		    	            double deltaTime = (dt - lastUpdateTime.get()) / 1000000000.0;
		    	            delta += deltaTime * 1000;
		    	            
		    	            if (delta > speed) {
		    	            		delta -= speed;
		    	            		moveSnakes();
		    	            }
		    	            
		    	        }
		    	        
		    	        lastUpdateTime.set(dt);
		    	        
		    	    }
		    	    
		    	};
		    	
		    	timer.start();
		    
	    }
	    
	    void Start() {
		    	snakeFragments = new Vector2[1];
		    	snakeFragments[0] = new Vector2(gridSize / 3, gridSize / 3);
		    	snakeFragments2 = new Vector2[1];
		    	snakeFragments2[0] = new Vector2(gridSize / 3 * 2, gridSize / 3 * 2);
		    	rand = new Random();
		    	fruits = new Vector2[numberOfFruit];
		    	for (int i = 0; i < numberOfFruit; i++) {
		    		PlaceFruit(i);
		    	}
		    	score = 0;
		    	score2 = 0;
		    	dead = false;
		    	dead2 = !twoPlayer;
		    	if (dead2) snakeFragments2[0] = new Vector2(-1,-1);
		    	move = 1;
		    	move2 = 3;
		    	/*if(song!=null) {
		    		song.stop();
		    	}
		    	Media sound = new Media(new File(musicFile).toURI().toString());
		    	song = new MediaPlayer(sound);
		    	song.setVolume(0.4);
    		 	song.play();*/
	    }
	    
	    void Draw() {
	    		if (customGrid2)gc.setFill(/*grid1*/Color.hsb(frames * mult, (frames*mult/360)%1, (frames*mult/360)%1));
	    		else { gc.setFill(grid1);} 
	    		gc.fillRect(0,0,cellSize*gridSize,cellSize*gridSize);
	    		gc.setFill(grid2);
	    		if (customGrid) for (int i = 0; i < gridSize; i++) {
	    			for (int j = 0; j < gridSize; j++) {
	    				if (oddsOnly) {
	    					if ((i%2 + j%2)%2 == 1) 
	    						gc.fillRect(i * cellSize, j * cellSize,cellSize,cellSize);
	    				}
	    				else {
	    					gc.setFill(Color.hsb(
	    							frames*mult+(i+j)*mult2*4,
	    							((frames*mult+(i+j)*mult2)/100)%1,
	    							((frames*mult+(i+j)*mult2)/100)%1
	    							));
	    					gc.fillRoundRect(i * cellSize + cellSize * 0.05, j * cellSize + cellSize * 0.05,cellSize * 0.9,cellSize * 0.9,cellSize*0.2,cellSize*0.2);
	    				}
	    			}
	    		}
	    		for (int i = 0; i < snakeFragments.length; i++) {
	    			gc.setFill(Color.hsb(h + i * rainbowI, 1, 1, 0.7));
	    			if (i > 0 && !noSmoothedCurves) {
	    				double size = 1 - Math.pow((double)i / (double)snakeFragments.length * 0.6, 2);
//	    				size *= 8;
//	    				size = Math.pow(size, 0.4);
	    				double added = (1 - size)/2;
	    				//gc.fillRect(snakeFragments[i].x * cellSize + cellSize * 0.1, snakeFragments[i].y * cellSize + cellSize * 0.1,cellSize * 0.8,cellSize * 0.8);
	    				gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * added, snakeFragments[i].y * cellSize + cellSize * added, cellSize * size, cellSize * size, cellSize * size, cellSize * size);
	    				Vector2 b4 = new Vector2(snakeFragments[i-1].x - snakeFragments[i].x, snakeFragments[i-1].y - snakeFragments[i].y);
	    				if (b4.x == 1 || b4.x < -5) {
	    					gc.fillRect(snakeFragments[i].x * cellSize + cellSize * 0.5, snakeFragments[i].y * cellSize + cellSize * added,cellSize * 0.5,cellSize * size);
	    				}
	    				else if (b4.x == -1 || b4.x > 5) {
	    					gc.fillRect(snakeFragments[i].x * cellSize, snakeFragments[i].y * cellSize + cellSize * added,cellSize * 0.5,cellSize * size);
	    				}
	    				else if (b4.y == -1 || b4.y > 5) {
	    					gc.fillRect(snakeFragments[i].x * cellSize + cellSize * added, snakeFragments[i].y * cellSize,cellSize * size,cellSize * 0.5);
	    				}
	    				else if (b4.y == 1 || b4.y < -5) {
	    					gc.fillRect(snakeFragments[i].x * cellSize + cellSize * added, snakeFragments[i].y * cellSize + cellSize * 0.5,cellSize * size,cellSize * 0.5);
	    				}
	    				if (i < snakeFragments.length - 1) {
		    				Vector2 af = new Vector2(snakeFragments[i+1].x - snakeFragments[i].x, snakeFragments[i+1].y - snakeFragments[i].y);
		    				if (af.x == 1 || af.x < -5) {
		    					gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * 0.5, snakeFragments[i].y * cellSize + cellSize * added,cellSize * 0.5,cellSize * size, cellSize * size / 5, cellSize * size / 5);
		    				}
		    				else if (af.x == -1 || af.x > 5) {
		    					gc.fillRoundRect(snakeFragments[i].x * cellSize, snakeFragments[i].y * cellSize + cellSize * added,cellSize * 0.5,cellSize * size, cellSize * size / 5, cellSize * size / 5);
		    				}
		    				else if (af.y == 1 || af.y < -5) {
		    					gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * added, snakeFragments[i].y * cellSize + cellSize * 0.5,cellSize * size,cellSize * 0.5, cellSize * size / 5, cellSize * size / 5);
		    				}
		    				else if (af.y == -1 || af.y > 5) {
		    					gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * added, snakeFragments[i].y * cellSize,cellSize * size,cellSize * 0.5, cellSize * size / 5, cellSize * size / 5);
		    				}
	    				}
	    			}
	    			else gc.fillRoundRect(snakeFragments[i].x * cellSize , snakeFragments[i].y * cellSize,cellSize,cellSize, cellSize/5, cellSize/5);
	    			if (i == 0) {
	    				gc.setFill(Color.rgb(0, 0, 0));
	    				switch (move) {
	    					case 0:
	    						gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * 0.7, snakeFragments[i].y * cellSize + cellSize * 0.2,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * 0.7, snakeFragments[i].y * cellSize + cellSize * 0.6,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						break;
	    					case 2:
	    						gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * 0.1, snakeFragments[i].y * cellSize + cellSize * 0.2,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * 0.1, snakeFragments[i].y * cellSize + cellSize * 0.6,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						break;
	    					case 1:
	    						gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * 0.2, snakeFragments[i].y * cellSize + cellSize * 0.7,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * 0.6, snakeFragments[i].y * cellSize + cellSize * 0.7,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						break;
	    					case 3:
	    						gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * 0.2, snakeFragments[i].y * cellSize + cellSize * 0.1,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						gc.fillRoundRect(snakeFragments[i].x * cellSize + cellSize * 0.6, snakeFragments[i].y * cellSize + cellSize * 0.1,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						break;
	    				}
	    			}
	    		}
	    		

	    		gc.setFill(plr2Color);
	    		for (int i = 0; i < snakeFragments2.length; i++) {
	    			if (i > 0 && !noSmoothedCurves) {
	    				double size = 1 - Math.pow((double)i / (double)snakeFragments2.length * 0.6, 2);
//	    				double size = 1 - Math.pow((double)i / (double)snakeFragments2.length * 0.6, 2);
//	    				size *= 8;
//	    				size = Math.pow(size, .4);
	    				double added = (1 - size)/2;
	    				//gc.fillRect(snakeFragments[i].x * cellSize + cellSize * 0.1, snakeFragments[i].y * cellSize + cellSize * 0.1,cellSize * 0.8,cellSize * 0.8);
	    				gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * added, snakeFragments2[i].y * cellSize + cellSize * added, cellSize * size, cellSize * size, cellSize * size, cellSize * size);
	    				Vector2 b4 = new Vector2(snakeFragments2[i-1].x - snakeFragments2[i].x, snakeFragments2[i-1].y - snakeFragments2[i].y);
	    				if (b4.x == 1 || b4.x < -5) {
	    					gc.fillRect(snakeFragments2[i].x * cellSize + cellSize * 0.5, snakeFragments2[i].y * cellSize + cellSize * added,cellSize * 0.5,cellSize * size);
	    				}
	    				else if (b4.x == -1 || b4.x > 5) {
	    					gc.fillRect(snakeFragments2[i].x * cellSize, snakeFragments2[i].y * cellSize + cellSize * added,cellSize * 0.5,cellSize * size);
	    				}
	    				else if (b4.y == -1 || b4.y > 5) {
	    					gc.fillRect(snakeFragments2[i].x * cellSize + cellSize * added, snakeFragments2[i].y * cellSize,cellSize * size,cellSize * 0.5);
	    				}
	    				else if (b4.y == 1 || b4.y < -5) {
	    					gc.fillRect(snakeFragments2[i].x * cellSize + cellSize * added, snakeFragments2[i].y * cellSize + cellSize * 0.5,cellSize * size,cellSize * 0.5);
	    				}
	    				if (i < snakeFragments2.length - 1) {
		    				Vector2 af = new Vector2(snakeFragments2[i+1].x - snakeFragments2[i].x, snakeFragments2[i+1].y - snakeFragments2[i].y);
		    				if (af.x == 1 || af.x < -5) {
		    					gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * 0.5, snakeFragments2[i].y * cellSize + cellSize * added,cellSize * 0.5,cellSize * size, cellSize * size / 5, cellSize * size / 5);
		    				}
		    				else if (af.x == -1 || af.x > 5) {
		    					gc.fillRoundRect(snakeFragments2[i].x * cellSize, snakeFragments2[i].y * cellSize + cellSize * added,cellSize * 0.5,cellSize * size, cellSize * size / 5, cellSize * size / 5);
		    				}
		    				else if (af.y == 1 || af.y < -5) {
		    					gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * added, snakeFragments2[i].y * cellSize + cellSize * 0.5,cellSize * size,cellSize * 0.5, cellSize * size / 5, cellSize * size / 5);
		    				}
		    				else if (af.y == -1 || af.y > 5) {
		    					gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * added, snakeFragments2[i].y * cellSize,cellSize * size,cellSize * 0.5, cellSize * size / 5, cellSize * size / 5);
		    				}
		    				
	    				}
	    			}
	    			else gc.fillRoundRect(snakeFragments2[i].x * cellSize , snakeFragments2[i].y * cellSize,cellSize,cellSize, cellSize/5, cellSize/5);
	    			if (i == 0) {
	    				gc.setFill(Color.rgb(0, 0, 0));
	    				switch (move2) {
	    					case 0:
	    						gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * 0.7, snakeFragments2[i].y * cellSize + cellSize * 0.2,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * 0.7, snakeFragments2[i].y * cellSize + cellSize * 0.6,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						break;
	    					case 2:
	    						gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * 0.1, snakeFragments2[i].y * cellSize + cellSize * 0.2,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * 0.1, snakeFragments2[i].y * cellSize + cellSize * 0.6,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						break;
	    					case 1:
	    						gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * 0.2, snakeFragments2[i].y * cellSize + cellSize * 0.7,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * 0.6, snakeFragments2[i].y * cellSize + cellSize * 0.7,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						break;
	    					case 3:
	    						gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * 0.2, snakeFragments2[i].y * cellSize + cellSize * 0.1,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize * 0.6, snakeFragments2[i].y * cellSize + cellSize * 0.1,cellSize * 0.2,cellSize * 0.2, cellSize/25, cellSize/25);
	    						break;
	    				}
	    				gc.setFill(plr2Color);
	    				gc.fillRoundRect(snakeFragments2[i].x * cellSize + cellSize*0.15, snakeFragments2[i].y * cellSize + cellSize*0.15,cellSize*.7,cellSize*0.7, cellSize, cellSize);
	    			}
	    		}
	    		
	    		
	    		gc.setFill(Color.rgb(255, 0, 0));
	    		for (int i = 0; i < fruits.length; i++) {
	    			gc.fillRoundRect(fruits[i].x * cellSize + cellSize * 0.1, fruits[i].y * cellSize + cellSize * 0.1,cellSize * 0.8,cellSize * 0.8,cellSize*0.8,cellSize*0.8);
	    		}
	    		gc.setFill(Color.WHITE);
	    		gc.fillText("Score: " + (score + score2), 10, 20);
	    }

	    void PlaceFruit(int f) {
	    		boolean success = false;
	    		fruits[f] = new Vector2(-1,-1);
	    		while(success == false) {
	    			fruits[f].x = rand.nextInt(gridSize);
	    			fruits[f].y = rand.nextInt(gridSize);
	    			boolean worked = true;
	    			if (snakeFragments.length + snakeFragments2.length >= gridSize * gridSize - numberOfFruit) {
	    				success = true;
	    				fruits[f].x = -1;
	    				fruits[f].y = -1;
	    				break;
	    			}
	    			for (int i = 0; i < snakeFragments.length; i++) {
	    				if (snakeFragments[i].x == fruits[f].x && snakeFragments[i].y == fruits[f].y) {
	    					worked = false;
	    					break;
	    				}
	    			}
	    			for (int i = 0; i < snakeFragments2.length; i++) {
	    				if (snakeFragments2[i].x == fruits[f].x && snakeFragments2[i].y == fruits[f].y) {
	    					worked = false;
	    					break;
	    				}
	    			}
	    			for (int i = 0; i < fruits.length; i++) {
	    				if (fruits[i] != null) {
		    				if ((fruits[i].x == fruits[f].x && fruits[i].y == fruits[f].y) && i != f) {
		    					worked = false;
		    					break;
		    				}
	    				}
	    			}
	    			if (worked == true) {
	    				success = true;
	    				break;
	    			}
	    		}
	    }
	    
	    public static void music(){
		    /*	AudioPlayer MGP = AudioPlayer.player;
		    	AudioStream BGM;
		    	AudioData MD;
		    	ContinuousAudioDataStream loop = null;
		    	try{
		    	BGM = new AudioStream(new FileInputStream("C:\\test\\ha.wav"));
		    	MD = BGM.getData();
		    	loop = new ContinuousAudioDataStream(MD);
		    	}catch(IOException error){
		    	System.out.print("file not found");
		    	}
		    	MGP.start(loop);*/
	    	}
	    
	    public static void main(String[] args) {
	        launch(args);
	    }
}