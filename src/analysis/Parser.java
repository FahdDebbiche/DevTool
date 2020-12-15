package analysis;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;


import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Parser {
	
	String srcFolder = "";
	ArrayList<File> allfiles = new ArrayList<>();
	String fileName="";
	JSONObject finalJson = new JSONObject();
	
	int diagramElements;
	public ArrayList<File> allClasses = new ArrayList<>();
	private static FileWriter file;
	public static HashMap<CompilationUnit, variableRepresenter > fieldsOfClasses = new HashMap<>();
	public static HashMap<CompilationUnit, javaMethodRepresenter > classAndMethods = new HashMap<>();
	public static HashMap<CompilationUnit, ImportedClasses> importedClasses = new HashMap<>();
	public static ArrayList<CompilationUnit>  complilationUnits = new ArrayList<>();
	public static ArrayList <JavaFileRepresenter> processedClasses = new ArrayList<>();
	public static HashMap<JavaFileRepresenter, List<String> > coupledClasses  = new HashMap<>();



	public Parser(String srcFolder, int diagramElements ) {
		this.srcFolder = srcFolder;
		this.diagramElements= diagramElements;
	}
	
	public void setJsonSkeleton()  {
		
	}
	
	// Method to parse java classes to json objects 
	
		public void javaToJSonFile () throws IOException {
			JSONObject headings; 
			JSONObject linkedclasses; 


			        getJavaSourceFiles(this.srcFolder, allClasses);
			        getFilesLengthSorted(allClasses);
					
					for (File javafile : allClasses) {

						CompilationUnit javaClass = StaticJavaParser.parse(javafile);
						
						complilationUnits.add(javaClass);
						NodeList<TypeDeclaration<?>> declarationTypes = javaClass.getTypes();
						 
						for (TypeDeclaration<?> b : declarationTypes) {						
						if ( b instanceof ClassOrInterfaceDeclaration)  {
							//ClassOrInterfaceDeclaration classOnly =  (ClassOrInterfaceDeclaration) declarationTypes.get(0); 
							processSyntax(javaClass);	
						    JavaFileRepresenter unitToJavaFile  = new JavaFileRepresenter(javaClass, classAndMethods.get(javaClass), 
							   fieldsOfClasses.get(javaClass), importedClasses.get(javaClass) );
						    processedClasses.add(unitToJavaFile);
						}	
						}	
									
					}	
					
					 List<JavaFileRepresenter> methodBasedSort  = processedClasses.stream()
                             .sorted(Comparator.comparing(JavaFileRepresenter::listOfMethods))
                             .collect(Collectors.toList());

					 for (JavaFileRepresenter lookForConnectedClasses :  methodBasedSort )  {
						 coupledClasses.put(lookForConnectedClasses,analyzeClassCoupling(lookForConnectedClasses));
					 }
						if ( this.diagramElements > methodBasedSort.size() ) { 
				        	this.diagramElements = processedClasses.size();
				        }

					    JSONArray classAndVariables = new JSONArray();
						JSONArray connectedClasses = new JSONArray();
						
					for ( JavaFileRepresenter baseFiles : methodBasedSort ) {
						headings= new JSONObject();
						linkedclasses = new JSONObject();
						JSONObject fields = new JSONObject();
						JSONObject classes = new JSONObject();

						for (String field :  baseFiles.variables.getVariables()) {
							fields.put("",field);
							
						}
						headings.put("name : " + baseFiles.getFileName(), " Fields : " + fields);
						classAndVariables.put(headings);
						
						for (String connection : coupledClasses.get(baseFiles) ) {
							classes.put("",connection);
							
					}
						linkedclasses.put("name : " + baseFiles.getFileName(), " ConnectedTo : " + classes);
						connectedClasses.put(linkedclasses);
						
						this.diagramElements --;
						if (this.diagramElements < 1  ) {
							break;
						}
					}
					finalJson.put("Classes", classAndVariables);
					finalJson.put("Connections", connectedClasses);

					/**
					 * 
					 * Wrap the json arrays and objects to a file.
					 **/
					 try {
				         file = new FileWriter("/Users/fahd/Desktop/json");
				         file.write(finalJson.toString());
				         //file.close();
					      System.out.println("JSON file created: "+finalJson.toString());			

				      } catch (IOException e) {
				         // TODO Auto-generated catch block
				         e.printStackTrace();
				      } finally  {
				    	  
				    	  try {
				    		  file.flush();
				    		  file.close();//
				    		  
				    	  }catch (IOException e) {
						         // TODO Auto-generated catch block
						         e.printStackTrace();
						      } 

				      }
				      }
				
		public static void processSyntax (CompilationUnit c1 )   {
			
			VoidVisitorAdapter<Void> methodeVisitor = new javaMethodRepresenter();
			 methodeVisitor.visit(c1, null);
			 classAndMethods.put(c1, (javaMethodRepresenter) methodeVisitor);
			 
			 ModifierVisitor<?> fieldvisitor = new variableRepresenter();
			 fieldvisitor.visit(c1, null).toString();	
			 fieldsOfClasses.put(c1, (variableRepresenter) fieldvisitor);	
			 
			 VoidVisitorAdapter<?> importVisitor = new ImportedClasses();
		     importVisitor.visit(c1, null);
		     importedClasses.put(c1, (ImportedClasses) importVisitor);	

	 }		

			public void getJavaSourceFiles(String path, ArrayList<File> allclasses ) throws FileNotFoundException {

			    File[] srcDirectory = new File(path).listFiles();
	            
				if (srcDirectory != null) {
				for (File file : srcDirectory) {

				if (file.isDirectory()) {
					getJavaSourceFiles(file.getAbsolutePath().toString(),allclasses);
			    } else {
			    	//String [] extension =  file.getName().split(".");
			    	
				    if (file.getName().endsWith(".java") &&  !(file.getName().startsWith("module-info"))) {
				    	allClasses.add(file);

				    } 
				    }		
		         continue;
			    }
				
				}
			}

			public ArrayList<File> getFilesLengthSorted(ArrayList<File> listeOfFiles) throws IOException {
				
				HashMap<File, Integer> filelength = new HashMap<File, Integer>();
				List<Map.Entry<File, Integer>> list = new LinkedList<Map.Entry<File, Integer>>(filelength.entrySet());  
				ArrayList<File> sortedFiled = new ArrayList<>();


				 for (File javafile : listeOfFiles) {
			            System.out.println("this is heen sorted  " + javafile.toString());
			//	BufferedReader reader = new BufferedReader(new FileReader(this.srcFolder + "/" + javafile.getName()));
			            BufferedReader reader = new BufferedReader ( new InputStreamReader(new FileInputStream(javafile)));
				int lines = 0;
				try {
					while (reader.readLine() != null) lines++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				filelength.put(javafile, lines);
				reader.close();
			}
				//sorting the list elements  
				 Collections.sort(list , new Comparator<Map.Entry<File, Integer> >() { 
			            public int compare(Map.Entry<File, Integer> file1,  
			                               Map.Entry<File, Integer> file2) 
			            { 
			                return (file1.getValue()).compareTo(file1.getValue()); 
			            } 
			        }); 
			          
			        // put data from sorted list to hashmap  
			        for (Map.Entry<File, Integer> file : list) { 
			        	filelength.put(file.getKey(), file.getValue()); 
			        	sortedFiled.add(file.getKey());
			        } 
			        return sortedFiled; 
			    } 
			
			public List<String> analyzeClassCoupling (JavaFileRepresenter c1) {
				
				List<String> associations = new ArrayList<String>();
				List<TypeDeclaration<?>> Ac = c1.compUnit.getTypes();
		        ClassOrInterfaceDeclaration Acclass = (ClassOrInterfaceDeclaration) Ac.get(0);
		        
					for (int i =0 ; i < processedClasses.size() ; i++)  { 

						List<TypeDeclaration<?>> Oc = processedClasses.get(i).compUnit.getTypes();
						ClassOrInterfaceDeclaration ci = (ClassOrInterfaceDeclaration) Oc.get(0);
			            System.out.println("class checked is " + ci.getNameAsString());
			            
						if ( !(ci.getNameAsString().equals(Acclass.getNameAsString())) ) {
							
							 for (String matchingName : processedClasses.get(i).variables.getVariables() ) {
							 
						 if (matchingName.equals(Acclass.getNameAsString()))  {
							 associations.add(ci.getNameAsString());
					            System.out.println("class added " + ci.getNameAsString());

							 break;
						 }
						}	
							 for (String matchingName : processedClasses.get(i).importedClasses.getImportedclassesList() ) {
								 
								 if (matchingName.equals(Acclass.getNameAsString()))  {
									 associations.add(ci.getNameAsString());
									 System.out.println("class added is " + ci.getNameAsString());

									 break;
								 }
								}	 
					}
					}	
					
				return associations; 
			}	
			
			public String getClasseName (File singleJavaFile) {
				
			    String currentLine;
			    Object [] classNameLine;
				try {
					
				    BufferedReader fileLines = new BufferedReader(new FileReader(singleJavaFile));
			       // Stream<String> codelines= Files.lines(singleJavaFile.toPath());

			        while((currentLine = fileLines.readLine()) != null) {
			        	//str = currentLine.replaceAll("\\s+", " "); 
			        	classNameLine= currentLine.split(" ");
			        	for ( int i=0 ; i < classNameLine.length ; i++ ) {
			        		
			        		if (classNameLine[i].toString()== "class" ) {
			        			this.fileName= classNameLine[i+1].toString();
			        		}
			        	}
			        }
			        fileLines.close(); 

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				return this.fileName;
				}		
			
			
}
