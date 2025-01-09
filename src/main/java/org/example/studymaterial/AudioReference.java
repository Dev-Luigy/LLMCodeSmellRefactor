package org.example.studymaterial;

import java.util.List;

class AudioAttributes {
    private AudioReference.AudioQuality audioQuality = null;
    private boolean isDownloadable = false;
    private String title = "";
    private String description = "";
    private String link = "";
    private String accessRights = "";
    private String license = "";
    private String language = "";
    private int rating = 0;
    private int viewCount = 0;
    private int shareCount = 0;

    private record BasicInfo(String title, String description, String link) {
        private BasicInfo {
            if (title == null || description == null || link == null) {
                throw new IllegalStateException("Basic info (title, description, link) must be set");
            }
        }
    }

    private record StatsInfo(int rating, int viewCount, int shareCount) {}

    private record RightsInfo(String accessRights, String license, String language) {
        private RightsInfo {
            if (accessRights == null || license == null || language == null) {
                throw new IllegalStateException("Rights info (accessRights, license, language) must be set");
            }
        }
    }

    private record QualityInfo(AudioReference.AudioQuality audioQuality, boolean isDownloadable) {
        private QualityInfo {
            if (audioQuality == null) {
                throw new IllegalStateException("AudioQuality must be set");
            }
        }
    }

    private record AttributeData(
            BasicInfo basicInfo,
            StatsInfo statsInfo,
            RightsInfo rightsInfo,
            QualityInfo qualityInfo
    ) {}

    private AudioAttributes(AttributeData data) {
        initializeFromGroups(
                data.basicInfo(),
                data.statsInfo(),
                data.rightsInfo(),
                data.qualityInfo()
        );
    }

    private void initializeFromGroups(BasicInfo basic, StatsInfo stats,
                                      RightsInfo rights, QualityInfo quality) {
        initializeQualityInfo(quality);
        initializeBasicInfo(basic);
        initializeRightsInfo(rights);
        initializeStatsInfo(stats);
    }

    private void initializeQualityInfo(QualityInfo quality) {
        this.audioQuality = quality.audioQuality();
        this.isDownloadable = quality.isDownloadable();
    }

    private void initializeBasicInfo(BasicInfo basic) {
        this.title = basic.title();
        this.description = basic.description();
        this.link = basic.link();
    }

    private void initializeRightsInfo(RightsInfo rights) {
        this.accessRights = rights.accessRights();
        this.license = rights.license();
        this.language = rights.language();
    }

    private void initializeStatsInfo(StatsInfo stats) {
        this.rating = stats.rating();
        this.viewCount = stats.viewCount();
        this.shareCount = stats.shareCount();
    }

    // Business logic methods
    public boolean isHighQualityAudio() {
        return audioQuality == AudioReference.AudioQuality.HIGH ||
                audioQuality == AudioReference.AudioQuality.VERY_HIGH;
    }

    public boolean isPopular() {
        return viewCount > 1000 && rating > 4;
    }

    public boolean isShareable() {
        return isDownloadable && shareCount > 0;
    }

    public void applyToReference(AudioReference reference) {
        reference.editBasic(title, description, link);
        reference.setAccessRights(accessRights);
        reference.setLicense(license);
        reference.setAudioQuality(audioQuality);
        reference.setRating(rating);
        reference.setShareCount(shareCount);
        reference.setViewCount(viewCount);
        reference.setDownloadable(isDownloadable);
        reference.setLanguage(language);
    }

    // Getters
    public AudioReference.AudioQuality getAudioQuality() { return audioQuality; }
    public boolean isDownloadable() { return isDownloadable; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLink() { return link; }
    public String getAccessRights() { return accessRights; }
    public String getLicense() { return license; }
    public String getLanguage() { return language; }
    public int getRating() { return rating; }
    public int getViewCount() { return viewCount; }
    public int getShareCount() { return shareCount; }

    // Builder class
    public static class Builder {
        private AudioReference.AudioQuality audioQuality;
        private boolean isDownloadable;
        private String title;
        private String description;
        private String link;
        private String accessRights;
        private String license;
        private String language;
        private int rating;
        private int viewCount;
        private int shareCount;

        public Builder() {
            this.rating = 0;
            this.viewCount = 0;
            this.shareCount = 0;
            this.isDownloadable = false;
        }

        public Builder withBasicInfo(String title, String description, String link) {
            this.title = title;
            this.description = description;
            this.link = link;
            return this;
        }

        public Builder withAudioQuality(AudioReference.AudioQuality quality) {
            this.audioQuality = quality;
            return this;
        }

        public Builder withRights(String accessRights, String license) {
            this.accessRights = accessRights;
            this.license = license;
            return this;
        }

        public Builder withStats(int rating, int viewCount, int shareCount) {
            this.rating = rating;
            this.viewCount = viewCount;
            this.shareCount = shareCount;
            return this;
        }

        public Builder withLanguageAndDownload(String language, boolean isDownloadable) {
            this.language = language;
            this.isDownloadable = isDownloadable;
            return this;
        }

        public AudioAttributes build() {
            BasicInfo basicInfo = new BasicInfo(title, description, link);
            StatsInfo statsInfo = new StatsInfo(rating, viewCount, shareCount);
            RightsInfo rightsInfo = new RightsInfo(accessRights, license, language);
            QualityInfo qualityInfo = new QualityInfo(audioQuality, isDownloadable);

            return new AudioAttributes(new AttributeData(
                    basicInfo, statsInfo, rightsInfo, qualityInfo
            ));
        }
    }
}

public class AudioReference extends Reference {
    public enum AudioQuality {
        LOW, MEDIUM, HIGH, VERY_HIGH;
    }
    private AudioQuality audioQuality;

    public AudioReference(AudioQuality quality) {
        if (quality == null) {
            throw new IllegalArgumentException("AudioQuality cannot be null");
        }
        this.audioQuality = quality;
    }

    public AudioQuality getAudioQuality() {
        return audioQuality;
    }

    public static AudioQuality audioQualityAdapter(String quality) {
        return switch (quality.toLowerCase()) {
            case "low" -> AudioQuality.LOW;
            case "medium" -> AudioQuality.MEDIUM;
            case "high" -> AudioQuality.HIGH;
            case "very_high" -> AudioQuality.VERY_HIGH;
            default -> null;
        };
    }

    public void setAudioQuality(AudioQuality audioQuality) {
        if (audioQuality == null) {
            throw new IllegalArgumentException("AudioQuality cannot be null");
        }
        this.audioQuality = audioQuality;
    }

    public void editAudio(AudioAttributes attributes) {
        attributes.applyToReference(this);
    }

    public void editAudioAdapter(List<String> properties, List<Integer> intProperties,
                                 AudioQuality audioQuality, boolean isDownloadable) {
        if (properties == null || properties.size() < 6 || intProperties == null || intProperties.size() < 3) {
            throw new IllegalArgumentException("Invalid properties or intProperties lists");
        }

        AudioAttributes attributes = new AudioAttributes.Builder()
                .withBasicInfo(properties.get(0), properties.get(1), properties.get(2))
                .withRights(properties.get(3), properties.get(4))
                .withLanguageAndDownload(properties.get(5), isDownloadable)
                .withAudioQuality(audioQuality)
                .withStats(intProperties.get(0), intProperties.get(1), intProperties.get(2))
                .build();

        this.editAudio(attributes);
    }

    public void editBasic(String title, String description, String link) {
        this.setTitle(title);
        this.setDescription(description);
        this.setLink(link);
    }
}