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
require 'PHPMailer/PHPMailerAutoload.php';
// define variables and set to empty values
$number_of_channels;
$number_of_queries;
$channelErr = $queryErr = $command = "";
$output = "";
$receiver = $emailErr = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  if (isset($_POST["addr"])) {
    if (empty($_POST["addr"])) {
      $emailErr = "Please enter your email address";
    } else {
     $receiver = $_POST["addr"];
     sendTo($receiver);
   }
 }
  else {
    if (empty($_POST["channels"])) {
      $channelErr = "Number of channels is required";
    } else {
     $number_of_channels = $_POST["channels"];
      if ($number_of_channels < 1) {
        $channelErr = "Number of channels must be positive integer"; 
      }
    }
    if (empty($_POST["queries"])) {
      $queryErr = "Number of queries is required";
    } else {
     $number_of_queries = $_POST["queries"];
     if ($number_of_queries < 1) {
        $queryErr = "Number of queries must be positive integer"; 
     }
    }
    if ($number_of_channels > 0 && $number_of_channels > 0) {
      $command = "java -cp Project tests/Main %s %s";
      $command = sprintf($command, strval($number_of_channels), strval($number_of_queries));
      exec($command, $output);
    }
    else {
      echo "Program is unable to start!";
    }
  }
}

function sendTo($recv) {
  //Create a new PHPMailer instance
  $mail = new PHPMailer;
  $mail->isSMTP();             // Set mailer to use SMTP
  $mail->SMTPAuth = false;
  $mail->Port = 8000;          // TCP port to connect to
  // // Set PHPMailer to use the sendmail transport
  // $mail->isSendmail();
  //Set who the message is to be sent from
  $mail->setFrom('from@example.com', 'First Last');
  //Set an alternative reply-to address
  $mail->addReplyTo('replyto@example.com', 'First Last');
  //Set who the message is to be sent to
  $mail->addAddress('ningli@vt.edu', 'Ning Li');

  $mail->Subject = 'PHPMailer sendmail test';
  $mail->Body    = 'This is the HTML message body <b>in bold!</b>';
  $mail->AltBody = 'This is a plain-text message body';

  //send the message, check for errors
  if (!$mail->send()) {
      echo "Mailer Error: " . $mail->ErrorInfo;
  } else {
      echo "Message sent!";
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
printArray($output);

function printArray($a) {
    foreach($a as $x => $x_value) {
      echo $x_value;
      echo "<br>";
  }
}
?>

<h2>Email:</h2>
<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
Email Address: <input type="text" name="addr">
<input type="submit" value="Send me Email">
<span ><?php
  if ($receiver != "") {
    echo "<br>";
    echo "Successfully sent to " . $receiver;
  }
  else {
    echo "<br>";
    echo $emailErr;
  }
    // print phpinfo();  
 ?></span>
</form>

</body>
</html>