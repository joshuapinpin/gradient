import styles from './Badge.module.css';
import type { Priority, AssessmentStatus } from '../../types';

interface PriorityBadgeProps {
  priority: Priority;
}

interface StatusBadgeProps {
  status: AssessmentStatus;
}

export function PriorityBadge({ priority }: PriorityBadgeProps) {
  const cls = {
    High: styles.high,
    Medium: styles.medium,
    Low: styles.low,
  }[priority];

  return <span className={`${styles.badge} ${cls}`}>{priority}</span>;
}

export function StatusBadge({ status }: StatusBadgeProps) {
  const cls = {
    'Not Started': styles.notStarted,
    'In Progress': styles.inProgress,
    'Completed': styles.completed,
  }[status];

  return <span className={`${styles.badge} ${cls}`}>{status}</span>;
}
