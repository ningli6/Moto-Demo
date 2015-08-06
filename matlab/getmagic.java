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
         /*
         Usage:
         probPlot(nc, rows, cols, latStart, latEnd, lngStart, lngEnd)
         @Param nc        [Number of channels]
         @Param rows      [Number of rows for analysis area]
         @Param cols      [Number of cols for analysis area]
         @Param latStart  [top bound]
         @Param latEnd    [bot bound]
         @Param lngStart  [left bound]
         @Param lngEnd    [right bound]
         */
         theMagic.probPlot(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
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

