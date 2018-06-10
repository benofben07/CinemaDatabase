CREATE TABLE MOVIE(
    MOVIE_ID     INTEGER AUTO_INCREMENT PRIMARY KEY,
    TITLE        VARCHAR(50) NOT NULL,
    ORIGIN       VARCHAR(50) NOT NULL,
    DUBBED       VARCHAR(1)  NOT NULL,
    DIRECTOR     VARCHAR(50) NOT NULL,
    DESCRIPTION  VARCHAR(50) NOT NULL,
    DURATION     INTEGER     NOT NULL,
    MAX_PLAY     INTEGER     NOT NULL,
    AGE_LIMIT    INTEGER     NOT NULL,
    PICTURE      VARCHAR(50) NOT NULL
);

CREATE TABLE CINEMA_HALL(
    CINEMA_HALL_ID    INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
    NAME              VARCHAR(50) NOT NULL,
    NUM_OF_ROW        INTEGER     NOT NULL,
    NUM_OF_COL        INTEGER     NOT NULL
);

CREATE TABLE SCREENING(
    SCREENING_ID      INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
    FK_MOVIE_ID       INTEGER     NOT NULL,
    FK_CINEMA_HALL_ID INTEGER     NOT NULL,
    BEGIN             VARCHAR(50) NOT NULL,
    FOREIGN KEY (FK_MOVIE_ID)       REFERENCES MOVIE(MOVIE_ID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (FK_CINEMA_HALL_ID) REFERENCES CINEMA_HALL(CINEMA_HALL_ID) ON DELETE CASCADE ON UPDATE CASCADE
    
);

CREATE TABLE SEAT(
    SEAT_ID           INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
    FK_SCREENING_ID   INTEGER    NOT NULL,
    ROW_NUM           INTEGER    NOT NULL,
    COL_NUM           INTEGER    NOT NULL,
    FOREIGN KEY (FK_SCREENING_ID) REFERENCES SCREENING(SCREENING_ID) ON DELETE CASCADE
);

ALTER TABLE MOVIE       AUTO_INCREMENT=1;
ALTER TABLE SCREENING   AUTO_INCREMENT=1;
ALTER TABLE CINEMA_HALL AUTO_INCREMENT=1;
ALTER TABLE SEAT        AUTO_INCREMENT=1;
