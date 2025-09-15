import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: (email, password) => api.post('/auth/login', { email, password }).then(res => res.data),
  register: (userData) => api.post('/auth/register', userData).then(res => res.data),
  getProfile: () => api.get('/auth/profile').then(res => res.data),
  updateProfile: (userData) => api.put('/auth/profile', userData).then(res => res.data),
};

// Restaurant API
export const restaurantAPI = {
  getAll: () => api.get('/restaurants').then(res => res.data),
  search: (params) => api.get('/restaurants/search', { params }).then(res => res.data),
  getById: (id) => api.get(`/restaurants/${id}`).then(res => res.data),
  getMenu: (id) => api.get(`/restaurants/${id}/menu`).then(res => res.data),
  addMenuItem: (restaurantId, menuItem) => api.post(`/restaurants/${restaurantId}/menu`, menuItem).then(res => res.data),
  updateMenuItem: (restaurantId, menuItemId, menuItem) => api.put(`/restaurants/${restaurantId}/menu/${menuItemId}`, menuItem).then(res => res.data),
  deleteMenuItem: (restaurantId, menuItemId) => api.delete(`/restaurants/${restaurantId}/menu/${menuItemId}`).then(res => res.data),
};

// Order API
export const orderAPI = {
  create: (order) => api.post('/orders', order).then(res => res.data),
  getByCustomer: (customerId) => api.get(`/orders/customer/${customerId}`).then(res => res.data),
  getByRestaurant: (restaurantId) => api.get(`/orders/restaurant/${restaurantId}`).then(res => res.data),
  getByDeliveryPartner: (deliveryPartnerId) => api.get(`/orders/delivery/${deliveryPartnerId}`).then(res => res.data),
  getById: (id) => api.get(`/orders/${id}`).then(res => res.data),
  updateStatus: (id, status) => api.put(`/orders/${id}/status`, { status }).then(res => res.data),
  assignDeliveryPartner: (id, deliveryPartnerId) => api.put(`/orders/${id}/assign-delivery`, { deliveryPartnerId }).then(res => res.data),
  updateDeliveryLocation: (id, latitude, longitude) => api.put(`/orders/${id}/delivery-location`, { latitude, longitude }).then(res => res.data),
  getPending: () => api.get('/orders/pending').then(res => res.data),
};

// Payment API
export const paymentAPI = {
  createRazorpayOrder: (orderId, customerId, amount) => api.post('/payments/create-razorpay-order', { orderId, customerId, amount }).then(res => res.data),
  verifyRazorpayPayment: (razorpayOrderId, razorpayPaymentId, razorpaySignature) => 
    api.post('/payments/verify-razorpay-payment', { razorpayOrderId, razorpayPaymentId, razorpaySignature }).then(res => res.data),
  getById: (id) => api.get(`/payments/${id}`).then(res => res.data),
  getByOrderId: (orderId) => api.get(`/payments/order/${orderId}`).then(res => res.data),
};

// User API
export const userAPI = {
  getAll: () => api.get('/users').then(res => res.data),
  getById: (id) => api.get(`/users/${id}`).then(res => res.data),
  update: (id, userData) => api.put(`/users/${id}`, userData).then(res => res.data),
  delete: (id) => api.delete(`/users/${id}`).then(res => res.data),
  approveRestaurant: (id) => api.put(`/users/${id}/approve-restaurant`).then(res => res.data),
  approveDeliveryPartner: (id) => api.put(`/users/${id}/approve-delivery-partner`).then(res => res.data),
};

// Review API
export const reviewAPI = {
  create: (review) => api.post('/reviews', review).then(res => res.data),
  getByRestaurant: (restaurantId) => api.get(`/reviews/restaurant/${restaurantId}`).then(res => res.data),
  getByMenuItem: (menuItemId) => api.get(`/reviews/menu-item/${menuItemId}`).then(res => res.data),
  getByDeliveryPartner: (deliveryPartnerId) => api.get(`/reviews/delivery-partner/${deliveryPartnerId}`).then(res => res.data),
  update: (id, review) => api.put(`/reviews/${id}`, review).then(res => res.data),
  delete: (id) => api.delete(`/reviews/${id}`).then(res => res.data),
};

export default api;
