<?php
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $args = $_REQUEST['args'];
        if (empty($args)) die("Empty argument!");
        /* start java program */
        $command = "java -cp Project boot/Boot " . $args;
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
        // $op = $command . "<br>";
        // printArray($output, $op);
        // echo $op;
    }

    function printArray($a, &$message) {
        foreach($a as $x => $x_value) {
            // echo $x_value;
            // echo "<br>";
            $message .= ($x_value .= "<br>");
        }
    }
?>