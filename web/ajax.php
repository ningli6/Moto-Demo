<?php
	session_start();
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $args = $_REQUEST['args'];
        if (empty($args)) {
            echo "Args is empty";
        } else {
            echo $args;
        }
        $_SESSION['args'] = $args;
    }
?>