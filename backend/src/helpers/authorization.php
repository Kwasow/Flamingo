<?php

require_once __DIR__.'/../../src/entities/user.php';
require_once __DIR__.'/../../src/helpers/firebase.php';

use Kreait\Firebase\Exception\Auth\FailedToVerifyToken;

function checkAuthorization($connection)
{
    $token = getBearerToken();

    if ($token === null) {
        return null;
    }

    // Check firebase
    $verifiedIdToken = null;
    try {
        $verifiedIdToken = $GLOBALS['firebaseAuth']->verifyIdToken($token);
    } catch (FailedToVerifyToken $e) {
        return null;
    }

    // Check database
    $email = $verifiedIdToken->claims()->get('email');
    $query =
        'SELECT
            this.*,
            other.id AS other_id,
            other.first_name AS other_name,
            other.email AS other_email,
            other.icon AS other_icon
        FROM Users this
        LEFT JOIN Users other ON this.couple_id = other.couple_id AND this.id != other.id
        WHERE this.email = ?';
    $stmt = mysqli_prepare($connection, $query);
    mysqli_stmt_bind_param($stmt, 's', $email);
    mysqli_stmt_execute($stmt);

    $result = $stmt->get_result();
    $stmt->close();

    if (mysqli_num_rows($result) != 1) {
        return null;
    }

    $user = mysqli_fetch_assoc($result);

    $partner = null;
    if ($user['other_id'] != null) {
        $partner = new Partner(
            $user['other_id'],
            $user['other_name'],
            $user['other_icon']
        );
    }

    return new User(
        $user['id'],
        $user['first_name'],
        $user['email'],
        $user['icon'],
        $partner
    );
}

function getAuthorizationHeader()
{
    $headers = getallheaders();
  
    return $headers['Authorization'];
}

function getBearerToken()
{
    $header = getAuthorizationHeader();

    if (!empty($header)) {
        if (preg_match('/Bearer\s(\S+)/', $header, $matches)) {
            return $matches[1];
        }
    }

    return null;
}
