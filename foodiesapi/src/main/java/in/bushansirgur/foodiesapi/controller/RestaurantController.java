package in.bushansirgur.foodiesapi.controller;

import in.bushansirgur.foodiesapi.model.MenuItem;
import in.bushansirgur.foodiesapi.model.User;
import in.bushansirgur.foodiesapi.repository.MenuItemRepository;
import in.bushansirgur.foodiesapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RestaurantController {
    
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    
    @GetMapping
    public ResponseEntity<?> getAllRestaurants() {
        try {
            List<User> restaurants = userRepository.findApprovedRestaurants();
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchRestaurants(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) String cuisineType,
                                            @RequestParam(required = false) String city) {
        try {
            List<User> restaurants;
            
            if (name != null && !name.isEmpty()) {
                restaurants = userRepository.findByRestaurantNameContainingIgnoreCase(name);
            } else if (cuisineType != null && !cuisineType.isEmpty()) {
                restaurants = userRepository.findByCuisineType(cuisineType);
            } else {
                restaurants = userRepository.findApprovedRestaurants();
            }
            
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{restaurantId}")
    public ResponseEntity<?> getRestaurantById(@PathVariable String restaurantId) {
        try {
            User restaurant = userRepository.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            
            if (restaurant.getRole() != User.UserRole.RESTAURANT) {
                return ResponseEntity.badRequest().body(Map.of("error", "User is not a restaurant"));
            }
            
            return ResponseEntity.ok(restaurant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<?> getRestaurantMenu(@PathVariable String restaurantId) {
        try {
            List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdAndIsAvailable(restaurantId, true);
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{restaurantId}/menu")
    public ResponseEntity<?> addMenuItem(@PathVariable String restaurantId, @RequestBody MenuItem menuItem) {
        try {
            // Verify restaurant exists
            userRepository.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            
            menuItem.setRestaurantId(restaurantId);
            menuItem.setIsAvailable(true);
            MenuItem savedMenuItem = menuItemRepository.save(menuItem);
            
            return ResponseEntity.ok(savedMenuItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{restaurantId}/menu/{menuItemId}")
    public ResponseEntity<?> updateMenuItem(@PathVariable String restaurantId, 
                                         @PathVariable String menuItemId, 
                                         @RequestBody MenuItem menuItem) {
        try {
            MenuItem existingMenuItem = menuItemRepository.findById(menuItemId)
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            
            if (!existingMenuItem.getRestaurantId().equals(restaurantId)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Menu item does not belong to this restaurant"));
            }
            
            menuItem.setId(menuItemId);
            menuItem.setRestaurantId(restaurantId);
            MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
            
            return ResponseEntity.ok(updatedMenuItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{restaurantId}/menu/{menuItemId}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable String restaurantId, 
                                         @PathVariable String menuItemId) {
        try {
            MenuItem menuItem = menuItemRepository.findById(menuItemId)
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            
            if (!menuItem.getRestaurantId().equals(restaurantId)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Menu item does not belong to this restaurant"));
            }
            
            menuItemRepository.delete(menuItem);
            return ResponseEntity.ok(Map.of("message", "Menu item deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
