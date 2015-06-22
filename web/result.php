<?php
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $args = $_REQUEST['args'];
        if (empty($args)) die("Empty argument!");
        /* start java program */
        $command = "java -cp C:\Users\Administrator\Desktop\motoDemo boot/Boot " . $args;
        // return to previous page without waiting
        pclose(popen("start /B ". $command, "r"));
        echo "OK";
    }
?>