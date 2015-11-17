import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class GameConfig extends JFrame {
	private static final long serialVersionUID = 1L;

	private TitledBorder titleBorder;
    private TitledBorder interfaceBorder;

    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel inputLabel;
    private JTextField inputField;
    private JLabel avatarLabel;
    private JTextField avatarField;
    private JLabel controlLabel;
    private JComboBox<String> controlCombo;
    private JLabel aniLabel;
    private JTextField aniField;
    private JComboBox<String> missTypeCombo;
    private JComboBox<String> missTriggerCombo;
    private JLabel levelLabel;
    private JTextField levelField;
    private JLabel tickLabel;
    private JTextField tickField;
    private JLabel lifeLabel;
    private JTextField lifeField;
    private JLabel missTypeLabel;
    private JLabel missConfigLabel;
    private JTextField missField;
    private JLabel scoreLabel;
    private JComboBox<String> scoreCombo;
    private JTextField scoreField;
    private JLabel pngLabel;
    private JTextField pngField;

    private JButton btnLoadGame;

    public GameConfig(){
    	initLayout();
    }

    private void initLayout(){
    	

    	JPanel configPanel = new JPanel(new GridBagLayout());

    	titleBorder = BorderFactory.createTitledBorder("Game Configuration");
    	configPanel.setBorder(titleBorder);
    	
    	GridBagConstraints configPos = new GridBagConstraints();
    	configPos.fill = GridBagConstraints.HORIZONTAL;

    	configPos.gridwidth = 1;
    	configPos.gridx = 0;
    	configPos.gridy = 0;
    	nameLabel = new JLabel("Game Name:");
    	nameLabel.setHorizontalAlignment(JLabel.RIGHT);
    	configPanel.add(nameLabel, configPos);
    	configPos.gridwidth = 4;
        configPos.gridx = 1;
        configPos.gridy = 0;
        nameField = new JTextField("Octopus",10);
        nameField.setHorizontalAlignment(JTextField.RIGHT);
        configPanel.add(nameField, configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 1;
        inputLabel = new JLabel("Inputs: ");
        inputLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(inputLabel, configPos);
        configPos.gridwidth = 4;
        configPos.gridx = 1;
        configPos.gridy = 1;
        inputField = new JTextField(10);
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        configPanel.add(inputField, configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 2;
        avatarLabel = new JLabel("Avatars: ");
        avatarLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(avatarLabel,configPos);
        configPos.gridwidth = 4;
        configPos.gridx = 1;
        configPos.gridy = 2;
        avatarField = new JTextField("1,0,0,0,0,0");
        avatarField.setHorizontalAlignment(JTextField.RIGHT);
        configPanel.add(avatarField,configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 3;
        controlLabel = new JLabel("Controlling Type: ");
        controlLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(controlLabel, configPos);
        configPos.gridwidth = 4;
        configPos.gridx = 1;
        configPos.gridy = 3;
        String[] contorlOption = {"1-1 location", "1-1 step"};
        controlCombo = new JComboBox<String>(contorlOption);
        configPanel.add(controlCombo,configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 4;
        aniLabel = new JLabel("Animations: ");
        aniLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(aniLabel,configPos);
        configPos.gridwidth = 3;
        configPos.gridx = 1;
        configPos.gridy = 4;
        aniField = new JTextField("3,1;4,1;5,1;4,1;3,1");
        aniField.setHorizontalAlignment(JTextField.RIGHT);
        configPanel.add(aniField,configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 5;
        levelLabel = new JLabel("Level Score: ");
        levelLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(levelLabel,configPos);
        configPos.gridwidth = 3;
        configPos.gridx = 1;
        configPos.gridy = 5;
        levelField = new JTextField(2);
        levelField.setHorizontalAlignment(JTextField.RIGHT);
        configPanel.add(levelField,configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 6;
        tickLabel = new JLabel("GameTick(number of TimeTicks): ");
        tickLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(tickLabel,configPos);
        configPos.gridwidth = 3;
        configPos.gridx = 1;
        configPos.gridy = 6;
        tickField = new JTextField(2);
        tickField.setHorizontalAlignment(JTextField.RIGHT);
        configPanel.add(tickField,configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 7;
        lifeLabel = new JLabel("Life: ");
        lifeLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(lifeLabel,configPos);
        configPos.gridwidth = 3;
        configPos.gridx = 1;
        configPos.gridy = 7;
        lifeField = new JTextField(2);
        lifeField.setHorizontalAlignment(JTextField.RIGHT);
        configPanel.add(lifeField,configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 8;
        missTypeLabel = new JLabel("Miss Type: ");
        missTypeLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(missTypeLabel,configPos);
        configPos.gridwidth = 1;
        configPos.gridx = 1;
        configPos.gridy = 8;
        String[] missOption = {"positive", "negative"};
        missTypeCombo = new JComboBox<String>(missOption);
        configPanel.add(missTypeCombo,configPos);
        configPos.gridwidth = 2;
        configPos.gridx = 2;
        configPos.gridy = 8;
        String[] missTrigger = {"Reset Avatar", "Reset Animation"};
        missTriggerCombo = new JComboBox<String>(missTrigger);
        configPanel.add(missTriggerCombo, configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 9;
        missConfigLabel = new JLabel("Miss Configuration: ");
        missConfigLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(missConfigLabel,configPos);
        configPos.gridwidth = 3;
        configPos.gridx = 1;
        configPos.gridy = 9;
        missField = new JTextField("1,0,2;2,1,3;3,2,4;4,3,3");
        missField.setHorizontalAlignment(JTextField.RIGHT);
        configPanel.add(missField,configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 10;
        scoreLabel = new JLabel("Score Type: ");
        scoreLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(scoreLabel,configPos);
        configPos.gridwidth = 2;
        configPos.gridx = 1;
        configPos.gridy = 10;
        String[] scoreOption = {"Not Miss", "Avatar Location"};
        scoreCombo = new JComboBox<String>(scoreOption);
        configPanel.add(scoreCombo,configPos);
        configPos.gridwidth = 1;
        configPos.gridx = 3;
        configPos.gridy = 10;
        scoreField = new JTextField("3");
        scoreField.setHorizontalAlignment(JTextField.RIGHT);
        configPanel.add(scoreField,configPos);

        configPos.gridwidth = 1;
        configPos.gridx = 0;
        configPos.gridy = 11;
        pngLabel = new JLabel("Interface Path: ");
        pngLabel.setHorizontalAlignment(JLabel.RIGHT);
        configPanel.add(pngLabel,configPos);
        configPos.gridwidth = 3;
        configPos.gridx = 1;
        configPos.gridy = 11;
        pngField = new JTextField("/res/octopus_res");
        pngField.setHorizontalAlignment(JTextField.RIGHT);
        configPanel.add(pngField,configPos);

        JPanel controlPanel = new JPanel();

        
        btnLoadGame = new JButton("Load Game");
        btnLoadGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	argparse();
                GameInterface view = new GameInterface(pngField.getText(), avatarField.getText(), aniField.getText(), missField.getText());
                Automaton model = new Automaton("solutions.dot");
                GameInput controller = new GameInput(inputField.getText());

                view.setController(controller);
                model.setView(view);
                controller.setModel(model);

                view.setVisible(true);
                //to do: add automaton, gameinput and automaton
            }
        });
        controlPanel.add(btnLoadGame);

        JPanel windowPanel = new JPanel(new BorderLayout());
        windowPanel.add(configPanel,BorderLayout.CENTER);
        windowPanel.add(controlPanel,BorderLayout.SOUTH);

        getContentPane().add(windowPanel);
    }

    //function to parse value arguments
    private void argparse(){
        CspGenerator cspGen = new CspGenerator(nameField.getText());
        cspGen.setInput(inputField.getText());
        cspGen.setAvaLoc(avatarField.getText());
        cspGen.setContorlType((String)controlCombo.getSelectedItem());
        cspGen.setAnimation(aniField.getText());
        cspGen.setLevel(levelField.getText());
        cspGen.setGameTick(tickField.getText());
        cspGen.setLife(lifeField.getText());
        cspGen.setMiss((String)missTypeCombo.getSelectedItem(),(String)missTriggerCombo.getSelectedItem(),missField.getText());
        cspGen.setScore((String)scoreCombo.getSelectedItem(),scoreField.getText());
        cspGen.generateCsp();
    }


    public static void main(String[] args) {
    	GameConfig config = new GameConfig();
    	config.setLocation(200,200);
    	config.setSize(600,400);
    	config.setResizable(false);
    	config.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	config.setVisible(true);
    }
}