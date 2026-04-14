import { useState } from 'react';
import type { Page } from './types';
import Layout from './components/layout/Layout';
import Dashboard from './pages/Dashboard/Dashboard';
import GradeTracker from './pages/GradeTracker/GradeTracker';

export default function App() {
  const [activePage, setActivePage] = useState<Page>('dashboard');

  const renderPage = () => {
    switch (activePage) {
      case 'dashboard': return <Dashboard onNavigate={setActivePage} />;
      case 'grades': return <GradeTracker />;
    }
  };

  return (
    <Layout activePage={activePage} onNavigate={setActivePage}>
      {renderPage()}
    </Layout>
  );
}
