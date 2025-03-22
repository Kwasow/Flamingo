<?php

require_once __DIR__.'/../../config/config.php';
require_once __DIR__.'/../../src/helpers/authorization.php';
require_once __DIR__.'/../../src/helpers/database.php';

// Open database connection
$conn = openConnection();

// Check if user is authorized
$user = checkAuthorization($conn);
if ($user !== null) {
    http_response_code(200);
} else {
    http_response_code(401);

    mysqli_close($conn);
    exit();
}

// Get request details
$postData = json_decode(file_get_contents('php://input'), true);
$startDate = $postData['startDate'];
$endDate = $postData['endDate'];
$title = $postData['title'];
$description = $postData['description'];
$photo = $postData['photo'];
$coupleId = $user->getCoupleId();

// Add wish to database
$stmt = mysqli_prepare(
    $conn,
    'INSERT INTO Memories VALUES(NULL, ?, ?, ?, ?, ?, ?)'
);
mysqli_stmt_bind_param(
    $stmt,
    'sssssi',
    $startDate,
    $endDate,
    $title,
    $description,
    $photo,
    $coupleId
);
mysqli_stmt_execute($stmt);
$stmt->close();

mysqli_close($conn);
exit();
