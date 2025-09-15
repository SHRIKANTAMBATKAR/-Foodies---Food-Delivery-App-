package in.bushansirgur.foodiesapi.repository;

import in.bushansirgur.foodiesapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findByRole(User.UserRole role);
    List<User> findByStatus(User.UserStatus status);
    List<User> findByRoleAndStatus(User.UserRole role, User.UserStatus status);
    
    @Query("{'restaurantName': {$regex: ?0, $options: 'i'}}")
    List<User> findByRestaurantNameContainingIgnoreCase(String restaurantName);
    
    @Query("{'cuisineType': ?0}")
    List<User> findByCuisineType(String cuisineType);
    
    @Query("{'isAvailable': true, 'role': 'DELIVERY_PARTNER'}")
    List<User> findAvailableDeliveryPartners();
    
    @Query("{'isRestaurantApproved': true, 'role': 'RESTAURANT'}")
    List<User> findApprovedRestaurants();
}
