<?php

require_once __DIR__.'/../../src/helpers/authorization.php';
require_once __DIR__.'/../../src/helpers/database.php';
require_once __DIR__.'/../../src/helpers/firebase.php';

use Kreait\Firebase\Exception\MessagingException;
use Kreait\Firebase\Messaging\CloudMessage;

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
$type = $postData['type'];

// Send message
$partnerId = null;
$data = null;

switch ($type) {
    case 'missing_you':
        $partnerId = $user->getPartner()->getId();
        $data = [
            'type' => 'missing_you',
            'name' => $user->getFirstName()
        ];
        break;
    case 'request_location':
        $partnerId = $user->getPartner()->getId();
        $data = [
        'type' => 'request_location'
        ];
        break;
    default:
        break;
}

try {
    $result = sendUserFirebaseMessage($partnerId, $data, $conn);

    if ($result == null) {
        http_response_code(400);
        error_log("[ERROR] Error handling message type: " . $type);
    }
} catch (Exception $e) {
    http_response_code(500);
    error_log("[ERROR] Error handling message type: " . $type);
    error_log("[EXCEPTION]: " . $e->getMessage());
}

mysqli_close($conn);
exit();
