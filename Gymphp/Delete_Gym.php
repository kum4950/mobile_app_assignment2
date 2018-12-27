<?php
    $con = mysqli_connect('localhost', 'root', 'rma135790', 'member_db');
     $userName = $_POST["userName"];
     $statement = mysqli_prepare($con, "DELETE FROM USER WHERE userName = ?");
     mysqli_stmt_bind_param($statement, "s", $userName);
     mysqli_stmt_execute($statement);
     #배열 선언 후
     $response = array();
     #success에 true라는 값을 넣어줌
     $response["success"] = true;
     echo json_encode($response);
?>
