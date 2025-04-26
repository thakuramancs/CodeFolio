import React, { useEffect, useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import axios from 'axios';
import ContestCard from './ContestCard';
import ContestSkeleton from './ContestSkeleton';
import { ToggleButton, ToggleButtonGroup, Typography, Alert } from '@mui/material';

// Create axios instance with configuration
const axiosInstance = axios.create({
  baseURL: '/api', // Add base URL for all requests
  timeout: 15000, // 15 seconds
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Add retry logic with exponential backoff
axiosInstance.interceptors.response.use(undefined, async (err) => {
  const { config } = err;
  if (!config || config.retry === undefined) {
    return Promise.reject(err);
  }
  
  // Set the retry count
  config.retry = config.retry || 3;
  
  if (config.retry === 0) {
    return Promise.reject(err);
  }
  
  // Decrease the retry count
  config.retry -= 1;
  
  // Create new promise to handle retry with exponential backoff
  const backoff = new Promise((resolve) => {
    const backoffTime = Math.min(1000 * (3 - config.retry) * 2, 10000);
    console.log(`Retrying request: ${config.url} in ${backoffTime}ms`);
    setTimeout(resolve, backoffTime);
  });
  
  await backoff;
  return axiosInstance(config);
});

const Contests = () => {
  const { getAccessTokenSilently, isAuthenticated } = useAuth0();
  const [contests, setContests] = useState({ upcoming: [], active: [] });
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('upcoming');
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchContests = async () => {
      if (!isAuthenticated) {
        setLoading(false);
        return;
      }

      setLoading(true);
      setError(null);
      
      try {
        const token = await getAccessTokenSilently();
        
        // Make requests with retry configuration
        const [upcomingRes, activeRes] = await Promise.all([
          axiosInstance.get('/contests/upcoming', {
            headers: {
              'Authorization': `Bearer ${token}`,
              'Accept': 'application/json'
            },
            retry: 3
          }).catch(error => {
            console.error('Error fetching upcoming contests:', error);
            return { data: [] };
          }),
          axiosInstance.get('/contests/active', {
            headers: {
              'Authorization': `Bearer ${token}`,
              'Accept': 'application/json'
            },
            retry: 3
          }).catch(error => {
            console.error('Error fetching active contests:', error);
            return { data: [] };
          })
        ]);

        const upcomingData = Array.isArray(upcomingRes.data) ? upcomingRes.data : [];
        const activeData = Array.isArray(activeRes.data) ? activeRes.data : [];

        console.log('Upcoming contests data:', upcomingData);
        console.log('Active contests data:', activeData);

        setContests({
          upcoming: upcomingData,
          active: activeData
        });
      } catch (error) {
        console.error('Error in fetchContests:', error);
        let errorMessage;
        
        if (error.code === 'ECONNABORTED') {
          errorMessage = 'Request timed out. The server is taking too long to respond. Please try again later.';
        } else if (error.code === 'ERR_NETWORK') {
          errorMessage = 'Unable to connect to the contest service. Please check your internet connection and try again.';
        } else {
          errorMessage = error.response?.data?.message || 
                        error.response?.data?.error || 
                        error.message || 
                        'Failed to fetch contests. Please try again later.';
        }
        
        setError(errorMessage);
        setContests({ upcoming: [], active: [] });
      } finally {
        setLoading(false);
      }
    };

    if (isAuthenticated) {
      fetchContests();
      // Refresh contests every 5 minutes
      const interval = setInterval(fetchContests, 300000);
      return () => clearInterval(interval);
    }
  }, [getAccessTokenSilently, isAuthenticated]);

  const handleTabChange = (event, newTab) => {
    if (newTab !== null) {
      setActiveTab(newTab);
    }
  };

  if (!isAuthenticated) {
    return (
      <Alert severity="info" className="mb-4">
        Please log in to view contests.
      </Alert>
    );
  }

  if (loading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {[1, 2, 3].map((n) => (
          <ContestSkeleton key={n} />
        ))}
      </div>
    );
  }

  if (error) {
    return (
      <Alert severity="error" className="mb-4">
        {error}
      </Alert>
    );
  }

  const currentContests = contests[activeTab] || [];

  return (
    <div>
      <Typography variant="h4" component="h1" className="mb-6">
        Coding Contests
      </Typography>

      <ToggleButtonGroup
        value={activeTab}
        exclusive
        onChange={handleTabChange}
        className="mb-6"
      >
        <ToggleButton value="upcoming">
          Upcoming ({contests.upcoming.length})
        </ToggleButton>
        <ToggleButton value="active">
          Active ({contests.active.length})
        </ToggleButton>
      </ToggleButtonGroup>

      {currentContests.length === 0 ? (
        <Typography variant="body1" color="text.secondary">
          No {activeTab} contests available.
        </Typography>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {currentContests.map((contest) => (
            <ContestCard key={contest.id} contest={contest} />
          ))}
        </div>
      )}
    </div>
  );
};

export default Contests;