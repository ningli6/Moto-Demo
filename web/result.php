<?php
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $args = $_REQUEST['args'];
        if (empty($args)) die("Empty argument!");
        /* start java program */
        $command = "java -cp C:\Users\Administrator\Desktop\motoDemo boot/Boot " . $args;
        /* indicate if program has started successfully */
        $output = ""; 
        exec($command, $output);
        if ($output[0] == "NOT IMPLEMENTED") {
            die("Sorry. This countermeasure has not been implemented yet! ");
        }
        if ($output[0] != "OK") {
            die("Demo failed to start! ");
        }
        echo "OK";
    }
?>