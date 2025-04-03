package arsw.wherewe.back.arepalocations.repository;

import arsw.wherewe.back.arepalocations.model.PushToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PushTokenRepository extends MongoRepository<PushToken, String> {
    List<PushToken> findByGroupId(String groupId);
    Optional<PushToken> findByUserId(String userId);
}
