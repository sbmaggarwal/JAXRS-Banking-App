package com.andrei.controllers.user;

import com.andrei.model.User;
import com.sun.jersey.api.view.Viewable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by shubham on 02/12/16.
 */
@Path("/users")
public class UserController {

    private final Logger logger = LogManager.getLogger("UserController");

    private UserService service;

    @Path("/login")
    @POST
    public void login(@Context HttpServletRequest request,
                      @Context HttpServletResponse response,
                      @FormParam("email") String email,
                      @FormParam("password") String password)
            throws SQLException, ServletException, IOException {

        service = UserService.getInstance();

        logger.warn("Email : {} and password : {}", email, password);

        User user = service.checkLogin(email, password);

        if (user == null) {

            String invalidUser = request.getServletContext().getContextPath() + "/invalidUser.html";
            response.sendRedirect(invalidUser);
        } else {

            HttpSession session = request.getSession();

            String dashboard = "/dashboard.jsp";
            logger.warn("login dashboard : {}", dashboard);
            session.setAttribute("user", user);
            request.getRequestDispatcher(dashboard).forward(request, response);
        }
    }

    @Path("/register")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public Viewable register(@Context HttpServletRequest request,
                             @Context HttpServletResponse response,
                             @FormParam("email") String email,
                             @FormParam("name") String name,
                             @FormParam("address") String address,
                             @FormParam("password") String password)
            throws SQLException, ServletException, IOException {

        service = UserService.getInstance();

        logger.warn("Email : {} and password : {}", email, password);

        User user = service.register(email, name, address, password);

        String dashboard = "/dashboard.jsp";
        request.getSession().setAttribute("user", user);
        return new Viewable(dashboard, null);
    }

    @Path("/dashboardPage")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable dashboardPage(@Context HttpServletRequest request,
                                  @Context HttpServletResponse response)
            throws IOException, ServletException {

        String dashboard = "/dashboard.jsp";
        logger.warn("dashboardPage : {}", dashboard);
        return new Viewable(dashboard, null);
    }

    @Path("/transactionPage")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable transactionPage(@Context HttpServletRequest request,
                                    @Context HttpServletResponse response)
            throws IOException, ServletException {

        String transactionPage = "/transaction.jsp";
        logger.warn("transactionPage : {}", transactionPage);
        return new Viewable(transactionPage, null);
    }

    @Path("/logout")
    @GET
    public void logout(@Context HttpServletRequest request,
                           @Context HttpServletResponse response)
            throws IOException, ServletException {

        String loginPage = request.getServletContext().getContextPath() + "/login.jsp";
        logger.warn("login : {}", loginPage);

        if (request.getSession().getAttribute("user") != null) {
            request.getSession().invalidate();
            response.sendRedirect(loginPage);
            return;
        }
    }

}
