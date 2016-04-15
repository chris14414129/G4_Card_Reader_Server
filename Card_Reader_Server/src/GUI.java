import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GUI{
	
	private JFrame window;
	private JPanel buttonPanel;
	private JButton startBtn, stopBtn, settingsBtn;
	private Icon startIcon, stopIcon;
	private roomSession rs;
	private registration reg;
	private autoAbsent aA;
	
	public GUI(){
		window = new JFrame();
		window.setTitle("Server");
		window.setSize(500, 500);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		buildButtonPanel();
		window.add(buttonPanel);
		
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.pack();
		window.setVisible(true);
		
	}
	
	public void buildButtonPanel(){
		buttonPanel = new JPanel();
		
		startIcon = new ImageIcon("\\icons\\Play.png");
		stopIcon = new ImageIcon("\\icons\\Stop.png");
		
		startBtn = new JButton("Start", startIcon);
		stopBtn = new JButton("Stop", stopIcon);
		settingsBtn = new JButton("Settings");
		
		startBtn.addActionListener(new actionListener());
		stopBtn.addActionListener(new actionListener());
		settingsBtn.addActionListener(new actionListener());
		
		buttonPanel.add(startBtn);
		buttonPanel.add(stopBtn);
		buttonPanel.add(settingsBtn);
	}
	
	private class actionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Object src = e.getSource();
			if (src == startBtn){
				rs = new roomSession(Settings.url, Settings.username, Settings.password, Integer.parseInt(Settings.minAt), Integer.parseInt(Settings.clientPort), Settings.serverIP );
				rs.start();
				aA = new autoAbsent(Settings.url, Settings.username, Settings.password, Settings.dayAt );
				aA.start();
				reg = new registration(Settings.url,Settings.username, Settings.password, Integer.parseInt(Settings.clientPort2), Integer.parseInt(Settings.minEarly), Integer.parseInt(Settings.minLate), Integer.parseInt(Settings.serverPort), Settings.serverIP);
				reg.start();
			}
			if (src == stopBtn){
				rs.interrupt();
				aA.interrupt();
				reg.interrupt();
			}
			if (src == settingsBtn){
				new Settings(window);
			}
		}
	} 
}

