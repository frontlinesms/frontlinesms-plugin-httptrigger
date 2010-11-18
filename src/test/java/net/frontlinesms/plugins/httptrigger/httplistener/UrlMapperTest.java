package net.frontlinesms.plugins.httptrigger.httplistener;

import net.frontlinesms.plugins.httptrigger.httplistener.UrlMapper;
import junit.framework.TestCase;

public class UrlMapperTest extends TestCase {
	UrlMapper mapper;
	
//> SETUP
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		mapper = UrlMapper.create("", "sm", "sms", "sms/send", "annoying");
	}
	
//> TEST METHODS
	public void testMapToPath(){
		testMappedPaths("","");
		testMappedPaths("",null);
		testMappedPaths("","something");
		testMappedPaths("sms/send","sms/send");
		testMappedPaths("sms/send","sms/send/to/boh");
		testMappedPaths("sm","sm/?stuff=4");
		testMappedPaths("sms","sms/?stuff=4");
		testMappedPaths("","smss/?stuff=4");
		testMappedPaths("sms","sms/?stuff=4&url=annoying");
		testMappedPaths("sms","sms/?stuff=4&url=sms/send");
		testMappedPaths("sms","sms/?stuff=4&url=/sms/send");
		testMappedPaths("annoying","annoying");
		testMappedPaths("sms","sms/ annoying");
	}
	
	public void testTwoMappers() {
		UrlMapper two = UrlMapper.create("map_two");
		UrlMapper one = UrlMapper.create("map_one");
		assertEquals("", one.mapToPath(""));
		assertEquals("", two.mapToPath(""));
		assertEquals("map_one", one.mapToPath("map_one"));
		assertEquals("map_two", two.mapToPath("map_two"));
	}

	private void testMappedPaths(String expectedPath, String url) {
		String actualPath = mapper.mapToPath(url);
		assertEquals(url + " returned the wrong path", expectedPath, actualPath);
	}
}
