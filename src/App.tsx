import React, { useState } from 'react';
import { StatusBar } from 'expo-status-bar';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { 
  NavigationContainer, 
  DefaultTheme 
} from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { 
  StyleSheet, 
  Text, 
  View, 
  TouchableOpacity, 
  ScrollView,
  TextInput,
  Share,
  Dimensions,
  Animated
} from 'react-native';
import { FontAwesome5, Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { COLORS, BRAND } from './utils/constants';
import { AISCERNTheme } from './theme/theme';
import { ScaleState } from 'react-native-reanimated';
import { ScanResult, ScanType, HistoryItem } from './types';

// Native navigation navigators
const Tab = createBottomTabNavigator();
const Stack = createNativeStackNavigator();

// Static mockup samples
const SCAN_HISTORY_INIT: HistoryItem[] = [
  {
    id: '1',
    type: 'text',
    inputTextOrPath: 'Standard LLM text: Furthermore, it is important to delve deep into the core complexities and explore the dynamic nature of generative AI. First and foremost, we must analyze the key paradigm shifts...',
    isAi: true,
    score: 87,
    confidence: 94,
    breakdown: { syntacticAnomaly: 90, semanticConsistency: 85, repetitionPattern: 86 },
    analysisText: 'Stylistic markers show high probability of AI origin. Structure showcases highly uniform sentence length, vocabulary density is overly normalized, and common LLM transition phrases such as "delve" and "furthermore" were detected.',
    timestamp: Date.now() - 3600000
  },
  {
    id: '2',
    type: 'image',
    inputTextOrPath: 'Close up selfie of elderly man in sunlight. Highly detailed skin microstructures.',
    isAi: false,
    score: 8,
    confidence: 89,
    breakdown: { syntacticAnomaly: 12, semanticConsistency: 42, repetitionPattern: 8 },
    analysisText: 'Analysis shows highly authentic, organic patterns. Texture frequencies are inconsistent with neural diffusion, skin pores display standard asymmetry, and lighting direction maintains accurate physical vector casting.',
    timestamp: Date.now() - 7200000
  }
];

// --- NAVIGATION CONTAINERS AND TAB PAGES ---

// Tab Screen 1: SCAN PAGE
function ScanScreen({ navigation }: any) {
  const [selectedType, setSelectedType] = useState<ScanType>('text');
  const [textInput, setTextInput] = useState('');
  const [isScanning, setIsScanning] = useState(false);
  const [result, setResult] = useState<ScanResult | null>(null);

  const handleScan = () => {
    setIsScanning(true);
    setTimeout(() => {
      setIsScanning(false);
      const mockResult: ScanResult = {
        isAi: textInput.toLowerCase().includes('furthermore') || textInput.length % 2 === 0,
        score: textInput.toLowerCase().includes('furthermore') ? 89 : 14,
        confidence: 85,
        breakdown: {
          syntacticAnomaly: textInput.toLowerCase().includes('furthermore') ? 91 : 18,
          semanticConsistency: textInput.toLowerCase().includes('furthermore') ? 82 : 35,
          repetitionPattern: textInput.toLowerCase().includes('furthermore') ? 88 : 22,
        },
        analysisText: textInput.toLowerCase().includes('furthermore')
          ? 'Scanning completed. High concentration of repetitive n-gram patterns and signature syntactic structures matching typical transformer-based LLM architectures.'
          : 'Frequency analysis complete. Distinct variety in sentence structure complexity and high entropy rate indicate an authentic human output.',
        metadataInfo: 'Length: ' + textInput.length + ' chars | Frequency: REST v1beta'
      };
      setResult(mockResult);
    }, 2000);
  };

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.contentContainer}>
      <View style={styles.header}>
        <View style={styles.badge}><Text style={styles.badgeText}>CORE BETA</Text></View>
        <Text style={styles.title}>NEURAL DESCRIPTOR</Text>
        <Text style={styles.subtitle}>Eradicate generative forgery on demand</Text>
      </View>

      {/* Mode selectors */}
      <View style={styles.tabRow}>
        {(['text', 'image', 'audio', 'video'] as ScanType[]).map((t) => (
          <TouchableOpacity 
            key={t}
            style={[styles.modeTab, selectedType === t && { backgroundColor: COLORS.accent }]}
            onPress={() => {
              setSelectedType(t);
              setResult(null);
              setTextInput('');
            }}
          >
            <Text style={[styles.modeTabText, selectedType === t && { color: COLORS.background }]}>
              {t.toUpperCase()}
            </Text>
          </TouchableOpacity>
        ))}
      </View>

      {isScanning ? (
        <View style={styles.scanningBox}>
          <Text style={styles.scanningText}>DECRYPTING SPECTRAL VECTORS...</Text>
          <Text style={styles.subterm}>Multi-spectral pattern analyzer active</Text>
        </View>
      ) : result ? (
        <View style={styles.card}>
          <Text style={styles.cardLabel}>SCAN VERDICT</Text>
          <Text style={[styles.verdictText, { color: result.isAi ? COLORS.alert : COLORS.success }]}>
            {result.isAi ? "AI SYNTHETIC PATH" : "HUMAN AUTHENTIC"}
          </Text>

          {/* Core score ring mockup */}
          <View style={styles.scoreGauge}>
            <Text style={styles.scoreNum}>{result.score}%</Text>
            <Text style={styles.scoreLabel}>{result.isAi ? "AI PROBABILITY" : "AUTHENTICITY"}</Text>
          </View>

          <Text style={styles.analysisBody}>{result.analysisText}</Text>

          <View style={styles.buttonRow}>
            <TouchableOpacity style={styles.btnSecondary} onPress={() => setResult(null)}>
              <Text style={styles.btnTextSecondary}>New Scan</Text>
            </TouchableOpacity>

            <TouchableOpacity 
              style={styles.btnPrimary} 
              onPress={() => navigation.navigate('Detail', { result, rawInput: textInput })}
            >
              <Text style={styles.btnText}>View Specs</Text>
            </TouchableOpacity>
          </View>
        </View>
      ) : (
        <View style={styles.card}>
          <Text style={styles.label}>INPUT SOURCE</Text>
          <TextInput
            style={styles.input}
            multiline
            numberOfLines={6}
            placeholder="Paste raw corpus, URL transcripts or image keywords to analyze for structural anomalies..."
            value={textInput}
            onChangeText={setTextInput}
            placeholderTextColor={COLORS.mutedText}
          />
          
          <TouchableOpacity 
            style={[styles.submitBtn, { opacity: textInput.length >= 10 ? 1 : 0.5 }]} 
            onPress={handleScan}
            disabled={textInput.length < 10}
          >
            <Text style={styles.submitBtnText}>EXECUTE SCAN</Text>
          </TouchableOpacity>
        </View>
      )}
    </ScrollView>
  );
}

