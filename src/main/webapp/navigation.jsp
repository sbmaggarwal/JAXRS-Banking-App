<%@ page import="com.andrei.model.User" %>
<nav>
    <div class="nav-wrapper">
        <a class="brand-logo">Hello, <%= ((User) request.getSession().getAttribute("user")).getName() %></a>
        <ul id="nav-mobile" class="right hide-on-med-and-down">
            <li><a href="lodgement">Lodgement</a></li>
            <li><a href="dashboardPage">Accounts</a></li>
            <li><a href="transactionPage">Transactions</a></li>
            <li><a href="withdrawal">Withdrawal</a></li>
            <li><a href="logout">Logout</a></li>
        </ul>
    </div>
</nav>