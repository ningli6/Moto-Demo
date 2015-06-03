<?php
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $args = $_REQUEST['args'];
        if (empty($args)) die("Empty argument!");
        /* start java program */
        $command = "java -cp Project boot/Boot " . $args;
        /* indicate if program has started successfully */
        $output = ""; 
        exec($command, $output);
        if ($output[0] != "OK") {
            die("Demo failed to start! ");
        }
        /* email message */
        $receiver = "ningli@vt.edu";
        if ($receiver == "") die("Empty email address");
        $emailResult = "";
        $emailResult = sendTo($receiver, $command);
        if ($emailResult != "OK") {
            die("Sending email failed. " . $emailResult);
        }
        echo "OK";
    }
?>