import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAuth } from '../contexts/AuthContext';
import { FiEye, FiEyeOff, FiMail, FiLock, FiUser, FiPhone } from 'react-icons/fi';

const Register = () => {
  const { register: registerUser } = useAuth();
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [userRole, setUserRole] = useState('CUSTOMER');

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const password = watch('password');

  const onSubmit = async (data) => {
    setIsLoading(true);
    try {
      const userData = {
        ...data,
        role: userRole,
        phoneNumber: data.phoneNumber,
      };
      await registerUser(userData);
      navigate('/login');
    } catch (error) {
      console.error('Registration error:', error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <div>
          <div className="flex justify-center">
            <div className="w-12 h-12 bg-orange-500 rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-2xl">F</span>
            </div>
          </div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            Create your account
          </h2>
          <p className="mt-2 text-center text-sm text-gray-600">
            Or{' '}
            <Link
              to="/login"
              className="font-medium text-orange-600 hover:text-orange-500"
            >
              sign in to your existing account
            </Link>
          </p>
        </div>

        <form className="mt-8 space-y-6" onSubmit={handleSubmit(onSubmit)}>
          {/* User Role Selection */}
          <div>
            <label className="form-label">I want to register as:</label>
            <div className="grid grid-cols-2 gap-4 mt-2">
              {[
                { value: 'CUSTOMER', label: 'Customer' },
                { value: 'RESTAURANT', label: 'Restaurant' },
                { value: 'DELIVERY_PARTNER', label: 'Delivery Partner' },
              ].map((role) => (
                <label key={role.value} className="flex items-center">
                  <input
                    type="radio"
                    value={role.value}
                    checked={userRole === role.value}
                    onChange={(e) => setUserRole(e.target.value)}
                    className="h-4 w-4 text-orange-600 focus:ring-orange-500 border-gray-300"
                  />
                  <span className="ml-2 text-sm text-gray-700">{role.label}</span>
                </label>
              ))}
            </div>
          </div>

          <div className="space-y-4">
            <div>
              <label htmlFor="name" className="form-label">
                Full Name
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <FiUser className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  {...register('name', {
                    required: 'Name is required',
                    minLength: {
                      value: 2,
                      message: 'Name must be at least 2 characters',
                    },
                  })}
                  type="text"
                  className="form-input pl-10"
                  placeholder="Enter your full name"
                />
              </div>
              {errors.name && (
                <p className="form-error">{errors.name.message}</p>
              )}
            </div>

            <div>
              <label htmlFor="email" className="form-label">
                Email address
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <FiMail className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  {...register('email', {
                    required: 'Email is required',
                    pattern: {
                      value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                      message: 'Invalid email address',
                    },
                  })}
                  type="email"
                  className="form-input pl-10"
                  placeholder="Enter your email"
                />
              </div>
              {errors.email && (
                <p className="form-error">{errors.email.message}</p>
              )}
            </div>

            <div>
              <label htmlFor="phoneNumber" className="form-label">
                Phone Number
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <FiPhone className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  {...register('phoneNumber', {
                    required: 'Phone number is required',
                    pattern: {
                      value: /^\+?[1-9]\d{1,14}$/,
                      message: 'Invalid phone number',
                    },
                  })}
                  type="tel"
                  className="form-input pl-10"
                  placeholder="Enter your phone number"
                />
              </div>
              {errors.phoneNumber && (
                <p className="form-error">{errors.phoneNumber.message}</p>
              )}
            </div>

            <div>
              <label htmlFor="password" className="form-label">
                Password
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <FiLock className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  {...register('password', {
                    required: 'Password is required',
                    minLength: {
                      value: 6,
                      message: 'Password must be at least 6 characters',
                    },
                  })}
                  type={showPassword ? 'text' : 'password'}
                  className="form-input pl-10 pr-10"
                  placeholder="Enter your password"
                />
                <button
                  type="button"
                  className="absolute inset-y-0 right-0 pr-3 flex items-center"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? (
                    <FiEyeOff className="h-5 w-5 text-gray-400" />
                  ) : (
                    <FiEye className="h-5 w-5 text-gray-400" />
                  )}
                </button>
              </div>
              {errors.password && (
                <p className="form-error">{errors.password.message}</p>
              )}
            </div>

            <div>
              <label htmlFor="confirmPassword" className="form-label">
                Confirm Password
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <FiLock className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  {...register('confirmPassword', {
                    required: 'Please confirm your password',
                    validate: (value) =>
                      value === password || 'Passwords do not match',
                  })}
                  type={showConfirmPassword ? 'text' : 'password'}
                  className="form-input pl-10 pr-10"
                  placeholder="Confirm your password"
                />
                <button
                  type="button"
                  className="absolute inset-y-0 right-0 pr-3 flex items-center"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                >
                  {showConfirmPassword ? (
                    <FiEyeOff className="h-5 w-5 text-gray-400" />
                  ) : (
                    <FiEye className="h-5 w-5 text-gray-400" />
                  )}
                </button>
              </div>
              {errors.confirmPassword && (
                <p className="form-error">{errors.confirmPassword.message}</p>
              )}
            </div>

            {/* Restaurant specific fields */}
            {userRole === 'RESTAURANT' && (
              <>
                <div>
                  <label htmlFor="restaurantName" className="form-label">
                    Restaurant Name
                  </label>
                  <input
                    {...register('restaurantName', {
                      required: userRole === 'RESTAURANT' ? 'Restaurant name is required' : false,
                    })}
                    type="text"
                    className="form-input"
                    placeholder="Enter restaurant name"
                  />
                  {errors.restaurantName && (
                    <p className="form-error">{errors.restaurantName.message}</p>
                  )}
                </div>

                <div>
                  <label htmlFor="cuisineType" className="form-label">
                    Cuisine Type
                  </label>
                  <select
                    {...register('cuisineType', {
                      required: userRole === 'RESTAURANT' ? 'Cuisine type is required' : false,
                    })}
                    className="form-input"
                  >
                    <option value="">Select cuisine type</option>
                    <option value="Indian">Indian</option>
                    <option value="Chinese">Chinese</option>
                    <option value="Italian">Italian</option>
                    <option value="Mexican">Mexican</option>
                    <option value="Thai">Thai</option>
                    <option value="Japanese">Japanese</option>
                    <option value="American">American</option>
                    <option value="Mediterranean">Mediterranean</option>
                  </select>
                  {errors.cuisineType && (
                    <p className="form-error">{errors.cuisineType.message}</p>
                  )}
                </div>
              </>
            )}

            {/* Delivery partner specific fields */}
            {userRole === 'DELIVERY_PARTNER' && (
              <>
                <div>
                  <label htmlFor="vehicleType" className="form-label">
                    Vehicle Type
                  </label>
                  <select
                    {...register('vehicleType', {
                      required: userRole === 'DELIVERY_PARTNER' ? 'Vehicle type is required' : false,
                    })}
                    className="form-input"
                  >
                    <option value="">Select vehicle type</option>
                    <option value="BICYCLE">Bicycle</option>
                    <option value="MOTORCYCLE">Motorcycle</option>
                    <option value="CAR">Car</option>
                  </select>
                  {errors.vehicleType && (
                    <p className="form-error">{errors.vehicleType.message}</p>
                  )}
                </div>

                <div>
                  <label htmlFor="vehicleNumber" className="form-label">
                    Vehicle Number
                  </label>
                  <input
                    {...register('vehicleNumber', {
                      required: userRole === 'DELIVERY_PARTNER' ? 'Vehicle number is required' : false,
                    })}
                    type="text"
                    className="form-input"
                    placeholder="Enter vehicle number"
                  />
                  {errors.vehicleNumber && (
                    <p className="form-error">{errors.vehicleNumber.message}</p>
                  )}
                </div>
              </>
            )}
          </div>

          <div className="flex items-center">
            <input
              id="terms"
              name="terms"
              type="checkbox"
              required
              className="h-4 w-4 text-orange-600 focus:ring-orange-500 border-gray-300 rounded"
            />
            <label htmlFor="terms" className="ml-2 block text-sm text-gray-900">
              I agree to the{' '}
              <a href="#" className="text-orange-600 hover:text-orange-500">
                Terms of Service
              </a>{' '}
              and{' '}
              <a href="#" className="text-orange-600 hover:text-orange-500">
                Privacy Policy
              </a>
            </label>
          </div>

          <div>
            <button
              type="submit"
              disabled={isLoading}
              className="btn btn-primary w-full"
            >
              {isLoading ? (
                <div className="flex items-center justify-center">
                  <div className="spinner w-4 h-4 mr-2"></div>
                  Creating account...
                </div>
              ) : (
                'Create account'
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register;
