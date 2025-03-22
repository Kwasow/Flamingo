<?php

require_once __DIR__.'/../../config/config.php';
require_once __DIR__.'/../../src/entities/wish.php';
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
$id = $postData['id'];
$content = $postData['content'];
$done = intval($postData['done']);
$userId = $user->getId();
$partnerId = $user->getPartner()->getId();

// Update wish in database
$stmt = mysqli_prepare(
    $conn,
    'UPDATE Wishlist
    SET content = ?, done = ?
    WHERE id = ? AND (author = ? OR author = ?)'
);
mysqli_stmt_bind_param(
    $stmt, 'siiii',
    $content, $done, $id, $userId, $partnerId
);
mysqli_stmt_execute($stmt);
$stmt->close();

mysqli_close($conn);
exit();
