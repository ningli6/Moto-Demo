import com.mathworks.toolbox.javabuilder.*;
import MatPlot.*;

class getmagic
{
   public static void main(String[] args)
   {
      Class1 theMagic = null;

      try
      {
         theMagic = new Class1();

         theMagic.runPlot();
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

