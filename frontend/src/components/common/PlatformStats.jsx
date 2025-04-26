import React from 'react';
import { Card, CardContent, Typography, Box, Chip, Grid, Divider } from '@mui/material';

const PlatformStats = ({ stats, platformName }) => {
    const formatValue = (value) => {
        if (typeof value === 'number') {
            return value.toLocaleString();
        }
        return value || 'N/A';
    };

    const renderDifficultyStats = () => {
        if (!stats.difficultyWiseSolved) return null;
        return (
            <Box mt={2}>
                <Typography variant="subtitle1" gutterBottom>Difficulty Breakdown</Typography>
                <Box display="flex" gap={1} flexWrap="wrap">
                    {Object.entries(stats.difficultyWiseSolved).map(([difficulty, count]) => (
                        <Chip
                            key={difficulty}
                            label={`${difficulty}: ${formatValue(count)}`}
                            color={
                                difficulty.toLowerCase() === 'easy' ? 'success' :
                                difficulty.toLowerCase() === 'medium' ? 'warning' :
                                'error'
                            }
                            variant="outlined"
                        />
                    ))}
                </Box>
            </Box>
        );
    };

    const renderTopicStats = () => {
        if (!stats.topicWiseSolved || Object.keys(stats.topicWiseSolved).length === 0) return null;
        return (
            <Box mt={2}>
                <Typography variant="subtitle1" gutterBottom>Topics Solved</Typography>
                <Box display="flex" gap={1} flexWrap="wrap">
                    {Object.entries(stats.topicWiseSolved).map(([topic, count]) => (
                        <Chip
                            key={topic}
                            label={`${topic}: ${formatValue(count)}`}
                            color="primary"
                            variant="outlined"
                        />
                    ))}
                </Box>
            </Box>
        );
    };

    const renderAwards = () => {
        if (!stats.awards) return null;
        try {
            const awards = JSON.parse(stats.awards);
            if (!Array.isArray(awards) || awards.length === 0) return null;

            return (
                <Box mt={2}>
                    <Typography variant="subtitle1" gutterBottom>Achievements</Typography>
                    <Box display="flex" gap={1} flexWrap="wrap">
                        {awards.map((award, index) => (
                            <Chip
                                key={index}
                                label={award.name}
                                color="secondary"
                                variant="outlined"
                                icon={award.icon ? (
                                    <img 
                                        src={award.icon} 
                                        alt="" 
                                        style={{ 
                                            width: 20, 
                                            height: 20, 
                                            marginLeft: 8,
                                            objectFit: 'contain'
                                        }} 
                                    />
                                ) : undefined}
                                sx={{
                                    '& .MuiChip-icon': {
                                        order: 1,
                                        marginLeft: '8px',
                                        marginRight: '-8px',
                                    }
                                }}
                            />
                        ))}
                    </Box>
                </Box>
            );
        } catch (e) {
            console.error('Error parsing awards:', e);
            return null;
        }
    };

    const renderSubmissionCalendar = () => {
        if (!stats.submissionCalendar) return null;
        try {
            const calendar = JSON.parse(stats.submissionCalendar);
            const totalDays = Object.keys(calendar).length;
            const totalSubmissions = Object.values(calendar).reduce((a, b) => a + b, 0);
            
            return (
                <Box mt={2}>
                    <Typography variant="subtitle1" gutterBottom>Submission Activity</Typography>
                    <Typography variant="body2">
                        Total Active Days: {formatValue(stats.totalActiveDays)}
                    </Typography>
                    <Typography variant="body2">
                        Total Submissions: {formatValue(totalSubmissions)}
                    </Typography>
                </Box>
            );
        } catch (e) {
            console.error('Error parsing submission calendar:', e);
            return null;
        }
    };

    return (
        <Card>
            <CardContent>
                <Typography variant="h5" gutterBottom>
                    {platformName} Profile {stats.username ? `- ${stats.username}` : ''}
                </Typography>
                <Grid container spacing={2}>
                    <Grid item xs={12} sm={6}>
                        <Typography>
                            <strong>Total Questions Solved:</strong> {formatValue(stats.totalQuestions)}
                        </Typography>
                        <Typography>
                            <strong>Rating:</strong> {formatValue(stats.rating)}
                        </Typography>
                        <Typography>
                            <strong>{stats.platformSpecificLabels?.contestRanking || 'Contest Ranking'}:</strong> {formatValue(stats.contestRanking)}
                        </Typography>
                    </Grid>
                    <Grid item xs={12} sm={6}>
                        <Typography>
                            <strong>Total Contests:</strong> {formatValue(stats.totalContests)}
                        </Typography>
                        <Typography>
                            <strong>Active Days:</strong> {formatValue(stats.totalActiveDays)}
                        </Typography>
                    </Grid>
                </Grid>
                <Divider sx={{ my: 2 }} />
                {renderDifficultyStats()}
                {renderTopicStats()}
                {renderSubmissionCalendar()}
                {renderAwards()}
            </CardContent>
        </Card>
    );
};

export default PlatformStats; 