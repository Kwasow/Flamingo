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
  couple INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT UK_User_Email UNIQUE (email),
  CONSTRAINT FK_User_Couple FOREIGN KEY (couple) REFERENCES Couples(id)
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
