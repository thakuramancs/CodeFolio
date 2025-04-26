import React from 'react';
import { Box, Typography, IconButton, Button, Paper } from '@mui/material';
import { styled } from '@mui/material/styles';
import LinkedInIcon from '@mui/icons-material/LinkedIn';
import TwitterIcon from '@mui/icons-material/Twitter';
import EmailIcon from '@mui/icons-material/Email';
import PhoneIcon from '@mui/icons-material/Phone';
import EditIcon from '@mui/icons-material/Edit';
import { useNavigate } from 'react-router-dom';

const StyledPaper = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.background.paper,
  color: theme.palette.text.primary,
  padding: theme.spacing(3),
  marginBottom: theme.spacing(3)
}));

const PlatformButton = styled(Button)(({ theme, active }) => ({
  color: active ? theme.palette.primary.main : theme.palette.text.secondary,
  backgroundColor: active ? theme.palette.background.card : 'transparent',
  '&:hover': {
    backgroundColor: theme.palette.background.card,
    color: theme.palette.primary.main
  }
}));

const ProfileHeader = ({ user, profileData, onPlatformSelect, selectedPlatform }) => {
  const navigate = useNavigate();

  const platforms = [
    { name: 'Overview', key: null },
    { name: 'LeetCode', key: 'leetcode' },
    { name: 'CodeForces', key: 'codeforces' },
    { name: 'CodeChef', key: 'codechef' },
    { name: 'GeeksForGeeks', key: 'geeksforgeeks' },
    { name: 'GitHub', key: 'github' }
  ];

  return (
    <StyledPaper elevation={0}>
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 3 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Box
            component="img"
            src={user?.picture || '/default-avatar.png'}
            alt="Profile"
            sx={{
              width: 80,
              height: 80,
              borderRadius: '50%'
            }}
          />
          <Box>
            <Typography variant="h5">{user?.name || 'User Name'}</Typography>
            <Typography variant="body2" color="text.secondary">
              @{profileData?.username || 'username'}
            </Typography>
          </Box>
        </Box>
        <Button
          startIcon={<EditIcon />}
          variant="contained"
          onClick={() => navigate('/profile/settings')}
          sx={{
            backgroundColor: 'background.card',
            '&:hover': { backgroundColor: 'background.paper' }
          }}
        >
          Edit Profile
        </Button>
      </Box>

      <Box sx={{ display: 'flex', gap: 2, mb: 2, flexWrap: 'wrap' }}>
        {platforms.map((platform) => (
          <PlatformButton
            key={platform.key || 'overview'}
            active={selectedPlatform === platform.key}
            onClick={() => onPlatformSelect(platform.key)}
          >
            {platform.name}
          </PlatformButton>
        ))}
      </Box>

      <Box sx={{ display: 'flex', gap: 1 }}>
        <IconButton size="small" sx={{ color: 'text.secondary', '&:hover': { color: 'primary.main' } }}>
          <LinkedInIcon />
        </IconButton>
        <IconButton size="small" sx={{ color: 'text.secondary', '&:hover': { color: 'primary.main' } }}>
          <TwitterIcon />
        </IconButton>
        <IconButton size="small" sx={{ color: 'text.secondary', '&:hover': { color: 'primary.main' } }}>
          <EmailIcon />
        </IconButton>
        <IconButton size="small" sx={{ color: 'text.secondary', '&:hover': { color: 'primary.main' } }}>
          <PhoneIcon />
        </IconButton>
      </Box>
    </StyledPaper>
  );
};

export default ProfileHeader; 