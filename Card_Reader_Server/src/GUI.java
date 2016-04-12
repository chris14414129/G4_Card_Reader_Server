import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GUI{
	
	private JFrame window;
	private JPanel buttonPanel;
	private JButton startBtn, stopBtn, settingsBtn;
	private Icon startIcon, stopIcon;
	
	public GUI(){
		window = new JFrame();
		window.setTitle("Server");
		window.setSize(300, 300);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		buildButtonPanel();
		
		window.setVisible(true);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
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
		
		buttonPanel.add(startBtn);
		buttonPanel.add(stopBtn);
	}
	
	private class actionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Object src = e.getSource();
			if (src == startBtn){
				
			}
			if (src == stopBtn){
				
			}
		}
	} 
}


