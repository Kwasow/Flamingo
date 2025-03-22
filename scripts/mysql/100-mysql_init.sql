-- Select database
USE flamingo;
GRANT ALL PRIVILEGES ON flamingo.* TO 'flamingo-user'@'%';
FLUSH PRIVILEGES;

-- Create tables
CREATE TABLE Users(
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(64) NOT NULL,
  last_name VARCHAR(64) NOT NULL,
  email VARCHAR(64) NOT NULL,
  missing_you_recipient INT NOT NULL,
  icon VARCHAR(64),
  PRIMARY KEY (id),
  CONSTRAINT FK_User_MissingYouRecipient FOREIGN KEY (missing_you_recipient) REFERENCES Users(id),
  CONSTRAINT UK_Email UNIQUE (email)
);

CREATE TABLE Wishlist(
  id INT NOT NULL AUTO_INCREMENT,
  author VARCHAR(64) NOT NULL,
  content TEXT NOT NULL,
  done TINYINT(1) NOT NULL,
  time_stamp BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Albums(
  id INT NOT NULL AUTO_INCREMENT,
  uuid VARCHAR(36) NOT NULL,
  title TEXT NOT NULL,
  artist VARCHAR(64) NOT NULL,
  cover_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT UC_Uuid UNIQUE (uuid)
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
