package teamlab;

import java.awt.*;
import javax.sound.sampled.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;


public class GamePanel extends JPanel implements ActionListener{
	GameInfo bingo = new GameInfo();
	JButton[][]  matrix = new JButton[bingo.size][bingo.size];	//make space for buttons
	JPanel Buttons,questions, imagePanel, scorePanel; //buttons place and questions place(include imagePanel and resultPanel)
	JTextArea background;	//question's background
	JButton results;
	JTextField rightButtonTextField;
	JLabel rightClickLabel;
	int rightButton = 0;
	int score = 0;
	Clock clock = new Clock(); // record the time for the player to get bingo
	
	GamePanel()
	{   
		bingo.addPlayer();
	    
		bingo.SetButton();
		bingo.SetImageNum();
		//9 buttons of answers;
		SetResultButton();
		this.add(Buttons);
		
		//set questions
		Setquestionboard();
		this.add(questions);
		//set button to check score
		scoreResultButton();
		this.add(scorePanel);
		//turn on background music
		String bgm = "sounds/ukulele.wav";
		MusicPlayer.playMusic(bgm, false);
	}
	//Method for player to check on scoreboard and scores for right attempt
		public void scoreResultButton() {
			scorePanel = new JPanel();
			results = new JButton("Check score:");
			scorePanel.add(results);
			results.addActionListener(this);
			
			rightClickLabel = new JLabel("Right: ");
			scorePanel.add(rightClickLabel);

			rightButtonTextField = new JTextField(3);
			rightButtonTextField.setEditable(false);
			rightButtonTextField.setFocusable(false);
			scorePanel.add(rightButtonTextField);	
		}
	private void Setquestionboard()
	{
		//questions
		questions = new JPanel();
		questions.setLayout(new BoxLayout(questions, BoxLayout.PAGE_AXIS));
		//set up timer
		clock.start();
		questions.add(clock.clockLabel);
		//set up questions
		background = new JTextArea(2,3);
		background.setText("How many peguins are here?");
		background.setEditable(false);
		questions.add(background);	
		//add images
		imagePanel = new JPanel();
		imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.PAGE_AXIS));
		resetImagePanel();
		questions.add(imagePanel);
		
		
	}
	//reset the number of images for count
	private void resetImagePanel() {
		imagePanel.removeAll();
		imagePanel.revalidate();
		imagePanel.repaint();
		ImageIcon peguin = new ImageIcon("images/peguin.png");
		//JOptionPane.showMessageDialog(null, ""+bingo.ImageNum);
		for (int i = 0; i < bingo.ImageNum; i++) {
			imagePanel.add(new JLabel(peguin));
		}
		
	}
	private void SetResultButton()
	{
		Buttons = new JPanel(new GridLayout(bingo.size, bingo.size));//set buttons Layout
		for(int i = 0; i < bingo.size; i++) 
		{
			for(int j = 0; j < bingo.size; j++)
			{   
				ImageIcon original = new ImageIcon("images/original.png"); //origianl image
				matrix[i][j] = new JButton(bingo.ButtonResult[i][j], original); 	//make new button with name and original image on it
				Buttons.add(matrix[i][j]);	//push on to frame
				//Display style
				matrix[i][j].setBackground(Color.BLUE);
				//matrix[i][j].setForeground(Color.BLUE);
				//matrix[i][j].setFocusPainted(false);
				matrix[i][j].setOpaque(true);
				matrix[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
				//event activity
				matrix[i][j].addActionListener(this);
			}
		}
		//Buttons.setSize(1000,1000);
	}
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		for(int i = 0; i < bingo.size; i++) 
		{
			for(int j = 0; j < bingo.size; j++)
			{   
				if (source == matrix[i][j] && !bingo.tapped[i][j])
				   {
						//tapped change color
						matrix[i][j].setBackground(Color.RED);
						//if correct change swap tapped[][] to true, reset image number, reset question, check bingo
						if(Integer.valueOf(bingo.ButtonResult[i][j])==bingo.ImageNum)
						{   
							String musicLocation = "sounds/correct.wav";
							MusicPlayer.playMusic(musicLocation,false);
							rightButton += 1;
							rightButtonTextField.setText(Integer.toString(rightButton));
							bingo.updatePlayerScore();
						    matrix[i][j].setIcon(new ImageIcon("images/haha.png"));
							//JOptionPane.showMessageDialog(null, "correct!");
							bingo.tapped[i][j] = true;		//swap tapped place to true
							bingo.ButtonResult[i][j] = Integer.toString(bingo.range+1);	//set the result to avoid duplicate
							bingo.SetImageNum();	//reset image number
							resetImagePanel();	//reset question
							//check if bingo
							if(bingo.CheckBingo()) {
								String bingoLocation = "sounds/bingo.wav";
							    MusicPlayer.playMusic(bingoLocation,false);
							    clock.stop();
							    bingo.updateBingoScore(clock.sec);
								//JOptionPane.showMessageDialog(null, "bingo！");
							    try {
									bingo.checkPlayerScore();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
						    }
					    }
						else	//else tell player wrong and swap back color
						{   
							//JOptionPane.showMessageDialog(null, "please try again!");
							String errorLocation = "sounds/error.wav";
							MusicPlayer.playMusic(errorLocation,false);
							matrix[i][j].setBackground(Color.BLUE);
							matrix[i][j].setIcon(new ImageIcon("images/tryAgain.png"));
						}
				   }
				else if(source == results)
				{
					try {
						bingo.checkPlayerScore();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}		
			}
		}
	}
}

