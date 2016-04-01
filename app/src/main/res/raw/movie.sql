DROP TABLE movie;

CREATE TABLE movie (
  title TEXT PRIMARY KEY,
  released TEXT,
  rating TEXT,
  genre TEXT,
  actors TEXT,
  plot TEXT,
  movie_year TEXT,
  runtime TEXT);

INSERT INTO movie VALUES
   ('Frozen','27 Nov 2013','PG','Animation','Kristen Bell, Idina Menzel, Jonathan Groff, Josh Gad','When the newly crowned Queen Elsa accidentally uses her power to turn things into ice to curse her home in infinite winter, her sister, Anna, teams up with a mountain man, his playful reindeer, and a snowman to change the weather condition.','2013','102 min');
INSERT INTO movie VALUES
   ('Interstellar','07 Nov 2014','PG','Adventure','Ellen Burstyn, Matthew McConaughey, Mackenzie Foy, John Lithgow','A team of explorers travel through a wormhole in space in an attempt to ensure humanitys survival.','2014','169 min');
INSERT INTO movie VALUES
   ('The Dark Knight','18 Jul 2008','PG-13','Action','Christian Bale, Heath Ledger, Aaron Eckhart, Michael Caine','When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, the caped crusader must come to terms with one of the greatest psychological tests of his ability to fight injustice.','2008','152 min');
