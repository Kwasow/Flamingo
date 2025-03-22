<?php

class UserLocation implements jsonSerializable
{
    private $userId;
    private $userName;
    private $latitude;
    private $longitude;
    private $accuracy;
    private $timestamp;

    public function __construct(
        $userId,
        $userName,
        $latitude,
        $longitude,
        $accuracy,
        $timestamp
    ) {
        $this->userId = $userId;
        $this->userName = $userName;
        $this->latitude = $latitude;
        $this->longitude = $longitude;
        $this->accuracy = $accuracy;
        $this->timestamp = $timestamp;
    }

    public function getFirstName()
    {
        return $this->firstName;
    }

    public function jsonSerialize()
    {
        return [
        'userId' => $this->userId,
        'userName' => $this->userName,
        'latitude' => $this->latitude,
        'longitude' => $this->longitude,
        'accuracy' => $this->accuracy,
        'timestamp' => $this->timestamp,
        'isCached' => false
        ];
    }
}
