package edu.ucsb.cs56.projects.scrapers.ucsb_curriculum;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.PrintStream;

import javax.swing.ImageIcon;


/** UCSBgui -- Graphics User Interface for UCSBCurriculum search,
 allows user to use drop down menus to search for lectures.
 
 @author Natasha Lee
 @author Kevin Zaragoza
 @version W16 
 
 */

public class UCSBgui{
	static  JFrame frame;

	
	public static void main (String [] args){
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				displayJFrame();
			}
		} );
	
	}
	
	static void displayJFrame() {
		try{
			
			frame = new JFrame();
			
			
			UCSBCurriculumSearch cssc = new UCSBCurriculumSearch();
			Object[] subject = cssc.findSubjectAreas(cssc.getMainPage()).toArray();
			
			
			/* Different quarters with their corresponding number ID (used by previous programmers
			to identify each quarter */
			Vector quarter = new Vector();
			quarter.addElement( new Item("1", "Winter"));
			quarter.addElement( new Item("2", "Spring"));
			quarter.addElement( new Item("3", "Summer"));
			quarter.addElement( new Item("4", "Fall"));
			
			//Array of years
			Object [] year = cssc.findQuarterAndYear(cssc.getMainPage()).toArray();
			
			//Array of Course Levels
			String [] level = {"Undergraduate", "Graduate", "ALL"};
			
			
			//Creates ComboBoxes of the aforementioned search criteria
			JComboBox subjectBox = new JComboBox(subject);
			subjectBox.setEditable(false);
			
			
			JComboBox yearBox = new JComboBox(year);
			yearBox.setEditable(false);
			
			JComboBox levelBox = new JComboBox(level);
			levelBox.setEditable(false);
			
			//Search Button
			JButton search = new JButton("SEARCH");
			
			
			//Creates textArea that displays your search results
			JTextArea textbox = new JTextArea(20, 40);
			textbox.setEditable(false);
			
			//Redirects terminal output to GUI
			PrintStream stream = new PrintStream(new CustomOutputStream(textbox));
			System.setOut(stream);
			System.setErr(stream);
		
			//Makes textarea scrollable
			JScrollPane scrollbar = new JScrollPane(textbox);
			
			//get current user directory to display ucsb logo
			String dir = System.getProperty("user.dir") + "/src/edu/ucsb/cs56/projects/scrapers/ucsb_curriculum/logo.png";
			File logo = new File(dir);
			BufferedImage myPicture = ImageIO.read(logo);
			JLabel picLabel = new JLabel();
			picLabel.setIcon(new ImageIcon(myPicture));
			
			
			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();

			//set constraints and add Piclabel at top of panel
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.weightx = .5;
			constraints.weighty = .5;
			constraints.gridwidth = 4;
			panel.add(picLabel, constraints);

			//constraints for second row of subject, quarter, year, and level boxes
			//then add them to pane
			constraints.gridwidth = 1;
			constraints.gridy = 1;
			constraints.insets = new Insets(0, 15, 0, 15);
			panel.add(subjectBox, constraints);
			constraints.gridx = 1;
			constraints.gridx = 2;
			panel.add(yearBox, constraints);
			constraints.gridx = 3;
			panel.add(levelBox, constraints);

			//constraints for third row of search button
			//then add them to pane
			constraints.insets = new Insets(0, 0, 0, 0);
			constraints.gridy = 2;
			constraints.gridx = 1;
			constraints.gridwidth = 2;
			panel.add(search, constraints);

			//constraints for displayed text field (scrollbar)
			//then add them to pane
			constraints.gridx = 0;
			constraints.gridy = 3;
			constraints.gridheight = 8;
			constraints.gridwidth = 5;
			constraints.weighty = 0.5;
			constraints.ipady = 200;
			constraints.ipadx = 800;
			scrollbar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollbar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			constraints.fill = GridBagConstraints.VERTICAL;
			constraints.insets = new Insets(10, 0, 100, 0);
			panel.add(scrollbar, constraints);


			search.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ee) {
					try{
						//reset the textbox so it clears each time you press search
						textbox.setText(null);
						
						//instantiate a new Curriculum search object
						UCSBCurriculumSearch cssc = new UCSBCurriculumSearch();
						
						//get the values of the selections
						String dept = String.valueOf(subjectBox.getSelectedItem());
						
					       
						String year = String.valueOf(yearBox.getSelectedItem()).substring(String.valueOf(yearBox.getSelectedItem()).length()-4, String.valueOf(yearBox.getSelectedItem()).length());
						String quarter = String.valueOf(yearBox.getSelectedItem());
						String quarter2 = "";
						if(quarter.substring(0,2).equals("FA"))
						    {
							quarter2 = "4";
						    }
						if(quarter.substring(0,2).equals("WI"))
						    {
							quarter2 = "1";
						    }
						if(quarter.substring(0,2).equals("SP"))
						    {
							quarter2 = "2";
						    }
						if(quarter.substring(0,2).equals("SU"))
						    {
							quarter2 = "3";
						    }

						
						//get the 
						String lev = String.valueOf(levelBox.getSelectedItem());
						
						
						String qtr = year + quarter2;
						//search with the corresponding selections in the gui
						cssc.loadCourses(dept, qtr, lev);
						cssc.printLectures();


						//set scrollbar to top of scrollpane
						javax.swing.SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								scrollbar.getVerticalScrollBar().setValue(0);
							}
						});
					}
					catch (Exception e){
						System.err.println(e);
						e.printStackTrace();
					}
				}
			} );
			
			//setup the JFrame
			frame.setDefaultCloseOperation(JFrame. EXIT_ON_CLOSE) ;
			frame.getContentPane().add(panel);
			frame.setSize(1280, 720);
			frame.setVisible(true);
			
			
		}catch(Exception e){
			System.err.println(e);
			e.printStackTrace();
		}
		
	}
}






