package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.TagsOperations;

import java.util.List;

public class pf220192_TagsOperations implements TagsOperations {
    @Override
    public Integer addTag(Integer integer, String s) {
        return 0;
    }

    @Override
    public Integer removeTag(Integer integer, String s) {
        return 0;
    }

    @Override
    public int removeAllTagsForMovie(Integer integer) {
        return 0;
    }

    @Override
    public boolean hasTag(Integer integer, String s) {
        return false;
    }

    @Override
    public List<String> getTagsForMovie(Integer integer) {
        return List.of();
    }

    @Override
    public List<Integer> getMovieIdsByTag(String s) {
        return List.of();
    }

    @Override
    public List<String> getAllTags() {
        return List.of();
    }
}
