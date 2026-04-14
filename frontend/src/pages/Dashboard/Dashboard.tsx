import { useState } from 'react';
import type { Page } from '../../types';
import { courses, assessments, attendance, gpa, gpaChange, totalLecturesAttended, totalLectures, terms } from '../../data/mockData';
import StatCard from '../../components/ui/StatCard';
import ProgressBar from '../../components/ui/ProgressBar';
import { PriorityBadge, StatusBadge } from '../../components/ui/Badge';
import styles from './Dashboard.module.css';

const StarIcon = () => (
  <svg width="22" height="22" viewBox="0 0 24 24" fill="#16a34a" stroke="none">
    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
  </svg>
);
const CoursesIcon = () => (
  <svg width="22" height="22" viewBox="0 0 24 24" fill="#2563eb" stroke="none">
    <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
  </svg>
);
const TaskIcon = () => (
  <svg width="22" height="22" viewBox="0 0 24 24" fill="#d97706" stroke="none">
    <path d="M18 2h-3a5 5 0 00-10 0H2v20h20V2zM12 4a3 3 0 013 3H9a3 3 0 013-3zm8 16H4V4h3v2h10V4h3v16z"/>
  </svg>
);
const PersonIcon = () => (
  <svg width="22" height="22" viewBox="0 0 24 24" fill="#16a34a" stroke="none">
    <path d="M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12zm0 2.4c-3.2 0-9.6 1.6-9.6 4.8v2.4h19.2v-2.4c0-3.2-6.4-4.8-9.6-4.8z"/>
  </svg>
);
const PlusIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
    <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
  </svg>
);
const ArrowRightIcon = () => (
  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
    <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/>
  </svg>
);
const WarnIcon = () => (
  <svg width="13" height="13" viewBox="0 0 24 24" fill="#f59e0b" stroke="none">
    <path d="M12 2L1 21h22L12 2zm0 3.5l8.5 14.5H3.5L12 5.5zM11 10v4h2v-4h-2zm0 6v2h2v-2h-2z"/>
  </svg>
);
const CheckIcon = () => (
  <svg width="13" height="13" viewBox="0 0 24 24" fill="#16a34a" stroke="none">
    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
  </svg>
);
const MarkIcon = () => (
  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M20 6L9 17l-5-5"/>
  </svg>
);

function getGradeColor(pct: number) {
  if (pct >= 80) return '#22c55e';
  if (pct >= 60) return '#f59e0b';
  return '#ef4444';
}

function getAttendanceColor(pct: number) {
  if (pct >= 95) return '#22c55e';
  if (pct >= 80) return '#f59e0b';
  return '#ef4444';
}

interface DashboardProps {
  onNavigate: (page: Page) => void;
}

