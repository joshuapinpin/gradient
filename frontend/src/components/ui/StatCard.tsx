import styles from './StatCard.module.css';

interface StatCardProps {
  label: string;
  value: string | number;
  subtitle: string;
  icon: React.ReactNode;
  iconColor: string;
}

export default function StatCard({ label, value, subtitle, icon, iconColor }: StatCardProps) {
  return (
    <div className={styles.card}>
      <div className={styles.content}>
        <span className={styles.label}>{label}</span>
        <span className={styles.value}>{value}</span>
        <span className={styles.subtitle}>{subtitle}</span>
      </div>
      <div className={styles.iconWrapper} style={{ background: iconColor }}>
        {icon}
      </div>
    </div>
  );
}