// Tab Screen 2: MONITOR PAGE
function MonitorScreen() {
  return (
    <View style={styles.centerContainer}>
      <MaterialCommunityIcons name="shield-key-outline" size={64} color={COLORS.accent} />
      <Text style={[styles.title, { marginTop: 12 }]}>AISCERN TELEMETRY</Text>
      <Text style={styles.subtext}>Global AI contamination rate: 18.4% (Guarded)</Text>
    </View>
  );
}

// Tab Screen 3: LEDGER PAGE
function LedgerScreen({ navigation }: any) {
  const [history] = useState<HistoryItem[]>(SCAN_HISTORY_INIT);

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.contentContainer}>
      <Text style={styles.title}>SCAN LEDGER</Text>
      <Text style={styles.subtitle}>Previous local spectral audits</Text>

      {history.map((item) => (
        <TouchableOpacity 
          key={item.id} 
          style={styles.ledgerItem}
          onPress={() => navigation.navigate('Detail', { result: item, rawInput: item.inputTextOrPath })}
        >
          <View style={styles.row}>
            <Text style={styles.ledgerType}>{item.type.toUpperCase()}</Text>
            <Text style={[styles.ledgerScore, { color: item.isAi ? COLORS.alert : COLORS.success }]}>
              Score: {item.score}%
            </Text>
          </View>
          <Text style={styles.ledgerSummary} numberOfLines={1}>{item.inputTextOrPath}</Text>
        </TouchableOpacity>
      ))}
    </ScrollView>
  );
}

