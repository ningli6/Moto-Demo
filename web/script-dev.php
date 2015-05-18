<?php
/* 
 * helper function for displaying java program stdout 
 * and store it in message for email to use later 
 */
function printArray($a, &$message) {
    foreach($a as $x => $x_value) {
      echo $x_value;
      echo "<br>";
      $message .= ($x_value .= "<br>");
  }
}

/*
 * helper function to start java program 
 * but first need to decide if params are passed correctly
 */
function startDemo($args) {
  // $output = "";
  //   if (empty($_POST["queries"])) {
  //     $queryErr = "Number of queries is required";
  //   } else {
  //    $number_of_queries = $_POST["queries"];
  //    if ($number_of_queries < 1) {
  //       $queryErr = "Number of queries must be positive"; 
  //    }
  //   }
  //   if ($number_of_queries > 0) {
  //     // $command = "java -cp Project tests/Main %s %s";
  //     $command = "java -cp Project tests/Main " + $args;
  //     // $command = sprintf($command, strval($number_of_channels), strval($number_of_queries));
  //     exec($command, $output);
  //   }
  //   else {
  //     return "Program is unable to start!";
  //   }
  //   return $output;
    $output = "";
    $command = "java -cp Project tests/Main " + $args;
    // $command = sprintf($command, strval($number_of_channels), strval($number_of_queries));
    exec($command, $output);
    if ($output == "") return "Program is unable to start!";
    return $output;
}

/* helper funtion for parsing an email */
function sendTo($recv, $message) {
  //Create a new PHPMailer instance
  $mail = new PHPMailer;
  // Set PHPMailer to use the sendmail transport
  $mail->isSendmail();
  //Set who the message is to be sent from
  $mail->setFrom('ningli@ec2-52-24-22-108.us-west-2.compute.amazonaws.com', 'Ning Li');
  //Set who the message is to be sent to
  $mail->addAddress('ningli@vt.edu', 'Ning Li');
  //Set the subject line
  $mail->Subject = 'PHPMailer sendmail test';
  //Read an HTML message body from an external file, convert referenced images to embedded,
  //convert HTML into a basic plain-text alternative body
  $body = ('DO NOT REPLY' . "<br><br>");
  $mail->Body = ($body .= $message);
  //Replace the plain text body with one created manually
  $mail->AltBody = 'This is a plain-text message body';
  $mail->addAttachment('/var/www/html/kc.png', 'kc.png');    // Optional name

  //send the message, check for errors
  if (!$mail->send()) {
      return "Mailer Error: " . $mail->ErrorInfo;
  } else {
      return "Message sent!";
  }
}
?>