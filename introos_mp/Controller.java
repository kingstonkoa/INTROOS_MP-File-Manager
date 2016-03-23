/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package introos_mp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kingston
 */
public class Controller
{
    String directory;

    public Controller()
    {
       System.out.println("Please enter your directory: ");


       Scanner scanIn = new Scanner(System.in);
       directory = scanIn.nextLine();
       //scanIn.close();            
       
    }

    public String getDirectory()
    {
        return directory;
    }

    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    public void DisplayMainMenu() throws IOException
    {
        String choice;
        
         System.out.print("[1] Show current files\n"
                       + "[2] Edit file name\n"
                       + "[3] Edit file extension\n"
                       + "[4] Move file\n"
                       + "[5] Copy file\n"
                       + "[6] Delete file\n"
                       + "[7] Change directory\n"
                       + "[8] Run a C program\n"
                       + "[9] Duplicate a file\n");   
         
       Scanner scanIn = new Scanner(System.in);
       choice = scanIn.nextLine();
       //scanIn.close(); 
       
       switch(choice)
       {
           case "1": DisplayAllFiles(); break;
           case "2": EditFileName(); break;
           case "3": EditFileExtension(); break;
           case "4": MoveFile(); break;
           case "5": CopyFile(); break;
           case "6": DeleteFile(); break;
           case "7": ChangeDirectory(); break;
               
           case "9": DuplicateFile(); break;
           default: System.out.println("Invalid choice");
                    DisplayMainMenu(); break;
               
       }
    }

    public void DisplayAllFiles() throws IOException
    {
        /** extra credits not done*/
        //(Extra Credit) It should display how much space in the disk has been occupied and how much are unused.
        //(Extra credit) It should display the percentage of space in the hard disk that is being consumed by each file. 
        String choice;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            BasicFileAttributes attr = Files.readAttributes(listOfFiles[i].toPath(), BasicFileAttributes.class);
            System.out.println("[" + (i+1) + "]" 
                                +" \nName: " + listOfFiles[i].getName() 
                                + " \nDate Created: " + attr.creationTime()
                                + " \nDate Modified: " + attr.lastModifiedTime() 
                                + " \nOwner: " + Files.getOwner(listOfFiles[i].toPath()) 
                                + " \nSize: " + readableFileSize(attr.size()));
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
        System.out.println("[0] Back to Main Menu");        
        do
        {
        Scanner scanIn = new Scanner(System.in);
        choice = scanIn.nextLine();
        }
        while(!"0".equals(choice));
        DisplayMainMenu();
    }
    
    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void EditFileName() throws IOException
    {
        String choice;
        String fileChosen;
        String newFileName;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            System.out.println("[" + (i+1) + "]" + listOfFiles[i].getName());
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
       Scanner scanIn = new Scanner(System.in);
       fileChosen = scanIn.nextLine();
       
       System.out.println("Rename " + removeFileExtension(listOfFiles[Integer.parseInt(fileChosen)-1].getName()) + " to?");
       Scanner scanIn2 = new Scanner(System.in);
       newFileName = scanIn2.nextLine();
       
       Path source = listOfFiles[Integer.parseInt(fileChosen)-1].toPath();
        try
        {
            Files.move(source, source.resolveSibling(newFileName+"."+getFileExtension(listOfFiles[Integer.parseInt(fileChosen)-1].getName())));
            System.out.println("file has been renamed");
        } catch (IOException ex)
        {
           System.out.println("error in renaming file");
        }
        System.out.println("[0] Back to Main Menu");        
        do
        {
        Scanner scanIn3 = new Scanner(System.in);
        choice = scanIn3.nextLine();
        }
        while(!"0".equals(choice));
        DisplayMainMenu();
    }

