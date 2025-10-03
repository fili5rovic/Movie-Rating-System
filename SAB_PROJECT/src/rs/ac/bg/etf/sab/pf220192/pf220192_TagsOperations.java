package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.TagsOperations;
import rs.ac.bg.etf.sab.util.Util;

import java.util.List;
import java.util.Map;

public class pf220192_TagsOperations implements TagsOperations {
    @Override
    public Integer addTag(Integer movieId, String tag) {
        Integer tagId = Util.fetchIdsWhere("Oznaka", "oznaka=?", List.of(tag)).get(0);
        if(tagId == null)
            return null;

        return Util.insert("FilmOznakaLink", Map.of("idFilm",movieId,"idOznaka",tagId));
    }

    @Override
    public Integer removeTag(Integer movieId, String tag) {
        List<Integer> idTags = Util.fetchIdsWhere("Oznaka", "oznaka=?", List.of(tag));
        if(idTags.size() != 1)
            return null;

        Integer id = idTags.get(0);

        return Util.deleteWhere("FilmOznakaLink","idOznaka=?",List.of(id)) ? id : null;
    }

    @Override
    public int removeAllTagsForMovie(Integer movieId) {
        return Util.deleteWhere("FilmOznakaLink","idFilm=?",List.of(movieId)) ? movieId:0;
    }

    @Override
    public boolean hasTag(Integer movieId, String tag) {
        return !Util.fetchIdsWhere("FilmOznakaLink","idFilm=? and idOznaka=?",List.of(movieId,tag)).isEmpty();
    }

    @Override
    public List<String> getTagsForMovie(Integer movieId) {
//        List<Integer> idTags = DBUtil.fetchColumnWhere("FilmOznakaLink", "idFilm=?", "idOznaka", List.of(movieId));
//        List<String> tags = new ArrayList<>();
//        for (var id : idTags) {
//            tags.add(DBUtil.fetchColumnWhere("Oznaka","oznaka","idOznaka=?",List.of(id)));
//        }
        return List.of();
    }

    @Override
    public List<Integer> getMovieIdsByTag(String tag) {
        return Util.fetchIdsWhere("FilmOznakaLink", "idOznaka=?", List.of(tag));
    }

    @Override
    public List<String> getAllTags() {
        return Util.fetchColumns("Oznaka","oznaka");
    }
}
