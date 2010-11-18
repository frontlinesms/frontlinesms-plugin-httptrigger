package net.frontlinesms.plugins.httptrigger.httplistener;

import java.io.File;

/**
 * Class that decides which script to use according to a specific URL
 * 
 * @author Gonçalo Silva
 */
public class ScriptFinder {
	
// > ISTANCE METHODS
	/**
	 * Determines which script to call with the passed URI
	 * 
	 * @param The URI path for the required script
	 * @return A {@link File} instance with the script's path
	 * @throws {@link IllegalArgumentException} if the path starts with a '/'
	 */
	public File mapToFile(String path) {

		if(path == null || path.equals("")){
			return new File("index.groovy");
		}
		
		if(path.charAt(0) == '/'){
			throw new IllegalArgumentException();
		}

		while(path.endsWith("/")){
			path = path.substring(0, path.length() - 1);
		}
		
		path += ".groovy";
		
		return new File(path);
	}
}
