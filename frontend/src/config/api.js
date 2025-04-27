const API_BASE_URL = 'https://codefolio-gateway.onrender.com';

const defaultHeaders = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
};

const fetchWithConfig = async (url) => {
  const response = await fetch(url, {
    headers: defaultHeaders,
    credentials: 'include',
  });
  
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  
  return response.json();
};

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
export const fetchApi = fetchWithConfig;

export default API_BASE_URL; 