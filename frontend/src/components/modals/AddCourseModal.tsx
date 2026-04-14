import { useState } from 'react';
import Modal from './Modal';
import styles from './formStyles.module.css';
import { coursesApi } from '../../api/courses';
import type { CourseResponse, TermResponse } from '../../types/api';

interface Props {
  terms: TermResponse[];
  defaultTermId?: number;
  onClose: () => void;
  onCreated: (course: CourseResponse) => void;
}

export default function AddCourseModal({ terms, defaultTermId, onClose, onCreated }: Props) {
  const [name, setName] = useState('');
  const [termId, setTermId] = useState<number>(defaultTermId ?? (terms[0]?.id ?? 0));
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim() || !termId) { setError('All fields are required.'); return; }
    setError('');
    setLoading(true);
    try {
      const course = await coursesApi.create({ name: name.trim(), termId });
      onCreated(course);
      onClose();
    } catch (err: unknown) {
      setError((err as Error).message ?? 'Failed to create course.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal title="Add Course" onClose={onClose} size="sm">
      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.field}>
          <label className={styles.label}>Course Name <span className={styles.required}>*</span></label>
          <input className={styles.input} value={name} onChange={e => setName(e.target.value)} placeholder="e.g. SWEN225 – Software Design" autoFocus />
        </div>
        <div className={styles.field}>
          <label className={styles.label}>Term <span className={styles.required}>*</span></label>
          <select className={styles.select} value={termId} onChange={e => setTermId(Number(e.target.value))}>
            {terms.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
          </select>
        </div>
        {error && <p className={styles.error}>{error}</p>}
        <div className={styles.actions}>
          <button type="button" className={styles.cancelBtn} onClick={onClose}>Cancel</button>
          <button type="submit" className={styles.submitBtn} disabled={loading}>
            {loading ? 'Creating…' : 'Add Course'}
          </button>
        </div>
      </form>
    </Modal>
  );
}
