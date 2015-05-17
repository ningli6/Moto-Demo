<!DOCTYPE HTML> 
<html lang="en-US">
<head>
    <title>Moto Demo</title>
    <meta charset="UTF-8">
    <meta name="author" content="Ning Li">
    <meta name="results" content="resutls of demo page">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">
    <?php
        require 'script.php';
        require 'PHPMailer/PHPMailerAutoload.php';
        session_start();
        $number_of_channels = $_SESSION['NUMBER_OF_CHANNELS'];
        $number_of_queries = $_SESSION['NUMBER_OF_QUERIES'];
        $output = $_SESSION['OUTPUT'];
        echo "<h3>Your Input Parameters:</h3>";
        echo "<h4>Number of channels:</h4>";
        echo $_SESSION['NUMBER_OF_CHANNELS'];
        echo "<h4>Number of queries:</h4>";
        echo $number_of_queries;
        echo "<h3>Program output:</h3>";
        printArray($output, $message);
        /* email message */
        $receiver = $emailResult = "";
        /* handle form submit */
        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            /* if submitted element is address, try to send email */
            if (isset($_POST["addr"])) {
                if (empty($_POST["addr"])) {
                    $emailResult = "Please enter your email address";
                }
                else {
                    $receiver = $_POST["addr"];
                    $emailResult = sendTo($receiver, $message);
                }
            }
        }
    ?>
    <h3>Send me email of this result</h3>
    <form role="form" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" class="form-control" name="addr" placeholder="Enter email">
        </div>
        <button type="submit" class="btn btn-default">Send <span class="glyphicon glyphicon-envelope"></span></button>
        <p>
            <?php 
                if ($emailResult == "") {}
                else if ($emailResult != "Message sent!") {
                    $emailstr = "<div class='alert alert-warning'>" . $emailResult . "</div>";
                }
                else {
                    $emailstr = "<div class='alert alert-success'>" . $emailResult . "</div>";
                }
                echo $emailstr;
            ?>
        </p>
    </form>
</div>
</body>

<?php

?>

</html>