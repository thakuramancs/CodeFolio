import React, { useState, useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { Card, CardContent, Alert, Button, Box, CircularProgress } from '@mui/material';
import GitHubProfile from './GitHubProfile';
import { profileService } from '../../services/profileService';

const GitHubProfileWrapper = () => {
  const { user, isAuthenticated, getAccessTokenSilently } = useAuth0();
  const [githubUsername, setGithubUsername] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUserProfile = async () => {
      if (!isAuthenticated || !user?.sub) return;

      try {
        setLoading(true);
        // Get the access token
        const token = await getAccessTokenSilently();
        // Store the token for API calls
        localStorage.setItem('auth_token', token);

        const profile = await profileService.getUserProfile(user.sub);
        if (profile?.githubUsername) {
          setGithubUsername(profile.githubUsername);
        }
      } catch (err) {
        console.error('Error fetching user profile:', err);
        setError('Failed to fetch GitHub username. Please set it in your profile settings.');
      } finally {
        setLoading(false);
      }
    };

    fetchUserProfile();
  }, [isAuthenticated, user, getAccessTokenSilently]);

  if (!isAuthenticated) {
    return (
      <Card>
        <CardContent>
          <Alert severity="info">Please log in to view your GitHub profile.</Alert>
        </CardContent>
      </Card>
    );
  }

  if (loading) {
    return (
      <Card>
        <CardContent sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
          <CircularProgress />
        </CardContent>
      </Card>
    );
  }

  if (error) {
    return (
      <Card>
        <CardContent>
          <Alert severity="warning">
            {error}
            <Box mt={2}>
              <Button
                variant="contained"
                color="primary"
                href="/profile/settings"
              >
                Go to Profile Settings
              </Button>
            </Box>
          </Alert>
        </CardContent>
      </Card>
    );
  }

  if (!githubUsername) {
    return (
      <Card>
        <CardContent>
          <Alert severity="info">
            Please connect your GitHub account in profile settings.
            <Box mt={2}>
              <Button
                variant="contained"
                color="primary"
                href="/profile/settings"
              >
                Go to Profile Settings
              </Button>
            </Box>
          </Alert>
        </CardContent>
      </Card>
    );
  }

  return <GitHubProfile userId={user.sub} />;
};

export default GitHubProfileWrapper; 