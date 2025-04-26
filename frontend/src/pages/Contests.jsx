import React, { useState, useEffect } from 'react';
import { Box, Typography, Tabs, Tab, CircularProgress, Alert } from '@mui/material';
import ContestList from '../components/contests/ContestList';

const Contests = () => {
    const [tab, setTab] = useState('active');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [contests, setContests] = useState({
        active: [],
        upcoming: []
    });

    useEffect(() => {
        const fetchContests = async () => {
            try {
                setLoading(true);
                setError(null);

                const [activeRes, upcomingRes] = await Promise.all([
                    fetch('http://localhost:8080/api/contests/active'),
                    fetch('http://localhost:8080/api/contests/upcoming')
                ]);

                if (!activeRes.ok || !upcomingRes.ok) {
                    throw new Error('Failed to fetch contests');
                }

                const [activeData, upcomingData] = await Promise.all([
                    activeRes.json(),
                    upcomingRes.json()
                ]);

                setContests({
                    active: activeData,
                    upcoming: upcomingData
                });
            } catch (error) {
                console.error('Error fetching contests:', error);
                setError('Failed to fetch contests. Please try again later.');
            } finally {
                setLoading(false);
            }
        };

        fetchContests();
    }, []);

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
                <Alert severity="error">{error}</Alert>
            </Box>
        );
    }

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Coding Contests
            </Typography>
            
            <Tabs
                value={tab}
                onChange={(e, newValue) => setTab(newValue)}
                sx={{ mb: 3 }}
            >
                <Tab label="Active Contests" value="active" />
                <Tab label="Upcoming Contests" value="upcoming" />
            </Tabs>

            <ContestList 
                contests={contests[tab]} 
                type={tab} 
            />
        </Box>
    );
};

export default Contests; 