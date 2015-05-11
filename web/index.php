<!DOCTYPE HTML> 
<html lang="en-US">
<head>
  <title>Moto Demo</title>
  <meta charset="UTF-8">
  <meta name="author" content="Ning Li">
  <meta name="description" 
        content="Demo of countermeasures for protecting Primary User privacy in spectrum sharing">
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

<h2>PHP Form Example</h2>
<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
   Number of channels: <input type="number" name="channels">
   <span class="error">* <?php echo $channelErr; ?></span>
   <br><br>
   Number of queries: <input type="number" name="queries">
   <span class="error">* <?php echo $queryErr; ?></span>
   <br><br>
   <input type="submit" name="submit" value="Submit"> 
</form>

<?php
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
?>

<!-- handler email request -->
<h2>Email:</h2>
<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
Email Address: <input type="text" name="addr">
<input type="submit" value="Send me Email">
<span ><?php echo "<br>" . $emailResult; ?></span>
</form>

</body>
</html>