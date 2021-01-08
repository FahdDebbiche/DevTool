package analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class javaParser {
	
	public static void main(String[] args) throws IOException {
		
		
				try {
					Path p1 = Paths.get("/Users/fahd/Desktop/xDrip-master/app/src/main/java/com/eveningoutpost/dexdrip/cgm");

						//if (args.length == 2) { 
						// Get input
						//String sourceFolder = args[0]; 
			        System.out.println("enter the number of files you want to include :");
			        
				//	try(Scanner studentInput = new Scanner(System.in)){
					    //rest of your code
						Integer includedClasses = 10;

					    if(includedClasses <100 && includedClasses >1) {
							Parser parser = new Parser(p1.toString(), includedClasses.intValue());
							parser.javaToJSonFile();
					    } else  {
					        System.out.println("invalid input :");

					    }
				//	}
					
				//	} else {
						//System.out.println("Please provide input in correct format - <sourcefolder> <output file name>");
				//	}
				} catch (Exception e) {
					System.out.println("Incorrect Input format");
				}
			}

	
	public String getDirectory( String path) {
		return path;
	
	}

}


