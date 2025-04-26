import React from 'react';
import {
    Box,
    Typography,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Alert,
    Tooltip,
    IconButton
} from '@mui/material';
import { Info as InfoIcon } from '@mui/icons-material';
import { styled } from '@mui/material/styles';
import { LineChart, Line, XAxis, YAxis, ResponsiveContainer } from 'recharts';

const StyledPaper = styled(Paper)(({ theme }) => ({
  backgroundColor: '#1E1E1E',
  color: '#fff',
  padding: theme.spacing(3),
  marginBottom: theme.spacing(3)
}));

const StatItem = styled(Box)(({ theme }) => ({
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  padding: theme.spacing(1),
  '&:hover': {
    backgroundColor: '#21262D',
    borderRadius: theme.shape.borderRadius
  }
}));

const ContestStats = ({ contestStats, ratingHistory, platformErrors = {} }) => {
    const platforms = Object.keys(contestStats || {});

    if (platforms.length === 0 && Object.keys(platformErrors).length === 0) {
        return (
            <Paper sx={{ p: 3, mt: 3 }}>
                <Typography variant="h6" gutterBottom>
                    Contest Statistics
                </Typography>
                <Typography color="textSecondary">
                    No contest data available. Make sure you've set up your platform usernames in profile settings.
                </Typography>
            </Paper>
        );
    }

    return (
        <Paper sx={{ p: 3, mt: 3 }}>
            <Typography variant="h6" gutterBottom>
                Contest Statistics
            </Typography>

            {/* Display platform-specific errors */}
            {Object.entries(platformErrors).map(([platform, error]) => (
                <Alert 
                    key={platform} 
                    severity="warning" 
                    sx={{ mb: 2 }}
                >
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        {platform.charAt(0).toUpperCase() + platform.slice(1)}: {error}
                        <Tooltip title="This error won't affect other platforms' data">
                            <IconButton size="small" sx={{ ml: 1 }}>
                                <InfoIcon fontSize="small" />
                            </IconButton>
                        </Tooltip>
                    </Box>
                </Alert>
            ))}

            {platforms.length > 0 && (
                <TableContainer>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Platform</TableCell>
                                <TableCell align="right">Total Contests</TableCell>
                                <TableCell align="right">Rating</TableCell>
                                <TableCell align="right">Ranking</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {platforms.map(platform => {
                                const stats = contestStats[platform];
                                return (
                                    <TableRow key={platform}>
                                        <TableCell component="th" scope="row">
                                            {platform.charAt(0).toUpperCase() + platform.slice(1)}
                                        </TableCell>
                                        <TableCell align="right">{stats.totalContests || 0}</TableCell>
                                        <TableCell align="right">{stats.rating || 0}</TableCell>
                                        <TableCell align="right">{stats.ranking || 'N/A'}</TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
        </Paper>
    );
};

export default ContestStats; 