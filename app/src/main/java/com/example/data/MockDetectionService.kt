package com.example.data

import kotlinx.coroutines.delay
import kotlin.random.Random

object MockDetectionService {
    
    suspend fun detectText(input: String): ScanResult {
        delay(Random.nextLong(2000, 4500))
        val isAi = Random.nextFloat() < 0.6f
        val confidence = Random.nextFloat() * 33f + 65f // 65-98
        
        return ScanResult(
            isAi = isAi,
            score = confidence,
            confidence = Random.nextFloat() * 10f + 85f,
            analysisText = "Analyzed ${input.length} characters using NLP models.",
            metadataInfo = "Tokens: ${input.length / 4}, Est. Time: 12ms",
            breakdown = generateBreakdown(isAi)
        )
    }
    
    suspend fun detectImage(uri: String): ScanResult {
        delay(Random.nextLong(2000, 4500))
        val isAi = Random.nextFloat() < 0.55f
        val confidence = Random.nextFloat() * 33f + 65f
        
        return ScanResult(
            isAi = isAi,
            score = confidence,
            confidence = Random.nextFloat() * 10f + 85f,
            analysisText = "Image topology and noise distributions analyzed.",
            metadataInfo = "Resolution: 1024x1024, Size: Unknown",
            breakdown = generateBreakdown(isAi)
        )
    }
    
    suspend fun detectAudio(uri: String): ScanResult {
        delay(Random.nextLong(2000, 4500))
        val isAi = Random.nextFloat() < 0.50f
        val confidence = Random.nextFloat() * 33f + 65f
        
        return ScanResult(
            isAi = isAi,
            score = confidence,
            confidence = Random.nextFloat() * 10f + 85f,
            analysisText = "Spectrographic analysis completed on audio waveform.",
            metadataInfo = "Duration: ~30s, Sample Rate: 44.1kHz",
            breakdown = generateBreakdown(isAi)
        )
    }
    
    suspend fun detectVideo(uri: String): ScanResult {
        delay(Random.nextLong(2000, 4500))
        val isAi = Random.nextFloat() < 0.45f
        val confidence = Random.nextFloat() * 33f + 65f
        
        return ScanResult(
            isAi = isAi,
            score = confidence,
            confidence = Random.nextFloat() * 10f + 85f,
            analysisText = "Temporal consistency check completed on frames.",
            metadataInfo = "Codec: H.264, Framerate: 30fps",
            breakdown = generateBreakdown(isAi)
        )
    }
    
    private fun generateBreakdown(isAi: Boolean): ModelBreakdown {
        val baseScore = if (isAi) 80f else 20f
        return ModelBreakdown(
            syntacticAnomaly = baseScore + Random.nextFloat() * 20 - 10,
            semanticConsistency = baseScore + Random.nextFloat() * 20 - 10,
            repetitionPattern = baseScore + Random.nextFloat() * 20 - 10
        )
    }
}
