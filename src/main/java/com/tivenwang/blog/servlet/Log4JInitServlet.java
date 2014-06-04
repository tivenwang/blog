/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivenwang.blog.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * @author Pecan 
 * 类说明：
 */
public class Log4JInitServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String ehcache_Path;
    /**
     * Initialize the properties of log4j.
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        String log4jLocation = config.getInitParameter("log4j-properties-location");

        ServletContext sc = config.getServletContext();

        if (log4jLocation == null) {
            System.err.println("*** No log4j-properties-location init param, "
                    + "so initializing log4j with BasicConfigurator.");
            BasicConfigurator.configure();
        } else {
            String webAppPath = sc.getRealPath("/");
            ehcache_Path = webAppPath;
            String log4jProp = webAppPath + log4jLocation;
            File log4jPropFile = new File(log4jProp);
            if (log4jPropFile.exists()) {
                System.out.println("Initializing log4j with:" + log4jProp);
                PropertyConfigurator.configure(log4jProp);
            } else {
                System.out.println("*** " + log4jProp + " file not found, "
                        + "so initializing log4j with Basic Configurator");
                BasicConfigurator.configure();
            }
        }
        super.init(config);
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Log4JInitServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Log4JInitServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
