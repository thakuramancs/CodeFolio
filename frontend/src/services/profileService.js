import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
const API_PROFILES_URL = `${API_BASE_URL}/api/profiles`;

// Create axios instance with default config
const api = axios.create({
    baseURL: API_PROFILES_URL,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
    withCredentials: true
});

// Cache for language data to avoid hitting rate limits
const languageCache = new Map();
const CACHE_DURATION = 1000 * 60 * 60; // 1 hour

// Add request interceptor to add auth token
api.interceptors.request.use(
    async (config) => {
        const token = localStorage.getItem('auth_token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Helper function to validate GitHub username format
const isValidGitHubUsername = (username) => {
    const usernameRegex = /^[a-z\d](?:[a-z\d]|-(?=[a-z\d])){0,38}$/i;
    return usernameRegex.test(username);
};

// Helper function to handle rate limiting
const handleRateLimit = async (error) => {
    if (error.response?.status === 403 && error.response.headers['x-ratelimit-remaining'] === '0') {
        const resetTime = error.response.headers['x-ratelimit-reset'];
        const waitTime = (resetTime * 1000) - Date.now();
        if (waitTime > 0) {
            await new Promise(resolve => setTimeout(resolve, Math.min(waitTime, 60000)));
            return true;
        }
    }
    return false;
};

export const profileService = {
    getUserProfile: async (userId) => {
        try {
            const response = await api.get(`/${userId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getGitHubProfile: async (username) => {
        try {
            const githubUsername = username.includes('|') 
                ? username.split('|')[1] 
                : username;

            if (!isValidGitHubUsername(githubUsername)) {
                throw new Error('Invalid GitHub username format');
            }
            
            const response = await api.get(`/github/${githubUsername}`);
            
            if (!response.data) {
                throw new Error('Empty response from server');
            }

            const rawData = response.data;
            
            return {
                login: rawData.login || githubUsername,
                name: rawData.name || '',
                bio: rawData.bio || '',
                company: rawData.company || '',
                location: rawData.location || '',
                blog: rawData.blog || '',
                email: rawData.email || '',
                createdAt: rawData.created_at,
                updatedAt: rawData.updated_at,
                publicRepos: rawData.public_repos || 0,
                followers: rawData.followers || 0,
                following: rawData.following || 0,
                totalStars: rawData.total_stars || 0,
                totalContributions: rawData.total_contributions || 0,
                commits: rawData.total_commits || 0,
                pullRequests: rawData.total_pull_requests || 0,
                issues: rawData.total_issues || 0,
                currentStreak: rawData.current_streak || 0,
                languages: rawData.languages || {},
                contributionCalendar: (rawData.contribution_calendar || []).map(day => ({
                    date: day.date,
                    count: day.count || 0
                }))
            };
        } catch (error) {
            if (await handleRateLimit(error)) {
                return this.getGitHubProfile(username);
            }
            throw error;
        }
    },

    getGitHubStats: async (userId) => {
        try {
            const response = await api.get(`/${userId}/github/stats`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateGitHubUsername: async (userId, username) => {
        try {
            if (!isValidGitHubUsername(username)) {
                throw new Error('Invalid GitHub username format');
            }

            const response = await api.put(`/${userId}/github`, { username });
            return response.data;
        } catch (error) {
            throw error;
        }
    }
}; 