// Stack Screen: DETAILED VECTOR ANALYSIS
function DetailScreen({ route, navigation }: any) {
  const { result, rawInput } = route.params;

  const handleShare = async () => {
    try {
      await Share.share({
        message: `AISCERN Neural Scan Result:\nVerdict: ${result.isAi ? "AI GENERATED" : "HUMAN AUTHENTIC"}\nConfidence: ${result.confidence}%\nAI Score: ${result.score}%\n\nAnalysis Summary:\n${result.analysisText}`
      });
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.contentContainer}>
      <View style={styles.backRow}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Ionicons name="arrow-back" size={24} color={COLORS.text} />
        </TouchableOpacity>
        <Text style={styles.headline}>SPECTRAL DIAGNOSTICS</Text>
      </View>

      <View style={[styles.card, { marginTop: 16 }]}>
        <Text style={styles.cardLabel}>COGNITIVE ANOMALISTIC GRIDS</Text>
        
        {/* Triple micro indicators */}
        <View style={styles.row}>
          <View style={styles.microMeter}>
            <Text style={styles.microTitle}>SYNTAX NOISE</Text>
            <Text style={styles.microValue}>{result.breakdown.syntacticAnomaly}%</Text>
          </View>
          <View style={styles.microMeter}>
            <Text style={styles.microTitle}>SEMANTIC consistency</Text>
            <Text style={styles.microValue}>{result.breakdown.semanticConsistency}%</Text>
          </View>
          <View style={styles.microMeter}>
            <Text style={styles.microTitle}>REPETITION TRACE</Text>
            <Text style={styles.microValue}>{result.breakdown.repetitionPattern}%</Text>
          </View>
        </View>

        <Text style={styles.detailBodyTitle}>NEURAL CONCLUSION</Text>
        <Text style={styles.detailBody}>{result.analysisText}</Text>

        <Text style={styles.detailBodyTitle}>ORIGINAL INPUT</Text>
        <Text style={styles.inputTextPreview}>{rawInput || "[Binary Payload Image]"}</Text>

        <TouchableOpacity style={styles.shareBtn} onPress={handleShare}>
          <Text style={styles.btnText}>SHARE FREQUENCY CERTIFICATE</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
}

// --- CORE NAVIGATION WRAPPING ---

function MasterTabNavigator() {
  return (
    <Tab.Navigator
      screenOptions={{
        headerShown: false,
        tabBarStyle: {
          backgroundColor: COLORS.cardBg,
          borderTopWidth: 0,
        },
        tabBarActiveTintColor: COLORS.accent,
        tabBarInactiveTintColor: COLORS.mutedText,
      }}
    >
      <Tab.Screen 
        name="Scanner" 
        component={ScanScreen}
        options={{
          tabBarIcon: ({ color }) => <Ionicons name="scan-circle" size={24} color={color} />
        }}
      />
      <Tab.Screen 
        name="Telemetry" 
        component={MonitorScreen} 
        options={{
          tabBarIcon: ({ color }) => <Ionicons name="analytics" size={24} color={color} />
        }}
      />
      <Tab.Screen 
        name="Ledger" 
        component={LedgerScreen} 
        options={{
          tabBarIcon: ({ color }) => <Ionicons name="folder-open" size={24} color={color} />
        }}
      />
    </Tab.Navigator>
  );
}

export default function App() {
  return (
    <SafeAreaProvider style={{ backgroundColor: COLORS.background }}>
      <StatusBar style="light" />
      <NavigationContainer theme={AISCERNTheme}>
        <Stack.Navigator screenOptions={{ headerShown: false }}>
          <Stack.Screen name="Main" component={MasterTabNavigator} />
          <Stack.Screen name="Detail" component={DetailScreen} />
        </Stack.Navigator>
      </NavigationContainer>
    </SafeAreaProvider>
  );
}

// --- PREMIUM SPACE Blue STYLESHEET ---

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  contentContainer: {
    padding: 16,
    paddingTop: 48,
  },
  centerContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: COLORS.background,
    padding: 24,
  },
  header: {
    alignItems: 'center',
    marginBottom: 20,
  },
  badge: {
    backgroundColor: COLORS.accent,
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
    marginBottom: 6,
  },
  badgeText: {
    fontSize: 9,
    fontWeight: '900',
    color: COLORS.background,
  },
  title: {
    fontSize: 22,
    fontWeight: '900',
    color: COLORS.accent,
    letterSpacing: 1.5,
  },
  subtitle: {
    fontSize: 12,
    color: COLORS.mutedText,
    textAlign: 'center',
    marginTop: 2,
  },
  subtext: {
    color: COLORS.text,
    fontSize: 14,
    fontWeight: '700',
    marginTop: 8,
  },
  tabRow: {
    flexDirection: 'row',
    backgroundColor: COLORS.cardBg,
    borderRadius: 10,
    padding: 4,
    marginBottom: 16,
  },
  modeTab: {
    flex: 1,
    paddingVertical: 10,
    alignItems: 'center',
    borderRadius: 8,
  },
  modeTabText: {
    fontSize: 10,
    fontWeight: '900',
    color: COLORS.mutedText,
  },
  card: {
    backgroundColor: COLORS.cardBg,
    borderRadius: 16,
    borderWidth: 1,
    borderColor: 'rgba(0, 212, 255, 0.15)',
    padding: 18,
  },
  cardLabel: {
    fontSize: 10,
    fontWeight: '900',
    color: COLORS.mutedText,
    letterSpacing: 1,
  },
  verdictText: {
    fontSize: 20,
    fontWeight: '900',
    marginTop: 4,
    marginBottom: 12,
  },
  scoreGauge: {
    width: 130,
    height: 130,
    borderRadius: 65,
    borderWidth: 10,
    borderColor: COLORS.accent,
    justifyContent: 'center',
    alignItems: 'center',
    alignSelf: 'center',
    marginVertical: 16,
    shadowColor: COLORS.accent,
    shadowRadius: 10,
    shadowOpacity: 0.3,
  },
  scoreNum: {
    fontSize: 32,
    fontWeight: '900',
    color: COLORS.text,
  },
  scoreLabel: {
    fontSize: 8,
    color: COLORS.mutedText,
    fontWeight: 'bold',
  },
  analysisBody: {
    fontSize: 13,
    color: COLORS.text,
    lineHeight: 18,
    textAlign: 'center',
    marginHorizontal: 8,
    marginBottom: 20,
  },
  buttonRow: {
    flexDirection: 'row',
    gap: 12,
  },
  btnPrimary: {
    flex: 1,
    backgroundColor: COLORS.accent,
    paddingVertical: 12,
    borderRadius: 10,
    alignItems: 'center',
  },
  btnText: {
    color: COLORS.background,
    fontWeight: '900',
    fontSize: 13,
    letterSpacing: 1,
  },
  btnSecondary: {
    flex: 1,
    borderWidth: 1,
    borderColor: 'rgba(0, 212, 255, 0.4)',
    paddingVertical: 12,
    borderRadius: 10,
    alignItems: 'center',
  },
  btnTextSecondary: {
    color: COLORS.accent,
    fontWeight: '700',
    fontSize: 13,
  },
  label: {
    fontSize: 11,
    fontWeight: '900',
    color: COLORS.accent,
    marginBottom: 8,
  },
  input: {
    backgroundColor: COLORS.background,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: 'rgba(248, 250, 252, 0.15)',
    color: COLORS.text,
    padding: 12,
    textAlignVertical: 'top',
    fontSize: 12,
    marginBottom: 16,
  },
  submitBtn: {
    backgroundColor: COLORS.accent,
    paddingVertical: 14,
    borderRadius: 12,
    alignItems: 'center',
  },
  submitBtnText: {
    color: COLORS.background,
    fontWeight: '900',
    letterSpacing: 1.5,
  },
  scanningBox: {
    height: 240,
    backgroundColor: 'rgba(0, 212, 255, 0.05)',
    borderWidth: 1,
    borderColor: COLORS.accent,
    borderRadius: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  scanningText: {
    color: COLORS.accent,
    fontWeight: '900',
    fontSize: 14,
    letterSpacing: 2,
  },
  subterm: {
    color: COLORS.mutedText,
    fontSize: 10,
    marginTop: 4,
  },
  ledgerItem: {
    backgroundColor: 'rgba(30, 41, 59, 0.5)',
    borderWidth: 1,
    borderColor: 'rgba(248, 250, 252, 0.05)',
    borderRadius: 12,
    padding: 14,
    marginBottom: 10,
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  ledgerType: {
    fontSize: 10,
    fontWeight: '900',
    color: COLORS.text,
  },
  ledgerScore: {
    fontSize: 11,
    fontWeight: '700',
  },
  ledgerSummary: {
    fontSize: 12,
    color: COLORS.mutedText,
    marginTop: 4,
  },
  backRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
  },
  headline: {
    fontSize: 14,
    fontWeight: '900',
    color: COLORS.accent,
    letterSpacing: 1.5,
  },
  microMeter: {
    flex: 1,
    backgroundColor: COLORS.background,
    padding: 10,
    borderRadius: 8,
    alignItems: 'center',
  },
  microTitle: {
    fontSize: 8,
    fontWeight: 'bold',
    color: COLORS.mutedText,
    textAlign: 'center',
  },
  microValue: {
    fontSize: 15,
    fontWeight: '900',
    color: COLORS.text,
    marginTop: 6,
  },
  detailBodyTitle: {
    fontSize: 11,
    fontWeight: '900',
    color: COLORS.accent,
    marginTop: 18,
    marginBottom: 6,
    letterSpacing: 1,
  },
  detailBody: {
    color: COLORS.text,
    fontSize: 13,
    lineHeight: 18,
  },
  inputTextPreview: {
    color: COLORS.mutedText,
    fontSize: 11,
    backgroundColor: COLORS.background,
    padding: 8,
    borderRadius: 6,
    lineHeight: 15,
  },
  shareBtn: {
    backgroundColor: COLORS.accent,
    marginTop: 20,
    paddingVertical: 14,
    borderRadius: 12,
    alignItems: 'center',
  }
});
