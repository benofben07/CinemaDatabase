INSERT INTO MOVIE 
    (TITLE, ORIGIN, DUBBED, DIRECTOR, DESCRIPTION, DURATION, MAX_PLAY, AGE_LIMIT, PICTURE) 
VALUES ('SZAMURAJ', 'MAGYAR', 'Y', 'BELA', 'EZ EGY SZAR', 50, 3, 1, 'NINCS');
INSERT INTO MOVIE 
    (TITLE, ORIGIN, DUBBED, DIRECTOR, DESCRIPTION, DURATION, MAX_PLAY, AGE_LIMIT, PICTURE) 
VALUES ('SZAMURAJ2', 'MAGYAR2', 'N', 'BELA2', 'EZ EGY SZAR2', 52, 2, 2, 'NINCS2');


INSERT INTO CINEMA_HALL (NAME, NUM_OF_ROW, NUM_OF_COL) 
    VALUES ('ANYAD MOZI', 8,8);
INSERT INTO CINEMA_HALL (NAME, NUM_OF_ROW, NUM_OF_COL) 
    VALUES ('ANYAD MOZI2', 4,4);


INSERT INTO SCREENING (FK_MOVIE_ID, FK_CINEMA_HALL_ID, BEGIN) VALUES
( (SELECT MOVIE_ID FROM MOVIE WHERE TITLE='SZAMURAJ'), 
  (SELECT CINEMA_HALL_ID FROM CINEMA_HALL WHERE NAME='ANYAD MOZI'), 
   '2018-06-20 15:00:00'
);


INSERT INTO SEAT (FK_SCREENING_ID, ROW_NUM, COL_NUM) VALUES
( (SELECT SCREENING_ID FROM SCREENING WHERE BEGIN='2018-06-20 15:00:00'), 5, 5);