<%@ page import="com.andrei.controllers.user.UserService" %>
<%@ page import="com.andrei.model.Transaction" %>
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

        <title>Lodgement</title>
    </head>
</head>
<body>
<div id="nav-placeholder"></div>

<div class="container">
    <div class="row">
        <div class="col s12 m6 offset-m3 cyan lighten-5">
            <div class="card z-depth-5">
                <div class="card-content">
                    <span class="card-title black-text">Add money to Account</span>
                    <div class="input-field">
                        <input id="toAccount" type="number" class="validate">
                        <label for="toAccount">Account number</label>
                    </div>
                    <div class="input-field">
                        <input id="amount" type="number" class="validate">
                        <label for="amount">Amount</label>
                    </div>
                    <input type="button" onclick="submitForm()" class="btn" value="Submit">
                    <label id="responseMessage"></label>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

<script>

    function submitForm() {

        var toAccount = document.getElementById("toAccount").value;
        var amount = document.getElementById("amount").value;

        console.log("toAccount : " + toAccount);
        console.log("amount : " + amount);

        var url = './addMoney/account/' + toAccount + '/amount/' + amount;
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

<!--Import jQuery before materialize.js-->
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>

<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.8/js/materialize.min.js"></script>

<script> $("#nav-placeholder").load('http://localhost:8080/BankApp/navigation.jsp'); </script>
</html>
