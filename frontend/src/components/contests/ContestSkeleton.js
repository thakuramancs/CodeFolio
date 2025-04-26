import React from 'react';
import { Card, CardContent } from '@mui/material';

const ContestSkeleton = () => (
  <Card className="mb-4">
    <CardContent className="flex justify-between items-center p-4">
      <div className="flex-grow">
        <div className="h-6 bg-gray-200 rounded w-3/5 mb-4 animate-pulse"></div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <div className="h-4 bg-gray-200 rounded w-20 mb-2 animate-pulse"></div>
            <div className="h-4 bg-gray-200 rounded w-32 animate-pulse"></div>
          </div>
          <div>
            <div className="h-4 bg-gray-200 rounded w-20 mb-2 animate-pulse"></div>
            <div className="h-4 bg-gray-200 rounded w-24 animate-pulse"></div>
          </div>
          <div>
            <div className="h-4 bg-gray-200 rounded w-20 mb-2 animate-pulse"></div>
            <div className="h-4 bg-gray-200 rounded w-28 animate-pulse"></div>
          </div>
        </div>
      </div>
      <div className="ml-4">
        <div className="h-10 bg-gray-200 rounded w-28 animate-pulse"></div>
      </div>
    </CardContent>
  </Card>
);

export default ContestSkeleton; 