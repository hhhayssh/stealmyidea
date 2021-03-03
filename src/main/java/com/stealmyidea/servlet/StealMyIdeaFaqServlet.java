package com.stealmyidea.servlet;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

public class StealMyIdeaFaqServlet extends AbstractStealMyIdeaServlet {
	
	private static final long serialVersionUID = 2021_02_13_11_36_00L;
	
	private static final Logger log = Logger.getLogger(StealMyIdeaFaqServlet.class);
	
	public void init() throws ServletException {
		log.info("Initializing faq servlet...");
		super.init();
		log.info("Done initializing faq servlet.");
    }

}
