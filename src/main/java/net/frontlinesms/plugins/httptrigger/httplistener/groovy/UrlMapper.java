package net.frontlinesms.plugins.httptrigger.httplistener.groovy;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Class that maps URLs to specified paths given at instantiation time
 * 
 * @author Goncalo Silva
 */
public class UrlMapper {
// > PROPERTIES
	private final String[] paths;

// > CONSTRUCTORS	
	private UrlMapper(String[] paths){
		this.paths = paths;
	}

// > ISTANCE METHODS
	/**
	 * Maps a String URL according to the pre-specified paths
	 * 
	 * @param The URL that needs to be mapped
	 * @return A String with the mapped path 
	 */
	public String mapToPath(String url) {
		if (url == null){
			url = "";
		}
		
		for (String s : paths) {
			if(url.equals(s)){
				return s;
			}
			
			if (url.startsWith(s + "/")) {
				return s;
			}
		}
		
		return "";
	}

// > FACTORY METHODS
	/**
	 * Factory method that will instantiate UrlMapper with the specified URL paths
	 * 
	 * @param The possible URL paths that will be mapped with this UrlMapper - this array may be modified
	 * @return A new Instance of UrlMapper
	 */
	public static UrlMapper create(String... paths) {
		Arrays.sort(paths, new Comparator<String>(){
			public int compare(String o1, String o2) {
				return o2.length() - o1.length();
			}
		});
		
		return new UrlMapper(paths);
	}
}
