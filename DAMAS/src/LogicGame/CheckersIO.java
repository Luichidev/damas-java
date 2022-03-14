package LogicGame;

import java.io.*;
import java.util.ArrayList;

public class CheckersIO {
	private BufferedReader in;
	private BufferedWriter out;
	private final String NL = System.getProperty("line.separator");
	
	
	public void setInputFile(String inputFile) throws FileNotFoundException {
		in = new BufferedReader(new FileReader(inputFile));
	}
	
	
	public void setOutputFile(String outputFile) throws IOException{
		out = new BufferedWriter(new FileWriter(outputFile));
	}
	
	public ArrayList<String> read() throws IOException{
		ArrayList<String> lines = new ArrayList<String>();
		String strCurrentLine;
		
		while ((strCurrentLine = in.readLine ()) != null) {    
			lines.add(strCurrentLine);
		}
		
		in.close();
		
		return lines;
	}
	
	public void write(ArrayList<String> lines) throws IOException {	
		for (String move : lines) {
			out.write(move + NL);
		}
		
		out.close();
	}
}
