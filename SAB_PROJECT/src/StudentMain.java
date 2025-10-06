import rs.ac.bg.etf.sab.operations.*;
import rs.ac.bg.etf.sab.tests.TestHandler;
import rs.ac.bg.etf.sab.tests.TestRunner;

import student.*;

public class StudentMain {

    public static void main(String[] args) {
        GeneralOperations generalOperations = new pf220192_GeneralOperations();
        GenresOperations genresOperations = new pf220192_GenresOperations();
        MoviesOperations moviesOperations = new pf220192_MoviesOperations();
        RatingsOperations ratingsOperation = new pf220192_RatingsOperations();
        TagsOperations tagsOperations = new pf220192_TagsOperations();
        UsersOperations usersOperations = new pf220192_UsersOperations();
        WatchlistsOperations watchlistsOperations = new pf220192_WatchlistsOperations();

        TestHandler.createInstance(
                genresOperations,
                moviesOperations,
                ratingsOperation,
                tagsOperations,
                usersOperations,
                watchlistsOperations,
                generalOperations);
        TestRunner.runTests();
    }
}