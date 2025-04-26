import React from 'react';
import { Card, CardContent, Typography, Button, Chip, Box } from '@mui/material';
import { AccessTime, Event, Language } from '@mui/icons-material';

const ContestCard = ({ contest }) => {
  const startTime = new Date(contest.startTime);
  const endTime = new Date(startTime.getTime() + contest.duration);
  const durationHours = Math.floor(contest.duration / (1000 * 60 * 60));
  const durationMinutes = Math.floor((contest.duration % (1000 * 60 * 60)) / (1000 * 60));

  const formatDate = (date) => {
    return date.toLocaleString('en-US', {
      weekday: 'short',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: true
    });
  };

  const getPlatformColor = (platform) => {
    const colors = {
      'LeetCode': 'warning',
      'Codeforces': 'error',
      'CodeChef': 'success',
      'HackerRank': 'info',
      'GeeksforGeeks': 'secondary'
    };
    return colors[platform] || 'default';
  };

  return (
    <Card className="hover:shadow-lg transition-shadow duration-200">
      <CardContent>
        <Box className="flex flex-col md:flex-row justify-between items-start gap-4">
          <Box className="flex-grow">
            <Typography variant="h6" component="h2" className="mb-2">
              {contest.name}
            </Typography>
            
            <Box className="flex items-center gap-2 mb-2">
              <Language fontSize="small" />
              <Chip 
                label={contest.platform} 
                color={getPlatformColor(contest.platform)}
                size="small"
              />
            </Box>

            <Box className="flex items-center gap-2 mb-2">
              <Event fontSize="small" />
              <Typography variant="body2" color="text.secondary">
                Starts: {formatDate(startTime)}
              </Typography>
            </Box>

            <Box className="flex items-center gap-2">
              <AccessTime fontSize="small" />
              <Typography variant="body2" color="text.secondary">
                Duration: {durationHours}h {durationMinutes > 0 ? `${durationMinutes}m` : ''}
              </Typography>
            </Box>
          </Box>

          <Box className="w-full md:w-auto">
            <Button 
              variant="contained" 
              color="primary" 
              href={contest.url} 
              target="_blank"
              rel="noopener noreferrer"
              className="w-full md:w-auto"
              size="large"
            >
              View Contest
            </Button>
          </Box>
        </Box>
      </CardContent>
    </Card>
  );
};

export default ContestCard; 