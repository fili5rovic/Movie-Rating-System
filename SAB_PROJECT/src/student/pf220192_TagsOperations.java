package student;

import rs.ac.bg.etf.sab.operations.TagsOperations;
import student.util.DBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class pf220192_TagsOperations implements TagsOperations {
    @Override
    public Integer addTag(Integer movieId, String tag) {
        if (!DBUtil.existsById("Film", movieId))
            return null;

        List<Integer> tagIds = DBUtil.fetchIdsWhere("Oznaka", "oznaka=?", List.of(tag));
        Integer tagId;
        if (tagIds.isEmpty()) {
            tagId = DBUtil.insert("Oznaka", Map.of("oznaka", tag));
        } else {
            tagId = tagIds.get(0);
        }

        if (tagId == null) return null;

        if (DBUtil.existsWhere("FilmOznakaLink", "idFilm=? AND idOznaka=?", List.of(movieId, tagId)))
            return null;

        Integer linkId = DBUtil.insert("FilmOznakaLink", Map.of("idFilm", movieId, "idOznaka", tagId));

        return linkId != null ? movieId : null;
    }


    @Override
    public Integer removeTag(Integer movieId, String tag) {
        List<Integer> idTags = DBUtil.fetchIdsWhere("Oznaka", "oznaka=?", List.of(tag));
        if(idTags.isEmpty())
            return null;
        Integer tagId = idTags.get(0);

        return DBUtil.deleteWhere("FilmOznakaLink", "idFilm=? AND idOznaka=?", List.of(movieId, tagId))
                ? movieId : null;
    }


    @Override
    public int removeAllTagsForMovie(Integer movieId) {
        List<Integer> tags = DBUtil.fetchColumnWhere("FilmOznakaLink", "idOznaka", "idFilm=?", List.of(movieId));
        int count = tags.size();

        DBUtil.deleteWhere("FilmOznakaLink", "idFilm=?", List.of(movieId));

        return count;
    }

    @Override
    public boolean hasTag(Integer movieId, String tag) {
        List<Integer> idTags = DBUtil.fetchIdsWhere("Oznaka", "oznaka=?", List.of(tag));
        if(idTags.isEmpty())
            return false;
        Integer tagId = idTags.get(0);

        return DBUtil.existsWhere("FilmOznakaLink", "idFilm=? AND idOznaka=?", List.of(movieId, tagId));
    }

    @Override
    public List<String> getTagsForMovie(Integer movieId) {
        List<Integer> idTags = DBUtil.fetchColumnWhere("FilmOznakaLink", "idOznaka", "idFilm=?", List.of(movieId));
        List<String> tags = new ArrayList<>();

        for (Integer tagId : idTags) {
            List<String> tagNames = DBUtil.fetchColumnStrWhere("Oznaka", "oznaka", "idOznaka=?", List.of(tagId));
            if(!tagNames.isEmpty()) {
                tags.add(tagNames.get(0));
            }
        }

        return tags;
    }

    @Override
    public List<Integer> getMovieIdsByTag(String tag) {
        List<Integer> tagIds = DBUtil.fetchIdsWhere("Oznaka", "oznaka=?", List.of(tag));
        if(tagIds.isEmpty())
            return new ArrayList<>();

        Integer tagId = tagIds.get(0);

        return DBUtil.fetchColumnWhere("FilmOznakaLink", "idFilm", "idOznaka=?", List.of(tagId));
    }
    @Override
    public List<String> getAllTags() {
        return DBUtil.fetchColumns("Oznaka","oznaka");
    }
}
