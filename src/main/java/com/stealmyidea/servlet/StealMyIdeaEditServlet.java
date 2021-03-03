package com.stealmyidea.servlet;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

public class StealMyIdeaEditServlet extends AbstractStealMyIdeaServlet {
	
	private static final long serialVersionUID = 2021_02_13_11_36_00L;

	private static final Logger log = Logger.getLogger(StealMyIdeaEditServlet.class);
	
	public void init() throws ServletException {
		log.info("Initializing edit servlet...");
		super.init();
		log.info("Done initializing edit servlet.");
    }
}
