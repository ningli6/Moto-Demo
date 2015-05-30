<!-- This page start java program and send out email -->
<?php
    require 'PHPMailer/PHPMailerAutoload.php';
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $args = $_REQUEST['args'];
        if (empty($args)) die("Empty argument!");
        /* start java program */
        $command = "java -cp Project tests/Main " . $args;
        /* indicate if program has started successfully */
        $output = "OK";
        // exec($command, $output);
        if ($output != "OK") die("Program failed to start!");
        /* email message */
        $receiver = $_REQUEST['Email'];
        if ($receiver == "") die("Empty email address");
        $emailResult = "";
        $emailResult = sendTo($receiver, $command);
        if ($emailResult == "OK") {
            echo "OK";
        }
        else {
            die("Sending email failed. " . $emailResult);
        }
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
            return "OK";
        }
    }
?>