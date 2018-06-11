package logic;

/**
 * Enum representing the state when we try to add a Screening.
 * SUCCESS if there was no ploblems.
 * INCORRECT_MAX_PLAY if Movie reached its max playable limit.
 * SCREENED_IN_TOO_MANY_HALLS if Movie would be screened more than in 3
 * overlapping time intervals.
 * SCREENINGS_OVERLAP if there is already a screening scheduled in given
 * Cinema Hall at given time
 * INCORRECT_AGE_LIMIT if Movie would be screened later than its age limit
 * allows.
 * @author Bence
 */
public enum ScreeningAdditionEnum {
    SUCCES,
    INCORRECT_MAX_PLAY,
    SCREENED_IN_TOO_MANY_HALLS,
    SCREENINGS_OVERLAP,
    INCORRECT_AGE_LIMIT
}
