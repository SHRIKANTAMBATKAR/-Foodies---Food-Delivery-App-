import React from 'react';
import { useParams } from 'react-router-dom';

const OrderTracking = () => {
  const { id } = useParams();
  
  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">
          Order Tracking - {id}
        </h1>
        <p className="text-gray-600">
          This page will show real-time order tracking with WebSocket updates.
        </p>
      </div>
    </div>
  );
};

export default OrderTracking;
