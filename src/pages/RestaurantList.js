import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useQuery } from 'react-query';
import { restaurantAPI } from '../services/api';
import LoadingSpinner from '../components/LoadingSpinner';
import { FiSearch, FiStar, FiClock, FiMapPin } from 'react-icons/fi';

const RestaurantList = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCuisine, setSelectedCuisine] = useState('');
  const [sortBy, setSortBy] = useState('rating');

  const { data: restaurants, isLoading, error } = useQuery(
    ['restaurants', searchQuery, selectedCuisine, sortBy],
    () => restaurantAPI.search({ name: searchQuery, cuisineType: selectedCuisine }),
    {
      enabled: true,
      staleTime: 5 * 60 * 1000,
    }
  );

  const cuisineTypes = [
    'Indian', 'Chinese', 'Italian', 'Mexican', 'Thai', 'Japanese', 'American', 'Mediterranean'
  ];

  const sortOptions = [
    { value: 'rating', label: 'Rating' },
    { value: 'name', label: 'Name' },
    { value: 'delivery_time', label: 'Delivery Time' },
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
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-4">Restaurants</h1>
          
          {/* Search and Filters */}
          <div className="flex flex-col md:flex-row gap-4 mb-6">
            <div className="flex-1">
              <div className="relative">
                <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                <input
                  type="text"
                  placeholder="Search restaurants..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                />
              </div>
            </div>
            
            <select
              value={selectedCuisine}
              onChange={(e) => setSelectedCuisine(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
            >
              <option value="">All Cuisines</option>
              {cuisineTypes.map((cuisine) => (
                <option key={cuisine} value={cuisine}>
                  {cuisine}
                </option>
              ))}
            </select>
            
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
            >
              {sortOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  Sort by {option.label}
                </option>
              ))}
            </select>
          </div>
        </div>

        {/* Restaurants Grid */}
        {restaurants && restaurants.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {restaurants.map((restaurant) => (
              <Link
                key={restaurant.id}
                to={`/restaurants/${restaurant.id}`}
                className="card hover:shadow-lg transition-all duration-200 transform hover:-translate-y-1"
              >
                <div className="aspect-w-16 aspect-h-9">
                  <img
                    src={restaurant.restaurantImageUrl || '/api/placeholder/400/250'}
                    alt={restaurant.restaurantName}
                    className="w-full h-48 object-cover rounded-t-lg"
                  />
                </div>
                <div className="p-4">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    {restaurant.restaurantName}
                  </h3>
                  <p className="text-gray-600 text-sm mb-2">
                    {restaurant.cuisineType}
                  </p>
                  <p className="text-gray-500 text-sm mb-3 line-clamp-2">
                    {restaurant.restaurantDescription}
                  </p>
                  
                  <div className="flex items-center justify-between mb-3">
                    <div className="flex items-center">
                      <FiStar className="w-4 h-4 text-yellow-400 mr-1" />
                      <span className="text-sm font-medium text-gray-900">
                        {restaurant.rating ? restaurant.rating.toFixed(1) : 'N/A'}
                      </span>
                      <span className="text-sm text-gray-500 ml-1">
                        ({restaurant.totalReviews || 0})
                      </span>
                    </div>
                    <div className="flex items-center text-sm text-gray-500">
                      <FiClock className="w-4 h-4 mr-1" />
                      <span>30-45 min</span>
                    </div>
                  </div>
                  
                  <div className="flex items-center text-sm text-gray-500">
                    <FiMapPin className="w-4 h-4 mr-1" />
                    <span className="truncate">
                      {restaurant.restaurantAddress?.city || 'Location not available'}
                    </span>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        ) : (
          <div className="text-center py-12">
            <div className="w-24 h-24 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center">
              <FiSearch className="w-12 h-12 text-gray-400" />
            </div>
            <h3 className="text-lg font-medium text-gray-900 mb-2">No restaurants found</h3>
            <p className="text-gray-500">
              Try adjusting your search criteria or filters
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default RestaurantList;
