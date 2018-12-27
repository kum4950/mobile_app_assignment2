<?php
    $con = mysqli_connect('localhost', 'root', 'rma135790', 'member_db');

     $userName = $_POST["userName"];
     $userPassword = $_POST["userPassword"];

     $statement = mysqli_prepare($con, "SELECT * FROM USER WHERE userName = ? AND userPassword = ?");
     mysqli_stmt_bind_param($statement, "ss", $userName, $userPassword);
     mysqli_stmt_execute($statement);
     mysqli_stmt_store_result($statement);
     mysqli_stmt_bind_result($statement, $userName, $userPassword, $userEmail);

     $response = array();
     $response["success"] = false;

     while(mysqli_stmt_fetch($statement)){
      $response["success"] = true;
      $response["userName"] = $userName;
      $response["userPassword"] = $userPassword;
      $response["userEmail"] = $userEmail;
     }

     echo json_encode($response);
?>
