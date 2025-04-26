import React, { useState, useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { 
    CircularProgress, 
    Box, 
    Alert, 
    Typography, 
    Link,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button
} from '@mui/material';
import PlatformStats from '../common/PlatformStats';

const PlatformProfile = ({ platform }) => {
    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [openDialog, setOpenDialog] = useState(false);
    const [username, setUsername] = useState('');
    const [usernameError, setUsernameError] = useState('');
    const { user, getAccessTokenSilently } = useAuth0();

    const platformDisplayNames = {
        leetcode: 'LeetCode',
        codeforces: 'CodeForces',
        codechef: 'CodeChef',
        geeksforgeeks: 'GeeksForGeeks',
        hackerrank: 'HackerRank',
        atcoder: 'AtCoder'
    };

    useEffect(() => {
        if (!user?.sub) return;
        if (platform === 'github') {
            setError('GitHub profile should be handled by GitHubProfileWrapper component');
            setLoading(false);
            return;
        }
        fetchProfile();
    }, [user, platform]);

    const fetchProfile = async () => {
        try {
            setLoading(true);
            setError(null);
            const token = await getAccessTokenSilently();
            
            const response = await fetch(`http://localhost:8080/api/profiles/${user.sub}/${platform}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Accept': 'application/json'
                }
            });

            const data = await response.json();
            
            if (!response.ok) {
                if (response.status === 404) {
                    setOpenDialog(true);
                    setLoading(false);
                    return;
                }
                throw new Error(data.message || `Failed to fetch ${platformDisplayNames[platform]} stats`);
            }

            // Transform the data to match PlatformStats format
            const transformedData = {
                ...data,
                username: data.username || '',
                totalQuestions: data.totalQuestions || data.totalSolved || 0,
                totalActiveDays: data.totalActiveDays || 0,
                rating: data.rating || 0,
                contestRanking: data.contestRanking || data.rank || 0,
                difficultyWiseSolved: data.difficultyWiseSolved || {},
                topicWiseSolved: data.topicWiseSolved || {},
                submissionCalendar: data.submissionCalendar || '{}',
                awards: data.awards || '[]',
                // Platform specific data
                totalContests: data.totalContests || 0,
                platformSpecificLabels: {
                    contestRanking: platform === 'geeksforgeeks' ? 'Institute Rank' : 'Contest Ranking'
                }
            };

            setStats(transformedData);
        } catch (error) {
            console.error(`Error fetching ${platform} stats:`, error);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleUsernameSubmit = async () => {
        if (!username) {
            setUsernameError('Username is required');
            return;
        }
        try {
            const token = await getAccessTokenSilently();
            const response = await fetch(`http://localhost:8080/api/profiles/${user.sub}/${platform}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({ username })
            });

            if (!response.ok) {
                throw new Error('Failed to update username');
            }

            setOpenDialog(false);
            fetchProfile();
        } catch (error) {
            setUsernameError(error.message);
        }
    };

    if (!user?.sub) {
        return (
            <Box sx={{ p: 3 }}>
                <Alert severity="info">Please log in to view your {platformDisplayNames[platform]} profile.</Alert>
            </Box>
        );
    }

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box sx={{ p: 3 }}>
                <Alert severity="error">
                    {error}
                    {error.includes('not found') && (
                        <Typography sx={{ mt: 1 }}>
                            Please set your {platformDisplayNames[platform]} username in your{' '}
                            <Link href="/profile" color="primary">
                                profile settings
                            </Link>
                            .
                        </Typography>
                    )}
                </Alert>
            </Box>
        );
    }

    if (platform === 'atcoder') {
        return (
            <Box className="bg-white p-6 rounded-lg shadow">
                <Typography variant="h6" className="mb-4">
                    Coming Soon
                </Typography>
                <Typography color="text.secondary">
                    AtCoder profile integration will be available soon.
                </Typography>
            </Box>
        );
    }

    return (
        <>
            <PlatformStats stats={stats} platformName={platformDisplayNames[platform]} />
            
            <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
                <DialogTitle>Enter {platformDisplayNames[platform]} Username</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Username"
                        fullWidth
                        value={username}
                        onChange={(e) => {
                            setUsername(e.target.value);
                            setUsernameError('');
                        }}
                        error={!!usernameError}
                        helperText={usernameError}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
                    <Button onClick={handleUsernameSubmit} variant="contained" color="primary">
                        Save
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    );
};

export default PlatformProfile; 