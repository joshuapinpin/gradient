import { useState, useEffect, useCallback } from 'react';
import type { Page } from '../../types';
import type {
  YearResponse, TermResponse, CourseResponse,
  CourseGradeSimpleSummary, YearGradeSimpleSummary,
} from '../../types/api';
import { yearsApi } from '../../api/years';
import { termsApi } from '../../api/terms';
import { coursesApi } from '../../api/courses';
import { gradesApi } from '../../api/grades';
import StatCard from '../../components/ui/StatCard';
import ProgressBar from '../../components/ui/ProgressBar';
import AddYearModal from '../../components/modals/AddYearModal';
import AddTermModal from '../../components/modals/AddTermModal';
import AddCourseModal from '../../components/modals/AddCourseModal';
import styles from './Dashboard.module.css';

const StarIcon = () => (
  <svg width="22" height="22" viewBox="0 0 24 24" fill="#16a34a">
    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
  </svg>
);
const CoursesIcon = () => (
  <svg width="22" height="22" viewBox="0 0 24 24" fill="#2563eb">
    <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
  </svg>
);
const TermIcon = () => (
  <svg width="22" height="22" viewBox="0 0 24 24" fill="#d97706">
    <path d="M19 4h-1V2h-2v2H8V2H6v2H5c-1.11 0-1.99.9-1.99 2L3 20c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V10h14v10zm0-12H5V6h14v2zm-7 5h5v5h-5z"/>
  </svg>
);
const PlusIcon = () => (
  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
    <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
  </svg>
);
const ArrowRightIcon = () => (
  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
    <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/>
  </svg>
);
const EmptyIcon = () => (
  <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
    <path d="M22 10v6M2 10l10-5 10 5-10 5z"/><path d="M6 12v5c3 3 9 3 12 0v-5"/>
  </svg>
);

function classificationColor(cls: string) {
  if (cls.startsWith('A')) return '#16a34a';
  if (cls.startsWith('B')) return '#2563eb';
  if (cls.startsWith('C')) return '#d97706';
  if (cls === 'D') return '#ea580c';
  return '#dc2626';
}

function gradeBarColor(grade: number) {
  if (grade >= 80) return '#22c55e';
  if (grade >= 65) return '#f59e0b';
  return '#ef4444';
}

type Modal = 'year' | 'term' | 'course' | null;

interface Props {
  onNavigate: (page: Page) => void;
}

interface CourseWithSummary {
  course: CourseResponse;
  summary: CourseGradeSimpleSummary | null;
}

