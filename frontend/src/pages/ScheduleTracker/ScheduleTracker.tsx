import { useState } from 'react';
import { schedule, attendance } from '../../data/mockData';
import ProgressBar from '../../components/ui/ProgressBar';
import styles from './ScheduleTracker.module.css';

const DAYS = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];

const TYPE_COLORS: Record<string, string> = {
  Lecture: '#dbeafe',
  Tutorial: '#fef3c7',
  Lab: '#dcfce7',
};

const TYPE_TEXT_COLORS: Record<string, string> = {
  Lecture: '#2563eb',
  Tutorial: '#d97706',
  Lab: '#16a34a',
};

const CheckIcon = () => (
  <svg width="14" height="14" viewBox="0 0 24 24" fill="#16a34a">
    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
  </svg>
);

const WarnIcon = () => (
  <svg width="14" height="14" viewBox="0 0 24 24" fill="#f59e0b">
    <path d="M12 2L1 21h22L12 2zm0 3.5l8.5 14.5H3.5L12 5.5zM11 10v4h2v-4h-2zm0 6v2h2v-2h-2z"/>
  </svg>
);

const ClockIcon = () => (
  <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
    <circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/>
  </svg>
);

const PinIcon = () => (
  <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/>
  </svg>
);

function getAttendanceColor(pct: number) {
  if (pct >= 95) return '#22c55e';
  if (pct >= 80) return '#f59e0b';
  return '#ef4444';
}

export default function ScheduleTracker() {
  const [activeDay, setActiveDay] = useState('Monday');

  const daySchedule = schedule.filter(s => s.day === activeDay).sort((a, b) =>
    a.startTime.localeCompare(b.startTime)
  );

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className={styles.title}>Schedule Tracker</h1>
          <p className={styles.subtitle}>Your weekly timetable and attendance overview</p>
        </div>
      </div>

      <div className={styles.mainGrid}>
        <div className={styles.scheduleCol}>
          <div className={styles.section}>
            <div className={styles.dayTabs}>
              {DAYS.map(day => (
                <button
                  key={day}
                  className={`${styles.dayTab} ${activeDay === day ? styles.activeDay : ''}`}
                  onClick={() => setActiveDay(day)}
                >
                  <span className={styles.dayFull}>{day}</span>
                  <span className={styles.dayShort}>{day.slice(0, 3)}</span>
                  <span className={styles.dayCount}>
                    {schedule.filter(s => s.day === day).length}
                  </span>
                </button>
              ))}
            </div>

            <div className={styles.dayContent}>
              {daySchedule.length === 0 ? (
                <div className={styles.emptyDay}>
                  <span className={styles.emptyIcon}>📅</span>
                  <p>No classes scheduled for {activeDay}</p>
                </div>
              ) : (
                <div className={styles.sessionList}>
                  {daySchedule.map(entry => (
                    <div key={entry.id} className={styles.sessionCard}>
                      <div
                        className={styles.sessionTypePill}
                        style={{
                          background: TYPE_COLORS[entry.type],
                          color: TYPE_TEXT_COLORS[entry.type],
                        }}
                      >
                        {entry.type}
                      </div>
                      <div className={styles.sessionBody}>
                        <div className={styles.sessionTitle}>{entry.courseCode} – {entry.courseName}</div>
                        <div className={styles.sessionMeta}>
                          <span className={styles.metaItem}><ClockIcon /> {entry.startTime} – {entry.endTime}</span>
                          <span className={styles.metaItem}><PinIcon /> {entry.location}</span>
                          {entry.lecturer && (
                            <span className={styles.metaItem}>👤 {entry.lecturer}</span>
                          )}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>

        <div className={styles.attendanceCol}>
          <div className={styles.section}>
            <h2 className={styles.sectionTitle}>Attendance Summary</h2>
            <div className={styles.attendanceList}>
              {attendance.map(record => (
                <div key={record.courseCode} className={styles.attendanceCard}>
                  <div className={styles.attendanceTop}>
                    <div>
                      <div className={styles.attendanceCourse}>{record.courseCode}</div>
                      <div className={styles.attendanceCourseName}>{record.courseName}</div>
                    </div>
                    <span
                      className={styles.attendancePct}
                      style={{ color: getAttendanceColor(record.percentage) }}
                    >
                      {record.percentage}%
                    </span>
                  </div>
                  <div className={styles.attendedRow}>
                    <span className={styles.attendedLabel}>
                      Attended {record.attended} / {record.total}
                    </span>
                  </div>
                  <ProgressBar
                    value={record.percentage}
                    color={getAttendanceColor(record.percentage)}
                    height={7}
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
      </div>
    </div>
  );
}
