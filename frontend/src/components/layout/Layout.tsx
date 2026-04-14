import type { Page } from '../../types';
import { user } from '../../data/mockData';
import Sidebar from './Sidebar';
import styles from './Layout.module.css';

interface LayoutProps {
  activePage: Page;
  onNavigate: (page: Page) => void;
  children: React.ReactNode;
}

export default function Layout({ activePage, onNavigate, children }: LayoutProps) {
  return (
    <div className={styles.layout}>
      <Sidebar activePage={activePage} onNavigate={onNavigate} user={user} />
      <main className={styles.main}>
        {children}
      </main>
    </div>
  );
}
