package analysis;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.ModifierVisitor;


public class variableRepresenter extends  ModifierVisitor<Void> {
	
	List <String > fields = new ArrayList<>();
	 
	@Override
	  public FieldDeclaration visit(FieldDeclaration variables, Void arg) {
          super.visit(variables, arg);
          variables.getVariables().forEach(v ->
                  v.getInitializer().ifPresent(i -> {
                	  fields.add(v.getType().toString());
                  }));

			Object node  = variables.getChildNodes().get(0);
			if ( node instanceof VariableDeclarator ) {
				
				fields.add( ((VariableDeclarator) node).getType().toString() + ":" + ((VariableDeclarator) node).getName().toString());
			}
             return variables;

  } 
    public JavaClassElement getType() {
        return JavaClassElement.Variables;
    }
    
    public List<String> getVariables() {
        return this.fields;
    }
    

}
