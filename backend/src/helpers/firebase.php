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
