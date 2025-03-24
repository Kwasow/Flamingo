<?php

require_once __DIR__.'/../../src/entities/memory.php';
require_once __DIR__.'/../../src/helpers/authorization.php';
require_once __DIR__.'/../../src/helpers/database.php';
require_once __DIR__.'/../../src/helpers/date.php';

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

// Parse memories CSV to JSON
header('Content-Type: application/json; charset=utf-8');

$coupleId = $user->getCoupleDetails()->getId();
$anniversary = $user->getCoupleDetails()->getAnniversary();

$stmt = mysqli_prepare($conn, 'SELECT m.* FROM Memories m WHERE couple = ?');
mysqli_stmt_bind_param($stmt, 'i', $coupleId);
mysqli_stmt_execute($stmt);
$result = $stmt->get_result();
$stmt->close();

$memories = [];
while ($row = mysqli_fetch_assoc($result)) {
    $memory = new Memory(
        intval($row['id']),
        $row['start_date'],
        $row['end_date'],
        $row['title'],
        $row['memory_description'],
        $row['photo']
    );

    $year = findRelationshipYear($row['start_date'], $anniversary);
    if (array_key_exists($year, $memories)) {
        $memories[$year][] = $memory;
    } else {
        $memories[$year] = [$memory];
    }
}

echo json_encode($memories);

mysqli_close($conn);
exit();
