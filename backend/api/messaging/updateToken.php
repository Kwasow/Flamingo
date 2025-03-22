<?php

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
}

// Get request details
$postData = json_decode(file_get_contents('php://input'), true);
$token = $postData['token'];
$debug = $postData['debug'];

$id = $user->getId();

// Add token to database
$stmt = mysqli_prepare(
    $conn,
    'INSERT INTO FirebaseTokens VALUES(NULL, ?, ?, ?, ?)'
);
mysqli_stmt_bind_param(
    $stmt,
    'iisi',
    $id,
    time(),
    $token,
    $debug
);
mysqli_stmt_execute($stmt);

mysqli_close($conn);
exit();
