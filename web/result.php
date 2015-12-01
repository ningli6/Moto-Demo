<?php
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $args = $_REQUEST['args'];
        if (empty($args)) die("Empty argument!");
        /* start java program */
        $command = "java -jar C:\Users\Administrator\Desktop\motoDemo\launch.jar " . $args;
        pclose(popen("start " . $command, "r"));
        echo "OK";
    }
?>