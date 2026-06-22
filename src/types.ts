export type ScanType = 'text' | 'image' | 'audio' | 'video';

export interface ModelBreakdown {
  syntacticAnomaly: number;      // 0-100 indicating stylistic or pixel frequency noise
  semanticConsistency: number;   // 0-100 indicating logical alignment
  repetitionPattern: number;     // 0-100 indicating repetition profiles
}

export interface ScanResult {
  isAi: boolean;
  score: number;                 // 0-100 (percentage likelihood of AI origin)
  confidence: number;            // 0-100 (scan execution certainty)
  breakdown: ModelBreakdown;
  analysisText: string;          // Elaborative text describing found markers
  metadataInfo?: string;         // Core file details like resolution or word count
}

export interface HistoryItem {
  id: string;
  type: ScanType;
  inputTextOrPath: string;       // Sourced text or URI/Description of media analyzed
  isAi: boolean;
  score: number;
  confidence: number;
  breakdown: ModelBreakdown;
  analysisText: string;
  timestamp: number;             // epoch timestamp
}
