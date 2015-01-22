package util.jrtl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClassWalker {
	
	private static final List<DynamicType> CLASSES;
	
	static {
		CLASSES = loadAllClasses();
	}
	
	public Object getFirstImplementing(final Generic type) {
		DynamicType _dynamicType;
		DynamicObject _dynamicObject;

		_dynamicType = ClassWalker.getFirst(new ClassFinder() {
			@Override public boolean matches(DynamicType c) {
//				c.getType(GenericType) - could we do with String description of type?
//				c.extendsClass(GenericType) - also --- could GenericType subsume non-generic types?
				// How about Type, and its subtypes?  Can any of these be used?
				return c.implementsInterface(type);
			}
		});
		_dynamicObject = _dynamicType.createInstance();
		return _dynamicObject.getRawObject();
	}
	
	public static void fillClassMap(String pkg, Map<ClassFinder,Collection<DynamicType>> map){
		
		for(DynamicType c : getClasses(pkg)){
			for(ClassFinder cf : map.keySet()){
				if(cf.matches(c)){
					map.get(cf).add(c);
				}
			}
		}	
	}
	
	public static void fillClassMap(Map<ClassFinder,Collection<DynamicType>> map){
		fillClassMap("", map);
	}

	public static List<DynamicType> getClasses(String pkg){
		List<DynamicType> list = new LinkedList<DynamicType>();
		
		for(DynamicType c : CLASSES){
			if(c.getType().getPackage()!=null && c.getType().getPackage().getName().startsWith(pkg))
				list.add(c);
		}
		
		return list;
	}
	
	public static List<DynamicType> findClass(ClassFinder cf){
		List<DynamicType> col = new LinkedList<DynamicType>();
		
		for(DynamicType c : CLASSES){
			if(cf.matches(c))
				col.add(c);
		}
		
		return col;
	}
	
	public static List<DynamicType> findClass(ClassFinder cf, String pkg){
		List<DynamicType> col = new LinkedList<DynamicType>();
		
		for(DynamicType c : CLASSES){
			if(cf.matches(c) && c.getPackage().startsWith(pkg))
				col.add(c);
		}
		
		return col;
	}
	
	public static Collection<DynamicType> reduce(Collection<DynamicType> start, ClassFinder cf){
		Collection<DynamicType> col = new HashSet<DynamicType>();
		
		for(DynamicType c : start){
			if(cf.matches(c))
				col.add(c);
		}
		
		return col;
	}
	
	public static DynamicType getFirst(ClassFinder cf){
		return getFirst(CLASSES,cf);
	}
	
	public static DynamicType getFirst(Collection<DynamicType> start, ClassFinder cf){
		for(DynamicType t : start){
			if(cf.matches(t))
				return t;
		}
		return DynamicType.NULL_TYPE;
	}

	private static List<DynamicType> loadAllClasses() {
		return loadClassesInPackage("");
	}
	
	private static List<DynamicType> loadClassesInPackage(String packageName) {
		List<DynamicType> classList = new LinkedList<DynamicType>();

		String packagePath = packageName.replace('.', '/');

		Enumeration<URL> packageURLs = null;
		try {
			packageURLs = ClassLoader.getSystemResources(packagePath);
		} catch (IOException e) {
			System.err.println("Error finding package: " + packageName);
			return Collections.emptyList();
		}

		while (packageURLs.hasMoreElements()) {
			URL packageURL = packageURLs.nextElement();
			File dir = null;

			try {
				dir = new File(packageURL.toURI());
			} catch (URISyntaxException e) {
				System.err.println("Could not load resource: " + packageURL);
				continue;
			}

			ArrayList<File> dirs = new ArrayList<File>();
			ArrayList<String> pkgs = new ArrayList<String>();
			dirs.add(dir);
			pkgs.add(packageName);

			for (int i = 0; i < dirs.size(); i++) {

				for (File f : dirs.get(i).listFiles()) {
					if (f.isDirectory()){
						dirs.add(f);
						String pkg = pkgs.get(i) + (pkgs.get(i) != ""?".":""); 
						pkgs.add(pkg + f.getName());
					}
				}
				
				// Blacklist eclipse classes, they are not all defined
				// as well, blacklist all the classes in this package
				if(!pkgs.get(i).startsWith("org.eclipse")
						&& !pkgs.get(i).equals(ClassWalker.class.getPackage().getName()))
					loadClassesInDir(pkgs.get(i), dirs.get(i), classList);

			}
		}

		return classList;
	}

	private static void loadClassesInDir(String pkg, File dir, List<DynamicType> list) {
		for (File f : dir.listFiles(new ClassFileFilter())) {
			String className = f.getName();
			try {

				className = f.getName().substring(0, f.getName().indexOf('.'));

				if (pkg != "") {
					className = pkg + "." + className;
				}

				Class<?> c = Class.forName(className, false, ClassLoader.getSystemClassLoader());
				list.add(DynamicType.getInstance(c));
			} catch (ClassNotFoundException e) {
				System.err.println("Trouble loading class: " + className);
			}
		}
	}

	private static class ClassFileFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".class");
		}
	}
}
