package analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class javaParser {
	
	public static void main(String[] args) throws IOException {
		
		//String sourceFolder = "./../../SoftwareEvolution/src/main/java" ;
		
				//try {
					Path p1 = Paths.get("/Users/fahd/eclipse-workspace/SoftwareEvolution/group7/src/main/java/org/jabref/model");

				//		if (args.length == 2) { 
						// Get input
						//String sourceFolder = args[0]; 
						//String curDir = System.getProperty(sourceFolder);  // get the current files in the source folder
					Parser parser = new Parser(p1.toString());
					parser.javaToJSonFile();
					
				//	} else {
						//System.out.println("Please provide input in correct format - <sourcefolder> <output file name>");
				//	}
			//	} catch (Exception e) {
			//		System.out.println("Incorrect Input format");
			//	}
			}

	
	public String getDirectory( String path) {
		return path;
	
	}

}

