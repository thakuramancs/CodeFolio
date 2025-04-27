import React from 'react';
import { Card, CardContent, Typography, Button, Box } from '@mui/material';
import { format } from 'date-fns';

const ContestCard = ({ contest }) => {
  const {
    title,
    description,
    startTime,
    endTime,
    platform,
    url,
    status
  } = contest;

  const formatDate = (dateString) => {
    return format(new Date(dateString), 'MMM dd, yyyy HH:mm');
  };

  return (
    <Card className="contest-card h-full flex flex-col">
      <CardContent className="flex-grow">
        <Typography variant="h6" component="h2" gutterBottom>
          {title}
        </Typography>
        <Typography variant="body2" color="text.secondary" paragraph>
          {description}
        </Typography>
        <Box className="mt-4">
          <Typography variant="body2" color="text.secondary">
            Start: {formatDate(startTime)}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            End: {formatDate(endTime)}
          </Typography>
          <Typography variant="body2" color="text.secondary" className="mt-2">
            Platform: {platform}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Status: {status.charAt(0).toUpperCase() + status.slice(1)}
          </Typography>
        </Box>
      </CardContent>
      <Box className="p-4 mt-auto">
        <Button
          variant="contained"
          color="primary"
          href={url}
          target="_blank"
          rel="noopener noreferrer"
          fullWidth
        >
          View Contest
        </Button>
      </Box>
    </Card>
  );
};

export default ContestCard; 