package rs.ac.bg.etf.sab;

import rs.ac.bg.etf.sab.tests.TestRunner;

public class StudentMain {
    public static void main(String[] args) throws Exception {
// Uncomment and change fallowing lines
//        GeneralOperations generalOperations = new piggbbbb_GeneralOperations();
//        GenresOperations genresOperations = new piggbbbb_GenresOperations();
//        MoviesOperations moviesOperations = new piggbbbb_MoviesOperations();
//        RatingsOperations ratingsOperation = new piggbbbb_RatingsOperations();
//        TagsOperations tagsOperations = new piggbbbb_TagsOperations();
//        UsersOperations usersOperations = new pn150121_UsersOperations();
//        WatchlistsOperations watchlistsOperations = new pn150121_WatchlistsOperations();

//        TestHandler.createInstance(
//                genresOperations,
//                moviesOperations,
//                ratingsOperation,
//                tagsOperations,
//                usersOperations,
//                watchlistsOperations,
//                generalOperations);
        TestRunner.runTests();
    }
}