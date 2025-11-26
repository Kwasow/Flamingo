-- Create tables
CREATE TABLE Couples(
    id INT NOT NULL AUTO_INCREMENT,
    anniversary_date DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Users(
    id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL,
    icon VARCHAR(64) NOT NULL,
    couple_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UK_User_Email UNIQUE (email),
    CONSTRAINT FK_User_Couple FOREIGN KEY (couple_id) REFERENCES Couples(id)
);

CREATE TABLE Wishlist(
    id INT NOT NULL AUTO_INCREMENT,
    author INT NOT NULL,
    content TEXT NOT NULL,
    done BOOLEAN NOT NULL,
    time_stamp BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_Wishlist_Author FOREIGN KEY (author) REFERENCES Users(id)
);

CREATE TABLE Albums(
    id INT NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(36) NOT NULL,
    title TEXT NOT NULL,
    artist VARCHAR(64) NOT NULL,
    cover_name VARCHAR(50) NOT NULL,
    couple_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_Album_Uuid UNIQUE (uuid),
    CONSTRAINT FK_Album_Couple FOREIGN KEY (couple_id) REFERENCES Couples(id)
);

CREATE TABLE Tracks(
    id INT NOT NULL AUTO_INCREMENT,
    title TEXT NOT NULL,
    comment TEXT,
    resource_name VARCHAR(50) NOT NULL,
    album_uuid VARCHAR(36) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_Track UNIQUE (album_uuid, resource_name),
    CONSTRAINT FK_Track_Album FOREIGN KEY (album_uuid) REFERENCES Albums(uuid)
);

CREATE TABLE Memories(
    id INT NOT NULL AUTO_INCREMENT,
    `start_date` DATE NOT NULL,
    end_date DATE,
    title TEXT NOT NULL,
    memory_description TEXT NOT NULL,
    photo TEXT,
    couple_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_Memory_Couple FOREIGN KEY (couple_id) REFERENCES Couples(id)
);

CREATE TABLE Locations(
    user_id INT NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    accuracy FLOAT NOT NULL,
    time_stamp BIGINT NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT FK_Location_User FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE FirebaseTokens(
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    time_stamp BIGINT NOT NULL,
    token TEXT NOT NULL UNIQUE,
    debug BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_FirebaseTokens_User FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Build indexes
CREATE INDEX idx_fcm_token ON FirebaseTokens(token);

-- Create data
INSERT INTO Couples VALUES (1, "2023-07-31");
INSERT INTO Couples VALUES (2, "2020-01-01");

INSERT INTO Users VALUES (1, "Alice", "alice@example.com", "CAT", 1);
INSERT INTO Users VALUES (2, "Bob", "bob@example.com", "SHEEP", 1);
INSERT INTO Users VALUES (3, "Mallory", "mallory@example.com", "SHEEP", 2);

-- Alice and Bob memories
INSERT INTO Memories VALUES (
     1,
     "2023-07-31",
     "2023-08-07",
     "First trip together",
     "We went to a shopping mall and got lost",
     "https://examplephotos.org/mall",
     1
);
INSERT INTO Memories VALUES (
    2,
    "2023-09-01",
    null,
    "One month together!",
    "31 days and counting",
    null,
    1
);
INSERT INTO Memories VALUES (
    3,
    "2024-07-31",
    null,
    "First anniversary",
    "The first year is done!",
    null,
    1
);

-- Mallory memories
INSERT INTO Memories VALUES (
    4,
    "2023-07-31",
    null,
    "I spied on Alice and Bob",
    "They went to a shopping mall and got lost",
    "https://examplephotos.org/mall",
    2
);

-- Alice and Bob wishes
INSERT INTO Wishlist VALUES (
    1,
    1,
    "A kitten",
    false,
    1722548749
);
INSERT INTO Wishlist VALUES (
    2,
    2,
    "A new belt for my suit",
    true,
    1713720737
);

-- Mallory wishes
INSERT INTO Wishlist VALUES (
    3,
    3,
    "A new spyglass",
    false,
    1691230314
);

-- FCM Tokens
INSERT INTO FirebaseTokens VALUES (
    1,
    1,
    1764068353,
    "alice-fcm-test-token",
    false
);
INSERT INTO FirebaseTokens VALUES (
    2,
    2,
    1764068353,
    "bob-fcm-test-token",
    false
);
INSERT INTO FirebaseTokens VALUES (
    3,
    3,
    1764068353,
    "mallory-fcm-test-token",
    false
);

-- Albums
INSERT INTO Albums VALUES (
    1,
    "alicebob-ecf4-4235-85c3-f0b5408fee17",
    "Alice and Bob sing together",
    "A&B Band",
    "alicebob-fa4d-4ee1-ba48-989e46067756.jpg",
    1
);

INSERT INTO Albums VALUES (
    2,
    "mallory2-e96a-4982-84db-1e2d70c72128",
    "M&B 101",
    "M and the boys",
    "mallory2-4081-443e-9319-5ad35e7edebe.png",
    2
);

-- Tracks for those albums
INSERT INTO Tracks VALUES (
    1,
    "Lulaby",
    "Sleep well",
    "alicebob-be8c-43fb-bc38-40b327b258c7.mp3",
    "alicebob-ecf4-4235-85c3-f0b5408fee17"
);

INSERT INTO Tracks VALUES (
    2,
    "Lulaby",
    "Sleep well",
    "alicebob-9bca-431d-ba7e-fc6a65bca088.mp3",
    "alicebob-ecf4-4235-85c3-f0b5408fee17"
);

INSERT INTO Tracks VALUES (
    3,
    "ROAR",
    null,
    "mallory2-7a60-4cb9-b3f9-2edf340ab3e2.mp3",
    "mallory2-e96a-4982-84db-1e2d70c72128"
);
