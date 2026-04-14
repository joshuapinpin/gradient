import type { Page } from '../../types';
import type { UserProfile } from '../../types';
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

const AssessmentIcon = () => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M9 11l3 3L22 4" /><path d="M21 12v7a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2h11" />
  </svg>
);

const ScheduleIcon = () => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <rect x="3" y="4" width="18" height="18" rx="2" ry="2" /><line x1="16" y1="2" x2="16" y2="6" />
    <line x1="8" y1="2" x2="8" y2="6" /><line x1="3" y1="10" x2="21" y2="10" />
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
  { id: 'assessments', label: 'Assessment Tracker', icon: <AssessmentIcon /> },
  { id: 'schedule', label: 'Schedule Tracker', icon: <ScheduleIcon /> },
];

interface SidebarProps {
  activePage: Page;
  onNavigate: (page: Page) => void;
  user: UserProfile;
}

export default function Sidebar({ activePage, onNavigate, user }: SidebarProps) {
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

      <div className={styles.userSection}>
        <div className={styles.userAvatar}>{user.initials}</div>
        <div className={styles.userInfo}>
          <span className={styles.userName}>{user.name}</span>
          <span className={styles.userProgram}>{user.program}</span>
        </div>
      </div>
    </aside>
  );
}
