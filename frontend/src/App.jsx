import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Auth0Provider, withAuthenticationRequired } from '@auth0/auth0-react';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Layout from './components/layout/Layout';
import Home from './pages/Home';
import Profile from './pages/Profile';
import Contests from './pages/Contests';
import AuthCallback from './pages/AuthCallback';
import auth0Config from './core/config/auth0Config';
import theme from './theme';
import PlatformProfile from './components/platform/PlatformProfile';
import GitHubProfileWrapper from './components/profiles/GitHubProfileWrapper';
import PlatformSettings from './components/profile/PlatformSettings';

// Protected route wrapper
const ProtectedRoute = ({ component: Component, ...args }) => {
    const WrappedComponent = withAuthenticationRequired(Component, {
        onRedirecting: () => (
            <div className="flex items-center justify-center h-screen">
                <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>
        ),
    });
    return <WrappedComponent {...args} />;
};

const App = () => {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Auth0Provider {...auth0Config}>
                <Router>
                    <Routes>
                        <Route path="/login/callback" element={<AuthCallback />} />
                        <Route path="/*" element={
                            <Layout>
                                <Routes>
                                    <Route path="/" element={<Home />} />
                                    
                                    {/* Main Profile Routes */}
                                    <Route path="/profile" element={<ProtectedRoute component={Profile} />} />
                                    <Route path="/profile/overview" element={<ProtectedRoute component={Profile} />} />
                                    
                                    {/* Platform Profile Routes */}
                                    <Route 
                                        path="/profile/leetcode" 
                                        element={<ProtectedRoute component={PlatformProfile} platform="leetcode" />} 
                                    />
                                    <Route 
                                        path="/profile/codeforces" 
                                        element={<ProtectedRoute component={PlatformProfile} platform="codeforces" />} 
                                    />
                                    <Route 
                                        path="/profile/codechef" 
                                        element={<ProtectedRoute component={PlatformProfile} platform="codechef" />} 
                                    />
                                    <Route 
                                        path="/profile/atcoder" 
                                        element={<ProtectedRoute component={PlatformProfile} platform="atcoder" />} 
                                    />
                                    <Route 
                                        path="/profile/geeksforgeeks" 
                                        element={<ProtectedRoute component={PlatformProfile} platform="geeksforgeeks" />} 
                                    />
                                    <Route 
                                        path="/profile/github" 
                                        element={<ProtectedRoute component={GitHubProfileWrapper} />} 
                                    />
                                    
                                    {/* Settings Route */}
                                    <Route 
                                        path="/profile/settings" 
                                        element={<ProtectedRoute component={PlatformSettings} />} 
                                    />
                                    
                                    {/* Other Routes */}
                                    <Route path="/contests" element={<ProtectedRoute component={Contests} />} />
                                    <Route path="*" element={<Navigate to="/" />} />
                                </Routes>
                            </Layout>
                        } />
                    </Routes>
                </Router>
            </Auth0Provider>
        </ThemeProvider>
    );
};

export default App; 