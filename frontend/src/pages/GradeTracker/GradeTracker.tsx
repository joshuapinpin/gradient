import { useState, useEffect, useCallback } from 'react';
import type {
  YearResponse, TermResponse, CourseResponse,
  AssessmentResponse, CourseGradeFullSummary,
} from '../../types/api';
import { yearsApi } from '../../api/years';
import { termsApi } from '../../api/terms';
import { coursesApi } from '../../api/courses';
import { assessmentsApi } from '../../api/assessments';
import { gradesApi } from '../../api/grades';
import ProgressBar from '../../components/ui/ProgressBar';
import AddYearModal from '../../components/modals/AddYearModal';
import AddTermModal from '../../components/modals/AddTermModal';
import AddCourseModal from '../../components/modals/AddCourseModal';
import AddAssessmentModal from '../../components/modals/AddAssessmentModal';
import GradeModal from '../../components/modals/GradeModal';
import styles from './GradeTracker.module.css';

const PlusIcon = () => (
  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
    <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
  </svg>
);
const ChevronIcon = ({ open }: { open: boolean }) => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"
    style={{ transform: open ? 'rotate(180deg)' : 'rotate(0)', transition: 'transform 0.2s' }}>
    <polyline points="6 9 12 15 18 9"/>
  </svg>
);
const TrashIcon = () => (
  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14H6L5 6"/><path d="M10 11v6M14 11v6"/><path d="M9 6V4h6v2"/>
  </svg>
);
const PencilIcon = () => (
  <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
    <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
  </svg>
);
const EmptyIcon = () => (
  <svg width="44" height="44" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
    <path d="M22 10v6M2 10l10-5 10 5-10 5z"/><path d="M6 12v5c3 3 9 3 12 0v-5"/>
  </svg>
);

function classColor(cls: string) {
  if (cls.startsWith('A')) return '#16a34a';
  if (cls.startsWith('B')) return '#2563eb';
  if (cls.startsWith('C')) return '#d97706';
  if (cls === 'D') return '#ea580c';
  return '#94a3b8';
}

function gradeColor(g: number) {
  if (g >= 80) return '#22c55e';
  if (g >= 65) return '#f59e0b';
  return '#ef4444';
}

function formatType(t: string) {
  return t.charAt(0) + t.slice(1).toLowerCase();
}

type ModalState =
  | { type: 'year' }
  | { type: 'term' }
  | { type: 'course' }
  | { type: 'assessment'; courseId: number; courseName: string }
  | { type: 'grade'; assessment: AssessmentResponse }
  | null;

interface CourseState {
  course: CourseResponse;
  assessments: AssessmentResponse[];
  summary: CourseGradeFullSummary | null;
  expanded: boolean;
  loading: boolean;
}

