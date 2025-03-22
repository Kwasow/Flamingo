<?php

class Wish implements JsonSerializable
{
    private $id;
    private $authorId;
    private $content;
    private $done;
    private $timestamp;

    public function __construct($id, $authorId, $content, $done, $timestamp)
    {
        $this->id = $id;
        $this->authorId = $authorId;
        $this->content = $content;
        $this->done = $done;
        $this->timestamp = $timestamp;
    }

    public function jsonSerialize()
    {
        return [
        'id' => $this->id,
        'authorId' => $this->authorId,
        'content' => $this->content,
        'done' => $this->done,
        'timestamp' => $this->timestamp
        ];
    }
}
