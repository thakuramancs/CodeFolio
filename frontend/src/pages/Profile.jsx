import React, { useState, useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { Box, CircularProgress, Typography, Alert } from '@mui/material';
import ProfileHeader from '../components/profile/ProfileHeader';
import StatsOverview from '../components/profile/StatsOverview';
import ProblemStats from '../components/profile/ProblemStats';
import ContestStats from '../components/profile/ContestStats';
import PlatformProfile from '../components/platform/PlatformProfile';
import GitHubProfileWrapper from '../components/profiles/GitHubProfileWrapper';

const Profile = () => {
  const { user, getAccessTokenSilently } = useAuth0();
  const [profileData, setProfileData] = useState(null);
  const [platformStats, setPlatformStats] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [platformErrors, setPlatformErrors] = useState({});
  const [selectedPlatform, setSelectedPlatform] = useState(null);

  // Fetch stats for a specific platform
  const fetchPlatformStats = async (userId, platform, token) => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/profiles/${userId}/${platform}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      
      // Handle non-200 responses
      if (!response.ok) {
        const errorData = await response.json();
        const errorMessage = errorData.message || errorData.error || `Failed to fetch ${platform} stats`;
        
        // Don't treat missing username as an error
        if (response.status === 400 && errorMessage.toLowerCase().includes('username not set')) {
          console.log(`${platform} username not set`);
          return null;
        }
        
        // Log other errors but don't throw
        console.warn(`Failed to fetch ${platform} stats:`, errorMessage);
        setPlatformErrors(prev => ({
          ...prev,
          [platform]: errorMessage
        }));
        return null;
      }

      const data = await response.json();
      console.log(`${platform} Stats:`, data);
      return { platform, data };
    } catch (err) {
      console.error(`Error fetching ${platform} stats:`, err);
      setPlatformErrors(prev => ({
        ...prev,
        [platform]: err.message || `Failed to fetch ${platform} stats`
      }));
      return null;
    }
  };

  useEffect(() => {
    const fetchAllData = async () => {
      try {
        const token = await getAccessTokenSilently();
        
        // Reset errors
        setPlatformErrors({});
        
        // Fetch basic profile data
        const profileResponse = await fetch(
          `http://localhost:8080/api/profiles/${user.sub}`,
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );
        const profileData = await profileResponse.json();
        console.log('Profile Data:', profileData);
        setProfileData(profileData);

        // Fetch stats for each platform if username exists
        const platforms = [
          { key: 'leetcode', username: profileData.leetcodeUsername },
          { key: 'codeforces', username: profileData.codeforcesUsername },
          { key: 'codechef', username: profileData.codechefUsername },
          { key: 'geeksforgeeks', username: profileData.geeksforgeeksUsername }
        ].filter(p => p.username); // Filter out platforms with no username

        // Create an array of promises for each platform
        const statsPromises = platforms.map(p => 
          fetchPlatformStats(user.sub, p.key, token)
        );

        // Wait for all promises to settle
        const results = await Promise.allSettled(statsPromises);
        
        // Process results
        const stats = {};
        results.forEach((result) => {
          if (result.status === 'fulfilled' && result.value) {
            const { platform, data } = result.value;
            stats[`${platform}Stats`] = data;
          }
        });

        console.log('Processed Platform Stats:', stats);
        setPlatformStats(stats);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching data:', err);
        setError('Failed to load profile data');
        setLoading(false);
      }
    };

    if (user?.sub) {
      fetchAllData();
    }
  }, [user, getAccessTokenSilently]);

  // Handle platform selection
  const handlePlatformSelect = (platform) => {
    setSelectedPlatform(platform);
  };

  if (loading) {
    return (
      <Box 
        display="flex" 
        justifyContent="center" 
        alignItems="center" 
        minHeight="80vh"
        sx={(theme) => ({
          backgroundColor: theme.palette.background.default
        })}
      >
        <CircularProgress color="primary" size={40} />
      </Box>
    );
  }

  // Initialize aggregate stats based on PlatformStatsDTO structure
  const aggregateStats = {
    totalQuestions: 0,
    totalActiveDays: 0,
    totalContests: 0,
    difficultyWise: {
      easy: 0,
      medium: 0,
      hard: 0
    },
    topicWise: {},
    contestStats: {},
    ratingHistory: []
  };

  // Process each platform's stats according to PlatformStatsDTO structure
  Object.entries(platformStats).forEach(([platformKey, stats]) => {
    if (!stats) return;
    
    const platform = platformKey.replace('Stats', '');
    console.log(`Processing ${platform} stats:`, stats);

    // Add total questions (directly from the platform's stats)
    aggregateStats.totalQuestions += Number(stats.totalQuestions || 0);

    // Add activity stats
    aggregateStats.totalActiveDays += Number(stats.totalActiveDays || 0);
    aggregateStats.totalContests += Number(stats.totalContests || 0);

    // Process difficulty-wise stats
    if (stats.difficultyWiseSolved) {
      if (platform === 'geeksforgeeks') {
        // For GFG, combine 'basic' with 'easy'
        const { basic = 0, easy = 0, medium = 0, hard = 0 } = stats.difficultyWiseSolved;
        aggregateStats.difficultyWise.easy += Number(basic || 0) + Number(easy || 0);
        aggregateStats.difficultyWise.medium += Number(medium || 0);
        aggregateStats.difficultyWise.hard += Number(hard || 0);
      } else {
        // For other platforms
        Object.entries(stats.difficultyWiseSolved).forEach(([difficulty, count]) => {
          const level = difficulty.toLowerCase();
          if (level.includes('easy') || level.includes('basic')) {
            aggregateStats.difficultyWise.easy += Number(count || 0);
          } else if (level.includes('medium') || level.includes('moderate')) {
            aggregateStats.difficultyWise.medium += Number(count || 0);
          } else if (level.includes('hard') || level.includes('difficult')) {
            aggregateStats.difficultyWise.hard += Number(count || 0);
          }
        });
      }
    }

    // Process topic-wise stats
    if (stats.topicWiseSolved) {
      Object.entries(stats.topicWiseSolved).forEach(([topic, count]) => {
        aggregateStats.topicWise[topic] = (aggregateStats.topicWise[topic] || 0) + Number(count || 0);
      });
    }

    // Add contest stats
    if (stats.totalContests) {
      aggregateStats.contestStats[platform] = {
        totalContests: stats.totalContests,
        rating: stats.rating,
        ranking: stats.contestRanking
      };
    }

    // Add rating history
    if (stats.ratingHistory) {
      try {
        const history = typeof stats.ratingHistory === 'string' 
          ? JSON.parse(stats.ratingHistory) 
          : stats.ratingHistory;
        
        if (Array.isArray(history)) {
          aggregateStats.ratingHistory.push(...history.map(entry => ({
            ...entry,
            platform
          })));
        }
      } catch (e) {
        console.warn(`Failed to parse rating history for ${platform}:`, e);
      }
    }
  });

  // Log final aggregated stats
  console.log('\n=== Final Aggregated Stats ===');
  console.log('Total Questions:', aggregateStats.totalQuestions);
  console.log('Difficulty-wise:', aggregateStats.difficultyWise);
  console.log('Topic-wise:', aggregateStats.topicWise);
  console.log('Contest Stats:', aggregateStats.contestStats);
  console.log('==========================\n');

  const renderContent = () => {
    if (selectedPlatform === 'github') {
      return <GitHubProfileWrapper />;
    }
    
    if (selectedPlatform) {
      return <PlatformProfile platform={selectedPlatform} />;
    }

    return (
      <>
        <StatsOverview
          totalQuestions={aggregateStats.totalQuestions}
          totalActiveDays={aggregateStats.totalActiveDays}
        />

        <ProblemStats
          topicWise={aggregateStats.topicWise}
          difficultyWise={aggregateStats.difficultyWise}
        />

        <ContestStats
          contestStats={aggregateStats.contestStats}
          ratingHistory={aggregateStats.ratingHistory}
          platformErrors={platformErrors}
        />
      </>
    );
  };

  return (
    <Box 
      sx={(theme) => ({
        p: 3,
        backgroundColor: theme.palette.background.default,
        minHeight: '100vh',
        '& > *': {
          mb: 3
        }
      })}
    >
      <Typography variant="h4" component="h1" gutterBottom>
        Your Profile
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 3 }}>
          {error}
        </Alert>
      )}

      <ProfileHeader 
        user={user} 
        profileData={profileData} 
        onPlatformSelect={handlePlatformSelect}
        selectedPlatform={selectedPlatform}
      />
      
      {renderContent()}
    </Box>
  );
};

export default Profile; 