/**
 * 
 */
package com.kukababy.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * <b>描述:Xss攻击处理</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2018年4月19日 下午7:23:11
 */
public class XssWraper extends HttpServletRequestWrapper {
	//private static final Logger log = LoggerFactory.getLogger(XssWraper.class);
	boolean isUpData = false;// 判断是否是上传 上传忽略

	public XssWraper(HttpServletRequest servletRequest) {
		super(servletRequest);
		String contentType = servletRequest.getContentType();
		if (null != contentType)
			isUpData = contentType.startsWith("multipart");
	}

	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = cleanXSS(values[i]);
		}
		return encodedValues;
	}

	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		return cleanXSS(value);
	}

	/**
	 * 获取request的属性时，做xss过滤
	 */
	@Override
	public Object getAttribute(String name) {
		Object value = super.getAttribute(name);
		if (null != value && value instanceof String) {
			value = cleanXSS((String) value);
		}
		return value;
	}

	@Override
	public String getHeader(String name) {

		String value = super.getHeader(name);
		if (value == null)
			return null;
		return cleanXSS(value);
	}

	private static String cleanXSS(String value) {
		//value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("%3C", "&lt;").replaceAll("%3E", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("%28", "&#40;").replaceAll("%29", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("script", "");
		return value;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (isUpData) {
			return super.getInputStream();
		} else {
			final ByteArrayInputStream bais = new ByteArrayInputStream(inputHandlers(super.getInputStream()).getBytes());
			return new ServletInputStream() {
				@Override
				public int read() throws IOException {
					return bais.read();
				}

				@Override
				public boolean isFinished() {
					return false;
				}

				@Override
				public boolean isReady() {
					return false;
				}

				@Override
				public void setReadListener(ReadListener readListener) {
				}
			};
		}

	}

	private String inputHandlers(ServletInputStream sis) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(sis, Charset.forName("UTF-8")));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			if (sis != null) {
				sis.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
		return cleanXSS(sb.toString());
	}
}