export default function Dashboard({ onNavigate }: DashboardProps) {
  const [term, setTerm] = useState(terms[0].id);
  const pendingTasks = assessments.filter(a => a.status !== 'Completed').length;
  const highPriority = assessments.filter(a => a.priority === 'High' && a.status !== 'Completed').length;
  const attendanceRate = Math.round((totalLecturesAttended / totalLectures) * 100);
  const upcomingAssessments = [...assessments].sort((a, b) => a.daysLeft - b.daysLeft).slice(0, 3);

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className={styles.title}>Dashboard</h1>
          <p className={styles.subtitle}>Overview of your academic progress</p>
        </div>
        <div className={styles.headerActions}>
          <select
            className={styles.termSelect}
            value={term}
            onChange={e => setTerm(e.target.value)}
          >
            {terms.map(t => <option key={t.id} value={t.id}>{t.label}</option>)}
          </select>
          <button className={styles.addBtn}>
            <PlusIcon /> Add New
          </button>
        </div>
      </div>

      <div className={styles.statsRow}>
        <StatCard
          label="Current GPA"
          value={gpa.toFixed(2)}
          subtitle={`${gpaChange} from last term`}
          icon={<StarIcon />}
          iconColor="#dcfce7"
        />
        <StatCard
          label="Active Courses"
          value={courses.length}
          subtitle={`${assessments.filter(a => a.daysLeft <= 7).length} assignments due this week`}
          icon={<CoursesIcon />}
          iconColor="#dbeafe"
        />
        <StatCard
          label="Pending Tasks"
          value={pendingTasks}
          subtitle={`${highPriority} high priority items`}
          icon={<TaskIcon />}
          iconColor="#fef3c7"
        />
        <StatCard
          label="Attendance Rate"
          value={`${attendanceRate}%`}
          subtitle={`${totalLecturesAttended} of ${totalLectures} lectures attended`}
          icon={<PersonIcon />}
          iconColor="#dcfce7"
        />
      </div>

      <div className={styles.mainGrid}>
        <div className={styles.leftCol}>
          <div className={styles.section}>
            <div className={styles.sectionHeader}>
              <h2 className={styles.sectionTitle}>Grade Tracker</h2>
              <button className={styles.viewAll} onClick={() => onNavigate('grades')}>
                View All <ArrowRightIcon />
              </button>
            </div>

            <div className={styles.courseList}>
              {courses.slice(0, 2).map(course => (
                <div key={course.id} className={styles.courseCard}>
                  <div className={styles.courseTop}>
                    <div>
                      <div className={styles.courseCode}>{course.code} – {course.name}</div>
                      <div className={styles.courseGrade}>
                        Current: {course.currentGrade}% ({course.letterGrade})
                      </div>
                    </div>
                    <span className={styles.required} style={{ color: course.requiredAvg > 90 ? '#ef4444' : '#22c55e' }}>
                      Need {course.requiredAvg}% avg for {course.targetGrade}
                    </span>
                  </div>

                  <div className={styles.weightRow}>
                    <span className={styles.weightLabel}>Assignment Weights</span>
                    <span className={styles.weightValue}>{course.assignmentWeightsTotal}% / 100%</span>
                  </div>
                  <ProgressBar
                    value={course.assignmentWeightsTotal}
                    color={getGradeColor(course.currentGrade)}
                    height={7}
                  />

                  <div className={styles.assessmentList}>
                    {course.assessments.map((a, i) => (
                      <div key={i} className={styles.assessmentRow}>
                        <span className={styles.assessmentName}>{a.name}</span>
                        <span className={styles.assessmentScore}>{a.grade}% ({a.weight}%)</span>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>

            <button className={styles.addAssignmentBtn} onClick={() => onNavigate('assessments')}>
              <PlusIcon /> Add Assignment
            </button>
          </div>

          <div className={`${styles.section} ${styles.attendanceSection}`}>
            <div className={styles.sectionHeader}>
              <h2 className={styles.sectionTitle}>Lecture Attendance</h2>
              <button className={styles.markAttendance} onClick={() => onNavigate('schedule')}>
                Mark Attendance <MarkIcon />
              </button>
            </div>

            <div className={styles.attendanceGrid}>
              {attendance.slice(0, 3).map(record => (
                <div key={record.courseCode} className={styles.attendanceCard}>
                  <div className={styles.attendanceTop}>
                    <span className={styles.attendanceCourse}>{record.courseCode}</span>
                    <span
                      className={styles.attendancePct}
                      style={{ color: getAttendanceColor(record.percentage) }}
                    >
                      {record.percentage}%
                    </span>
                  </div>
                  <div className={styles.attendedLabel}>
                    Attended{' '}
                    <span className={styles.attendedCount}>{record.attended} / {record.total}</span>
                  </div>
                  <ProgressBar
                    value={record.percentage}
                    color={getAttendanceColor(record.percentage)}
                    height={6}
                  />
                  {record.perfect ? (
                    <div className={styles.perfectNote}>
                      <CheckIcon /> Perfect attendance!
                    </div>
                  ) : record.missedNotes ? (
                    <div className={styles.missedNote}>
                      <WarnIcon />
                      <div>
                        <div className={styles.missedText}>{record.missedNotes}</div>
                        {record.reviewLink && (
                          <a className={styles.reviewLink} href="#">{record.reviewLink}</a>
                        )}
                      </div>
                    </div>
                  ) : null}
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className={styles.rightCol}>
          <div className={styles.section}>
            <div className={styles.sectionHeader}>
              <h2 className={styles.sectionTitle}>Upcoming Assignments</h2>
              <button className={styles.viewAll} onClick={() => onNavigate('assessments')}>
                View All <ArrowRightIcon />
              </button>
            </div>

            <div className={styles.assignmentList}>
              {upcomingAssessments.map(a => (
                <div key={a.id} className={styles.assignmentCard}>
                  <div className={styles.assignmentTop}>
                    <div className={styles.assignmentInfo}>
                      <div className={styles.assignmentName}>{a.name}</div>
                      <div className={styles.assignmentCourse}>{a.courseCode} • {a.type}</div>
                    </div>
                    <div className={styles.assignmentBadges}>
                      <PriorityBadge priority={a.priority} />
                      <StatusBadge status={a.status} />
                    </div>
                  </div>

                  <div className={styles.progressRow}>
                    <span className={styles.progressLabel}>Progress: {a.progress}%</span>
                    <ProgressBar value={a.progress} color="#3b82f6" height={6} />
                  </div>

                  <div className={styles.dueInfo}>
                    <span
                      className={styles.daysLeft}
                      style={{ color: a.daysLeft <= 3 ? '#ef4444' : a.daysLeft <= 7 ? '#f59e0b' : '#6366f1' }}
                    >
                      {a.daysLeft} days left
                    </span>
                    <span className={styles.dueDate}>Due: {a.dueDate}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
