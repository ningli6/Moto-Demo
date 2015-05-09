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
// define variables and set to empty values
$number_of_channels;
$number_of_queries;
$channelErr = $queryErr = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  if (empty($_POST["channels"])) {
    $channelErr = "Number of channels is required";
  } else {
   $number_of_channels = $_POST["channels"];
    if ($number_of_channels < 1) {
      $channelErr = "Number of channels must be greater than 0"; 
    }
  }

  if (empty($_POST["queries"])) {
    $queryErr = "Number of queries is required";
  } else {
   $number_of_queries = $_POST["queries"];
   if ($number_of_queries < 1) {
      $queryErr = "Number of queries must be greater than 0"; 
   }
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
?>

</body>
</html>