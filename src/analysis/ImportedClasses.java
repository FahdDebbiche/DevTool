package analysis;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.ImportDeclaration;

public class ImportedClasses extends VoidVisitorAdapter<Void> {
	
	List <String> collector = new ArrayList<>();
	
	 public void visit(ImportDeclaration imports, Void arg) {
		 super.visit(imports, arg); 
		    String[] importstatement = new String[10]; 
		    importstatement =  imports.getNameAsString().split("\\.");
		    collector.add(importstatement[importstatement.length-1]);
	        // System.out.println("imports are  " + importstatement[importstatement.length-1]);
		    }
	 
	    public List<String> getImportedclassesList() {
	        return this.collector;
	    }

	 
	
}