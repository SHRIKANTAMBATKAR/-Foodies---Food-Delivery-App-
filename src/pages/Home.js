import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useQuery } from 'react-query';
import { restaurantAPI } from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import LoadingSpinner from '../components/LoadingSpinner';
import { FiSearch, FiStar, FiClock, FiTruck } from 'react-icons/fi';

const Home = () => {
  const { user, isCustomer } = useAuth();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCuisine, setSelectedCuisine] = useState('');

  const { data: restaurants, isLoading, error } = useQuery(
    ['restaurants', searchQuery, selectedCuisine],
    () => restaurantAPI.search({ name: searchQuery, cuisineType: selectedCuisine }),
    {
      enabled: true,
      staleTime: 5 * 60 * 1000, // 5 minutes
    }
  );

  const cuisineTypes = [
    'Indian', 'Chinese', 'Italian', 'Mexican', 'Thai', 'Japanese', 'American', 'Mediterranean'
  ];

  const features = [
    {
      icon: <FiTruck className="w-8 h-8 text-orange-500" />,
      title: 'Fast Delivery',
      description: 'Get your food delivered in 30 minutes or less'
    },
    {
      icon: <FiStar className="w-8 h-8 text-orange-500" />,
      title: 'Quality Food',
      description: 'Fresh ingredients from the best restaurants'
    },
    {
      icon: <FiClock className="w-8 h-8 text-orange-500" />,
      title: '24/7 Service',
      description: 'Order anytime, anywhere'
    }
  ];

  if (isLoading) {
    return <LoadingSpinner text="Loading restaurants..." />;
  }

  if (error) {
    return (
      <div className="text-center py-12">
        <p className="text-red-600">Failed to load restaurants. Please try again.</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <section className="bg-gradient-to-r from-orange-500 to-red-500 text-white py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h1 className="text-4xl md:text-6xl font-bold mb-6">
            Delicious Food Delivered to Your Door
          </h1>
          <p className="text-xl md:text-2xl mb-8 text-orange-100">
            Order from your favorite restaurants and get it delivered fast
          </p>
          
          {/* Search Bar */}
          <div className="max-w-2xl mx-auto">
            <div className="relative">
              <input
                type="text"
                placeholder="Search for restaurants or cuisines..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full px-6 py-4 pr-12 text-gray-900 rounded-lg text-lg focus:outline-none focus:ring-2 focus:ring-orange-300"
              />
              <FiSearch className="absolute right-4 top-1/2 transform -translate-y-1/2 w-6 h-6 text-gray-400" />
            </div>
          </div>
        </div>
      </section>

      {/* Cuisine Filter */}
      <section className="py-8 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-wrap gap-2 justify-center">
            <button
              onClick={() => setSelectedCuisine('')}
              className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                selectedCuisine === '' 
                  ? 'bg-orange-500 text-white' 
                  : 'bg-white text-gray-700 hover:bg-orange-50'
              }`}
            >
              All Cuisines
            </button>
            {cuisineTypes.map((cuisine) => (
              <button
                key={cuisine}
                onClick={() => setSelectedCuisine(cuisine)}
                className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                  selectedCuisine === cuisine 
                    ? 'bg-orange-500 text-white' 
                    : 'bg-white text-gray-700 hover:bg-orange-50'
                }`}
              >
                {cuisine}
              </button>
            ))}
          </div>
        </div>
      </section>

      {/* Restaurants Section */}
      <section className="py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-8">
            {searchQuery || selectedCuisine ? 'Search Results' : 'Popular Restaurants'}
          </h2>
          
          {restaurants && restaurants.length > 0 ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {restaurants.map((restaurant) => (
                <Link
                  key={restaurant.id}
                  to={`/restaurants/${restaurant.id}`}
                  className="card hover:shadow-lg transition-shadow"
                >
                  <div className="aspect-w-16 aspect-h-9">
                    <img
                      src={restaurant.restaurantImageUrl || '/api/placeholder/300/200'}
                      alt={restaurant.restaurantName}
                      className="w-full h-48 object-cover"
                    />
                  </div>
                  <div className="p-4">
                    <h3 className="text-lg font-semibold text-gray-900 mb-2">
                      {restaurant.restaurantName}
                    </h3>
                    <p className="text-gray-600 text-sm mb-2">
                      {restaurant.cuisineType}
                    </p>
                    <div className="flex items-center justify-between">
                      <div className="flex items-center">
                        <FiStar className="w-4 h-4 text-yellow-400 mr-1" />
                        <span className="text-sm text-gray-600">
                          {restaurant.rating ? restaurant.rating.toFixed(1) : 'N/A'}
                        </span>
                      </div>
                      <span className="text-sm text-gray-500">
                        {restaurant.totalReviews || 0} reviews
                      </span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          ) : (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg">No restaurants found</p>
            </div>
          )}
        </div>
      </section>

      {/* Features Section */}
      <section className="py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl font-bold text-center text-gray-900 mb-12">
            Why Choose Foodies?
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <div key={index} className="text-center">
                <div className="flex justify-center mb-4">
                  {feature.icon}
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-2">
                  {feature.title}
                </h3>
                <p className="text-gray-600">
                  {feature.description}
                </p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      {!isCustomer && (
        <section className="py-16 bg-orange-500">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
            <h2 className="text-3xl font-bold text-white mb-4">
              Ready to Order?
            </h2>
            <p className="text-xl text-orange-100 mb-8">
              Sign up now and get 20% off your first order
            </p>
            <Link
              to="/register"
              className="inline-block bg-white text-orange-500 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition-colors"
            >
              Get Started
            </Link>
          </div>
        </section>
      )}
    </div>
  );
};

export default Home;
