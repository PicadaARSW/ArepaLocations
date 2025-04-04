package arsw.wherewe.back.arepalocations.repository;

import arsw.wherewe.back.arepalocations.model.PushToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;
import java.util.Optional;

public interface PushTokenRepository extends MongoRepository<PushToken, String> {
    Optional<PushToken> findByUserId(String userId);
    List<PushToken> findByGroupId(String groupId);

    @Query(value = "{ 'userId' : ?0 }", delete = true)
    void deleteByUserId(String userId);

    @Query("{ 'userId' : ?0 }")
    @Update("{ '$set' : { 'pushToken' : ?1, 'groupId' : ?2 } }")
    void upsertByUserId(String userId, String pushToken, String groupId);
}