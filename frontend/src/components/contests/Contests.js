import React, { useEffect, useState } from 'react';
import { API_URLS, fetchApi } from '../../config/api';
import { ToggleButton, ToggleButtonGroup, Typography, Alert } from '@mui/material';
import ContestCard from './ContestCard';
import ContestSkeleton from './ContestSkeleton';

const Contests = () => {
  const [contests, setContests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('upcoming');

  useEffect(() => {
    const fetchContests = async () => {
      try {
        setLoading(true);
        const [activeContests, upcomingContests] = await Promise.all([
          fetchApi(API_URLS.contests.active),
          fetchApi(API_URLS.contests.upcoming)
        ]);

        console.log('Active contests:', activeContests);
        console.log('Upcoming contests:', upcomingContests);

        setContests([
          ...(Array.isArray(activeContests) ? activeContests : []),
          ...(Array.isArray(upcomingContests) ? upcomingContests : [])
        ]);
        setError(null);
      } catch (err) {
        console.error('Error fetching contests:', err);
        setError('Failed to fetch contests. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchContests();
  }, []);

  const handleTabChange = (event, newTab) => {
    if (newTab !== null) {
      setActiveTab(newTab);
    }
  };

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

  const filteredContests = contests.filter(contest => 
    contest && contest.status && contest.status.toLowerCase() === activeTab
  );

  return (
    <div className="container mx-auto px-4 py-8">
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
          Upcoming ({contests.filter(c => c?.status?.toLowerCase() === 'upcoming').length})
        </ToggleButton>
        <ToggleButton value="active">
          Active ({contests.filter(c => c?.status?.toLowerCase() === 'active').length})
        </ToggleButton>
      </ToggleButtonGroup>

      {filteredContests.length === 0 ? (
        <Typography variant="body1" color="text.secondary">
          No {activeTab} contests available.
        </Typography>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredContests.map((contest) => (
            <ContestCard key={contest.id} contest={contest} />
          ))}
        </div>
      )}
    </div>
  );
};

export default Contests;