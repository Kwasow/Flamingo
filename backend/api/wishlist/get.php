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

// Get wishlist from database and return as JSON
header('Content-Type: application/json; charset=utf-8');

$userId = $user->getId();
$partnerId = $user->getPartner()->getId();

$stmt = mysqli_prepare(
    $conn,
    'SELECT * FROM Wishlist WHERE author = ? OR author = ?'
);
mysqli_stmt_bind_param($stmt, 'ii', $userId, $partnerId);
mysqli_stmt_execute($stmt);
$result = $stmt->get_result();
$stmt->close();

$wishlist = [];
while ($row = mysqli_fetch_assoc($result)) {
    $wishlist[] = new Wish(
        $row['id'],
        $row['author'],
        $row['content'],
        boolval($row['done']),
        $row['time_stamp']
    );
}

echo json_encode($wishlist);

mysqli_close($conn);
exit();
