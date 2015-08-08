package javaEmail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BuildText {
	/**
	 * Put the content of the string into a text file of given path
	 * @param path         file path
	 * @param fileName     text file name
	 * @param content      string that need to be printed
	 */
	public static void printText(String path, String fileName, String content) {
		if (content == null || content.length() == 0) return;
		System.out.println("Start writing file...");
		File file = new File(path + fileName);
		try {
			PrintWriter out = new PrintWriter(file);
			out.println(content.replaceAll("\n", System.getProperty("line.separator")));
			out.close (); // this is necessary
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		String directory = "C:\\Users\\Administrator\\Desktop\\motoData\\";
		String name = "test.txt";
		String content = "test\nnew line";
		BuildText.printText(directory, name, content);
	}
}
