package utility;

import java.io.*;
 
public class JavaRunCommand {

    public static boolean sendEmail(String recv, String message) {
        String s = null;
        try {
            System.out.println(message);
            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("python /var/www/html/Project/python/send.py " + recv + " " + message);
             
            BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));
 
            BufferedReader stdError = new BufferedReader(new
                 InputStreamReader(p.getErrorStream()));
 
            // read the output from the command
            // System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
             
            // read any errors from the attempted command
            // System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            return true;
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            return false;
        }
    }
 
    public static void main(String args[]) {
        JavaRunCommand.sendEmail("ningli@vt.edu", "Life\nis\ngood");
    }
}