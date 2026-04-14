import type { Page } from '../../types';
import styles from './Sidebar.module.css';

interface NavItem {
  id: Page;
  label: string;
  icon: React.ReactNode;
}

const DashboardIcon = () => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <rect x="3" y="3" width="7" height="7" /><rect x="14" y="3" width="7" height="7" />
    <rect x="14" y="14" width="7" height="7" /><rect x="3" y="14" width="7" height="7" />
  </svg>
);

const GradeIcon = () => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <polyline points="22 12 18 12 15 21 9 3 6 12 2 12" />
  </svg>
);

const GradCapIcon = () => (
  <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 3L1 9l11 6 9-4.91V17h2V9L12 3zm0 12.08L5.08 11 12 7.08 18.92 11 12 15.08zM5 13.18v4L12 21l7-3.82v-4L12 17l-7-3.82z"/>
  </svg>
);

const navItems: NavItem[] = [
  { id: 'dashboard', label: 'Dashboard', icon: <DashboardIcon /> },
  { id: 'grades', label: 'Grade Tracker', icon: <GradeIcon /> },
];

interface SidebarProps {
  activePage: Page;
  onNavigate: (page: Page) => void;
}

export default function Sidebar({ activePage, onNavigate }: SidebarProps) {
  return (
    <aside className={styles.sidebar}>
      <div className={styles.logo}>
        <div className={styles.logoIcon}>
          <GradCapIcon />
        </div>
        <span className={styles.logoText}>Gradient</span>
      </div>

      <nav className={styles.nav}>
        {navItems.map((item) => (
          <button
            key={item.id}
            className={`${styles.navItem} ${activePage === item.id ? styles.active : ''}`}
            onClick={() => onNavigate(item.id)}
          >
            <span className={styles.navIcon}>{item.icon}</span>
            <span className={styles.navLabel}>{item.label}</span>
          </button>
        ))}
      </nav>

      <div className={styles.brandFooter}>
        <div className={styles.logoIcon} style={{ width: 28, height: 28, fontSize: 13 }}>
          <GradCapIcon />
        </div>
        <div>
          <div className={styles.brandName}>Gradient</div>
          <div className={styles.brandSub}>Grade Tracker</div>
        </div>
      </div>
    </aside>
  );
}
