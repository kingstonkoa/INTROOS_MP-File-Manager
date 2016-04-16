/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import view.EditFrame;

/**
 * Implements the various tasks of the File Manager:
 * <li> Show current files
 * <li> Edit file extension
 * <li> Move file to another directory
 * <li> Copy file to another directory
 * <li> Delete file
 * <li> Change current directory
 * <li> Duplicate a file
 * <li> Run a C program
 */
public class Controller {
    String directory;
    String cContens;

    public String getcContens()
    {
        return cContens;
    }

    public void setcContens(String cContens)
    {
        this.cContens = cContens;
    }

    /**
     * Gets the current directory of the File Manager.
     * @return the current directory
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets/Changes the current directory to the given directory if it exists.
     * @param directory the directory the program will go to
     * @return			{@link true} if the file manager successfully redirected to the directory
     * 					<p>{@link false} if the directory does not exist or cannot be redirected to
     */
    public boolean setDirectory(String directory) {
        File file = new File(directory);
        if (!file.isDirectory()) {
        	return false;
        } else {
        	this.directory = directory;
        	return true;
        }
    }

    /**
     * Displays all files in the current directory
     * @throws IOException
     * @return the list of files and their details
     */
    public String displayAllFiles() throws IOException
    {
        String format = "%1$5s %2$-40s %3$-20s %3$-20s %3$-20s %3$-20s" ;
        String someLine;
    	String filesDisplay = "";
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        File cDrive = new File("c:");
        long totalSpace = cDrive.getTotalSpace();
        System.out.println(totalSpace);
        filesDisplay += " " 
                            + padString("Name",35)
                            + padString("Date Created",30)
                            + padString("Last Modified",30)
                            + padString("Owner",30)
                            + padString("Size",10)
                            + padString("Percentage",30)
                            + "\n"; 
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            BasicFileAttributes attr = Files.readAttributes(listOfFiles[i].toPath(), BasicFileAttributes.class);
            filesDisplay += " " 
                            + padString(listOfFiles[i].getName(),35)
                            + padString(attr.creationTime().toString(),30)
                            + padString(attr.lastModifiedTime().toString(),30)
                            + padString(Files.getOwner(listOfFiles[i].toPath()).toString(),30)
                            + padString(readableFileSize(attr.size()),10)
                            + padString(String.format("%f", getPercentage(attr.size(), totalSpace)) + "%",30)
                            + "\n"; 
//            filesDisplay += ""+listOfFiles[i].getName() 
//                                + "\t" + attr.creationTime()
//                                + "\t" + attr.lastModifiedTime() 
//                                + "\t" + Files.getOwner(listOfFiles[i].toPath()) 
//                                + "\t" + readableFileSize(attr.size())
//                                + "\t" + String.format("%f", getPercentage(attr.size(), totalSpace)) + "%"
//            					+ "\n\n";
          } else if (listOfFiles[i].isDirectory()) {
            filesDisplay += "[" + (i+1) + "] " + "Folder: " + listOfFiles[i].getName() + "\n\n";
          }
        }
        filesDisplay += "space occupied: "+ readableFileSize(getSize(folder))+ "\n";
        filesDisplay += "space unused: " + readableFileSize(cDrive.getUsableSpace()) + "\n\n";

        return filesDisplay;
    }
    
    /**
     * Formats the file size of a file and classifies it as one of the following -- bytes, kilobytes, megabytes, gigabytes, terabytes.
     * @param size unformatted file size
     * @return the formatted file size
     */
    public static long getSize(File file) {
    long size;
    if (file.isDirectory()) {
        size = 0;
        for (File child : file.listFiles()) {
            size += getSize(child);
        }
    } else {
        size = file.length();
    }
    return size;
    }
    
    private static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    public static float getPercentage(long n, long total) {
    float proportion = ((float) n) / ((float) total);
    return proportion * 100;
    }
    
    private String padString(String str, int n){
    if(str.length() < n){
        for(int j = str.length(); j < n; j++){
            str += " ";
        } // end for
    } // end if
    return str;
} // end padString

    /**
     * Edits the file name of an existing file/folder.
     * @param oldFileName name of the existing file
     * @param newFileName new name of the existing file 
     * @return {@link true} if the file name was successfully changed
     * 		<p>{@link false} if the file name was not changed
     * @throws IOException
     */
    public boolean editFileName(String oldFileName, String newFileName) throws IOException {
        int fileChosen;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        
        // check if file exists
        fileChosen  = fileNameExists(listOfFiles, oldFileName);
        if (fileChosen == -1) {
        	return false;
        } else {
    		Path source = listOfFiles[fileChosen].toPath();
    		try {
                Files.move(source, source.resolveSibling(newFileName+"."+getFileExtension(listOfFiles[fileChosen].getName())));
                return true;
            } catch (IOException ex) {
               return false;
            }
    	}
    }
    
    /**
     * Checks if the file entered exists.
     * @param list list of files in the directory being searched in
     * @param fileName name of file to look for
     * @return index of the file in the directory
     */
    private int fileNameExists(File[] list, String fileName) {
    	ArrayList<String> listNames = new ArrayList<String>();
    	for (File f : list) {
    		listNames.add(f.getName());
    	}
    	return listNames.indexOf(fileName);
    }

    /**
     * Removes the file extension.
     * @param fileName the file name with the extension
     * @return returns the file name without the extension
     */
    private String removeFileExtension(String fileName) {
        
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
}
        return fileName;
    }
    
    /**
     * Gets the file extension of the file name.
     * @param fileName the name of the file 
     * @return the extension of the given file
     */
    public String getFileExtension(String fileName) {
	    try {
	        return fileName.substring(fileName.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
    }

    /**
     * Edits the type of extension of a file.
     * @param fileName the name of file to be changed
     * @param ext the new type of extension of the file
     * @return {@link true} if the file extension was successfully changed
     * 		<p>{@link false} if the file extension was not changed
     * @throws IOException
     */
    public boolean editFileExtension(String fileName, String ext) throws IOException {
        int fileChosen;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        
     // check if file exists
        fileChosen  = fileNameExists(listOfFiles, fileName);
        if (fileChosen == -1) {
        	return false;
        } else {
    		Path source = listOfFiles[fileChosen].toPath();
    		try {
                Files.move(source, source.resolveSibling(removeFileExtension(listOfFiles[fileChosen].getName()) + "." + ext));
                return true;
            } catch (IOException ex) {
               return false;
            }
    	}
    }

    /**
     * Moves the file to a new directory.
     * @param fileName the name of the file to move
     * @param newDir the new directory of the file
     * @return {@link true} if the file name was successfully moved
     * 		<p>{@link false} if the file name was not moved
     * @throws IOException
     */
    public boolean moveFile(String fileName, String newDir) throws IOException {
        int fileChosen;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

     // check if file exists
        fileChosen  = fileNameExists(listOfFiles, fileName);
        if (fileChosen == -1) {
        	return false;
        } else {
        	Path source = listOfFiles[fileChosen].toPath();
            try
            {
                Files.move(source, Paths.get(newDir).resolve(source.getFileName()), REPLACE_EXISTING);
                System.out.println("file move successful");
                return true;
            } catch (IOException ex)
            {
                System.out.println("error in moving file");
                return false;
            }
        }
    }

    /**
     * Copies the file to a new directory.
     * @param fileName the name of the file to copy
     * @param newDir the new directory of the copied file
     * @return {@link true} if the file name was successfully copied
     * 		<p>{@link false} if the file name was not copied
     * @throws IOException
     */
    public boolean copyFile(String fileName, String newDir) throws IOException {
    	int fileChosen;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

     // check if file exists
        fileChosen  = fileNameExists(listOfFiles, fileName);
        if (fileChosen == -1) {
        	return false;
        } else {
        	Path source = listOfFiles[fileChosen].toPath();
        	 try
                    {
                        Files.copy(source, Paths.get(newDir+"\\"+listOfFiles[fileChosen].getName()), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("file copy successful");
                        return true;
                    } catch (IOException ex)
                    {
                        System.out.println("error in copying file");
                        return false;
                    }

        }
    }

    /**
     * Deletes the file in the directory.
     * @param fileName the name of the file to delete
     * @return {@link true} if the file name was successfully deleted
     * 		<p>{@link false} if the file name was not deleted
     * @throws IOException
     */
    public boolean deleteFile(String fileName) throws IOException {
    	int fileChosen;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

     // check if file exists
        fileChosen  = fileNameExists(listOfFiles, fileName);
        if (fileChosen == -1) {
        	return false;
        } else {
        	listOfFiles[fileChosen].delete();
        	return true;
        }
    }
    
    public boolean duplicateFile(String fileName) throws FileNotFoundException {
        int fileChosen;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

     // check if file exists
        fileChosen  = fileNameExists(listOfFiles, fileName);
        if (fileChosen == -1) {
        	return false;
        } else {
        	String source = listOfFiles[fileChosen].getAbsolutePath();
            String filePath = source.substring(0,source.lastIndexOf(File.separator));
            Path path = listOfFiles[fileChosen].toPath();
            String target = filePath+"\\Copy_of_"+listOfFiles[fileChosen].getName();
            if(Files.exists(path)) {
            	InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target);
                
             // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
           
                try {
					while ((len = in.read(buf)) > 0) {
					    out.write(buf, 0, len);
					}
					
					in.close();
	                out.close();
				} catch (IOException e) {
					return false;
				}
            }
        	return true;
        }
    }

    public boolean runC(String fileName) throws FileNotFoundException, InterruptedException
    {
        int fileChosen;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        String cContents = "";
        Process p = null;
        

     // check if file exists
        fileChosen  = fileNameExists(listOfFiles, fileName);
        if (fileChosen == -1) {
        	return false;
        } else {
            try
            {
                if(getFileExtension(listOfFiles[fileChosen]).equals("c"))
                {
                
                 p = Runtime.getRuntime().exec("cmd /C cd "+directory + " && gcc "  +listOfFiles[fileChosen].getName()
                    + " -o "+removeFileExtension(listOfFiles[fileChosen].getName())
                    +" && "+removeFileExtension(listOfFiles[fileChosen].getName())+".exe");
                }
                else if(getFileExtension(listOfFiles[fileChosen]).equals("java"))
                {
                 p = Runtime.getRuntime().exec("cmd /C cd "+directory + " && javac "  +listOfFiles[fileChosen].getName()
                    +" && java "+removeFileExtension(listOfFiles[fileChosen].getName()));
                }
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));  
            String line = null;  
            while ((line = in.readLine()) != null) {  
                System.out.println(line);
                cContents += line;
            }
            setcContens(cContents);

        }   catch (IOException ex)
            {
                return false;
            }
        return true;
        }
    }
    
    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
    
    public String readFile(File file) throws IOException
    {
        String content = null;
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null){reader.close();}
        }
        return content;
    }

    public boolean editFileContents(String fileName) throws IOException
    {
        int fileChosen;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
             // check if file exists
        fileChosen  = fileNameExists(listOfFiles, fileName);
        if (fileChosen == -1) {
        	return false;
        } else {
            JFrame frame = new JFrame ("Edit Frame");
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            frame.getContentPane().add (new EditFrame(listOfFiles[fileChosen],readFile(listOfFiles[fileChosen]), this));
            frame.pack();
            frame.setVisible (true);
            
            return true;
        }
    }
    
}




