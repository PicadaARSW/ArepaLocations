package arsw.wherewe.back.arepalocations.service;

import arsw.wherewe.back.arepalocations.dto.FavoritePlaceDTO;
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

    // Map FavoritePlace to FavoritePlaceDTO
    private FavoritePlaceDTO toFavoritePlaceDTO(FavoritePlace favoritePlace) {
        return new FavoritePlaceDTO(
                favoritePlace.getId(),
                favoritePlace.getPlaceName(),
                favoritePlace.getLatitude(),
                favoritePlace.getLongitude(),
                favoritePlace.getGroupId(),
                favoritePlace.getRadius(),
                favoritePlace.getTimestamp()
        );
    }

    // Map FavoritePlaceDTO to FavoritePlace
    private FavoritePlace toFavoritePlace(FavoritePlaceDTO favoritePlaceDTO) {
        FavoritePlace favoritePlace = new FavoritePlace(
                favoritePlaceDTO.getPlaceName(),
                favoritePlaceDTO.getLatitude(),
                favoritePlaceDTO.getLongitude(),
                favoritePlaceDTO.getGroupId(),
                favoritePlaceDTO.getRadius()
        );
        favoritePlace.setId(favoritePlaceDTO.getId());
        favoritePlace.setTimestamp(favoritePlaceDTO.getTimestamp());
        return favoritePlace;
    }



    public void deleteFavoritePlace(String id){
        favoritePlaceRepository.deleteById(id);
    }

    public FavoritePlaceDTO saveFavoritePlace(FavoritePlaceDTO favoritePlaceDTO) {
        FavoritePlace favoritePlace = toFavoritePlace(favoritePlaceDTO);
        FavoritePlace savedPlace = favoritePlaceRepository.save(favoritePlace);
        return toFavoritePlaceDTO(savedPlace);
    }

    public List<FavoritePlaceDTO> getFavoritePlacesByGroupId(String groupId) {
        return favoritePlaceRepository.findByGroupId(groupId).stream()
                .map(this::toFavoritePlaceDTO)
                .toList();
    }

    public FavoritePlaceDTO editFavoritePlace(FavoritePlaceDTO favoritePlaceDTO) {
        FavoritePlace favoritePlace = toFavoritePlace(favoritePlaceDTO);
        FavoritePlace updatedPlace = favoritePlaceRepository.save(favoritePlace);
        return toFavoritePlaceDTO(updatedPlace);
    }
}
