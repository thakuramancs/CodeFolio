import React, { useState, useEffect } from 'react';
import { Box, Typography, CircularProgress, Grid, Paper, Alert } from '@mui/material';
import { styled } from '@mui/material/styles';
import { profileService } from '../../services/profileService';

const StyledPaper = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(2),
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
  backgroundColor: theme.palette.background.paper,
}));

const ContributionBox = styled(Box)(({ count }) => ({
  width: '10px',
  height: '10px',
  margin: '1px',
  borderRadius: '2px',
  backgroundColor: getContributionColor(count),
}));

function getContributionColor(count) {
  if (count === 0) return '#ebedf0';
  if (count <= 3) return '#9be9a8';
  if (count <= 6) return '#40c463';
  if (count <= 9) return '#30a14e';
  return '#216e39';
}

const GitHubProfile = ({ userId }) => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchGitHubStats = async () => {
      if (!userId) {
        setError('User ID is required');
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError(null);
        const data = await profileService.getGitHubStats(userId);
        setStats(data);
      } catch (err) {
        setError(err.response?.data?.message || err.message || 'Failed to fetch GitHub stats');
        setStats(null);
      } finally {
        setLoading(false);
      }
    };

    fetchGitHubStats();
  }, [userId]);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box p={2}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  if (!stats) {
    return (
      <Box p={2}>
        <Alert severity="info">No GitHub statistics available</Alert>
      </Box>
    );
  }

  return (
    <Grid container spacing={3}>
      <Grid item xs={12} md={6}>
        <StyledPaper>
          <Typography variant="h6" gutterBottom>Overview</Typography>
          <Box display="flex" flexDirection="column" gap={1}>
            <Typography>Public Repositories: {stats.repositories || 0}</Typography>
            <Typography>Total Stars: {stats.stars || 0}</Typography>
            <Typography>Followers: {stats.followers || 0}</Typography>
            <Typography>Following: {stats.following || 0}</Typography>
          </Box>
        </StyledPaper>
      </Grid>
      
      <Grid item xs={12} md={6}>
        <StyledPaper>
          <Typography variant="h6" gutterBottom>Activity</Typography>
          <Box display="flex" flexDirection="column" gap={1}>
            <Typography>Total Contributions: {stats.totalContributions || 0}</Typography>
            <Typography>Active Days: {stats.totalActiveDays || 0}</Typography>
            <Typography>Current Streak: {stats.currentStreak || 0} days</Typography>
            <Typography>Longest Streak: {stats.maxStreak || 0} days</Typography>
          </Box>
        </StyledPaper>
      </Grid>

      <Grid item xs={12} md={6}>
        <StyledPaper>
          <Typography variant="h6" gutterBottom>Development</Typography>
          <Box display="flex" flexDirection="column" gap={1}>
            <Typography>Pull Requests: {stats.prs || 0}</Typography>
            <Typography>Issues: {stats.issues || 0}</Typography>
            <Typography>Commits: {stats.commits || 0}</Typography>
          </Box>
        </StyledPaper>
      </Grid>

      {stats.languages && Object.keys(stats.languages).length > 0 && (
        <Grid item xs={12} md={6}>
          <StyledPaper>
            <Typography variant="h6" gutterBottom>Languages</Typography>
            <Box display="flex" flexDirection="column" gap={1}>
              {Object.entries(stats.languages)
                .sort(([, a], [, b]) => b - a)
                .map(([language, percentage]) => (
                  <Box key={language} display="flex" justifyContent="space-between">
                    <Typography>{language}</Typography>
                    <Typography>{percentage.toFixed(1)}%</Typography>
                  </Box>
                ))}
            </Box>
          </StyledPaper>
        </Grid>
      )}

      {stats.contributionCalendar && stats.contributionCalendar.length > 0 && (
        <Grid item xs={12}>
          <StyledPaper>
            <Typography variant="h6" gutterBottom>Contribution Calendar</Typography>
            <Box 
              display="flex" 
              flexWrap="wrap" 
              gap={0.5} 
              maxWidth="100%" 
              sx={{ overflowX: 'auto', padding: 1 }}
            >
              {stats.contributionCalendar.map((day, index) => (
                <ContributionBox
                  key={day.date}
                  count={day.count}
                  title={`${day.date}: ${day.count} contributions`}
                />
              ))}
            </Box>
          </StyledPaper>
        </Grid>
      )}
    </Grid>
  );
};

export default GitHubProfile; 