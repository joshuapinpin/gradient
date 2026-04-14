import { useState } from 'react';
import Modal from './Modal';
import styles from './formStyles.module.css';
import { assessmentsApi } from '../../api/assessments';
import type { AssessmentResponse, AssessmentType } from '../../types/api';

const TYPES: AssessmentType[] = ['HOMEWORK', 'QUIZ', 'EXAM', 'PROJECT', 'LAB', 'TUTORIAL', 'OTHER'];

interface Props {
  courseId: number;
  courseName: string;
  onClose: () => void;
  onCreated: (assessment: AssessmentResponse) => void;
}

export default function AddAssessmentModal({ courseId, courseName, onClose, onCreated }: Props) {
  const [name, setName] = useState('');
  const [type, setType] = useState<AssessmentType>('HOMEWORK');
  const [weight, setWeight] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const w = parseFloat(weight);
    if (!name.trim()) { setError('Name is required.'); return; }
    if (!weight || isNaN(w) || w <= 0 || w > 100) { setError('Weight must be between 0.01 and 100.'); return; }
    setError('');
    setLoading(true);
    try {
      const body: Parameters<typeof assessmentsApi.create>[0] = {
        name: name.trim(),
        assessmentType: type,
        weight: w,
        courseId,
      };
      if (dueDate) body.dueDate = dueDate + ':00';
      const assessment = await assessmentsApi.create(body);
      onCreated(assessment);
      onClose();
    } catch (err: unknown) {
      setError((err as Error).message ?? 'Failed to create assessment.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal title={`Add Assessment — ${courseName}`} onClose={onClose}>
      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.field}>
          <label className={styles.label}>Name <span className={styles.required}>*</span></label>
          <input className={styles.input} value={name} onChange={e => setName(e.target.value)} placeholder="e.g. Midterm Exam" autoFocus />
        </div>
        <div className={styles.row}>
          <div className={styles.field}>
            <label className={styles.label}>Type <span className={styles.required}>*</span></label>
            <select className={styles.select} value={type} onChange={e => setType(e.target.value as AssessmentType)}>
              {TYPES.map(t => <option key={t} value={t}>{t.charAt(0) + t.slice(1).toLowerCase()}</option>)}
            </select>
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Weight (%) <span className={styles.required}>*</span></label>
            <input className={styles.input} type="number" min="0.01" max="100" step="0.01" value={weight} onChange={e => setWeight(e.target.value)} placeholder="e.g. 25" />
          </div>
        </div>
        <div className={styles.field}>
          <label className={styles.label}>Due Date</label>
          <input className={styles.input} type="datetime-local" value={dueDate} onChange={e => setDueDate(e.target.value)} />
          <span className={styles.hint}>Optional</span>
        </div>
        {error && <p className={styles.error}>{error}</p>}
        <div className={styles.actions}>
          <button type="button" className={styles.cancelBtn} onClick={onClose}>Cancel</button>
          <button type="submit" className={styles.submitBtn} disabled={loading}>
            {loading ? 'Adding…' : 'Add Assessment'}
          </button>
        </div>
      </form>
    </Modal>
  );
}
