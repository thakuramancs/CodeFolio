import React from 'react';
import { useLocation } from 'react-router-dom';
import { useAuthUtils } from '../../hooks/useAuthUtils';
import Sidebar from '../common/Sidebar';
import { Box, CircularProgress } from '@mui/material';

const Layout = ({ children }) => {
    const { isAuthenticated, isLoading } = useAuthUtils();
    const location = useLocation();

    if (isLoading) {
        return (
            <Box 
                display="flex" 
                justifyContent="center" 
                alignItems="center" 
                minHeight="100vh"
                sx={(theme) => ({
                    backgroundColor: theme.palette.background.default
                })}
            >
                <CircularProgress color="primary" />
            </Box>
        );
    }

    return (
        <Box 
            display="flex" 
            minHeight="100vh"
            sx={(theme) => ({
                backgroundColor: theme.palette.background.default,
                color: theme.palette.text.primary
            })}
        >
            {isAuthenticated && <Sidebar />}
            <Box 
                component="main"
                flex={1}
                overflow="auto"
                sx={(theme) => ({
                    backgroundColor: theme.palette.background.default,
                    p: 3
                })}
            >
                {children}
            </Box>
        </Box>
    );
};

export default Layout; 