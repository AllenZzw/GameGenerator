import java.util.Timer;
import java.util.TimerTask;
import java.io.FileInputStream;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.lang.String;



public class GameInput extends JPanel{
	private Automaton model;

	private JButton startBtn;
	private JButton stopBtn;

	private ActionListener btnaction;
	private long ticktime;
	private Timer timer;


	//class initialize method
	public GameInput(String inputStr){
        //initialize the ticktime to be 0.1s
		ticktime = 200;

		//add keylistener 
		KeyListener listener = new keyboardListener(inputStr);
		addKeyListener(listener);
		setFocusable(true);

		//add button
		setButton();

        this.setBackground(new Color(0xFF, 0xFF, 0xFF));
	}

	//Keyboard listener class to listen on keyboard
    //to be improved: need to support dynamic keyboard input set by GameConfig

    //this is the key boardlistener for manhole game
    private class keyboardListener implements KeyListener {
        private char[] inputKey;
        public keyboardListener(String inputStr){
            String[] keyStr = inputStr.split(",");
            inputKey = new char[keyStr.length];
            for(int i=0; i != keyStr.length; i++){
                inputKey[i] = Character.toUpperCase(keyStr[i].charAt(0));
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
            //set default input as 0
            model.setInput(0);
            for(int i=0; i!=inputKey.length; i++){
                if( e.getKeyCode() == inputKey[i] ){
                    model.setInput(i);
                    break;
                }
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
        }
    };


    //BtnAction class to define action for button
    private class StartAction implements ActionListener{
    	public void actionPerformed(ActionEvent e){
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
    		timer = new Timer();
    		timer.schedule(new GameTimer(), ticktime, ticktime);
    	}
    };

    private class StopAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            stopBtn.setEnabled(false);
            startBtn.setEnabled(true);
            timer.cancel();
        }
    };

    //GameTimer class to define timer class
    private class GameTimer extends TimerTask{
        public GameTimer(){
        }
    	public void run() {
            if( model == null ){
                System.out.println("Error: automaton is null");
                return;
            }
            if( !model.nextStep() ){
                System.out.println("Error: wrong input for current node");
            }
    	}
    };


    //function to set Button
    private void setButton(){
    	startBtn = new JButton("Start");
    	stopBtn = new JButton("Stop");
    	this.add(startBtn);
    	this.add(stopBtn);
        startBtn.setEnabled(true);
        stopBtn.setEnabled(false);
    }

    //function to set automata and the timer
    public void setModel(Automaton model){
    	this.model = model;
    	startBtn.addActionListener(new StartAction());
        stopBtn.addActionListener(new StopAction());

    }
}