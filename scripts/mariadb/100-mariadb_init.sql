-- Select database
USE flamingo;
GRANT ALL PRIVILEGES ON flamingo.* TO 'flamingo-user'@'%';
FLUSH PRIVILEGES;

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
    date DATE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_Wishlist_Author FOREIGN KEY (author) REFERENCES Users(id)
);

CREATE TABLE Albums(
    id INT NOT NULL AUTO_INCREMENT,
    uuid UUID NOT NULL,
    title VARCHAR(128) NOT NULL,
    artist VARCHAR(64) NOT NULL,
    cover_name VARCHAR(64) NOT NULL,
    couple_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_Album_Uuid UNIQUE (uuid),
    CONSTRAINT FK_Album_Couple FOREIGN KEY (couple_id) REFERENCES Couples(id)
);

CREATE TABLE Tracks(
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(128) NOT NULL,
    comment VARCHAR(256),
    resource_name VARCHAR(64) NOT NULL,
    album_uuid UUID NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_Track UNIQUE (album_uuid, resource_name),
    CONSTRAINT FK_Track_Album FOREIGN KEY (album_uuid) REFERENCES Albums(uuid)
);

CREATE TABLE Memories(
    id INT NOT NULL AUTO_INCREMENT,
    start_date DATE NOT NULL,
    end_date DATE,
    title VARCHAR(128) NOT NULL,
    memory_description TEXT NOT NULL,
    photo TEXT,
    couple_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_Memory_Couple FOREIGN KEY (couple_id) REFERENCES Couples(id)
);

CREATE TABLE UserLocations(
    user_id INT NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    accuracy FLOAT NOT NULL,
    last_seen TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT FK_Location_User FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- TODO: Last seen was all in 1970
CREATE TABLE FirebaseTokens(
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    last_seen TIMESTAMP NOT NULL,
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
    "2024-08-01"
);
INSERT INTO Wishlist VALUES (
    2,
    2,
    "A new belt for my suit",
    true,
    "2024-04-21"
);

-- Mallory wishes
INSERT INTO Wishlist VALUES (
    3,
    3,
    "A new spyglass",
    false,
    "2023-08-05"
);

-- FCM Tokens
INSERT INTO FirebaseTokens VALUES (
    1,
    1,
    NOW(),
    "alice-fcm-test-token",
    false
);
INSERT INTO FirebaseTokens VALUES (
    2,
    2,
    NOW(),
    "bob-fcm-test-token",
    false
);
INSERT INTO FirebaseTokens VALUES (
    3,
    3,
    NOW(),
    "mallory-fcm-test-token",
    false
);

-- Albums
INSERT INTO Albums VALUES (
    1,
    "aaaaabbb-ecf4-4235-85c3-f0b5408fee17",
    "Alice and Bob sing together",
    "A&B Band",
    "aaaaabbb-fa4d-4ee1-ba48-989e46067756.jpg",
    1
);

INSERT INTO Albums VALUES (
    2,
    "ffffffff-e96a-4982-84db-1e2d70c72128",
    "M&B 101",
    "M and the boys",
    "ffffffff-4081-443e-9319-5ad35e7edebe.png",
    2
);

-- Tracks for those albums
INSERT INTO Tracks VALUES (
    1,
    "Lulaby",
    "Sleep well",
    "aaaaabbb-be8c-43fb-bc38-40b327b258c7.mp3",
    "aaaaabbb-ecf4-4235-85c3-f0b5408fee17"
);

INSERT INTO Tracks VALUES (
    2,
    "Lulaby",
    "Sleep well",
    "aaaaabbb-9bca-431d-ba7e-fc6a65bca088.mp3",
    "aaaaabbb-ecf4-4235-85c3-f0b5408fee17"
);

INSERT INTO Tracks VALUES (
    3,
    "ROAR",
    null,
    "ffffffff-7a60-4cb9-b3f9-2edf340ab3e2.mp3",
    "ffffffff-e96a-4982-84db-1e2d70c72128"
);

-- Location info
INSERT INTO UserLocations VALUES (
    1, -- Alice
    1.1,
    1.1,
    1.1,
    NOW()
);

INSERT INTO UserLocations VALUES (
    2, -- Bob
    2.2,
    2.2,
    2.2,
    NOW()
);

INSERT INTO UserLocations VALUES (
    3, -- Mallory
    3.3,
    3.3,
    3.3,
    NOW()
);
