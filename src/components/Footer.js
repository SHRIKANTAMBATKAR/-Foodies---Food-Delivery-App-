import React from 'react';
import { Link } from 'react-router-dom';
import { FiFacebook, FiTwitter, FiInstagram, FiMail, FiPhone, FiMapPin } from 'react-icons/fi';

const Footer = () => {
  return (
    <footer className="bg-gray-900 text-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Company Info */}
          <div className="col-span-1 md:col-span-2">
            <div className="flex items-center space-x-2 mb-4">
              <div className="w-8 h-8 bg-orange-500 rounded-lg flex items-center justify-center">
                <span className="text-white font-bold text-lg">F</span>
              </div>
              <span className="text-xl font-bold">Foodies</span>
            </div>
            <p className="text-gray-300 mb-4 max-w-md">
              Your favorite food delivered fast and fresh. Order from the best restaurants in your area.
            </p>
            <div className="flex space-x-4">
              <a href="#" className="text-gray-300 hover:text-orange-500 transition-colors">
                <FiFacebook className="w-5 h-5" />
              </a>
              <a href="#" className="text-gray-300 hover:text-orange-500 transition-colors">
                <FiTwitter className="w-5 h-5" />
              </a>
              <a href="#" className="text-gray-300 hover:text-orange-500 transition-colors">
                <FiInstagram className="w-5 h-5" />
              </a>
            </div>
          </div>

          {/* Quick Links */}
          <div>
            <h3 className="text-lg font-semibold mb-4">Quick Links</h3>
            <ul className="space-y-2">
              <li>
                <Link to="/restaurants" className="text-gray-300 hover:text-orange-500 transition-colors">
                  Restaurants
                </Link>
              </li>
              <li>
                <Link to="/about" className="text-gray-300 hover:text-orange-500 transition-colors">
                  About Us
                </Link>
              </li>
              <li>
                <Link to="/contact" className="text-gray-300 hover:text-orange-500 transition-colors">
                  Contact
                </Link>
              </li>
              <li>
                <Link to="/help" className="text-gray-300 hover:text-orange-500 transition-colors">
                  Help Center
                </Link>
              </li>
            </ul>
          </div>

          {/* Contact Info */}
          <div>
            <h3 className="text-lg font-semibold mb-4">Contact Info</h3>
            <div className="space-y-3">
              <div className="flex items-center space-x-3">
                <FiPhone className="w-5 h-5 text-orange-500" />
                <span className="text-gray-300">+1 (555) 123-4567</span>
              </div>
              <div className="flex items-center space-x-3">
                <FiMail className="w-5 h-5 text-orange-500" />
                <span className="text-gray-300">support@foodies.com</span>
              </div>
              <div className="flex items-start space-x-3">
                <FiMapPin className="w-5 h-5 text-orange-500 mt-1" />
                <span className="text-gray-300">
                  123 Food Street<br />
                  New York, NY 10001
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="border-t border-gray-700 mt-8 pt-8">
          <div className="flex flex-col md:flex-row justify-between items-center">
            <p className="text-gray-300 text-sm">
              © 2024 Foodies. All rights reserved.
            </p>
            <div className="flex space-x-6 mt-4 md:mt-0">
              <a href="#" className="text-gray-300 hover:text-orange-500 transition-colors text-sm">
                Privacy Policy
              </a>
              <a href="#" className="text-gray-300 hover:text-orange-500 transition-colors text-sm">
                Terms of Service
              </a>
              <a href="#" className="text-gray-300 hover:text-orange-500 transition-colors text-sm">
                Cookie Policy
              </a>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
