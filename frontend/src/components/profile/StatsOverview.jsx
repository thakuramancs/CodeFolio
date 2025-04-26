import React from 'react';
import { Box, Typography, Paper } from '@mui/material';
import { styled } from '@mui/material/styles';

const StyledPaper = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.background.paper,
  color: theme.palette.text.primary,
  padding: theme.spacing(3),
  marginBottom: theme.spacing(3),
  display: 'flex',
  justifyContent: 'space-around',
  alignItems: 'center',
  borderRadius: theme.shape.borderRadius,
  boxShadow: theme.shadows[1],
  '&:hover': {
    boxShadow: theme.shadows[2],
    transform: 'translateY(-2px)',
  }
}));

const StatItem = styled(Box)(({ theme }) => ({
  textAlign: 'center',
  flex: 1,
  padding: theme.spacing(2),
  '& .MuiTypography-h3': {
    color: theme.palette.primary.main,
    marginBottom: theme.spacing(1),
    fontWeight: 600
  },
  '& .MuiTypography-body1': {
    color: theme.palette.text.secondary,
    fontSize: '1rem',
    fontWeight: 500
  }
}));

const StatsOverview = ({ totalQuestions, totalActiveDays }) => {
  return (
    <StyledPaper>
      <StatItem>
        <Typography variant="h3" component="div">
          {totalQuestions}
        </Typography>
        <Typography variant="body1">
          Total Questions
        </Typography>
      </StatItem>

      <StatItem>
        <Typography variant="h3" component="div">
          {totalActiveDays}
        </Typography>
        <Typography variant="body1">
          Active Days
        </Typography>
      </StatItem>
    </StyledPaper>
  );
};

export default StatsOverview; 