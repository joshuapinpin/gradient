import { useState } from 'react';
import Modal from './Modal';
import styles from './formStyles.module.css';
import { assessmentsApi } from '../../api/assessments';
import type { AssessmentResponse } from '../../types/api';

interface Props {
  assessment: AssessmentResponse;
  onClose: () => void;
  onGraded: (assessment: AssessmentResponse) => void;
}

export default function GradeModal({ assessment, onClose, onGraded }: Props) {
  const [grade, setGrade] = useState(assessment.grade != null ? String(assessment.grade) : '');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const g = grade === '' ? null : parseFloat(grade);
    if (g !== null && (isNaN(g) || g < 0 || g > 100)) {
      setError('Grade must be between 0 and 100.');
      return;
    }
    setError('');
    setLoading(true);
    try {
      const updated = await assessmentsApi.grade(assessment.id, g);
      onGraded(updated);
      onClose();
    } catch (err: unknown) {
      setError((err as Error).message ?? 'Failed to save grade.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal title="Enter Grade" onClose={onClose} size="sm">
      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.field}>
          <label className={styles.label}>
            {assessment.name}
            <span style={{ color: '#94a3b8', fontWeight: 400, marginLeft: 6 }}>({assessment.weight}% weight)</span>
          </label>
          <input
            className={styles.input}
            type="number"
            min="0"
            max="100"
            step="0.01"
            value={grade}
            onChange={e => setGrade(e.target.value)}
            placeholder="Enter grade (0–100)"
            autoFocus
          />
          <span className={styles.hint}>Leave blank to clear the grade.</span>
        </div>
        {error && <p className={styles.error}>{error}</p>}
        <div className={styles.actions}>
          <button type="button" className={styles.cancelBtn} onClick={onClose}>Cancel</button>
          <button type="submit" className={styles.submitBtn} disabled={loading}>
            {loading ? 'Saving…' : 'Save Grade'}
          </button>
        </div>
      </form>
    </Modal>
  );
}
