import { useState } from 'react';
import { assessments } from '../../data/mockData';
import type { AssessmentStatus, Priority } from '../../types';
import { PriorityBadge, StatusBadge } from '../../components/ui/Badge';
import ProgressBar from '../../components/ui/ProgressBar';
import styles from './AssessmentTracker.module.css';

const PlusIcon = () => (
  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
    <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
  </svg>
);

const FilterIcon = () => (
  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/>
  </svg>
);

type StatusFilter = 'All' | AssessmentStatus;
type PriorityFilter = 'All' | Priority;

export default function AssessmentTracker() {
  const [statusFilter, setStatusFilter] = useState<StatusFilter>('All');
  const [priorityFilter, setPriorityFilter] = useState<PriorityFilter>('All');

  const filtered = assessments.filter(a => {
    if (statusFilter !== 'All' && a.status !== statusFilter) return false;
    if (priorityFilter !== 'All' && a.priority !== priorityFilter) return false;
    return true;
  });

  const statuses: StatusFilter[] = ['All', 'Not Started', 'In Progress', 'Completed'];
  const priorities: PriorityFilter[] = ['All', 'High', 'Medium', 'Low'];

  const countsByStatus = {
    all: assessments.length,
    notStarted: assessments.filter(a => a.status === 'Not Started').length,
    inProgress: assessments.filter(a => a.status === 'In Progress').length,
    completed: assessments.filter(a => a.status === 'Completed').length,
  };

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className={styles.title}>Assessment Tracker</h1>
          <p className={styles.subtitle}>Track all your assignments, projects and exams</p>
        </div>
        <button className={styles.addBtn}><PlusIcon /> Add Assessment</button>
      </div>

      <div className={styles.summaryRow}>
        <div className={styles.summaryCard} onClick={() => setStatusFilter('All')} style={{ cursor: 'pointer' }}>
          <span className={styles.summaryValue}>{countsByStatus.all}</span>
          <span className={styles.summaryLabel}>Total</span>
        </div>
        <div className={styles.summaryCard} onClick={() => setStatusFilter('Not Started')} style={{ cursor: 'pointer' }}>
          <span className={styles.summaryValue} style={{ color: '#64748b' }}>{countsByStatus.notStarted}</span>
          <span className={styles.summaryLabel}>Not Started</span>
        </div>
        <div className={styles.summaryCard} onClick={() => setStatusFilter('In Progress')} style={{ cursor: 'pointer' }}>
          <span className={styles.summaryValue} style={{ color: '#2563eb' }}>{countsByStatus.inProgress}</span>
          <span className={styles.summaryLabel}>In Progress</span>
        </div>
        <div className={styles.summaryCard} onClick={() => setStatusFilter('Completed')} style={{ cursor: 'pointer' }}>
          <span className={styles.summaryValue} style={{ color: '#16a34a' }}>{countsByStatus.completed}</span>
          <span className={styles.summaryLabel}>Completed</span>
        </div>
      </div>

      <div className={styles.filters}>
        <div className={styles.filterGroup}>
          <FilterIcon />
          <span className={styles.filterGroupLabel}>Status:</span>
          {statuses.map(s => (
            <button
              key={s}
              className={`${styles.filterBtn} ${statusFilter === s ? styles.filterActive : ''}`}
              onClick={() => setStatusFilter(s)}
            >
              {s}
            </button>
          ))}
        </div>
        <div className={styles.filterGroup}>
          <span className={styles.filterGroupLabel}>Priority:</span>
          {priorities.map(p => (
            <button
              key={p}
              className={`${styles.filterBtn} ${priorityFilter === p ? styles.filterActive : ''}`}
              onClick={() => setPriorityFilter(p)}
            >
              {p}
            </button>
          ))}
        </div>
      </div>

      <div className={styles.tableWrapper}>
        <div className={styles.tableHeader}>
          <span>Assessment</span>
          <span>Course</span>
          <span>Priority</span>
          <span>Status</span>
          <span>Progress</span>
          <span>Due Date</span>
        </div>
        {filtered.length === 0 ? (
          <div className={styles.empty}>No assessments match the current filters.</div>
        ) : (
          filtered.map(a => (
            <div key={a.id} className={styles.tableRow}>
              <div className={styles.assessmentCell}>
                <div className={styles.assessmentName}>{a.name}</div>
                <div className={styles.assessmentType}>{a.type}</div>
              </div>
              <div className={styles.courseCell}>
                <span className={styles.courseCode}>{a.courseCode}</span>
              </div>
              <div><PriorityBadge priority={a.priority} /></div>
              <div><StatusBadge status={a.status} /></div>
              <div className={styles.progressCell}>
                <ProgressBar value={a.progress} color="#3b82f6" height={6} showLabel />
              </div>
              <div className={styles.dueCell}>
                <span
                  className={styles.daysLeft}
                  style={{ color: a.daysLeft <= 3 ? '#ef4444' : a.daysLeft <= 7 ? '#f59e0b' : '#6366f1' }}
                >
                  {a.daysLeft}d left
                </span>
                <span className={styles.dueDate}>{a.dueDate}</span>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
