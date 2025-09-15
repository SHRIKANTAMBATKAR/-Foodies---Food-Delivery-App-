import React, { createContext, useContext, useEffect, useState } from 'react';
import { io } from 'socket.io-client';
import { useAuth } from './AuthContext';
import toast from 'react-hot-toast';

const WebSocketContext = createContext();

export const useWebSocket = () => {
  const context = useContext(WebSocketContext);
  if (!context) {
    throw new Error('useWebSocket must be used within a WebSocketProvider');
  }
  return context;
};

export const WebSocketProvider = ({ children }) => {
  const [socket, setSocket] = useState(null);
  const [connected, setConnected] = useState(false);
  const [orderUpdates, setOrderUpdates] = useState([]);
  const [deliveryUpdates, setDeliveryUpdates] = useState([]);
  const { user, isAuthenticated } = useAuth();

  useEffect(() => {
    if (isAuthenticated && user) {
      const newSocket = io(process.env.REACT_APP_WS_URL || 'ws://localhost:8080', {
        transports: ['websocket'],
        query: {
          userId: user.id,
          role: user.role
        }
      });

      newSocket.on('connect', () => {
        console.log('WebSocket connected');
        setConnected(true);
      });

      newSocket.on('disconnect', () => {
        console.log('WebSocket disconnected');
        setConnected(false);
      });

      newSocket.on('order_update', (data) => {
        console.log('Order update received:', data);
        setOrderUpdates(prev => [data, ...prev.slice(0, 9)]); // Keep last 10 updates
        
        // Show toast notification
        if (data.type === 'ORDER_UPDATE') {
          toast.success(data.message);
        }
      });

      newSocket.on('delivery_update', (data) => {
        console.log('Delivery update received:', data);
        setDeliveryUpdates(prev => [data, ...prev.slice(0, 9)]); // Keep last 10 updates
        
        // Show toast notification
        if (data.type === 'DELIVERY_UPDATE') {
          toast.info(data.message);
        }
      });

      newSocket.on('error', (error) => {
        console.error('WebSocket error:', error);
        toast.error('Connection error');
      });

      setSocket(newSocket);

      return () => {
        newSocket.close();
      };
    }
  }, [isAuthenticated, user]);

  const sendOrderUpdate = (orderId, status, data) => {
    if (socket && connected) {
      socket.emit('order.update', {
        type: 'ORDER_UPDATE',
        orderId,
        status,
        data,
        timestamp: new Date().toISOString()
      });
    }
  };

  const sendDeliveryLocation = (orderId, latitude, longitude) => {
    if (socket && connected) {
      socket.emit('delivery.location', {
        type: 'DELIVERY_UPDATE',
        orderId,
        latitude,
        longitude,
        timestamp: new Date().toISOString()
      });
    }
  };

  const subscribeToOrderUpdates = (orderId) => {
    if (socket && connected) {
      socket.emit('subscribe.order', orderId);
    }
  };

  const subscribeToDeliveryUpdates = (orderId) => {
    if (socket && connected) {
      socket.emit('subscribe.delivery', orderId);
    }
  };

  const value = {
    socket,
    connected,
    orderUpdates,
    deliveryUpdates,
    sendOrderUpdate,
    sendDeliveryLocation,
    subscribeToOrderUpdates,
    subscribeToDeliveryUpdates
  };

  return (
    <WebSocketContext.Provider value={value}>
      {children}
    </WebSocketContext.Provider>
  );
};
