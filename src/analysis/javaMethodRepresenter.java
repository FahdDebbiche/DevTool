package analysis;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.MethodDeclaration;


public class javaMethodRepresenter extends VoidVisitorAdapter<Void> {
	
	List <String> collector = new ArrayList<>();
	String test;
	
	@Override
	 public void visit(MethodDeclaration method, Void arg) {
		 super.visit(method, arg); 
		// collector.add(method.getDeclarationAsString(false,false,false));
		 test= method.getName().toString(); 
		 collector.add(test);
	     }
	     
	    public JavaClassElement getType() {
	        return JavaClassElement.Method;
	    }
	    
	    public List <String> getMethods() {
	        return this.collector;
	    }

}
