package analysis;

import java.io.File;
import java.io.FileNotFoundException;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class JavaFileRepresenter  {

	public CompilationUnit compUnit; 
	public ImportedClasses  importedClasses;
	public variableRepresenter variables ;
	public javaMethodRepresenter MethodRepresenter;
	
	
	public JavaFileRepresenter (CompilationUnit compUnit,javaMethodRepresenter MethodRepresenter, variableRepresenter variables,
			ImportedClasses  importedClasses )  {
		
	this.compUnit= compUnit;
    this.importedClasses= importedClasses;
	this.variables= variables;
	this.MethodRepresenter= MethodRepresenter;
	}
		
	public String getFileName() throws FileNotFoundException {
		
		String filename = null;
		NodeList<TypeDeclaration<?>> declarationTypes = this.compUnit.getTypes();
		for (TypeDeclaration<?> b : declarationTypes) {						
		if ( b instanceof ClassOrInterfaceDeclaration)  {

			filename= b.getNameAsString();
		}
		}
		return filename;
	}
	
    public JavaClassElement getType() {
        return JavaClassElement.Class;
    }

	
}


