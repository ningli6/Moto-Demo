package utility;

import java.io.*;
 
public class JavaRunCommand {

    public static boolean sendEmail(String recv, String message) {
        String s = null;
        try {
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("python C:\\Users\\Administrator\\Desktop\\motoDemo\\python\\send.py " + recv + " " + message);
             
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

    public static boolean generatePlot() {
        String s = null;
        try {
            // using the Runtime exec method:
            String cmd = "java -cp \"C:\\Users\\Administrator\\Desktop\\plotMap\";\"C:\\Program Files\\MATLAB\\MATLAB Compiler Runtime\\v83\\toolbox\\javabuilder\\jar\\win64\\javabuilder.jar\";\"C:\\Users\\Administrator\\Desktop\\plotMap\\MatPlot.jar\" getmagic";
            // Process p = Runtime.getRuntime().exec("python /var/www/html/Project/python/plotContour.py");
            Process p = Runtime.getRuntime().exec(cmd);
            int r = p.waitFor();

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
            return r == 0;
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            return false;
        }
        catch (InterruptedException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            return false;
        }
    }
}