    public String removeFileExtension(String fileName)
    {
        
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
}
        return fileName;
    }
    
    public String getFileExtension(String fileName) {
    try {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    } catch (Exception e) {
        return "";
    }
}

    public void EditFileExtension() throws IOException
    {
        String choice;
        String fileChosen;
        String newFileExtension;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            System.out.println("[" + (i+1) + "]" + listOfFiles[i].getName());
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
       Scanner scanIn = new Scanner(System.in);
       fileChosen = scanIn.nextLine();
       
       System.out.println("Change Extension of " + listOfFiles[Integer.parseInt(fileChosen)-1].getName() + " to?");
       Scanner scanIn2 = new Scanner(System.in);
       newFileExtension = scanIn2.nextLine();
       
       Path source = listOfFiles[Integer.parseInt(fileChosen)-1].toPath();
        try
        {
            Files.move(source, source.resolveSibling(removeFileExtension(listOfFiles[Integer.parseInt(fileChosen)-1].getName())+"."+newFileExtension));
            System.out.println("file extension has been changed");
        } catch (IOException ex)
        {
           System.out.println("error in changing file extension");
        }
        System.out.println("[0] Back to Main Menu");
         do
        {
        Scanner scanIn3 = new Scanner(System.in);
        choice = scanIn3.nextLine();
        }
        while(!"0".equals(choice));
        DisplayMainMenu();
    }

    public void MoveFile() throws IOException
    {
        String choice;
        String fileChosen;
        String newFileDirectory;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            System.out.println("[" + (i+1) + "]" + listOfFiles[i].getName());
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
       Scanner scanIn = new Scanner(System.in);
       fileChosen = scanIn.nextLine();
       
       System.out.println("Move " + listOfFiles[Integer.parseInt(fileChosen)-1].getName() + " to which directory?");
       Scanner scanIn2 = new Scanner(System.in);
       newFileDirectory = scanIn2.nextLine();
       
       Path source = listOfFiles[Integer.parseInt(fileChosen)-1].toPath();
       Path newdir = Paths.get(newFileDirectory);
        try
        {
            Files.move(source, newdir.resolve(source.getFileName()), REPLACE_EXISTING);
            System.out.println("file move successful");
        } catch (IOException ex)
        {
            System.out.println("error in moving file");
        }
        
        System.out.println("[0] Back to Main Menu");
         do
        {
        Scanner scanIn3 = new Scanner(System.in);
        choice = scanIn3.nextLine();
        }
        while(!"0".equals(choice));
        DisplayMainMenu();
    }

    public void CopyFile() throws IOException
    {
        String choice;
        String fileChosen;
        String newFileDirectory;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            System.out.println("[" + (i+1) + "]" + listOfFiles[i].getName());
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
       Scanner scanIn = new Scanner(System.in);
       fileChosen = scanIn.nextLine();
       
       System.out.println("Copy " + listOfFiles[Integer.parseInt(fileChosen)-1].getName() + " to which directory?");
       Scanner scanIn2 = new Scanner(System.in);
       newFileDirectory = scanIn2.nextLine();
       
       Path source = listOfFiles[Integer.parseInt(fileChosen)-1].toPath();
       Path newdir = Paths.get(newFileDirectory+"\\"+listOfFiles[Integer.parseInt(fileChosen)-1].getName());
        try
        {
            Files.copy(source, newdir, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("file copy successful");
        } catch (IOException ex)
        {
            System.out.println("error in copying file");
        }
        
        System.out.println("[0] Back to Main Menu");
         do
        {
        Scanner scanIn3 = new Scanner(System.in);
        choice = scanIn3.nextLine();
        }
        while(!"0".equals(choice));
        DisplayMainMenu();
    }

    public void DeleteFile() throws IOException
    {
        String choice;
        String fileChosen;
        String newFileDirectory;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            System.out.println("[" + (i+1) + "]" + listOfFiles[i].getName());
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
       Scanner scanIn = new Scanner(System.in);
       fileChosen = scanIn.nextLine();
       
       
       listOfFiles[Integer.parseInt(fileChosen)-1].delete();
       System.out.println("file delete successful");
        
        System.out.println("[0] Back to Main Menu");
         do
        {
        Scanner scanIn3 = new Scanner(System.in);
        choice = scanIn3.nextLine();
        }
        while(!"0".equals(choice));
        DisplayMainMenu();
    }

   public void ChangeDirectory() throws IOException
    {
        String choice;
        String newDirectory;
        Scanner scanIn = new Scanner(System.in);
        newDirectory = scanIn.nextLine();
        setDirectory(newDirectory);
        System.out.println("directory has been changed");
        
         System.out.println("[0] Back to Main Menu");
         do
        {
        Scanner scanIn3 = new Scanner(System.in);
        choice = scanIn3.nextLine();
        }
        while(!"0".equals(choice));
        DisplayMainMenu();
    }

    public void DuplicateFile() throws FileNotFoundException, IOException
    {
        String choice;
        String fileChosen;
        String newFileDirectory;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            System.out.println("[" + (i+1) + "]" + listOfFiles[i].getName());
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
       Scanner scanIn = new Scanner(System.in);
       fileChosen = scanIn.nextLine();
       
       String source = listOfFiles[Integer.parseInt(fileChosen)-1].getAbsolutePath();
       String filePath = source.substring(0,source.lastIndexOf(File.separator));
       
       Path path = listOfFiles[Integer.parseInt(fileChosen)-1].toPath();
       String target = filePath+"\\Copy of "+listOfFiles[Integer.parseInt(fileChosen)-1].getName();
       if(Files.exists(path))
       {
      InputStream in = new FileInputStream(source);
      OutputStream out = new FileOutputStream(target);
   
      // Copy the bits from instream to outstream
      byte[] buf = new byte[1024];
      int len;
 
      while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
      }
 
      in.close();
      out.close();
         System.out.println("File duplication successful");
       }
       
      
        
        System.out.println("[0] Back to Main Menu");
         do
        {
        Scanner scanIn3 = new Scanner(System.in);
        choice = scanIn3.nextLine();
        }
        while(!"0".equals(choice));
        DisplayMainMenu();
    }
    

}
