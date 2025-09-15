import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { CartProvider } from './contexts/CartContext';
import { WebSocketProvider } from './contexts/WebSocketContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import RestaurantList from './pages/RestaurantList';
import RestaurantDetail from './pages/RestaurantDetail';
import MenuItemDetail from './pages/MenuItemDetail';
import Cart from './pages/Cart';
import Checkout from './pages/Checkout';
import OrderTracking from './pages/OrderTracking';
import Profile from './pages/Profile';
import RestaurantDashboard from './pages/RestaurantDashboard';
import DeliveryDashboard from './pages/DeliveryDashboard';
import AdminDashboard from './pages/AdminDashboard';
import NotFound from './pages/NotFound';

function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <WebSocketProvider>
          <div className="App">
            <Routes>
              {/* Public routes */}
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/restaurants" element={<RestaurantList />} />
              <Route path="/restaurants/:id" element={<RestaurantDetail />} />
              <Route path="/menu-items/:id" element={<MenuItemDetail />} />
              
              {/* Protected routes with layout */}
              <Route path="/" element={
                <ProtectedRoute>
                  <Layout />
                </ProtectedRoute>
              }>
                <Route index element={<Home />} />
                <Route path="cart" element={<Cart />} />
                <Route path="checkout" element={<Checkout />} />
                <Route path="orders/:id" element={<OrderTracking />} />
                <Route path="profile" element={<Profile />} />
                
                {/* Restaurant routes */}
                <Route path="restaurant/*" element={
                  <ProtectedRoute allowedRoles={['RESTAURANT', 'ADMIN']}>
                    <RestaurantDashboard />
                  </ProtectedRoute>
                } />
                
                {/* Delivery partner routes */}
                <Route path="delivery/*" element={
                  <ProtectedRoute allowedRoles={['DELIVERY_PARTNER', 'ADMIN']}>
                    <DeliveryDashboard />
                  </ProtectedRoute>
                } />
                
                {/* Admin routes */}
                <Route path="admin/*" element={
                  <ProtectedRoute allowedRoles={['ADMIN']}>
                    <AdminDashboard />
                  </ProtectedRoute>
                } />
              </Route>
              
              {/* 404 route */}
              <Route path="*" element={<NotFound />} />
            </Routes>
          </div>
        </WebSocketProvider>
      </CartProvider>
    </AuthProvider>
  );
}

export default App;
