<?php

require_once __DIR__.'/../../vendor/autoload.php';

use Kreait\Firebase\Exception\MessagingException;
use Kreait\Firebase\Factory;
use Kreait\Firebase\Messaging\CloudMessage;

$factory = (new Factory)->withServiceAccount(
    __DIR__.'/../../config/googleServiceAccount.json'
);
$firebaseAuth = $factory->createAuth();
$firebaseMessaging = $factory->createMessaging();

function sendTopicFirebaseMessage($topic, $data)
{
    $messaging = $GLOBALS['firebaseMessaging'];

    if ($topic == null) {
        return null;
    }

    $message = CloudMessage::withTarget('topic', $topic)
        ->withData($data);
        
    return $messaging->send($message);
}

function sendUserFirebaseMessage($userId, $data, $connection) {
    $stmt = mysqli_prepare(
        $connection,
        'SELECT token FROM FirebaseTokens WHERE user_id = ?'
    );
    mysqli_stmt_bind_param($stmt, 'i', $userId);
    mysqli_stmt_execute($stmt);

    $result = $stmt->get_result();

    $tokens = [];
    while (($row = mysqli_fetch_row($result)) != null) {
        $tokens[] = $row[0];
    }

    if (count($tokens) == 0) {
        return null;
    }

    $messaging = $GLOBALS['firebaseMessaging'];
    $message = CloudMessage::new()
        ->withData($data);
    return $messaging->sendMulticast($message, $tokens);
}
