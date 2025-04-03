package arsw.wherewe.back.arepalocations.service;

import arsw.wherewe.back.arepalocations.model.FavoritePlace;
import arsw.wherewe.back.arepalocations.repository.FavoritePlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritePlaceService {

    private FavoritePlaceRepository favoritePlaceRepository;

    /**
     * Constructor for FavoritePlaceService injecting FavoritePlaceRepository
     */
    @Autowired
    public FavoritePlaceService(FavoritePlaceRepository favoritePlaceRepository) {
        this.favoritePlaceRepository = favoritePlaceRepository;
    }



    public void deleteFavoritePlace(String id){
        favoritePlaceRepository.deleteById(id);
    }

    public FavoritePlace saveFavoritePlace(FavoritePlace favoritePlace){
        return favoritePlaceRepository.save(favoritePlace);
    }

    public List<FavoritePlace> getFavoritePlacesByGroupId(String groupId){
        return favoritePlaceRepository.findByGroupId(groupId);
    }

    public FavoritePlace editFavoritePlace(FavoritePlace favoritePlace){
        return favoritePlaceRepository.save(favoritePlace);
    }
}
