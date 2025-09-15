package in.bushansirgur.foodiesapi.repository;

import in.bushansirgur.foodiesapi.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    List<MenuItem> findByRestaurantId(String restaurantId);
    List<MenuItem> findByRestaurantIdAndIsAvailable(String restaurantId, Boolean isAvailable);
    List<MenuItem> findByCategory(String category);
    List<MenuItem> findByIsVegetarian(Boolean isVegetarian);
    List<MenuItem> findByIsVegan(Boolean isVegan);
    List<MenuItem> findByIsGlutenFree(Boolean isGlutenFree);
    
    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    List<MenuItem> findByNameContainingIgnoreCase(String name);
    
    @Query("{'tags': {$in: ?0}}")
    List<MenuItem> findByTagsIn(List<String> tags);
    
    @Query("{'price': {$gte: ?0, $lte: ?1}}")
    List<MenuItem> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("{'restaurantId': ?0, 'category': ?1, 'isAvailable': true}")
    List<MenuItem> findByRestaurantIdAndCategoryAndIsAvailable(String restaurantId, String category, Boolean isAvailable);
    
    @Query("{'rating': {$gte: ?0}}")
    List<MenuItem> findByRatingGreaterThanEqual(Double rating);
}
