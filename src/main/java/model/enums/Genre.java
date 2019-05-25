package model.enums;

import java.util.Random;

public enum Genre {

    ACTION, HORROR, FANTASY, DRAMA, COMEDY;

    Genre() {
    }

    public static Genre getRandomGenre() {
        return Genre.values()[new Random().nextInt(Genre.values().length)];
    }
}



