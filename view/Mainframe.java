package view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.awt.Color;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import controller.Controller;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 * The CLI of the OS.
 * In this JFrame the user can do the following:
 * <li> Show current files
 * <li> Edit file extension
 * <li> Move file to another directory
 * <li> Copy file to another directory
 * <li> Delete file
 * <li> Change current directory
 * <li> Duplicate a file
 * <li> Run a C program
 */
@SuppressWarnings("serial")
public class Mainframe extends JFrame {
	private JTextArea txtCLI;
	private String commandOffset;
	private JScrollPane scroll;
        private ArrayList<String> commandList;
        private int pointer;
	
	/**
	 * Constructor of Mainframe.
	 */
	public Mainframe(final Controller controller) {
		setTitle("Command-Line Interface");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1200, 750));
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(5, 5));
		
		txtCLI = new JTextArea();
		txtCLI.setFont(new Font("Consolas", Font.BOLD, 13));
		txtCLI.setLineWrap(true);
		getContentPane().add(txtCLI, BorderLayout.CENTER);
		txtCLI.setForeground(Color.LIGHT_GRAY);
		txtCLI.setBackground(Color.BLACK);
		txtCLI.setCaretColor(Color.LIGHT_GRAY);
		
		scroll = new JScrollPane (txtCLI);
                scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                getContentPane().add(scroll, BorderLayout.CENTER);
		
                disableKeys(txtCLI.getInputMap());
                disableKeys(scroll.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));
                commandList = new ArrayList<>();
		commandOffset = controller.getDirectory() + ">";
		txtCLI.setText(commandOffset);
		resetCommand();
		
		txtCLI.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				String command = "";
				if(e.getKeyChar() == KeyEvent.VK_ENTER){
					command = txtCLI.getText().substring(txtCLI.getText().lastIndexOf('>')+1, txtCLI.getText().length()-1);
					switch (command.split(" ")[0]) {
						case "cd": 
                                                        commandList.add(command);
							if (controller.setDirectory(command.split(" ")[1])) {
								commandOffset = controller.getDirectory() + ">";
							} else {
								txtCLI.append("Error: '" + command.split(" ")[1] + "' is not a valid file path.\n");
							}
							resetCommand();
							txtCLI.append(commandOffset);
                                                        pointer = commandList.size();
							break;
							
						case "dir":
                                                    commandList.add(command);
							try {
								txtCLI.append(controller.displayAllFiles());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							txtCLI.append(commandOffset);
                                                        pointer = commandList.size();
							break;
						case "rename":
                                                    commandList.add(command);
							try {
								String oldFileName = command.split(" ")[1];
								String newFileName = command.split(" ")[2];
								if (controller.editFileName(oldFileName, newFileName))
									txtCLI.append("Successfully renamed file.\n");
								else txtCLI.append("Error: Cannot find file or does not exist.\n");
							} catch (IOException e1) {
								txtCLI.append("Error: Cannot find file or does not exist.\n");
							}
							txtCLI.append(commandOffset);
                                                        pointer = commandList.size();
							break;
							
						case "ext":
                                                    commandList.add(command);
							try {
								String fileName = command.split(" ")[1];
								String extension = command.split(" ")[2];
								if (controller.editFileExtension(fileName, extension))
									txtCLI.append("Successfully changed file extension.\n");
								else txtCLI.append("Error: Cannot find file or does not exist.\n");
							} catch (IOException e1) {
								txtCLI.append("Error: Cannot find file or does not exist.\n");
							}
							txtCLI.append(commandOffset);
                                                        pointer = commandList.size();
							break;
							
						case "move":
                                                    commandList.add(command);
							try {
								String fileName = command.split(" ")[1];
								String dir = command.split(" ")[2];
								if (controller.moveFile(fileName, dir))
									txtCLI.append("Successfully moved file.\n");
								else txtCLI.append("Error: Cannot find file or cannot move file.\n");
							} catch (IOException e1) {
								txtCLI.append("Error: Cannot find file or cannot move file.\n");
							}
							txtCLI.append(commandOffset);
                                                        pointer = commandList.size();
							break;
							
						case "copy":
                                                    commandList.add(command);
							try {
								String fileName = command.split(" ")[1];
								String dir = command.split(" ")[2];
								if (controller.copyFile(fileName, dir))
									txtCLI.append("Successfully copied file.\n");
								else txtCLI.append("Error: Cannot find file or cannot copy file.\n");
							} catch (IOException e1) {
								txtCLI.append("Error: Cannot find file or cannot copy file.\n");
							}
							txtCLI.append(commandOffset);
                                                        pointer = commandList.size();
							break;
						
						case "del":
                                                    commandList.add(command);
							try {
								String fileName = command.split(" ")[1];
								if (controller.deleteFile(fileName))
									txtCLI.append("Successfully deleted file.\n");
								else txtCLI.append("Error: Cannot find file or does not exist.\n");
							} catch (IOException e1) {
								txtCLI.append("Error: Cannot find file or does not exist.\n");
							}
							txtCLI.append(commandOffset);
                                                        pointer = commandList.size();
							break;
							
						case "dup":
                                                    commandList.add(command);
							try {
								String fileName = command.split(" ")[1];
								if (controller.duplicateFile(fileName))
									txtCLI.append("Successfully duplicated file.\n");
								else txtCLI.append("Error: Cannot find file or does not exist.\n");
							} catch (IOException e1) {
								txtCLI.append("Error: Cannot find file or does not exist.\n");
							}
							txtCLI.append(commandOffset);
                                                        pointer = commandList.size();
							break;
						case "run":
                                                    commandList.add(command);
							try {
								String fileName = command.split(" ")[1];
								if (controller.runC(fileName))
                                                                {
                                                                    txtCLI.append(controller.getcContens()+"\n");
                                                                    txtCLI.append("Successfully run.\n");
                                                                }
								else txtCLI.append("Error: Cannot find file or does not exist.\n");
							} catch (IOException e1) {
								txtCLI.append("Error: Cannot find file or does not exist.\n");
							} catch (InterruptedException ex)
                                        {
                                            Logger.getLogger(Mainframe.class.getName()).log(Level.SEVERE, null, ex);
                                        }
							txtCLI.append(commandOffset);
                                                        pointer = commandList.size();
							break;
                                                case "cls":
                                                    commandList.add(command);
                                                        txtCLI.setText("");
                                                        resetCommand();
                                                        pointer = commandList.size();
                                                        break;
							
						case "exit":
                                                    commandList.add(command);
							dispose();
                                                        pointer = commandList.size();
							break;
							
						default: txtCLI.append("'" + command.split(" ")[0] + "' is not recognized as an internal or external command or operable program.\n");
								resetCommand();
								txtCLI.append(commandOffset);
					}
		        } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && txtCLI.getCaretPosition() <= txtCLI.getText().length()) {
		        	// TODO: fix this. avoid the user from deleting the display all the way!!!
                            }
			}

			@Override
			public void keyPressed(KeyEvent e) {
//                        int keyCode = e.getKeyCode();
//                        switch( keyCode ) { 
//                            case KeyEvent.VK_UP:
//                                if(pointer > 0)
//                                {
//                                pointer -= 1;
//                                txtCLI.append(commandList.get(pointer));
//                                }
//                                else
//                                    pointer = commandList.size();
//                                break;
//                         }
                        }

			@Override
			public void keyReleased(KeyEvent e) { }
		});
	}
       	
	/**
	 * Updates content of the CLI.
	 * This method is usually called after a command has been entered. 
	 * @param content the content to be added to the current display
	 */
	public void appendContent (String content) {
		txtCLI.append("\n" + content);
		repaint();
		revalidate();
	}
        /**
	 * Disables respones to a key.
	 * @param inputMap the content to be added to the inputMap
	 */
        public void disableKeys(InputMap inputMap) {
        String[] keys = {"UP"};
        for (String key : keys) {
            inputMap.put(KeyStroke.getKeyStroke(key), "none");
            }
        }
	
	/**
	 * Clears the command line after a command has been entered
	 */
	private void resetCommand() {
		((AbstractDocument) txtCLI.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (offset < commandOffset.length()) {
                    return;
                }
                super.insertString(fb, offset, string, attr);
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (offset < commandOffset.length()) {
                    length = Math.max(0, length - commandOffset.length());
                    offset = commandOffset.length();
                }
                super.replace(fb, offset, length, text, attrs);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                if (offset < commandOffset.length()) {
                    length = Math.max(0, length + offset - commandOffset.length());
                    offset = commandOffset.length();
                }
                if (length > 0) {
                    super.remove(fb, offset, length);
                }
            }
		});
	}
}
