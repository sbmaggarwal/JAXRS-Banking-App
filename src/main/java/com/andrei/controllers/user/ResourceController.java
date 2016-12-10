package com.andrei.controllers.user;

import com.andrei.model.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

/**
 * Created by shubham on 10/12/16.
 */
@Path("/json/resources")
public class ResourceController {

    @Path("/balance/user/{userid}/account/{account}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalance(@PathParam("userid") String userid,
                               @PathParam("account") String account) throws SQLException, JsonProcessingException {

        ApiResponse response = new ApiResponse();
        response.setUserId(userid);
        response.setMessage("Balance is " + UserService.getInstance().getBalance(userid, account));
        return Response.ok(new ObjectMapper()
                .writeValueAsString(response)).build();
    }

    @Path("/lodgement/user/{userid}/account/{account}/amount/{amount}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response lodgement(@PathParam("userid") String userId,
                              @PathParam("amount") String amount,
                              @PathParam("account") String account) throws SQLException, JsonProcessingException {

        ApiResponse response = new ApiResponse();
        response.setUserId(userId);
        response.setMessage(UserService.getInstance().lodgement(userId, account, amount));
        return Response.ok(new ObjectMapper()
                .writeValueAsString(response)).build();
    }

    @Path("/transfer/user/{userid}/fromAccount/{fromAccount}/toAccount/{toAccount}/amount/{amount}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(@PathParam("userid") String userid,
                               @PathParam("fromAccount") String fromAccount,
                               @PathParam("toAccount") String toAccount,
                               @PathParam("amount") String amount) throws SQLException, JsonProcessingException {

        ApiResponse response = new ApiResponse();
        response.setUserId(userid);
        response.setMessage(UserService.getInstance().transfer(fromAccount, toAccount, amount));
        return Response.ok(new ObjectMapper()
                .writeValueAsString(response)).build();
    }

    @Path("/withdrawal/user/{userid}/account/{account}/amount/{amount}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdrawal(@PathParam("userid") String userid,
                               @PathParam("account") String account,
                               @PathParam("amount") String amount) throws SQLException, JsonProcessingException {

        ApiResponse response = new ApiResponse();
        response.setUserId(userid);
        response.setMessage(UserService.getInstance().withdrawMoney(account, amount));
        return Response.ok(new ObjectMapper()
                .writeValueAsString(response)).build();
    }
}
