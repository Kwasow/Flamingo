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
    if ($topic == null) {
        return null;
    }

    $message = CloudMessage::withTarget('topic', $data)
        ->withData($data);
    
    return $firebaseMessaging->send($message);
}
