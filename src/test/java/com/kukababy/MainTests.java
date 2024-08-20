package com.kukababy;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kukababy.utils.MD5;

public class MainTests {
	private static final Logger log = LoggerFactory.getLogger(MainTests.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void test02() throws IOException, ParseException {
		//Pattern PCharDigitReg = Pattern.compile("[0-9]|[a-z]|[A-Z]|-|_");
		Pattern p = Pattern.compile("^[a-zA-Z0-9-]{6,20}$");
		Matcher m = p.matcher("qqqq_");

		log.info("" + m.matches());

	}

	@Test
	public void test01() throws IOException, ParseException {

		String unsafe = "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
		String safe = Jsoup.clean(unsafe, Whitelist.basic());
		String txt = Jsoup.parse(unsafe).text();
		log.info(safe);
		log.info(txt);
		log.info(MD5.convert("abc123"));
		// now: <p><a href="http://example.com/" rel="nofollow">Link</a></p>
	}

}
