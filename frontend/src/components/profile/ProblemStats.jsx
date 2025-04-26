import React from 'react';
import { Box, Typography, Paper } from '@mui/material';
import { styled } from '@mui/material/styles';
import { BarChart, Bar, XAxis, YAxis, ResponsiveContainer } from 'recharts';

const StyledPaper = styled(Paper)(({ theme }) => ({
  backgroundColor: '#1E1E1E',
  color: '#fff',
  padding: theme.spacing(3),
  marginBottom: theme.spacing(3),
  height: '100%'
}));

// Circular progress component with responsive sizing
const CircularProgress = styled(Box)(({ theme }) => ({
  position: 'relative',
  width: '100%',
  maxWidth: '200px',
  '&:before': {
    content: '""',
    display: 'block',
    paddingTop: '100%'
  }
}));

const ProgressCircle = styled('svg')(({ theme }) => ({
  position: 'absolute',
  top: 0,
  left: 0,
  width: '100%',
  height: '100%',
  transform: 'rotate(-90deg)'
}));

const ProgressText = styled(Box)(({ theme }) => ({
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  textAlign: 'center'
}));

const DifficultyLabel = styled(Box)(({ theme, color }) => ({
  display: 'flex',
  alignItems: 'center',
  gap: theme.spacing(1),
  marginBottom: theme.spacing(1),
  '& .dot': {
    width: 8,
    height: 8,
    borderRadius: '50%',
    backgroundColor: color
  }
}));

const ProblemStats = ({ topicWise = {}, difficultyWise = {} }) => {
  // Process topic data for bar chart
  const topicData = Object.entries(topicWise)
    .map(([name, count]) => ({
      name,
      count: Number(count) || 0
    }))
    .filter(item => item.count > 0)
    .sort((a, b) => b.count - a.count)
    .slice(0, 10);

  // Ensure difficulty values are numbers
  const easy = Number(difficultyWise.easy) || 0;
  const medium = Number(difficultyWise.medium) || 0;
  const hard = Number(difficultyWise.hard) || 0;
  const total = easy + medium + hard;

  // Calculate stroke lengths for circular progress
  const radius = 90;
  const circumference = 2 * Math.PI * radius;
  
  const easyPercent = total > 0 ? (easy / total) * 100 : 0;
  const mediumPercent = total > 0 ? (medium / total) * 100 : 0;
  const hardPercent = total > 0 ? (hard / total) * 100 : 0;

  const easyLength = (easyPercent / 100) * circumference;
  const mediumLength = (mediumPercent / 100) * circumference;
  const hardLength = (hardPercent / 100) * circumference;

  return (
    <Box sx={{ display: 'flex', gap: 3, flexDirection: { xs: 'column', md: 'row' } }}>
      <StyledPaper elevation={0} sx={{ flex: { xs: '1', md: '0 0 auto' }, minWidth: { md: '300px' } }}>
        <Typography variant="h6" gutterBottom>Problems Solved</Typography>
        <Box sx={{ 
          display: 'flex', 
          flexDirection: { xs: 'column', sm: 'row', md: 'column' },
          alignItems: 'center', 
          gap: 4,
          py: 2
        }}>
          <Box sx={{ 
            width: { xs: '200px', sm: '180px' },
            margin: 'auto'
          }}>
            <CircularProgress>
              <ProgressCircle viewBox="0 0 200 200">
                {/* Background circle */}
                <circle
                  cx="100"
                  cy="100"
                  r={radius}
                  fill="none"
                  stroke="#2D2D2D"
                  strokeWidth="16"
                />
                {total > 0 ? (
                  <>
                    {/* Hard problems */}
                    <circle
                      cx="100"
                      cy="100"
                      r={radius}
                      fill="none"
                      stroke="#FF375F"
                      strokeWidth="16"
                      strokeDasharray={`${hardLength} ${circumference}`}
                      strokeDashoffset="0"
                    />
                    {/* Medium problems */}
                    <circle
                      cx="100"
                      cy="100"
                      r={radius}
                      fill="none"
                      stroke="#FFC01E"
                      strokeWidth="16"
                      strokeDasharray={`${mediumLength} ${circumference}`}
                      strokeDashoffset={-hardLength}
                    />
                    {/* Easy problems */}
                    <circle
                      cx="100"
                      cy="100"
                      r={radius}
                      fill="none"
                      stroke="#00B8A3"
                      strokeWidth="16"
                      strokeDasharray={`${easyLength} ${circumference}`}
                      strokeDashoffset={-(hardLength + mediumLength)}
                    />
                  </>
                ) : null}
              </ProgressCircle>
              <ProgressText>
                <Typography variant="h3">
                  {total > 0 ? total : '0'}
                </Typography>
              </ProgressText>
            </CircularProgress>
          </Box>
          <Box sx={{ 
            width: '100%',
            maxWidth: { xs: '300px', sm: '200px' },
            margin: 'auto'
          }}>
            <DifficultyLabel color="#00B8A3">
              <span className="dot" />
              <Typography variant="body2">Easy</Typography>
              <Typography variant="body2" sx={{ ml: 'auto' }}>{easy}</Typography>
            </DifficultyLabel>
            <DifficultyLabel color="#FFC01E">
              <span className="dot" />
              <Typography variant="body2">Medium</Typography>
              <Typography variant="body2" sx={{ ml: 'auto' }}>{medium}</Typography>
            </DifficultyLabel>
            <DifficultyLabel color="#FF375F">
              <span className="dot" />
              <Typography variant="body2">Hard</Typography>
              <Typography variant="body2" sx={{ ml: 'auto' }}>{hard}</Typography>
            </DifficultyLabel>
          </Box>
        </Box>
      </StyledPaper>

      <StyledPaper elevation={0} sx={{ flex: 1 }}>
        <Typography variant="h6" gutterBottom>DSA Topic Analysis</Typography>
        {topicData.length > 0 ? (
          <Box sx={{ height: 400, mt: 2 }}>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart
                data={topicData}
                layout="vertical"
                margin={{ top: 5, right: 30, left: 100, bottom: 5 }}
              >
                <XAxis type="number" stroke="#8B949E" />
                <YAxis
                  dataKey="name"
                  type="category"
                  stroke="#8B949E"
                  width={100}
                  tick={{ fill: '#8B949E' }}
                />
                <Bar
                  dataKey="count"
                  fill="#0066FF"
                  radius={[0, 4, 4, 0]}
                />
              </BarChart>
            </ResponsiveContainer>
          </Box>
        ) : (
          <Box sx={{ 
            height: 400, 
            display: 'flex', 
            alignItems: 'center', 
            justifyContent: 'center',
            color: '#8B949E' 
          }}>
            <Typography>No topic data available</Typography>
          </Box>
        )}
      </StyledPaper>
    </Box>
  );
};

export default ProblemStats; 