export default function Dashboard({ onNavigate }: Props) {
  const [years, setYears] = useState<YearResponse[]>([]);
  const [terms, setTerms] = useState<TermResponse[]>([]);
  const [selectedYearId, setSelectedYearId] = useState<number | null>(null);
  const [selectedTermId, setSelectedTermId] = useState<number | null>(null);
  const [coursesWithSummary, setCoursesWithSummary] = useState<CourseWithSummary[]>([]);
  const [yearSummary, setYearSummary] = useState<YearGradeSimpleSummary | null>(null);
  const [modal, setModal] = useState<Modal>(null);
  const [loading, setLoading] = useState(true);

  const loadYears = useCallback(async () => {
    try {
      const data = await yearsApi.list();
      setYears(data);
      if (data.length > 0 && selectedYearId === null) {
        setSelectedYearId(data[0].id);
      }
    } catch { /* empty */ }
  }, [selectedYearId]);

  useEffect(() => { loadYears(); }, []);

  useEffect(() => {
    if (!selectedYearId) { setTerms([]); setSelectedTermId(null); setLoading(false); return; }
    (async () => {
      setLoading(true);
      try {
        const [ts, ys] = await Promise.all([
          termsApi.listByYear(selectedYearId),
          gradesApi.yearAverage(selectedYearId),
        ]);
        setTerms(ts);
        setYearSummary(ys);
        if (ts.length > 0) setSelectedTermId(prev => prev ?? ts[0].id);
      } catch { setTerms([]); setYearSummary(null); }
      finally { setLoading(false); }
    })();
  }, [selectedYearId]);

  useEffect(() => {
    if (!selectedTermId) { setCoursesWithSummary([]); return; }
    (async () => {
      try {
        const cs = await coursesApi.listByTerm(selectedTermId);
        const results = await Promise.all(cs.map(async course => {
          try {
            const summary = await gradesApi.courseAverage(course.id);
            return { course, summary };
          } catch {
            return { course, summary: null };
          }
        }));
        setCoursesWithSummary(results);
      } catch { setCoursesWithSummary([]); }
    })();
  }, [selectedTermId]);

  const selectedYear = years.find(y => y.id === selectedYearId);
  const selectedTerm = terms.find(t => t.id === selectedTermId);
  const totalCourses = coursesWithSummary.length;
  const gradedCourses = coursesWithSummary.filter(c => c.summary && c.summary.averageGrade > 0).length;

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className={styles.title}>Dashboard</h1>
          <p className={styles.subtitle}>Overview of your academic progress</p>
        </div>
        <div className={styles.headerActions}>
          {years.length > 0 && (
            <select
              className={styles.yearSelect}
              value={selectedYearId ?? ''}
              onChange={e => { setSelectedYearId(Number(e.target.value)); setSelectedTermId(null); }}
            >
              {years.map(y => <option key={y.id} value={y.id}>{y.name}</option>)}
            </select>
          )}
          <div className={styles.addMenu}>
            <button className={styles.addBtn}><PlusIcon /> Add New</button>
            <div className={styles.addDropdown}>
              <button onClick={() => setModal('year')}>Academic Year</button>
              <button onClick={() => setModal('term')} disabled={years.length === 0}>Term</button>
              <button onClick={() => setModal('course')} disabled={terms.length === 0}>Course</button>
            </div>
          </div>
        </div>
      </div>

      {years.length === 0 && !loading ? (
        <div className={styles.emptyState}>
          <EmptyIcon />
          <h2>Get started with Gradient</h2>
          <p>Create your first academic year to start tracking your grades.</p>
          <button className={styles.emptyBtn} onClick={() => setModal('year')}>
            <PlusIcon /> Create Academic Year
          </button>
        </div>
      ) : (
        <>
          <div className={styles.statsRow}>
            <StatCard
              label="Year GPA"
              value={yearSummary ? Number(yearSummary.averageGpa).toFixed(2) : '—'}
              subtitle={yearSummary ? `${yearSummary.classification} · ${Number(yearSummary.averageGrade).toFixed(1)}%` : 'No grades yet'}
              icon={<StarIcon />}
              iconColor="#dcfce7"
            />
            <StatCard
              label="Active Courses"
              value={totalCourses}
              subtitle={selectedTerm ? `${selectedTerm.name}` : 'Select a term'}
              icon={<CoursesIcon />}
              iconColor="#dbeafe"
            />
            <StatCard
              label="Terms"
              value={terms.length}
              subtitle={selectedYear ? selectedYear.name : ''}
              icon={<TermIcon />}
              iconColor="#fef3c7"
            />
          </div>

          {terms.length > 0 && (
            <div className={styles.termTabs}>
              {terms.map(t => (
                <button
                  key={t.id}
                  className={`${styles.termTab} ${selectedTermId === t.id ? styles.activeTab : ''}`}
                  onClick={() => setSelectedTermId(t.id)}
                >
                  {t.name}
                </button>
              ))}
            </div>
          )}

          <div className={styles.section}>
            <div className={styles.sectionHeader}>
              <h2 className={styles.sectionTitle}>
                Grade Overview
                {selectedTerm && <span className={styles.sectionSub}> — {selectedTerm.name}</span>}
              </h2>
              <button className={styles.viewAll} onClick={() => onNavigate('grades')}>
                View Grade Tracker <ArrowRightIcon />
              </button>
            </div>

            {loading ? (
              <div className={styles.loadingRow}>
                {[1,2,3].map(i => <div key={i} className={styles.skeleton} />)}
              </div>
            ) : coursesWithSummary.length === 0 ? (
              <div className={styles.emptyCourses}>
                <p>No courses in this term yet.</p>
                <button className={styles.inlineAddBtn} onClick={() => setModal('course')}>
                  <PlusIcon /> Add Course
                </button>
              </div>
            ) : (
              <div className={styles.courseGrid}>
                {coursesWithSummary.map(({ course, summary }) => (
                  <div key={course.id} className={styles.courseCard} onClick={() => onNavigate('grades')}>
                    <div className={styles.courseCardTop}>
                      <div className={styles.courseCardName}>{course.name}</div>
                      {summary && summary.averageGrade > 0 ? (
                        <span
                          className={styles.classBadge}
                          style={{ color: classificationColor(summary.classification), background: classificationColor(summary.classification) + '18' }}
                        >
                          {summary.classification}
                        </span>
                      ) : (
                        <span className={styles.noBadge}>No grades</span>
                      )}
                    </div>
                    {summary && summary.averageGrade > 0 ? (
                      <>
                        <div className={styles.gradeRow}>
                          <span className={styles.gradeVal}>{Number(summary.averageGrade).toFixed(1)}%</span>
                          <span className={styles.gpaVal}>GPA {Number(summary.averageGpa).toFixed(1)}</span>
                        </div>
                        <ProgressBar value={Number(summary.averageGrade)} color={gradeBarColor(Number(summary.averageGrade))} height={6} />
                      </>
                    ) : (
                      <div className={styles.noGradeRow}>
                        <ProgressBar value={0} color="#e2e8f0" height={6} />
                        <span className={styles.noGradeText}>{course.assessmentCount} assessment{course.assessmentCount !== 1 ? 's' : ''} — awaiting grades</span>
                      </div>
                    )}
                  </div>
                ))}
                <div className={styles.addCourseCard} onClick={() => setModal('course')}>
                  <PlusIcon />
                  <span>Add Course</span>
                </div>
              </div>
            )}
          </div>

          {gradedCourses > 0 && (
            <div className={styles.statsSection}>
              <h2 className={styles.sectionTitle}>Quick Stats</h2>
              <div className={styles.quickStats}>
                <div className={styles.statItem}>
                  <span className={styles.statLabel}>Courses with grades</span>
                  <span className={styles.statValue}>{gradedCourses} / {totalCourses}</span>
                </div>
                {yearSummary && yearSummary.averageGrade > 0 && (
                  <div className={styles.statItem}>
                    <span className={styles.statLabel}>Year average</span>
                    <span className={styles.statValue}>{Number(yearSummary.averageGrade).toFixed(1)}%</span>
                  </div>
                )}
                <div className={styles.statItem}>
                  <span className={styles.statLabel}>Top course</span>
                  <span className={styles.statValue}>
                    {coursesWithSummary
                      .filter(c => c.summary)
                      .sort((a, b) => Number(b.summary!.averageGrade) - Number(a.summary!.averageGrade))[0]
                      ?.course.name ?? '—'}
                  </span>
                </div>
              </div>
            </div>
          )}
        </>
      )}

      {modal === 'year' && (
        <AddYearModal
          onClose={() => setModal(null)}
          onCreated={y => { setYears(prev => [...prev, y]); setSelectedYearId(y.id); }}
        />
      )}
      {modal === 'term' && (
        <AddTermModal
          years={years}
          defaultYearId={selectedYearId ?? undefined}
          onClose={() => setModal(null)}
          onCreated={t => { setTerms(prev => [...prev, t]); setSelectedTermId(t.id); }}
        />
      )}
      {modal === 'course' && (
        <AddCourseModal
          terms={terms}
          defaultTermId={selectedTermId ?? undefined}
          onClose={() => setModal(null)}
          onCreated={c => {
            setCoursesWithSummary(prev => [...prev, { course: c, summary: null }]);
          }}
        />
      )}
    </div>
  );
}
