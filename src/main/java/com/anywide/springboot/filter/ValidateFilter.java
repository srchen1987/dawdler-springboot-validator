package com.anywide.springboot.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.anywide.dawdler.clientplug.web.validator.RuleOperatorProvider;
import com.anywide.dawdler.clientplug.web.validator.operators.RuleOperator;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * Servlet Filter implementation class ValidateFilter
 */
public class ValidateFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public ValidateFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hrequest = (HttpServletRequest) request;
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String type = hrequest.getHeader("Content-Type");
		boolean isJson = type != null && type.contains("application/json");
		if (isJson) {
			ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(hrequest);
			chain.doFilter(requestWrapper, response);
		} else {
			chain.doFilter(request, response);
		}

	}

	public void init(FilterConfig fConfig) throws ServletException {
		RuleOperatorProvider.registerRuleOperatorScanPackage(RuleOperator.class);
	}

	public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

		private String body;

		public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
			super(request);
			body = getBodyString(request);
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(getInputStream()));
		}

		public String getBody() {
			return body;
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8")));
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

	public static String getBodyString(ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			inputStream = request.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();
	}
}
