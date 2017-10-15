package command.line.interpreter;

import sun.text.resources.iw.FormatData_iw_IL;

import javax.naming.Name;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class CommandLineInterpreter {

    public static void rm(File cur ,ArrayList<String>name){
        for(int i=1 ; i<name.size() ; i++){
            File f;
            if(contain(cur, name.get(i)))
                f =  new File(cur.getPath()+'/'+name.get(i));
            else f = new File(name.get(i));
            if(f.exists() && !f.isDirectory())
                f.delete();
            else
                System.out.println("Error!!");
        }
    }
    public static void mkdir(File cur , ArrayList<String> name){
        for(int i=1 ; i<name.size() ; i++){
            String path = "";
            File f1 = new File(name.get(i));
            if( f1.getParent() == null ){
                path = cur.getPath() + '/' + name.get(i);
                f1 = new File(path);
            }
            
            if(f1.exists())
                System.out.println("mkdir: cannot create directory ‘" + name.get(i) + "’: File exists");
            else f1.mkdir();
        }
    }
    public static void rmdir(File cur , ArrayList<String> name){
        for(int i=1 ; i<name.size() ; i++){
            String path = "";
            File f1 = new File(name.get(i));
            if( f1.getParent() == null ){
                path = cur.getPath() + '/' + name.get(i);
                f1 = new File(path);
            }
            if(f1.exists() && f1.isDirectory() && f1.list().length == 0)
                f1.delete();
            
            else System.out.println("Not a Directory or Directory doesnt exist or Directory not empty");
        }
    }
    public static void ls(File f){
        String []Names = f.list();
        for (String Name : Names) {
            System.out.println((char)27+"[34m"+Name);
        }
    }
    public static void date(){
        Date date = new Date();
        System.out.println(date.toString());
    }
    public static void pwd(File f){
        System.out.println(f.getPath());
    }
    public static void Read(File f){
        try {
            BufferedReader input = new BufferedReader(new FileReader(f));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void cat(File cur , ArrayList<String> name){
        for(int i=1 ; i<name.size() ; i++){
            File f;
            if(contain(cur , name.get(i))) f = new File(cur.getPath() + '/' + name.get(i));
            else f = new File(name.get(i));
            if(f.exists() && !f.isDirectory())
                Read(f);
            else System.out.println("cat: "+f.getName()+": Is a directory");
        }
    }
    public static void mv(File cur , ArrayList<String> arra){
        File f2 = new File(arra.get(arra.size()-1));
        if(f2.getParent() == null)
            f2 = new File(cur.getPath() + '/' + arra.get(arra.size()-1));
        if(arra.size()>3 && !f2.isDirectory()){
            System.out.println("mv: target '"+f2.getName()+"' is not a directory");
            return;
        }
        for(int i=1 ; i<arra.size()-1 ; i++ ){
            File f1 = new File(arra.get(i));
            if(f1.getParent() == null)
                f1 = new File(cur.getPath() + '/' + arra.get(i));
            if(!f1.exists() || !f2.exists())
                System.out.println("Error");
            else if(f1.isDirectory() && !f2.isDirectory())
                System.out.println("mv: cannot overwrite non-directory '"+f2.getName()+"' with directory '"+f1.getName()+"'");
            else{
                if(!f1.isDirectory() && !f2.isDirectory())
                    f1.renameTo(f2);
                else if(f1.isDirectory() && f2.isDirectory() || (!f1.isDirectory() && f2.isDirectory())){
                    try{
                        Path p1 = f1.toPath();
                        Path p2 = f2.toPath();
                        Files.move(p1,p2.resolve(p1.getFileName()) , REPLACE_EXISTING);
                    }
                    catch(IOException e){
                        System.out.println("Error");
                    }
                }
            }
        }
    }
    public static void cp(File cur , String n1 , String n2){
        File f1 , f2;
        if(contain(cur , n1)) f1 = new File(cur.getPath() + '/' + n1);
        else f1 = new File(n1);
        if(contain(cur , n2)) f2 = new File(cur.getPath() + '/' + n2);
        else f2 = new File(n2);
        if(f1.isDirectory())
            System.out.println("cp: omitting directory '"+f1.getName()+"'");
        else{
            try {
                Files.copy(f1.toPath(), f2.toPath(),REPLACE_EXISTING);
            }
            catch(IOException e){
                System.out.println("Error");
            }
        }
    }
    public static void help(){
        try{
            File f1 = new File("files");
            File []array = f1.listFiles();
            for(int i=0 ; i<array.length ; i++){
                System.out.println(array[i].getName()+" : ");
                Read(array[i]);
            }
        }
        catch (Exception e){
            System.out.println("Error");
        }
    }
    public static void check(String input){
        if(input.equals("clear"))Read(new File("files/clear"));
        else if(input.equals("cat"))Read(new File("files/cat"));
        else if(input.equals("pwd"))Read(new File("files/pwd"));
        else if(input.equals("mkdir"))Read(new File("files/mkdir"));
        else if(input.equals("rmdir"))Read(new File("files/rmdir"));
        else if(input.equals("cp"))Read(new File("files/cp"));
        else if(input.equals("date"))Read(new File("files/date"));
        else if(input.equals("ls"))Read(new File("files/ls"));
        else if(input.equals("cd"))Read(new File("files/cd"));
        else if(input.equals("mv"))Read(new File("files/mv"));
        else if(input.equals("rm"))Read(new File("files/rm"));
        else if(input.equals("more"))Read(new File("files/more"));
        else if(input.equals(">>"))Read(new File("files/>>"));
        else if(input.equals("pipe"))Read(new File("files/pipe"));
        else if(input.equals(">"))Read(new File("files/>"));
        else if(input.equals("args"))Read(new File("files/args"));
        else System.out.println("Error!");
    }
    public static void args(String input){
        if(input.equals("cat"))Read(new File("args/cat"));
        else if(input.equals("mkdir"))Read(new File("args/mkdir"));
        else if(input.equals("rmdir"))Read(new File("args/rmdir"));
        else if(input.equals("cp"))Read(new File("args/cp"));
        else if(input.equals("ls"))Read(new File("args/ls"));
        else if(input.equals("cd"))Read(new File("args/cd"));
        else if(input.equals("mv"))Read(new File("args/mv"));
        else if(input.equals("rm"))Read(new File("args/rm"));
        else if(input.equals("?"))Read(new File("args/?"));
        else if(input.equals("args"))Read(new File("args/args"));
        else if(input.equals("clear"))Read(new File("args/clear"));
        else if(input.equals("more"))Read(new File("args/more"));
        else if(input.equals("date"))Read(new File("args/date"));
        else if(input.equals("help"))Read(new File("args/help"));
        else if(input.equals("pwd"))Read(new File("args/pwd"));
        else System.out.println("Error!");
    }
    public static void clear(){
        try {
            Robot pressbot = new Robot();
            pressbot.keyPress(17); // Holds CTRL key.
            pressbot.keyPress(76); // Holds L key.
            pressbot.keyRelease(17); // Releases CTRL key.
            pressbot.keyRelease(76); // Releases L key.
        }
        catch (AWTException ex) {
        }
    }
    public static File cd(File current ,String s){
        for(int i=0 ; i<current.list().length ; i++)
            if(s.equals(current.list()[i]) && current.listFiles()[i].isDirectory()){
                current = new File(current.listFiles()[i].getPath());
                return current;
            }
        File f = new File(s);
        if(!f.isDirectory()) System.out.println("isn't directory");
        return current;
    }
    public static void more(File cur , ArrayList<String>arr){
        for(int i = 1 ; i < arr.size() ; ++i){
            File file;
            if(contain(cur,arr.get(i)))file = new File(cur.getPath() + '/' + arr.get(i));
            else file = new File(arr.get(i));
            if(file.isDirectory() || !file.exists())
                System.out.println("Not a file");
            else{
                try {
                    Scanner s;
                    s = new Scanner(System.in);
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    char input;
                    while ((line = bufferedReader.readLine()) != null) {
                        input = s.next().charAt(0);
                        if(input == 'p'){
                            stringBuffer.append(line);
                            stringBuffer.append("\n");
                            for(int j = 0 ; j < 6 ; ++j){
                                if((line = bufferedReader.readLine()) != null){
                                    stringBuffer.append(line);
                                    stringBuffer.append("\n");
                                }
                            }
                            System.out.print(stringBuffer.toString());
                        }
                        else System.out.println(line);
                    }
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void ls2(File cur , String name , boolean app){
        try{
            File f = new File(cur.getPath()+'/'+name);
            FileWriter f1 = new FileWriter(f , app);
            String []Names = f.getParentFile().list();
            for(int i=0 ; i<Names.length ; i++)
                f1.write(Names[i]+"\n");
            f1.flush();
            f1.close();
        }
        catch(IOException e){
            System.out.println("Error!!");
        }
    }

    public static ArrayList<String> Split(String s){
        ArrayList<String> command = new ArrayList<String>();
        String word = "";
        for(int i = 0 ; i < s.length() ; ++i){
            if(s.charAt(i) == ' '){
                word = "";
                continue;
            }
            else {
                for( ; i < s.length(); ++i){
                    if(s.charAt(i) == ' ')break;
                    word += s.charAt(i);
                }
                command.add(word);
                word = "";
            }
        }
        return command;
    }
    public static boolean contain(File cur , String name){
        for(int i=0 ; i<cur.list().length ; i++)
            if(cur.list()[i].equals(name))
                return true;
        return false;
    }

    public static void main(String[] args) {
        File cur = new File("/home/karim/");
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print((char)27 + "[32m"+cur.getPath()+":~$ ");
            String input = sc.nextLine();
            String []commands;
            // check " ";
            if(input.charAt(0) == '"'){
                if( input.charAt(input.length()-1) == '"' ){
                    input = input.substring(1,input.length());
                }
                else {
                    System.out.println("No such Command");
                    continue;
                }
            }
            commands = input.split("\\|"); // for the pipe
            for(int i = 0 ; i < commands.length ; ++i){
                ArrayList<String> command = new ArrayList<String>();
                command = Split(commands[i]);
                if( command.isEmpty() ) continue;
                else if(command.get(0).equals("ls"))ls(cur);
                else if(command.get(0).equals("date"))date();
                else if(command.get(0).equals("clear"))clear();
                else if(command.get(0).equals("pwd"))pwd(cur);
                else if(command.get(0).equals("help"))help();
                else if(command.get(0).equals("more") && command.size() > 1)more(cur , command);
                else if(command.get(0).charAt(0) == ('?') )check( command.get(0).substring( 1,command.get(0).length() ) );
                else if(command.get(0).equals("cd") && command.size() > 1)cur = cd(cur, command.get(1));
                else if(command.get(0).equals("cd") && command.size() == 1)cur = new File("/home/karim/");
                else if(command.get(0).equals("mkdir") && command.size() > 1)mkdir(cur,command);
                else if(command.get(0).equals("rmdir") && command.size() > 1)rmdir(cur,command);
                else if(command.get(0).equals("mv")){
                    if(command.size() <= 2)System.out.println("mv: missing file operand\nTry 'mv --help' for more information.");
                    else mv(cur,command);
                }
                else if(command.get(0).equals("rm") && command.size() > 1 )rm(cur,command);
                else if(command.get(0).equals("cp") && command.size() > 2 )cp(cur,command.get(1),command.get(2));
                else if(command.get(0).equals("cat") && command.size() > 1)cat(cur,command);
                else if(command.get(0).equals("args") && command.size() > 1)args(command.get(1));
                else if(command.get(0).equals("ls>") && command.size() > 1)ls2(cur , command.get(1), false);
                else if(command.get(0).equals("ls>>") && command.size() > 1)ls2(cur , command.get(1), true);
                else if(command.get(0).equals("exit"))return;
                else System.out.println(command.get(0)+": command not found or missing parameter");
            }
        }
    }
}
