package com.andrei.controllers.user;

import com.andrei.model.User;
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

    @Path("/all")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {

        return "Yayyyy";
    }

    @Path("/login")
    @POST
    public void login(@Context HttpServletRequest request,
                      @Context HttpServletResponse response,
                      @FormParam("email") String email,
                      @FormParam("password") String password)
            throws SQLException, ServletException, IOException {

        HttpSession session = request.getSession(false);

        service = UserService.getInstance();

        logger.warn("Email : {} and password : {}", email, password);

        User user = service.checkLogin(email, password);

        if (user == null) {

            String invalidUser = request.getServletContext().getContextPath() + "/invalidUser.html";
            response.sendRedirect(invalidUser);
        } else {

            String dashboard = "/dashboard.jsp";
            logger.warn("login dashboard : {}", dashboard);
            logger.warn("user login name : {}", user.getName());
            request.getSession().setAttribute("user", user);
            request.getRequestDispatcher(dashboard).forward(request, response);
        }
    }

    @Path("/register")
    @POST
    public void register(@Context HttpServletRequest request,
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
        request.getRequestDispatcher(dashboard).forward(request, response);
    }

}
