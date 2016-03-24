package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import controller.Controller;

import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

/**
 * PopUp to set the directory to access at the start of the program.
 *
 */
@SuppressWarnings("serial")
public class Directory extends JFrame {
	private JTextField txtDirectory;
	private JButton btnOk;
	
	public Directory(final Controller controller) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setAlwaysOnTop(true);
		setTitle("Set directory");
		setSize(300, 130);
		setResizable(false);
		setType(Type.POPUP);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(5, 5));
		
		JLabel lblDirectory = new JLabel("Set path directory:");
		getContentPane().add(lblDirectory, BorderLayout.NORTH);
		
		txtDirectory = new JTextField();
		getContentPane().add(txtDirectory, BorderLayout.CENTER);
		txtDirectory.setColumns(20);
		
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		btnOk = new JButton("OK");
		buttonPanel.add(btnOk);
		
		/** LISTENERS HERE */
		txtDirectory.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                txtDirectory.setText("");
                txtDirectory.setForeground(Color.BLACK);
                
                repaint();
                revalidate();
            }
        });
		
		
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (controller.setDirectory(txtDirectory.getText())) {
					txtDirectory.setText("\n Redirected to " + txtDirectory.getText());
					txtDirectory.setForeground(Color.GREEN);
					new Mainframe(controller).setVisible(true);
					dispose();
				} else {
					txtDirectory.setText(txtDirectory.getText() + "\tError: Directory does not exist.");
					txtDirectory.setForeground(Color.RED);
				}
				repaint();
				revalidate();
			}
		});
	}

}
