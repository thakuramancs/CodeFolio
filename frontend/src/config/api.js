const API_BASE_URL = process.env.NODE_ENV === 'production' 
  ? 'https://codefolio-gateway.onrender.com'
  : 'http://localhost:8080';

export const API_URLS = {
  contests: {
    active: `${API_BASE_URL}/api/contests/active`,
    upcoming: `${API_BASE_URL}/api/contests/upcoming`,
    past: `${API_BASE_URL}/api/contests/past`,
    all: `${API_BASE_URL}/api/contests`,
  },
  profiles: {
    base: `${API_BASE_URL}/api/profiles`,
  },
  auth: {
    base: `${API_BASE_URL}/auth`,
  },
};

export const getApiUrl = (path) => `${API_BASE_URL}${path}`;

export default API_BASE_URL; 