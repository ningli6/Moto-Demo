<!DOCTYPE HTML> 
<html lang="en-US">
<head>
  <title>Moto Demo</title>
  <meta charset="UTF-8">
  <meta name="author" content="Ning Li">
  <meta name="description" 
        content="Demo of countermeasures for protecting Primary User privacy in spectrum sharing">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- Latest compiled and minified CSS -->
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <!-- jQuery library -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <!-- Latest compiled JavaScript -->
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body>

<?php
/* add external helper php script */
require 'script.php';
require 'PHPMailer/PHPMailerAutoload.php';
/* start a php session */
session_start();
/* nuber of channels */
$number_of_channels;
/* nuber of queries */
$number_of_queries;
/* error message */
$channelErr = $queryErr = $command = "";
/* email message */
$receiver = $emailResult = $message = "";
/* handle form submit */
if ($_SERVER["REQUEST_METHOD"] == "POST") {
  /* if submitted element is address, try to send email */
  if (isset($_POST["addr"])) {
    if (empty($_POST["addr"])) {
      $emailResult = "Please enter your email address";
    }
    elseif (strlen($_SESSION['message']) == 0) {
       $emailResult = "Please start demo first";
     }
    else {
     $receiver = $_POST["addr"];
     $emailResult = sendTo($receiver, $_SESSION['message']);
     session_unset();
   }
 }
  else {
    /* otherwise, try to start demo */
    $output = startDemo($number_of_channels, $number_of_queries, $channelErr, $queryErr, $command);
  }
}
?>

<div class="container">
  <div class="jumbotron">
    <h1>Moto demo</h1>      
    <p>This is a demo for several techniques for protecting the Primary Users’ operational privacy in spectrum sharing 
       proposed in the paper: 
    </p>
    <p><cite>Protecting the Primary Users’ Operational Privacy in Spectrum Sharing. 
        [2014 IEEE International Symposium on Dynamic Spectrum Access Networks (DYSPAN), p 236-47, 2014]
    </cite></p>
  </div>
  <form role="form" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
    <div class="form-group">
      <label>Number of channels:</label>
      <input type="number" class="form-control" name="channels" placeholder="Enter number of channels">
      <p class="error"><?php 
        if ($channelErr != "")
          $str = "<div class='alert alert-danger'>" . $channelErr . "</div>";
          echo $str; ?>
      </p>
    </div>
    <div class="form-group">
      <label>Number of queries:</label>        
      <input type="number" class="form-control" name="queries" placeholder="Enter number of queries">
      <p class="error"><?php 
        if ($queryErr != "")
          $str = "<div class='alert alert-danger'>" . $queryErr . "</div>";
          echo $str; ?>
      </p>
    </div>
    <button type="submit" class="btn btn-default">Start demo</button>
  </form>

  <?php
    if ($output == "Program is unable to start!") {
      $alert = "<script type='text/javascript'>window.alert('Fail to start demo')</script>";
      echo $alert;
    }
    else if ($output != "") {
      echo "<h2>Your Input Parameters:</h2>";
      echo "<h4>Number of channels:</h4>";
      echo $number_of_channels;
      echo "<h4>Number of queries:</h4>";
      echo $number_of_queries;
      echo "<h2>Execute:</h2>";
      echo $command;
      echo "<h2>Output:</h2>";
      // print out standard output of java program
      printArray($output, $message);
      /* use session to store message to keep values between pages */
      $_SESSION['message'] = $message;
    }
  ?>
  <br>
  <form role="form" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
    <div class="form-group">
      <label for="email">Email:</label>
      <input type="email" class="form-control" name="addr" placeholder="Enter email">
    </div>
    <button type="submit" class="btn btn-default">Send me Email <span class="glyphicon glyphicon-envelope"></span></button>
    <p><?php 
      if ($emailResult == "") {}
      else if ($emailResult != "Message sent!") {
        $emailstr = "<div class='alert alert-warning'>" . $emailResult . "</div>";
      }
      else {
        $emailstr = "<div class='alert alert-success'>" . $emailResult . "</div>";
      }
      echo $emailstr;
    ?></p>
  </form>
</div>

</body>
</html>