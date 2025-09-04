package com.kydas.build.core.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitingService {
    private final Map<String, Bucket> cacheMetrics = new ConcurrentHashMap<>();
    private final Map<String, Bucket> cacheFeedbacks = new ConcurrentHashMap<>();
    private final Map<String, Bucket> cacheReviews = new ConcurrentHashMap<>();
    private final Map<String, Bucket> cacheReviewsFiles = new ConcurrentHashMap<>();

    public Bucket resolveMetricsBucket(String ip) {
        return cacheMetrics.computeIfAbsent(ip, this::newMetricsBucket);
    }

    public Bucket resolveReviewsBucket(String ip, String userId) {
        return cacheReviews.computeIfAbsent(ip + userId, this::newReviewsBucket);
    }

    public Bucket resolveReviewsFilesBucket(String ip, String userId) {
        return cacheReviewsFiles.computeIfAbsent(ip + userId, this::newReviewsFilesBucket);
    }

    public Bucket resolveFeedbacksBucket(UUID userId) {
        return cacheFeedbacks.computeIfAbsent(userId.toString(), this::newFeedbacksBucket);
    }

    private Bucket newFeedbacksBucket(String ip) {
        Bandwidth limit = Bandwidth.builder().capacity(10).refillIntervally(10, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket newMetricsBucket(String ip) {
        Bandwidth limit = Bandwidth.builder().capacity(10).refillIntervally(10, Duration.ofSeconds(1)).build();
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket newReviewsBucket(String ip) {
        Bandwidth limit = Bandwidth.builder().capacity(1).refillIntervally(1, Duration.ofHours(12)).build();
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket newReviewsFilesBucket(String ip) {
        Bandwidth limit = Bandwidth.builder().capacity(10).refillIntervally(10, Duration.ofHours(12)).build();
        return Bucket.builder().addLimit(limit).build();
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void reportCurrentTime() {
        cacheMetrics.clear();
        cacheFeedbacks.clear();
    }

    @Scheduled(fixedRate = 24, timeUnit = TimeUnit.HOURS)
    public void reportCurrentTimeLong() {
        cacheReviews.clear();
        cacheReviewsFiles.clear();
    }
}
