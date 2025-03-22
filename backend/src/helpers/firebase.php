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
        $conn,
        'SELECT token FROM FirebaseTokens WHERE user_id = ?'
    );
    mysqli_stmt_bind_param($stmt, 'i', $userId);
    mysqli_stmt_execute($stmt);

    $tokens = $stmt->get_result();

    if ($tokens == null) {
        return null;
    }

    $messaging = $GLOBALS['firebaseMessaging'];

    $messages = [];
    foreach ($tokens as $token) {
        $message = CloudMessage::new()
            ->withData($data)
            ->toToken($token);
    }

    return $messaging->sendAll($messages);
}
