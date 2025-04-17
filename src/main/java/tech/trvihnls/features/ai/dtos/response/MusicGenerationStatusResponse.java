package tech.trvihnls.features.ai.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class MusicGenerationStatusResponse {
    private int code;
    private String msg;
    private MusicGenerationData data;

    @Data
    public static class MusicGenerationData {
        private String taskId;
        private String parentMusicId;
        private String param;
        private ResponseDetails response;
        private String status;
        private String type;
        private Integer errorCode;
        private String errorMessage;

        @Data
        public static class ResponseDetails {
            private String taskId;
            private List<MusicTrack> sunoData;
        }

        @Data
        public static class MusicTrack {
            private String id;
            private String audioUrl;
            private String streamAudioUrl;
            private String imageUrl;
            private String prompt;
            private String modelName;
            private String title;
            private String tags;
            private String createTime;
            private double duration;
        }
    }
}
