package arsw.wherewe.back.arepalocations.repository;

import arsw.wherewe.back.arepalocations.model.FavoritePlace;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FavoritePlaceRepository extends MongoRepository<FavoritePlace, String>{

    List<FavoritePlace> findByGroupId(String groupId);
}
