import { useState } from 'react';
import Modal from './Modal';
import styles from './formStyles.module.css';
import { termsApi } from '../../api/terms';
import type { TermResponse, YearResponse } from '../../types/api';

interface Props {
  years: YearResponse[];
  defaultYearId?: number;
  onClose: () => void;
  onCreated: (term: TermResponse) => void;
}

export default function AddTermModal({ years, defaultYearId, onClose, onCreated }: Props) {
  const [name, setName] = useState('');
  const [yearId, setYearId] = useState<number>(defaultYearId ?? (years[0]?.id ?? 0));
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim() || !yearId) { setError('Name and year are required.'); return; }
    setError('');
    setLoading(true);
    try {
      const body: Parameters<typeof termsApi.create>[0] = { name: name.trim(), yearId };
      if (startDate) body.startDate = startDate;
      if (endDate) body.endDate = endDate;
      const term = await termsApi.create(body);
      onCreated(term);
      onClose();
    } catch (err: unknown) {
      setError((err as Error).message ?? 'Failed to create term.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal title="Add Term" onClose={onClose} size="sm">
      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.field}>
          <label className={styles.label}>Term Name <span className={styles.required}>*</span></label>
          <input className={styles.input} value={name} onChange={e => setName(e.target.value)} placeholder="e.g. Trimester 1" autoFocus />
        </div>
        <div className={styles.field}>
          <label className={styles.label}>Academic Year <span className={styles.required}>*</span></label>
          <select className={styles.select} value={yearId} onChange={e => setYearId(Number(e.target.value))}>
            {years.map(y => <option key={y.id} value={y.id}>{y.name}</option>)}
          </select>
        </div>
        <div className={styles.row}>
          <div className={styles.field}>
            <label className={styles.label}>Start Date</label>
            <input className={styles.input} type="date" value={startDate} onChange={e => setStartDate(e.target.value)} />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>End Date</label>
            <input className={styles.input} type="date" value={endDate} onChange={e => setEndDate(e.target.value)} />
          </div>
        </div>
        {error && <p className={styles.error}>{error}</p>}
        <div className={styles.actions}>
          <button type="button" className={styles.cancelBtn} onClick={onClose}>Cancel</button>
          <button type="submit" className={styles.submitBtn} disabled={loading}>
            {loading ? 'Creating…' : 'Create Term'}
          </button>
        </div>
      </form>
    </Modal>
  );
}