export default function GradeTracker() {
  const [years, setYears] = useState<YearResponse[]>([]);
  const [terms, setTerms] = useState<TermResponse[]>([]);
  const [selectedYearId, setSelectedYearId] = useState<number | null>(null);
  const [selectedTermId, setSelectedTermId] = useState<number | null>(null);
  const [courseStates, setCourseStates] = useState<CourseState[]>([]);
  const [modal, setModal] = useState<ModalState>(null);
  const [pageLoading, setPageLoading] = useState(true);

  const loadYears = useCallback(async () => {
    try {
      const data = await yearsApi.list();
      setYears(data);
      if (data.length > 0) setSelectedYearId(prev => prev ?? data[0].id);
    } catch { /* empty */ }
    finally { setPageLoading(false); }
  }, []);

  useEffect(() => { loadYears(); }, []);

  useEffect(() => {
    if (!selectedYearId) { setTerms([]); setSelectedTermId(null); return; }
    termsApi.listByYear(selectedYearId).then(ts => {
      setTerms(ts);
      if (ts.length > 0) setSelectedTermId(prev => prev ?? ts[0].id);
    }).catch(() => setTerms([]));
  }, [selectedYearId]);

  const loadCourseSummary = useCallback(async (courseId: number): Promise<CourseGradeFullSummary | null> => {
    try { return await gradesApi.courseFullSummary(courseId); }
    catch { return null; }
  }, []);

  useEffect(() => {
    if (!selectedTermId) { setCourseStates([]); return; }
    (async () => {
      try {
        const cs = await coursesApi.listByTerm(selectedTermId);
        const states: CourseState[] = cs.map(c => ({ course: c, assessments: [], summary: null, expanded: false, loading: false }));
        setCourseStates(states);
      } catch { setCourseStates([]); }
    })();
  }, [selectedTermId]);

  const toggleCourse = useCallback(async (courseId: number) => {
    setCourseStates(prev => prev.map(cs =>
      cs.course.id !== courseId ? cs : { ...cs, expanded: !cs.expanded }
    ));

    const state = courseStates.find(cs => cs.course.id === courseId);
    if (!state || state.assessments.length > 0 || state.loading) return;

    setCourseStates(prev => prev.map(cs =>
      cs.course.id !== courseId ? cs : { ...cs, loading: true }
    ));
    try {
      const [assessments, summary] = await Promise.all([
        assessmentsApi.listByCourse(courseId),
        loadCourseSummary(courseId),
      ]);
      setCourseStates(prev => prev.map(cs =>
        cs.course.id !== courseId ? cs : { ...cs, assessments, summary, loading: false }
      ));
    } catch {
      setCourseStates(prev => prev.map(cs =>
        cs.course.id !== courseId ? cs : { ...cs, loading: false }
      ));
    }
  }, [courseStates, loadCourseSummary]);

  const refreshCourse = useCallback(async (courseId: number) => {
    const [assessments, summary] = await Promise.all([
      assessmentsApi.listByCourse(courseId),
      loadCourseSummary(courseId),
    ]);
    setCourseStates(prev => prev.map(cs =>
      cs.course.id !== courseId ? cs : { ...cs, assessments, summary }
    ));
  }, [loadCourseSummary]);

  const handleAssessmentCreated = async (assessment: AssessmentResponse) => {
    await refreshCourse(assessment.courseId);
  };

  const handleGraded = async (updated: AssessmentResponse) => {
    await refreshCourse(updated.courseId);
  };

  const handleDeleteAssessment = async (a: AssessmentResponse) => {
    if (!confirm(`Delete "${a.name}"?`)) return;
    await assessmentsApi.delete(a.id);
    await refreshCourse(a.courseId);
  };

  const handleDeleteCourse = async (courseId: number) => {
    if (!confirm('Delete this course and all its assessments?')) return;
    await coursesApi.delete(courseId);
    setCourseStates(prev => prev.filter(cs => cs.course.id !== courseId));
  };

  const selectedYear = years.find(y => y.id === selectedYearId);

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className={styles.title}>Grade Tracker</h1>
          <p className={styles.subtitle}>Track assessments and grades across your courses</p>
        </div>
        <div className={styles.headerActions}>
          <div className={styles.addMenu}>
            <button className={styles.addBtn}><PlusIcon /> Add New</button>
            <div className={styles.addDropdown}>
              <button onClick={() => setModal({ type: 'year' })}>Academic Year</button>
              <button onClick={() => setModal({ type: 'term' })} disabled={years.length === 0}>Term</button>
              <button onClick={() => setModal({ type: 'course' })} disabled={terms.length === 0}>Course</button>
            </div>
          </div>
        </div>
      </div>

      {pageLoading ? (
        <div className={styles.loadingState}>Loading…</div>
      ) : years.length === 0 ? (
        <div className={styles.emptyState}>
          <EmptyIcon />
          <h2>No academic years yet</h2>
          <p>Create an academic year to start tracking grades.</p>
          <button className={styles.emptyBtn} onClick={() => setModal({ type: 'year' })}>
            <PlusIcon /> Create Academic Year
          </button>
        </div>
      ) : (
        <>
          <div className={styles.selectors}>
            <div className={styles.yearTabs}>
              {years.map(y => (
                <button
                  key={y.id}
                  className={`${styles.yearTab} ${selectedYearId === y.id ? styles.activeYearTab : ''}`}
                  onClick={() => { setSelectedYearId(y.id); setSelectedTermId(null); }}
                >
                  {y.name}
                </button>
              ))}
            </div>
            {terms.length > 0 && (
              <div className={styles.termTabs}>
                {terms.map(t => (
                  <button
                    key={t.id}
                    className={`${styles.termTab} ${selectedTermId === t.id ? styles.activeTermTab : ''}`}
                    onClick={() => setSelectedTermId(t.id)}
                  >
                    {t.name}
                  </button>
                ))}
                <button className={styles.addTermBtn} onClick={() => setModal({ type: 'term' })}>
                  <PlusIcon /> Term
                </button>
              </div>
            )}
          </div>

          {terms.length === 0 ? (
            <div className={styles.inlineEmpty}>
              <p>No terms in {selectedYear?.name} yet.</p>
              <button className={styles.inlineAddBtn} onClick={() => setModal({ type: 'term' })}>
                <PlusIcon /> Add Term
              </button>
            </div>
          ) : courseStates.length === 0 ? (
            <div className={styles.inlineEmpty}>
              <p>No courses in this term yet.</p>
              <button className={styles.inlineAddBtn} onClick={() => setModal({ type: 'course' })}>
                <PlusIcon /> Add Course
              </button>
            </div>
          ) : (
            <div className={styles.courseList}>
              {courseStates.map(cs => (
                <div key={cs.course.id} className={styles.courseCard}>
                  <button className={styles.courseHeader} onClick={() => toggleCourse(cs.course.id)}>
                    <div className={styles.courseHeaderLeft}>
                      <span className={styles.courseName}>{cs.course.name}</span>
                      {cs.summary && cs.summary.averageGrade > 0 && (
                        <div className={styles.summaryPills}>
                          <span className={styles.gradeChip}>{Number(cs.summary.averageGrade).toFixed(1)}%</span>
                          <span className={styles.classChip} style={{ color: classColor(cs.summary.classification), background: classColor(cs.summary.classification) + '18' }}>
                            {cs.summary.classification}
                          </span>
                          <span className={styles.gpaChip}>GPA {Number(cs.summary.averageGpa).toFixed(1)}</span>
                        </div>
                      )}
                    </div>
                    <div className={styles.courseHeaderRight}>
                      {cs.summary && cs.summary.averageGrade > 0 && (
                        <div className={styles.headerProgress}>
                          <ProgressBar value={Number(cs.summary.averageGrade)} color={gradeColor(Number(cs.summary.averageGrade))} height={6} />
                        </div>
                      )}
                      <button
                        className={styles.deleteCourseBtn}
                        onClick={e => { e.stopPropagation(); handleDeleteCourse(cs.course.id); }}
                        title="Delete course"
                      >
                        <TrashIcon />
                      </button>
                      <ChevronIcon open={cs.expanded} />
                    </div>
                  </button>

                  {cs.expanded && (
                    <div className={styles.courseBody}>
                      {cs.loading ? (
                        <div className={styles.courseLoading}>Loading…</div>
                      ) : (
                        <>
                          {cs.summary && cs.summary.averageGrade > 0 && (
                            <div className={styles.summaryRow}>
                              <div className={styles.summaryGrid}>
                                <div className={styles.summaryItem}>
                                  <span className={styles.summaryLabel}>Average</span>
                                  <span className={styles.summaryValue} style={{ color: gradeColor(Number(cs.summary.averageGrade)) }}>
                                    {Number(cs.summary.averageGrade).toFixed(2)}%
                                  </span>
                                </div>
                                <div className={styles.summaryItem}>
                                  <span className={styles.summaryLabel}>GPA</span>
                                  <span className={styles.summaryValue}>{Number(cs.summary.averageGpa).toFixed(2)}</span>
                                </div>
                                <div className={styles.summaryItem}>
                                  <span className={styles.summaryLabel}>Graded</span>
                                  <span className={styles.summaryValue}>{Number(cs.summary.percentageGraded).toFixed(0)}%</span>
                                </div>
                                <div className={styles.summaryItem}>
                                  <span className={styles.summaryLabel}>Max possible</span>
                                  <span className={styles.summaryValue}>{Number(cs.summary.maxPossibleGrade).toFixed(1)}%</span>
                                </div>
                              </div>

                              {cs.summary.gradeTargets.length > 0 && (
                                <div className={styles.targetsSection}>
                                  <span className={styles.targetsLabel}>To achieve:</span>
                                  <div className={styles.targets}>
                                    {cs.summary.gradeTargets.map(t => (
                                      <div key={t.classification} className={styles.targetChip}>
                                        <span className={styles.targetClass} style={{ color: classColor(t.classification) }}>
                                          {t.classification}
                                        </span>
                                        <span className={styles.targetReq}>need {Number(t.requiredAverage).toFixed(1)}%</span>
                                      </div>
                                    ))}
                                  </div>
                                </div>
                              )}
                            </div>
                          )}

                          {cs.assessments.length > 0 ? (
                            <div className={styles.assessmentsTable}>
                              <div className={styles.tableHead}>
                                <span>Assessment</span>
                                <span>Type</span>
                                <span>Weight</span>
                                <span>Grade</span>
                                <span>Progress</span>
                                <span></span>
                              </div>
                              {cs.assessments.map(a => (
                                <div key={a.id} className={styles.tableRow}>
                                  <span className={styles.aName}>{a.name}</span>
                                  <span className={styles.aType}>{formatType(a.assessmentType)}</span>
                                  <span className={styles.aWeight}>{Number(a.weight).toFixed(0)}%</span>
                                  <button
                                    className={`${styles.aGrade} ${a.graded ? styles.graded : styles.ungraded}`}
                                    onClick={() => setModal({ type: 'grade', assessment: a })}
                                    title={a.graded ? 'Edit grade' : 'Enter grade'}
                                  >
                                    {a.graded ? (
                                      <><span style={{ color: gradeColor(Number(a.grade)) }}>{Number(a.grade).toFixed(1)}%</span> <PencilIcon /></>
                                    ) : (
                                      <span>— Enter</span>
                                    )}
                                  </button>
                                  <div className={styles.aProgress}>
                                    {a.graded
                                      ? <ProgressBar value={Number(a.grade)} color={gradeColor(Number(a.grade))} height={5} />
                                      : <span className={styles.ungradeHint}>Not graded</span>
                                    }
                                  </div>
                                  <button
                                    className={styles.deleteBtn}
                                    onClick={() => handleDeleteAssessment(a)}
                                    title="Delete assessment"
                                  >
                                    <TrashIcon />
                                  </button>
                                </div>
                              ))}
                            </div>
                          ) : (
                            <div className={styles.noAssessments}>No assessments yet.</div>
                          )}

                          <button
                            className={styles.addAssessmentBtn}
                            onClick={() => setModal({ type: 'assessment', courseId: cs.course.id, courseName: cs.course.name })}
                          >
                            <PlusIcon /> Add Assessment
                          </button>
                        </>
                      )}
                    </div>
                  )}
                </div>
              ))}

              <button className={styles.addCourseBtn} onClick={() => setModal({ type: 'course' })}>
                <PlusIcon /> Add Course
              </button>
            </div>
          )}
        </>
      )}

      {modal?.type === 'year' && (
        <AddYearModal
          onClose={() => setModal(null)}
          onCreated={y => { setYears(prev => [...prev, y]); setSelectedYearId(y.id); setSelectedTermId(null); }}
        />
      )}
      {modal?.type === 'term' && (
        <AddTermModal
          years={years}
          defaultYearId={selectedYearId ?? undefined}
          onClose={() => setModal(null)}
          onCreated={t => { setTerms(prev => [...prev, t]); setSelectedTermId(t.id); }}
        />
      )}
      {modal?.type === 'course' && (
        <AddCourseModal
          terms={terms}
          defaultTermId={selectedTermId ?? undefined}
          onClose={() => setModal(null)}
          onCreated={c => setCourseStates(prev => [...prev, { course: c, assessments: [], summary: null, expanded: false, loading: false }])}
        />
      )}
      {modal?.type === 'assessment' && (
        <AddAssessmentModal
          courseId={modal.courseId}
          courseName={modal.courseName}
          onClose={() => setModal(null)}
          onCreated={handleAssessmentCreated}
        />
      )}
      {modal?.type === 'grade' && (
        <GradeModal
          assessment={modal.assessment}
          onClose={() => setModal(null)}
          onGraded={handleGraded}
        />
      )}
    </div>
  );
}
