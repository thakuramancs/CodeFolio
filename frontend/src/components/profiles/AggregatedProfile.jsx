import React from 'react';
import { Card, CardContent, Typography, Grid, Box, Chip, Divider } from '@mui/material';
import { 
    Code as CodeIcon,
    EmojiEvents as TrophyIcon,
    Timeline as TimelineIcon,
    Group as CommunityIcon 
} from '@mui/icons-material';

const StatCard = ({ title, value, icon }) => (
    <Card sx={{ height: '100%' }}>
        <CardContent>
            <Box display="flex" alignItems="center" mb={1}>
                {icon}
                <Typography variant="h6" component="div" ml={1}>
                    {title}
                </Typography>
            </Box>
            <Typography variant="h4" component="div" color="primary">
                {value}
            </Typography>
        </CardContent>
    </Card>
);

const AggregatedProfile = ({ leetcode, codeforces, codechef, atcoder, geeksforgeeks, github }) => {
    // Calculate total problems solved across all platforms
    const totalProblemsSolved = [
        leetcode?.totalQuestions || 0,
        codeforces?.totalQuestions || 0,
        codechef?.totalQuestions || 0,
        atcoder?.totalQuestions || 0,
        geeksforgeeks?.totalQuestions || 0
    ].reduce((a, b) => a + b, 0);

    // Calculate total contest participations
    const totalContests = [
        leetcode?.totalContests || 0,
        codeforces?.totalContests || 0,
        codechef?.totalContests || 0,
        atcoder?.totalContests || 0,
        geeksforgeeks?.totalContests || 0
    ].reduce((a, b) => a + b, 0);

    // Calculate total active days
    const totalActiveDays = [
        leetcode?.totalActiveDays || 0,
        codeforces?.totalActiveDays || 0,
        codechef?.totalActiveDays || 0,
        atcoder?.totalActiveDays || 0,
        geeksforgeeks?.totalActiveDays || 0
    ].reduce((a, b) => a + b, 0);

    // Combine all difficulty-wise problems
    const allDifficulties = {};
    [leetcode, codeforces, codechef, atcoder, geeksforgeeks].forEach(platform => {
        if (platform?.difficultyWiseSolved) {
            Object.entries(platform.difficultyWiseSolved).forEach(([difficulty, count]) => {
                const normalizedDifficulty = difficulty.toLowerCase();
                allDifficulties[normalizedDifficulty] = (allDifficulties[normalizedDifficulty] || 0) + count;
            });
        }
    });

    // GitHub specific stats
    const githubStats = {
        repos: github?.publicRepos || 0,
        stars: github?.totalStars || 0,
        followers: github?.followers || 0,
        contributions: github?.totalContributions || 0
    };

    return (
        <Box sx={{ py: 3 }}>
            <Typography variant="h4" gutterBottom>
                Overall Coding Statistics
            </Typography>
            
            <Grid container spacing={3} sx={{ mb: 4 }}>
                <Grid item xs={12} sm={6} md={3}>
                    <StatCard 
                        title="Problems Solved"
                        value={totalProblemsSolved}
                        icon={<CodeIcon color="primary" />}
                    />
                </Grid>
                <Grid item xs={12} sm={6} md={3}>
                    <StatCard 
                        title="Contests Participated"
                        value={totalContests}
                        icon={<TrophyIcon color="primary" />}
                    />
                </Grid>
                <Grid item xs={12} sm={6} md={3}>
                    <StatCard 
                        title="Active Days"
                        value={totalActiveDays}
                        icon={<TimelineIcon color="primary" />}
                    />
                </Grid>
                <Grid item xs={12} sm={6} md={3}>
                    <StatCard 
                        title="GitHub Contributions"
                        value={githubStats.contributions}
                        icon={<CommunityIcon color="primary" />}
                    />
                </Grid>
            </Grid>

            <Card sx={{ mb: 4 }}>
                <CardContent>
                    <Typography variant="h6" gutterBottom>
                        Difficulty Distribution
                    </Typography>
                    <Box display="flex" gap={1} flexWrap="wrap">
                        {Object.entries(allDifficulties).map(([difficulty, count]) => (
                            <Chip
                                key={difficulty}
                                label={`${difficulty}: ${count}`}
                                color={
                                    difficulty.includes('easy') ? 'success' :
                                    difficulty.includes('medium') ? 'warning' :
                                    'error'
                                }
                                variant="outlined"
                            />
                        ))}
                    </Box>
                </CardContent>
            </Card>

            <Card>
                <CardContent>
                    <Typography variant="h6" gutterBottom>
                        GitHub Overview
                    </Typography>
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={6} md={3}>
                            <Typography variant="subtitle1">Public Repositories</Typography>
                            <Typography variant="h6" color="primary">{githubStats.repos}</Typography>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Typography variant="subtitle1">Total Stars</Typography>
                            <Typography variant="h6" color="primary">{githubStats.stars}</Typography>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Typography variant="subtitle1">Followers</Typography>
                            <Typography variant="h6" color="primary">{githubStats.followers}</Typography>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Typography variant="subtitle1">Total Contributions</Typography>
                            <Typography variant="h6" color="primary">{githubStats.contributions}</Typography>
                        </Grid>
                    </Grid>
                </CardContent>
            </Card>
        </Box>
    );
};

export default AggregatedProfile; 