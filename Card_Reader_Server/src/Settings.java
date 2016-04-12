import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Settings {
	
	private JDialog settings;
	private JPanel settingsPanel, buttonPanel;
	private JLabel usernameLbl, passwordLbl, urlLbl, serverPortLbl, clientPortLbl, dayAtLbl;
	private JTextField usernameInput, passwordInput, urlInput, serverPortInput, clientPortInput, dayAtInput;
	private JButton submitBtn, clearBtn;
	private String username, password, url, serverPort, clientPort, dayAt;
	
	
	public Settings(JFrame parentFrame) {
		settings = new JDialog(parentFrame, true);
		settings.setTitle("Settings");
		settings.setModal(true);
		settings.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		settings.setLayout(new BoxLayout(settings.getContentPane(), BoxLayout.Y_AXIS));
		
		buildSettingsPanel();
		buildButtonPanel();
		
		settings.add(settingsPanel);
		settings.add(buttonPanel);
		
		settings.setLocationRelativeTo(null);
		settings.pack();
		settings.setVisible(true);
		settings.setResizable(false);
	}
	
	private void buildSettingsPanel(){
		
		settingsPanel = new JPanel();
	    settingsPanel.setLayout(new GridLayout(6,2));
	    
	    usernameLbl = new JLabel("Username: ");
	    passwordLbl = new JLabel("Password: ");
	    urlLbl = new JLabel("URL: ");
	    serverPortLbl = new JLabel("Server Port: ");
	    clientPortLbl = new JLabel("Client Port: ");
	    dayAtLbl = new JLabel("Day At: ");
	    
	    usernameInput = new JTextField();
	    passwordInput = new JTextField();
	    urlInput = new JTextField();
	    serverPortInput = new JTextField();
	    clientPortInput = new JTextField();
	    dayAtInput = new JTextField();
	    
	    settingsPanel.add(usernameLbl);
	    settingsPanel.add(usernameInput);
	    settingsPanel.add(passwordLbl);
	    settingsPanel.add(passwordInput);
	    settingsPanel.add(urlLbl);
	    settingsPanel.add(urlInput);
	    settingsPanel.add(serverPortLbl);
	    settingsPanel.add(serverPortInput);
	    settingsPanel.add(clientPortLbl);
	    settingsPanel.add(clientPortInput);
	    settingsPanel.add(dayAtLbl);
	    settingsPanel.add(dayAtInput);
	    
	}
	
	private void buildButtonPanel(){
		buttonPanel = new JPanel();
		
		submitBtn = new JButton("Submit");
	    clearBtn = new JButton("Clear");
	    
	    submitBtn.addActionListener(new actionListener());
	    clearBtn.addActionListener(new actionListener());
	    
	    buttonPanel.add(clearBtn);
	    buttonPanel.add(submitBtn);
	}
	
	
	 private class actionListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				Object src = e.getSource();
				if (src == clearBtn){
					usernameInput.setText("");
					passwordInput.setText("");
					urlInput.setText("");
					serverPortInput.setText("");
					clientPortInput.setText("");
					dayAtInput.setText("");
				}
				if (src == submitBtn){
					if (!usernameInput.getText().equals("") && !passwordInput.getText().equals("") && !urlInput.getText().equals("") && 
							!serverPortInput.getText().equals("") && !clientPortInput.getText().equals("") && !dayAtInput.getText().equals("")){
						username = usernameInput.getText().trim();
						password = passwordInput.getText().trim();
						url = urlInput.getText().trim();
						serverPort = serverPortInput.getText().trim();
						clientPort = clientPortInput.getText().trim();
						dayAt = dayAtInput.getText().trim();
						
					}
					else {
						JOptionPane.showMessageDialog(null,  "Please fill in all text fields first!", "Error", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		}
}
