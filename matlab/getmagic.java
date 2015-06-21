import com.mathworks.toolbox.javabuilder.*;
import MatPlot.*;

class getmagic
{
   public static void main(String[] args)
   {
      Class1 theMagic = null;

      if (args.length != 1) throw new IllegalArgumentException();

      try
      {
         theMagic = new Class1();

         theMagic.runPlot(Integer.valueOf(args[0]));
         System.out.println("Success!");
      }
      catch (Exception e)
      {
         System.out.println("Exception: " + e.toString());
      }
      finally
      {
         theMagic.dispose();
      }
   }
}

