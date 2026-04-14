import { useState } from 'react';
import { courses } from '../../data/mockData';
import ProgressBar from '../../components/ui/ProgressBar';
import styles from './GradeTracker.module.css';

const ChevronIcon = ({ open }: { open: boolean }) => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"
    style={{ transform: open ? 'rotate(180deg)' : 'rotate(0deg)', transition: 'transform 0.2s' }}>
    <polyline points="6 9 12 15 18 9"/>
  </svg>
);

const PlusIcon = () => (
  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
    <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
  </svg>
);

function getGradeColor(pct: number) {
  if (pct >= 85) return '#22c55e';
  if (pct >= 70) return '#f59e0b';
  return '#ef4444';
}

function getLetterColor(letter: string) {
  if (letter.startsWith('A')) return '#16a34a';
  if (letter.startsWith('B')) return '#2563eb';
  if (letter.startsWith('C')) return '#d97706';
  return '#dc2626';
}

export default function GradeTracker() {
  const [expandedId, setExpandedId] = useState<string | null>('1');
  const overallGpa = (courses.reduce((s, c) => s + c.currentGrade, 0) / courses.length / 25).toFixed(2);

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className={styles.title}>Grade Tracker</h1>
          <p className={styles.subtitle}>Monitor your academic performance across all courses</p>
        </div>
        <button className={styles.addBtn}><PlusIcon /> Add Course</button>
      </div>

      <div className={styles.summaryRow}>
        <div className={styles.summaryCard}>
          <span className={styles.summaryLabel}>Overall GPA</span>
          <span className={styles.summaryValue} style={{ color: '#2563eb' }}>{overallGpa}</span>
        </div>
        <div className={styles.summaryCard}>
          <span className={styles.summaryLabel}>Courses</span>
          <span className={styles.summaryValue}>{courses.length}</span>
        </div>
        <div className={styles.summaryCard}>
          <span className={styles.summaryLabel}>Avg Grade</span>
          <span className={styles.summaryValue} style={{ color: '#22c55e' }}>
            {(courses.reduce((s, c) => s + c.currentGrade, 0) / courses.length).toFixed(1)}%
          </span>
        </div>
        <div className={styles.summaryCard}>
          <span className={styles.summaryLabel}>On Target</span>
          <span className={styles.summaryValue} style={{ color: '#22c55e' }}>
            {courses.filter(c => c.currentGrade >= 85).length}/{courses.length}
          </span>
        </div>
      </div>

      <div className={styles.courseList}>
        {courses.map(course => (
          <div key={course.id} className={styles.courseCard}>
            <button
              className={styles.courseHeader}
              onClick={() => setExpandedId(expandedId === course.id ? null : course.id)}
            >
              <div className={styles.courseLeft}>
                <span className={styles.letterBadge} style={{ background: getLetterColor(course.letterGrade) + '18', color: getLetterColor(course.letterGrade) }}>
                  {course.letterGrade}
                </span>
                <div>
                  <div className={styles.courseTitle}>{course.code} – {course.name}</div>
                  <div className={styles.courseMeta}>Current: {course.currentGrade}%</div>
                </div>
              </div>
              <div className={styles.courseRight}>
                <div className={styles.progressWrapper}>
                  <ProgressBar value={course.currentGrade} color={getGradeColor(course.currentGrade)} height={7} />
                </div>
                <span className={styles.target} style={{ color: course.requiredAvg > 90 ? '#ef4444' : '#22c55e' }}>
                  Need {course.requiredAvg}% for {course.targetGrade}
                </span>
                <ChevronIcon open={expandedId === course.id} />
              </div>
            </button>

            {expandedId === course.id && (
              <div className={styles.courseBody}>
                <div className={styles.weightHeader}>
                  <span>Assessment</span>
                  <span>Grade</span>
                  <span>Weight</span>
                  <span>Contribution</span>
                </div>
                {course.assessments.map((a, i) => (
                  <div key={i} className={styles.assessmentRow}>
                    <span className={styles.assessmentName}>{a.name}</span>
                    <span className={styles.assessmentGrade} style={{ color: getGradeColor(a.grade) }}>
                      {a.grade}%
                    </span>
                    <span className={styles.assessmentWeight}>{a.weight}%</span>
                    <div className={styles.contributeWrapper}>
                      <ProgressBar value={a.grade} color={getGradeColor(a.grade)} height={5} />
                    </div>
                  </div>
                ))}
                <div className={styles.weightFooter}>
                  <span>Weighted Total</span>
                  <span style={{ fontWeight: 700, color: getGradeColor(course.currentGrade) }}>
                    {course.currentGrade}%
                  </span>
                  <span>{course.assignmentWeightsTotal}% completed</span>
                  <div></div>
                </div>
                <button className={styles.addAssessmentBtn}><PlusIcon /> Add Assessment</button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
