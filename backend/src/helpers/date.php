<?php

function findRelationshipYear($stringDate, $anniversaryDate)
{
    $year = intval(substr($stringDate, 0, 4));
    $month = intval(substr($stringDate, 5, 2));
    $day = intval(substr($stringDate, 8, 2));

    $anniversaryMonth = intval(substr($anniversaryDate, 5, 2));
    $anniversaryDay = intval(substr($anniversaryDate, 8, 2));

    if ($month <= $anniversaryMonth && $day < $anniversaryDay) {
        return $year - 1;
    }

    return $year;
}
