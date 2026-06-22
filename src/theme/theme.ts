import { COLORS, SPACING, TYPOGRAPHY } from '../utils/constants';

export const AISCERNTheme = {
  dark: true,
  colors: {
    primary: COLORS.accent,
    background: COLORS.background,
    card: COLORS.cardBg,
    text: COLORS.text,
    border: 'rgba(0, 212, 255, 0.25)',
    notification: COLORS.secondaryAccent,
  },
  typography: {
    sizes: {
      h1: 28,
      h2: 24,
      h3: 18,
      body: 14,
      caption: 11,
      mono: 12,
    },
    weights: {
      regular: '400',
      semibold: '600',
      bold: '700',
      black: '900',
    },
  },
  spacing: SPACING,
};

export default AISCERNTheme;
