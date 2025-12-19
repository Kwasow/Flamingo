SET NAMES utf8;

-- Populate user data
INSERT INTO Couples VALUES (NULL, '2022-03-27');
INSERT INTO Couples VALUES (NULL, '2020-04-30');

INSERT INTO Users VALUES (
  NULL,
  "Karol",
  "wasowski02@gmail.com",
  "sheep",
  1
);

INSERT INTO Users VALUES (
  NULL,
  "Ronia",
  "example@gmail.com",
  "cat",
  1
);

INSERT INTO Users VALUES (
  NULL,
  "Obcy",
  "obcy@example.com",
  "cat",
  2
);

-- Populate wishlist data
INSERT INTO Wishlist VALUES (
  NULL,
  1,
  "Testowe niespełnione życzenie Karola",
  0,
  1703547021952
);

INSERT INTO Wishlist VALUES (
  NULL,
  2,
  "Testowe spełnione życzenie Roni",
  1,
  1707951026238
);

INSERT INTO Wishlist VALUES (
  NULL,
  3,
  "Testowe spełnione życzenie Obcego",
  1,
  1707951066238
);

-- Populate audio data
INSERT INTO Albums VALUES (
  NULL,
  BIN_TO_UUID(RANDOM_BYTES(16)),
  "Tytuł albumu",
  "Autor albumu",
  "cover.jpg",
  1
);

INSERT INTO Tracks VALUES (
  NULL,
  "Track 1 title",
  "Track comment",
  BIN_TO_UUID(RANDOM_BYTES(16)),
  (SELECT uuid FROM Albums WHERE id = 1)
);

INSERT INTO Tracks VALUES (
  NULL,
  "Track 2 title",
  NULL,
  BIN_TO_UUID(RANDOM_BYTES(16)),
  (SELECT uuid FROM Albums WHERE id = 1)
);

INSERT INTO Memories VALUES (
  NULL,
  "2022-08-27",
  NULL,
  "Title 1",
  "Description 1",
  "www.photo1.com",
  1
);

INSERT INTO Memories VALUES (
  NULL,
  "2023-08-27",
  "2023-08-30",
  "Title 2",
  "Description 2",
  "www.photo2.com",
  1
);

INSERT INTO Memories VALUES (
  NULL,
  "2024-08-27",
  NULL,
  "Title 3",
  "Description 3",
  "www.photo3.com",
  2
);
