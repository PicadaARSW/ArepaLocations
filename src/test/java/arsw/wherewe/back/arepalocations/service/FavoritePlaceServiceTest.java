package arsw.wherewe.back.arepalocations.service;

import arsw.wherewe.back.arepalocations.dto.FavoritePlaceDTO;
import arsw.wherewe.back.arepalocations.model.FavoritePlace;
import arsw.wherewe.back.arepalocations.repository.FavoritePlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FavoritePlaceServiceTest {

    @Mock
    private FavoritePlaceRepository favoritePlaceRepository;

    @InjectMocks
    private FavoritePlaceService favoritePlaceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveFavoritePlace_savesAndReturnsFavoritePlace() {
        FavoritePlaceDTO favoritePlaceDTO = new FavoritePlaceDTO();
        FavoritePlace favoritePlace = new FavoritePlace();
        when(favoritePlaceRepository.save(any(FavoritePlace.class))).thenReturn(favoritePlace);

        FavoritePlaceDTO result = favoritePlaceService.saveFavoritePlace(favoritePlaceDTO);

        assertEquals(favoritePlace.getId(), result.getId());
        verify(favoritePlaceRepository, times(1)).save(any(FavoritePlace.class));
    }

    @Test
    void deleteFavoritePlace_deletesFavoritePlaceById() {
        String id = "1";

        favoritePlaceService.deleteFavoritePlace(id);

        verify(favoritePlaceRepository, times(1)).deleteById(id);
    }

    @Test
    void getFavoritePlacesByGroupId_returnsFavoritePlaces() {
        List<FavoritePlace> favoritePlaces = List.of(new FavoritePlace());
        when(favoritePlaceRepository.findByGroupId("group1")).thenReturn(favoritePlaces);

        List<FavoritePlaceDTO> result = favoritePlaceService.getFavoritePlacesByGroupId("group1");

        assertEquals(favoritePlaces.size(), result.size());
        verify(favoritePlaceRepository, times(1)).findByGroupId("group1");
    }

    @Test
    void editFavoritePlace_editsAndReturnsFavoritePlace() {
        FavoritePlaceDTO favoritePlaceDTO = new FavoritePlaceDTO();
        FavoritePlace favoritePlace = new FavoritePlace();
        when(favoritePlaceRepository.save(any(FavoritePlace.class))).thenReturn(favoritePlace);

        FavoritePlaceDTO result = favoritePlaceService.editFavoritePlace(favoritePlaceDTO);

        assertEquals(favoritePlace.getId(), result.getId());
        verify(favoritePlaceRepository, times(1)).save(any(FavoritePlace.class));
    }
}