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
    couple INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_Album_Uuid UNIQUE (uuid),
    CONSTRAINT FK_Album_Couple FOREIGN KEY (couple) REFERENCES Couples(id)
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
    couple INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_Memory_Couple FOREIGN KEY (couple) REFERENCES Couples(id)
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
    time_stamp INT NOT NULL,
    token TEXT NOT NULL,
    debug BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_FirebaseTokens_User FOREIGN KEY (user_id) REFERENCES Users(id)
);

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
