package com.andrei.controllers.user;

import com.andrei.database.Constants;
import com.andrei.model.User;

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

    @Path("/login")
    @POST
    public void login(@Context HttpServletRequest request,
                      @Context HttpServletResponse response,
                      @FormParam("email") String email,
                      @FormParam("password") String password)
            throws SQLException, ServletException, IOException {

        User user = UserService.getInstance().checkLogin(email, password);

        if (user == null) {
            response.sendRedirect(request.getServletContext().getContextPath() + "/invalidUser.html");
        } else {

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        }
    }

    @Path("/register")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public void register(@Context HttpServletRequest request,
                             @Context HttpServletResponse response,
                             @FormParam("email") String email,
                             @FormParam("name") String name,
                             @FormParam("address") String address,
                             @FormParam("password") String password)
            throws SQLException, ServletException, IOException {

        request.getSession().setAttribute("user", UserService.getInstance().register(email, name, address, password));
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    @Path("/dashboardPage")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public void dashboardPage(@Context HttpServletRequest request,
                                  @Context HttpServletResponse response)
            throws IOException, ServletException {

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    @Path("/transactionPage")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public void transactionPage(@Context HttpServletRequest request,
                                    @Context HttpServletResponse response)
            throws IOException, ServletException {

        request.getRequestDispatcher("/transaction.jsp").forward(request, response);
    }

    @Path("/logout")
    @GET
    public void logout(@Context HttpServletRequest request,
                       @Context HttpServletResponse response)
            throws IOException, ServletException {

        if (request.getSession().getAttribute("user") != null) {
            request.getSession().invalidate();
            response.sendRedirect(request.getServletContext().getContextPath() + "/login.jsp");
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

    @Path("/lodgement")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public void lodgement(@Context HttpServletRequest request,
                              @Context HttpServletResponse response)
            throws IOException, ServletException {

        request.getRequestDispatcher("/lodgement.jsp").forward(request, response);
    }

    @Path("/withdrawal")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public void withdrawal(@Context HttpServletRequest request,
                               @Context HttpServletResponse response)
            throws IOException, ServletException {

        request.getRequestDispatcher("/withdrawal.jsp").forward(request, response);
    }

    @Path("/addMoney/account/{toAccount}/amount/{amount}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response addMoney(@PathParam("toAccount") String toAccount,
                             @PathParam("amount") String amount) throws SQLException {

        return Response.status(200).entity(
                UserService.getInstance().addMoney(toAccount, amount)).build();
    }

    @Path("/withdrawMoney/account/{toAccount}/amount/{amount}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response withdrawMoney(@PathParam("toAccount") String toAccount,
                             @PathParam("amount") String amount) throws SQLException {

        return Response.status(200).entity(
                UserService.getInstance().withdrawMoney(toAccount, amount)).build();
    }
}
