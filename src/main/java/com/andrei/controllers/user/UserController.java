package com.andrei.controllers.user;

import com.andrei.database.Constants;
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
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by shubham on 02/12/16.
 */
@Path("/users")
public class UserController {

    private final Logger logger = LogManager.getLogger("UserController");

    @Path("/login")
    @POST
    public void login(@Context HttpServletRequest request,
                      @Context HttpServletResponse response,
                      @FormParam("email") String email,
                      @FormParam("password") String password)
            throws SQLException, ServletException, IOException {

        User user = UserService.getInstance().checkLogin(email, password);

        if (user == null) {

            String invalidUser = request.getServletContext().getContextPath() + "/invalidUser.html";
            response.sendRedirect(invalidUser);
        } else {

            HttpSession session = request.getSession();

            String dashboard = "/dashboard.jsp";
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

        User user = UserService.getInstance().register(email, name, address, password);

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
        return new Viewable(dashboard, null);
    }

    @Path("/transactionPage")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable transactionPage(@Context HttpServletRequest request,
                                    @Context HttpServletResponse response)
            throws IOException, ServletException {

        String transactionPage = "/transaction.jsp";
        return new Viewable(transactionPage, null);
    }

    @Path("/logout")
    @GET
    public void logout(@Context HttpServletRequest request,
                       @Context HttpServletResponse response)
            throws IOException, ServletException {

        String loginPage = request.getServletContext().getContextPath() + "/login.jsp";

        if (request.getSession().getAttribute("user") != null) {
            request.getSession().invalidate();
            response.sendRedirect(loginPage);
            return;
        }
    }

    @Path("/newAccount/user/{id}/type/{type}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response addNewAccount(@PathParam("id") String id,
                                @PathParam("type") String type) {

        UserService.getInstance().addNewAccount(id, type);
        return Response.status(200).entity(Constants.ACCOUNT_ADDED).build();
    }

    @Path("/transaction/fromAccount/{fromAccount}/toAccount/{toAccount}/amount/{amount}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response makeTransaction(@PathParam("fromAccount") String fromAccount,
                                    @PathParam("toAccount") String toAccount,
                                    @PathParam("amount") String amount) throws SQLException {

        return Response.status(200).entity(
                UserService.getInstance().makeTransaction(fromAccount, toAccount, amount)).build();
    }
}
