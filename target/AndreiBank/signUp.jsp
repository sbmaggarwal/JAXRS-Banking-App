<html>
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

    <title>Register</title>
</head>
<body>

<div class="container">
    <div class="row">
        <div class="col s12 m6 offset-m3 cyan lighten-5">
            <div class="card z-depth-5">
                <div class="card-content">
                    <span class="card-title black-text">Sign Up</span>
                    <form id="signUpForm" action="api/users/register" method="post">
                        <div class="row">
                            <div class="input-field col s12">
                                <i class="material-icons prefix">email</i>
                                <input id="email" type="email" class="validate" name="email">
                                <label for="email" class="active">Email</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col s12">
                                <i class="material-icons prefix">email</i>
                                <input id="name" type="text" class="validate" name="name">
                                <label for="name" class="active">Name</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col s12">
                                <i class="material-icons prefix">email</i>
                                <input id="address" type="text" class="validate" name="address">
                                <label for="address" class="active">Address</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col s12">
                                <i class="material-icons prefix">person_pin</i>
                                <input id="password" type="password" class="validate" name="password">
                                <label for="password" class="active">Password</label>
                            </div>
                        </div>
                        <div class="card-action">
                            <input type="submit" class="btn" value="Sign Up">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
