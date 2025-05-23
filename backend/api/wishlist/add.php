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
$authorId = $postData['authorId'];
$content = $postData['content'];
$done = intval(false);
$timestamp = strval($postData['timestamp']);

// Check if authorId is allowed
if ($authorId != $user->getId() && $authorId != $user->getPartner()->getId()) {
    http_response_code(403);

    mysqli_close($conn);
    exit();
}

// Add wish to database
$stmt = mysqli_prepare(
    $conn,
    'INSERT INTO Wishlist VALUES(NULL, ?, ?, ?, ?)'
);
mysqli_stmt_bind_param($stmt, 'isis', $authorId, $content, $done, $timestamp);
mysqli_stmt_execute($stmt);
$stmt->close();

mysqli_close($conn);
exit();
