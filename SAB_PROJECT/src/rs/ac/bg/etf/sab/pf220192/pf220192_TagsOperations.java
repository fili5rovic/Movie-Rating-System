package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.TagsOperations;
import rs.ac.bg.etf.sab.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class pf220192_TagsOperations implements TagsOperations {
    @Override
    public Integer addTag(Integer movieId, String tag) {
        // Check if movie exists
        if(!Util.existsById("Film", movieId))
            return null;

        List<Integer> tagIds = Util.fetchIdsWhere("Oznaka", "oznaka=?", List.of(tag));
        if(tagIds.isEmpty())
            return null;
        Integer tagId = tagIds.get(0);

        if(Util.existsWhere("FilmOznakaLink", "idFilm=? AND idOznaka=?", List.of(movieId, tagId)))
            return null;

        Integer linkId = Util.insert("FilmOznakaLink", Map.of("idFilm", movieId, "idOznaka", tagId));
        return linkId != null ? movieId : null;
    }

    @Override
    public Integer removeTag(Integer movieId, String tag) {
        List<Integer> idTags = Util.fetchIdsWhere("Oznaka", "oznaka=?", List.of(tag));
        if(idTags.isEmpty())
            return null;
        Integer tagId = idTags.get(0);

        return Util.deleteWhere("FilmOznakaLink", "idFilm=? AND idOznaka=?", List.of(movieId, tagId))
                ? movieId : null;
    }


    @Override
    public int removeAllTagsForMovie(Integer movieId) {
        List<Integer> tags = Util.fetchColumnWhere("FilmOznakaLink", "idOznaka", "idFilm=?", List.of(movieId));
        int count = tags.size();

        Util.deleteWhere("FilmOznakaLink", "idFilm=?", List.of(movieId));

        return count;
    }

    @Override
    public boolean hasTag(Integer movieId, String tag) {
        List<Integer> idTags = Util.fetchIdsWhere("Oznaka", "oznaka=?", List.of(tag));
        if(idTags.isEmpty())
            return false;
        Integer tagId = idTags.get(0);

        return Util.existsWhere("FilmOznakaLink", "idFilm=? AND idOznaka=?", List.of(movieId, tagId));
    }

    @Override
    public List<String> getTagsForMovie(Integer movieId) {
        List<Integer> idTags = Util.fetchColumnWhere("FilmOznakaLink", "idOznaka", "idFilm=?", List.of(movieId));
        List<String> tags = new ArrayList<>();

        for (Integer tagId : idTags) {
            List<String> tagNames = Util.fetchColumnStrWhere("Oznaka", "oznaka", "idOznaka=?", List.of(tagId));
            if(!tagNames.isEmpty()) {
                tags.add(tagNames.get(0));
            }
        }

        return tags;
    }

    @Override
    public List<Integer> getMovieIdsByTag(String tag) {
        List<Integer> tagIds = Util.fetchIdsWhere("Oznaka", "oznaka=?", List.of(tag));
        if(tagIds.isEmpty())
            return new ArrayList<>();

        Integer tagId = tagIds.get(0);

        return Util.fetchColumnWhere("FilmOznakaLink", "idFilm", "idOznaka=?", List.of(tagId));
    }
    @Override
    public List<String> getAllTags() {
        return Util.fetchColumns("Oznaka","oznaka");
    }
}
