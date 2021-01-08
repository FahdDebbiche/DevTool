package tests;


import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import analysis.Parser;


public class AllTests {
	
	private Parser parser; 
	private Path p1 = Paths.get("/Users/fahd/Desktop/xDrip-master/app/src/main/java/com/eveningoutpost/dexdrip/cgm");

		
	@Test 
	public void unitInputDomainTest ()  {
		parser = new Parser (p1.toString(), 5);
		Assert.assertEquals(parser.getprocessedClassesSize().intValue(), 5 );
    }
	
	@Test 
	public void unitProcessedClass () throws FileNotFoundException  {
		parser = new Parser (p1.toString(), 5);
		Assert.assertEquals( parser.returnprocessedFiles().intValue(), 47);
    }

	
	//@Test 
	//public void unitProcessedmethods ()  {
	//	parser = new Parser (p1.toString(), 2);
	//	ArrayList<File> allclasses;
	//	Assert.assertEquals( allclasses.size(), 	"automata");

   // }


	
	
}
