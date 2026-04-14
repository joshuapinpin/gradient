import styles from './ProgressBar.module.css';

interface ProgressBarProps {
  value: number;
  max?: number;
  color?: string;
  height?: number;
  showLabel?: boolean;
}

export default function ProgressBar({ value, max = 100, color = '#3b82f6', height = 6, showLabel = false }: ProgressBarProps) {
  const pct = Math.min(100, Math.max(0, (value / max) * 100));
  return (
    <div className={styles.wrapper}>
      <div className={styles.track} style={{ height }}>
        <div
          className={styles.fill}
          style={{ width: `${pct}%`, background: color, height }}
        />
      </div>
      {showLabel && <span className={styles.label}>{Math.round(pct)}%</span>}
    </div>
  );
}
