import { useState } from 'react';
import Modal from './Modal';
import styles from './formStyles.module.css';
import { yearsApi } from '../../api/years';
import type { YearResponse } from '../../types/api';

interface Props {
  onClose: () => void;
  onCreated: (year: YearResponse) => void;
}

export default function AddYearModal({ onClose, onCreated }: Props) {
  const [name, setName] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim() || !startDate || !endDate) { setError('All fields are required.'); return; }
    if (endDate <= startDate) { setError('End date must be after start date.'); return; }
    setError('');
    setLoading(true);
    try {
      const year = await yearsApi.create({ name: name.trim(), startDate, endDate });
      onCreated(year);
      onClose();
    } catch (err: unknown) {
      setError((err as Error).message ?? 'Failed to create year.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal title="Add Academic Year" onClose={onClose} size="sm">
      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.field}>
          <label className={styles.label}>Name <span className={styles.required}>*</span></label>
          <input className={styles.input} value={name} onChange={e => setName(e.target.value)} placeholder="e.g. 2024" autoFocus />
        </div>
        <div className={styles.row}>
          <div className={styles.field}>
            <label className={styles.label}>Start Date <span className={styles.required}>*</span></label>
            <input className={styles.input} type="date" value={startDate} onChange={e => setStartDate(e.target.value)} />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>End Date <span className={styles.required}>*</span></label>
            <input className={styles.input} type="date" value={endDate} onChange={e => setEndDate(e.target.value)} />
          </div>
        </div>
        {error && <p className={styles.error}>{error}</p>}
        <div className={styles.actions}>
          <button type="button" className={styles.cancelBtn} onClick={onClose}>Cancel</button>
          <button type="submit" className={styles.submitBtn} disabled={loading}>
            {loading ? 'Creating…' : 'Create Year'}
          </button>
        </div>
      </form>
    </Modal>
  );
}
