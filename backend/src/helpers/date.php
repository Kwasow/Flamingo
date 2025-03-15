<?php

require_once __DIR__.'/../../config/config.php';

function findRelationshipYear($stringDate)
{
    $year = intval(substr($stringDate, 0, 4));
    $month = intval(substr($stringDate, 5, 2));
    $day = intval(substr($stringDate, 8, 2));

    $config = getFlamingoConfig();
    $anniversaryMonth = $config['anniversaryMonth'];
    $anniversaryDay = $config['anniversaryDay'];

    if ($month <= $anniversaryMonth && $day < $anniversaryDay) {
        return $year - 1;
    }

    return $year;
}
