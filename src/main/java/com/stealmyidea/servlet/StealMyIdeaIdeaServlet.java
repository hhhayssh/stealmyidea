package com.stealmyidea.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class StealMyIdeaIdeaServlet extends AbstractStealMyIdeaServlet {
	
	private static final long serialVersionUID = 2021_02_13_11_36_00L;
	
	private static final Logger log = Logger.getLogger(StealMyIdeaIdeaServlet.class);
	
	public void init() throws ServletException {
		log.info("Initializing main servlet...");
		super.init();
		log.info("Done initializing main servlet.");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Processing request... request = " + request.getRequestURL() + "?" + request.getQueryString());
		log.info("in idea servlet...");
	}
}
