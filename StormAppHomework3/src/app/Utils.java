package app;

import java.io.FileWriter;
import java.io.IOException;

public class Utils {
	
	public void write(String line, String filename) {
		try {
			FileWriter fw = new FileWriter(filename, true);
			fw.write(line);
			fw.flush();
			fw.close();
			System.out.println("AlertLogBolt, successfully written bytes to the file");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
