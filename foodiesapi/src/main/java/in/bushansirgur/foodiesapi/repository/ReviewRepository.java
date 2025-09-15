package in.bushansirgur.foodiesapi.repository;

import in.bushansirgur.foodiesapi.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByCustomerId(String customerId);
    List<Review> findByRestaurantId(String restaurantId);
    List<Review> findByMenuItemId(String menuItemId);
    List<Review> findByDeliveryPartnerId(String deliveryPartnerId);
    List<Review> findByType(Review.ReviewType type);
    List<Review> findByRating(Integer rating);
    
    @Query("{'restaurantId': ?0, 'type': 'RESTAURANT'}")
    List<Review> findRestaurantReviews(String restaurantId);
    
    @Query("{'menuItemId': ?0, 'type': 'MENU_ITEM'}")
    List<Review> findMenuItemReviews(String menuItemId);
    
    @Query("{'deliveryPartnerId': ?0, 'type': 'DELIVERY_PARTNER'}")
    List<Review> findDeliveryPartnerReviews(String deliveryPartnerId);
    
    @Query("{'rating': {$gte: ?0}}")
    List<Review> findByRatingGreaterThanEqual(Integer rating);
    
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Review> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'isVerified': true}")
    List<Review> findVerifiedReviews();
    
    @Query("{'isReported': true}")
    List<Review> findReportedReviews();
}
