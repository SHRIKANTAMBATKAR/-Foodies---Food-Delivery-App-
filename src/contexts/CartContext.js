import React, { createContext, useContext, useState, useEffect } from 'react';
import toast from 'react-hot-toast';

const CartContext = createContext();

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};

export const CartProvider = ({ children }) => {
  const [items, setItems] = useState([]);
  const [restaurantId, setRestaurantId] = useState(null);

  useEffect(() => {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      const { items: savedItems, restaurantId: savedRestaurantId } = JSON.parse(savedCart);
      setItems(savedItems || []);
      setRestaurantId(savedRestaurantId);
    }
  }, []);

  useEffect(() => {
    localStorage.setItem('cart', JSON.stringify({ items, restaurantId }));
  }, [items, restaurantId]);

  const addItem = (menuItem, quantity = 1, variant = null, specialInstructions = '') => {
    setItems(prevItems => {
      // Check if we're adding from a different restaurant
      if (restaurantId && restaurantId !== menuItem.restaurantId) {
        if (window.confirm('You have items from another restaurant in your cart. Do you want to clear the cart and add this item?')) {
          setRestaurantId(menuItem.restaurantId);
          const newItem = {
            id: `${menuItem.id}_${variant || 'default'}`,
            menuItemId: menuItem.id,
            name: menuItem.name,
            price: variant ? variant.price : menuItem.price,
            imageUrl: menuItem.imageUrl,
            restaurantId: menuItem.restaurantId,
            quantity,
            variant,
            specialInstructions,
            addedAt: new Date().toISOString()
          };
          toast.success(`${menuItem.name} added to cart`);
          return [newItem];
        } else {
          return prevItems;
        }
      }

      // Set restaurant ID if this is the first item
      if (!restaurantId) {
        setRestaurantId(menuItem.restaurantId);
      }

      // Check if item already exists
      const existingItemIndex = prevItems.findIndex(
        item => item.menuItemId === menuItem.id && 
                item.variant?.name === variant?.name &&
                item.specialInstructions === specialInstructions
      );

      if (existingItemIndex >= 0) {
        const updatedItems = [...prevItems];
        updatedItems[existingItemIndex].quantity += quantity;
        toast.success(`${menuItem.name} quantity updated`);
        return updatedItems;
      } else {
        const newItem = {
          id: `${menuItem.id}_${variant?.name || 'default'}_${Date.now()}`,
          menuItemId: menuItem.id,
          name: menuItem.name,
          price: variant ? variant.price : menuItem.price,
          imageUrl: menuItem.imageUrl,
          restaurantId: menuItem.restaurantId,
          quantity,
          variant,
          specialInstructions,
          addedAt: new Date().toISOString()
        };
        toast.success(`${menuItem.name} added to cart`);
        return [...prevItems, newItem];
      }
    });
  };

  const updateQuantity = (itemId, quantity) => {
    if (quantity <= 0) {
      removeItem(itemId);
      return;
    }

    setItems(prevItems =>
      prevItems.map(item =>
        item.id === itemId ? { ...item, quantity } : item
      )
    );
  };

  const removeItem = (itemId) => {
    setItems(prevItems => {
      const updatedItems = prevItems.filter(item => item.id !== itemId);
      
      // Clear restaurant ID if cart is empty
      if (updatedItems.length === 0) {
        setRestaurantId(null);
      }
      
      return updatedItems;
    });
    toast.success('Item removed from cart');
  };

  const clearCart = () => {
    setItems([]);
    setRestaurantId(null);
    toast.success('Cart cleared');
  };

  const getTotalItems = () => {
    return items.reduce((total, item) => total + item.quantity, 0);
  };

  const getSubtotal = () => {
    return items.reduce((total, item) => total + (item.price * item.quantity), 0);
  };

  const getDeliveryFee = () => {
    const subtotal = getSubtotal();
    if (subtotal >= 500) return 0; // Free delivery above ₹500
    return 40; // ₹40 delivery fee
  };

  const getTax = () => {
    const subtotal = getSubtotal();
    return subtotal * 0.18; // 18% GST
  };

  const getTotal = () => {
    return getSubtotal() + getDeliveryFee() + getTax();
  };

  const value = {
    items,
    restaurantId,
    addItem,
    updateQuantity,
    removeItem,
    clearCart,
    getTotalItems,
    getSubtotal,
    getDeliveryFee,
    getTax,
    getTotal,
    isEmpty: items.length === 0
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
};
