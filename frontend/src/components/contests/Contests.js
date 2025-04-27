import React, { useEffect, useState } from 'react';
import { API_URLS } from '../../config/api';
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
        const [activeResponse, upcomingResponse] = await Promise.all([
          fetch(API_URLS.contests.active),
          fetch(API_URLS.contests.upcoming)
        ]);

        if (!activeResponse.ok || !upcomingResponse.ok) {
          throw new Error('Failed to fetch contests');
        }

        const activeContests = await activeResponse.json();
        const upcomingContests = await upcomingResponse.json();

        setContests([...activeContests, ...upcomingContests]);
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

  if (loading) return <div>Loading contests...</div>;
  if (error) return <div className="error-message">{error}</div>;
  if (!contests.length) return <div>No contests available.</div>;

  const filteredContests = contests.filter(contest => contest.status === activeTab);

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
          Upcoming ({contests.filter(c => c.status === 'upcoming').length})
        </ToggleButton>
        <ToggleButton value="active">
          Active ({contests.filter(c => c.status === 'active').length})
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