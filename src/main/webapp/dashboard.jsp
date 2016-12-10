<%@ page import="com.andrei.controllers.user.UserService" %>
<%@ page import="com.andrei.model.Account" %>
<%@ page import="com.andrei.model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <head>
        <meta charset='utf-8'>
        <meta http-equiv="X-UA-Compatible" content="chrome=1">

        <!--Let browser know website is optimized for mobile-->
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

        <!-- Compiled and minified CSS -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/css/materialize.min.css">

        <!--Import jQuery before materialize.js-->
        <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>

        <!-- Compiled and minified JavaScript -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/js/materialize.min.js"></script>

        <!--Import Google Icon Font-->
        <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

        <title>Dashboard</title>
    </head>
</head>
<body>
<div id="nav-placeholder"></div>

<div class="container">
    <div class="row">
        <div class="col s12 m6 offset-m3 cyan lighten-5">
            <div class="card z-depth-5">
                <div class="card-content">
                    <span class="card-title black-text">Select Account Type</span>
                    <div class="container input-field col s12">
                        <select id="accountType" name="accountType">
                            <option value="Savings" selected>Savings</option>
                            <option value="Current">Current</option>
                        </select>
                    </div>
                    <input type="button" onclick="submitForm()" class="btn" value="Add New Account">
                    <label id="responseMessage"></label>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <div class="row">
        <div class="col s12 cyan lighten-5">
            <div class="card z-depth-5">
                <table class="centered" id="accountTable">
                    <thead>
                    <tr>
                        <th data-field="name">Account Number</th>
                        <th data-field="price">Type</th>
                        <th data-field="message">Balance (USD)</th>
                    </tr>
                    </thead>

                    <tbody>

                        <% List<Account> accounts = UserService.getInstance().
                                getUserAccounts(((User) request.getSession().getAttribute("user")).getId());
                            for(Account account : accounts) {
                        %>
                        <tr>
                            <td><%= account.getAccountNumber() %></td>
                            <td><%= account.getAccountType() %></td>
                            <td><%= account.getBalance() %></td>
                        </tr>
                        <%}%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

</body>

<script>

    $(document).ready(function () {
        $('select').material_select();
    });

    function submitForm() {

        var data = document.getElementById("accountType").value;
        console.log("submitForm : " + data);
        var url = './newAccount/user/1/type/' + data;
        console.log(url);
        $.ajax({
            type: "POST",
            url: url,
            success: function (result) {
                console.log(result);

                document.getElementById("responseMessage").innerHTML = result;
            }
        });
    }


</script>
<script> $("#nav-placeholder").load('http://localhost:8080/BankApp/navigation.jsp'); </script>
</html>
