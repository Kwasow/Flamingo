<?php

class Partner implements jsonSerializable
{
    protected $id;
    protected $firstName;
    protected $icon;

    public function __construct($id, $firstName, $icon)
    {
        $this->id = $id;
        $this->firstName = $firstName;
        $this->icon = $icon;
    }

    public function getId()
    {
        return $this->id;
    }

    public function getFirstName()
    {
        return $this->firstName;
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
        $partner
    ) {
        $this->id = $id;
        $this->firstName = $firstName;
        $this->email = $email;
        $this->icon = $icon;
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
