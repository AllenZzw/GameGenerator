import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

//compile command: java GameInterface.java GameInput.java Automaton.java Edge.java Node.java

public class GameInterface extends JFrame {
	private int windowWidth;
	private int windowHeight;

	private ImageIcon imgBlank;
	private ImageIcon imgBackground;
	private ImageIcon[] imgAvaLocation;
	private ImageIcon[][] imgAnimation;
	private ImageIcon[] imgMiss;

	private JLabel bgDisplay;
	private JLabel[] avaDisplay;
	private JLabel[][] aniDisplay;
	private JLabel[] missDisplay;
	private int[] missAniStep;

	private GameInput controller;

	public GameInterface(String imgPath, String avaStr, String aniStr, String missStr){
		int avaLocNum = avaStr.split(",").length;
		String[] aniConfig = aniStr.split(";");
		int aniNum = aniConfig.length;
		int[] aniStep = new int[aniConfig.length];
		for(int i=0; i!=aniConfig.length;i++){
			aniStep[i] = Integer.parseInt((aniConfig[i].split(","))[0]);
		}
		int missNum = missStr.split(";").length;
		setnumber(avaLocNum,aniNum,aniStep,missNum);
		loadImages(imgPath);
		initLayout();
	}

	//function to set number of avatar step, animations, and step of animation
	private void setnumber(int avaLocNum, int aniNum, int[] aniStep, int missNum){
		imgAvaLocation = new ImageIcon[avaLocNum];
		imgAnimation = new ImageIcon[aniNum][];
		for ( int i = 0; i != aniStep.length; i++ ){
			imgAnimation[i] = new ImageIcon[aniStep[i]];
		}
		imgMiss = new ImageIcon[missNum];
	}

	//function to load background image
	//for now we only hava background, avatar location and game animation
	//need to improved later by adding more images
	private void loadImages(String imgPath){
		for( int i = 0; i != imgAvaLocation.length; i++ ){
			imgAvaLocation[i] = new ImageIcon(getClass().getResource(imgPath+"AvaLoc"+i+".png"));
		}
		for( int i = 0; i != imgAnimation.length; i++ ){
			for( int j = 0; j != imgAnimation[i].length; j++ ){
				imgAnimation[i][j] = new ImageIcon(getClass().getResource(imgPath+"Ani"+i+"Seq"+j+".png"));
			}
		}
		for( int i = 0; i != imgMiss.length; i++){
			imgMiss[i] = new ImageIcon(getClass().getResource(imgPath+"Miss"+i+".png"));
		}
		imgBlank = new ImageIcon(getClass().getResource(imgPath + "blank.png"));
		imgBackground = new ImageIcon(getClass().getResource(imgPath + "Background.png"));
		windowWidth = imgBackground.getImage().getWidth(null);
		windowHeight = imgBackground.getImage().getHeight(null);	
	}

	//function to initialize the layout by setting position of different images and buttons
	//for now images are of the same size and the position are unified
	//later we can add setting in GameConfig class for image position
	private void initLayout(){
		//associate images with JLabel
		//setting the position of background image
		JPanel displayPanel = new JPanel(new GridBagLayout());
		displayPanel.setBackground(new Color(0xFF, 0xFF, 0xFF));
		GridBagConstraints position = new GridBagConstraints();
		position.fill = GridBagConstraints.HORIZONTAL;
		position.gridwidth = 1;
		position.gridx = 0;
		position.gridy = 0;

		avaDisplay = new JLabel[imgAvaLocation.length];
		for(int i = 0; i != avaDisplay.length; i++){
			avaDisplay[i] = new JLabel(imgAvaLocation[i]);
			displayPanel.add(avaDisplay[i], position);
		}

		aniDisplay = new JLabel[imgAnimation.length][];
		for(int i = 0; i != imgAnimation.length; i++){
			aniDisplay[i] = new JLabel[imgAnimation[i].length];
			for(int j = 0; j != imgAnimation[i].length; j++){
				aniDisplay[i][j] = new JLabel(imgAnimation[i][j]);
				displayPanel.add(aniDisplay[i][j], position);
			}
		}

		missDisplay = new JLabel[imgMiss.length];
		for(int i=0; i!=imgMiss.length; i++){
			missDisplay[i] = new JLabel(imgMiss[i]);
			displayPanel.add(missDisplay[i],position);
		}
		missAniStep = new int[imgMiss.length];

		bgDisplay = new JLabel(imgBackground);
		displayPanel.add(bgDisplay, position);

		getContentPane().add(displayPanel, BorderLayout.CENTER);
		this.setLocation(300,300);
		this.setSize((int)(windowWidth * 1.2), (int)(windowHeight * 1.2));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//function to accept stream value from automaton to update display
	public void updateDisplay(Hashtable<String, Integer> currentVarMap, boolean gameover){
		//to do: update life, score, gameover, level
		//update avatar location
		for(int i = 0; i != avaDisplay.length; i++ ){
			String varName = new String("Ava"+i);
			if(currentVarMap.get(varName) == 0 || gameover)
				avaDisplay[i].setIcon(imgBlank);
			else
				avaDisplay[i].setIcon(imgAvaLocation[i]);
		}

		//update game animation
		if(currentVarMap.get("Gametick") == 0){
			for(int i = 0; i != aniDisplay.length; i++ ){
				for(int j = 0; j != aniDisplay[i].length; j++ ){
					String varName = new String("Ani"+i+"Seq"+j);
					if(currentVarMap.get(varName) == 0 || gameover)
						aniDisplay[i][j].setIcon(imgBlank);
					else{
						aniDisplay[i][j].setIcon(imgAnimation[i][j]);
					}
				}
			}
		}

		//update miss animation
		//intuition: since the miss animation is not related to game engine
		//			 so if would be set to display for some steps
		if(currentVarMap.get("Gametick") == 0){
			for(int i=0; i != missDisplay.length; i++){
				if(missAniStep[i] > 0){
					missDisplay[i].setIcon(imgMiss[i]);
					missAniStep[i]--;
				}
				else{
					missDisplay[i].setIcon(imgBlank);
				}
				String varName = new String("MissLoc"+i);
				if(currentVarMap.get(varName) == 1 && !gameover){
					missAniStep[i] = 1;
				}
			}
		}
	}

	public void setController(GameInput controller){
		this.controller = controller;
		getContentPane().add(controller, BorderLayout.SOUTH);
	}

	//this function is only for testing 
	//to be improved:the arguments would be set by GameConfig class
	// public static void main(String[] args){
	// 	int[] aniStep = new int[]{16};
	// 	GameInterface view = new GameInterface("/imgresource/manhole/", 4, 1,aniStep);
	// 	Automaton model = new Automaton("solutions.dot");
	// 	GameInput controller = new GameInput();

	// 	view.setController(controller);
	// 	model.setView(view);
	// 	controller.setModel(model);

	// 	view.setVisible(true);
	// }
}