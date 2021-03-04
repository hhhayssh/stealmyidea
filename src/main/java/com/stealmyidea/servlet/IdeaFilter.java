package com.stealmyidea.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class IdeaFilter implements Filter, Serializable {

	private static final long serialVersionUID = 2021_02_13_11_36_00L;
	
	private static final Logger log = Logger.getLogger(IdeaFilter.class);
	
	protected static final String IDEA_GET_REGEX = "/idea/(\\d+)";
	protected static final Pattern IDEA_GET_PATTERN = Pattern.compile(IDEA_GET_REGEX);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getRequestURI().substring(req.getContextPath().length());

		log.info("Path = " + path);
		Matcher matcher = IDEA_GET_PATTERN.matcher(path);
		if (matcher.matches()) {
			String ideaNumber = matcher.group(1);
			request.setAttribute("ideaNumber", ideaNumber);
			request.getRequestDispatcher("/idea-static.html").include(request, response);
			return;
		}
		
		chain.doFilter(request, response);
		
//		if (path.startsWith("/static")) {
//		     // Goes to default servlet.
//		} else {
//		    request.getRequestDispatcher("/pages" + path).forward(request, response);
//		}
		
	}

	@Override
	public void destroy() {
		
	}

}
