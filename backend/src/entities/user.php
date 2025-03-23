<?php

class Partner implements jsonSerializable
{
    protected $id;
    protected $firstName;
    protected $icon;
    protected $coupleId;

    public function __construct($id, $firstName, $icon, $coupleId)
    {
        $this->id = $id;
        $this->firstName = $firstName;
        $this->icon = $icon;
        $this->coupleId = $coupleId;
    }

    public function getId()
    {
        return $this->id;
    }

    public function getFirstName()
    {
        return $this->firstName;
    }

    public function getCoupleId()
    {
        return $this->coupleId;
    }

    public function jsonSerialize()
    {
        return [
        'id' => $this->id,
        'firstName' => $this->firstName,
        'icon' => $this->icon
        ];
    }
}

class User extends Partner implements JsonSerializable
{
    private $email;
    private $partner;

    public function __construct(
        $id,
        $firstName,
        $email,
        $icon,
        $coupleId,
        $partner
    ) {
        $this->id = $id;
        $this->firstName = $firstName;
        $this->email = $email;
        $this->icon = $icon;
        $this->coupleId = $coupleId;
        $this->partner = $partner;
    }

    public function getPartner()
    {
        return $this->partner;
    }

    public function jsonSerialize()
    {
        return [
        'id' => $this->id,
        'firstName' => $this->firstName,
        'email' => $this->email,
        'icon' => $this->icon,
        'partner' => $this->partner
        ];
    }
}
