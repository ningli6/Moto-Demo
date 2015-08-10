<?php
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $args = $_REQUEST['args'];
        if (empty($args)) die("Empty argument!");
        /* start java program */
        $command = "java -jar C:\Users\Administrator\Desktop\motoDemo\launch.jar " . $args;
        // return to previous page without waiting
        pclose(popen("start /B ". $command, "r"));
        echo "OK";
    }
?>