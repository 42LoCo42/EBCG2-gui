package ebcg2gui;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;

public class FileIO {
	public static String readString(File file) {
		try {
			return new String(Files.readAllBytes(file.toPath()));
		}
		catch(Exception ex) {ex.printStackTrace();}
		return null;
	}
	
	public static void writeString(File file, String text) {
		try {
			PrintWriter out = new PrintWriter(file);
			out.write(text);
			out.flush();
			out.close();
		}
		catch(Exception ex) {ex.printStackTrace();}
	}
	
	public static String readInputStream(InputStream in) {
		Scanner s = new Scanner(in);
		s.useDelimiter("\\A");
		String data = s.hasNext() ? s.next() : null;
		s.close();
		return data;
	}
	
	public static String readFromClasspath(String fileName) {
		return readInputStream(FileIO.class.getResourceAsStream(fileName));
	}
}