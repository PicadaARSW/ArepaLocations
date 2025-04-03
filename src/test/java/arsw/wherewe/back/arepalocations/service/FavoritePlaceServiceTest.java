package arsw.wherewe.back.arepalocations.service;

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
        FavoritePlace favoritePlace = new FavoritePlace();
        when(favoritePlaceRepository.save(any(FavoritePlace.class))).thenReturn(favoritePlace);

        FavoritePlace result = favoritePlaceService.saveFavoritePlace(favoritePlace);

        assertEquals(favoritePlace, result);
        verify(favoritePlaceRepository, times(1)).save(favoritePlace);
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

        List<FavoritePlace> result = favoritePlaceService.getFavoritePlacesByGroupId("group1");

        assertEquals(favoritePlaces, result);
        verify(favoritePlaceRepository, times(1)).findByGroupId("group1");
    }

    @Test
    void editFavoritePlace_editsAndReturnsFavoritePlace() {
        FavoritePlace favoritePlace = new FavoritePlace();
        when(favoritePlaceRepository.save(any(FavoritePlace.class))).thenReturn(favoritePlace);

        FavoritePlace result = favoritePlaceService.editFavoritePlace(favoritePlace);

        assertEquals(favoritePlace, result);
        verify(favoritePlaceRepository, times(1)).save(favoritePlace);
    }
}