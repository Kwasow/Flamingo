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
$userId = $user->getId;

$stmt = mysqli_prepare(
    $conn,
    'SELECT id FROM FirebaseTokens WHERE token = ?'
);
mysqli_stmt_bind_param($stmt, 's', $token);
mysqli_stmt_execute($stmt);

$result = $stmt->get_result();

// Update if exists, else add
if (($row = mysqli_fetch_row($result)) != null) {
    $stmt = mysqli_prepare(
        $conn,
        'UPDATE FirebaseTokens SET time_stamp = ? WHERE id = ?'
    );
    mysqli_stmt_bind_param($stmt, 'ii', time(), $row[0]);
    mysqli_stmt_execute($stmt);
} else {
    $stmt = mysqli_prepare(
        $conn,
        'INSERT INTO FirebaseTokens VALUES(NULL, ?, ?, ?, ?)'
    );
    mysqli_stmt_bind_param($stmt, 'iisi', $userId, time(), $token, $debug);
    mysqli_stmt_execute($stmt);
}

mysqli_close($conn);
exit();
