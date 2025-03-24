<?php

class CoupleDetails implements jsonSerializable
{
    protected $id;
    protected $anniversary;

    public function __construct($id, $anniversary)
    {
        $this->id = $id;
        $this->anniversary = $anniversary;
    }

    public function getId()
    {
        return $this->id;
    }

    public function getAnniversary()
    {
        return $this->anniversary;
    }

    public function jsonSerialize()
    {
        return [
        'anniversary' => $this->anniversary
        ];
    }
}
