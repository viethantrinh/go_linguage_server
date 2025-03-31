package tech.trvihnls.commons.bootstraps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tech.trvihnls.commons.domains.Song;
import tech.trvihnls.commons.domains.WordTimestamp;
import tech.trvihnls.commons.repositories.SongRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SongRepository songRepository;


    @Override
    public void run(String... args) throws Exception {
        WordTimestamp wordTimestamp = objectMapper.readValue(sampleWordTimeList, WordTimestamp.class);
        Song song = Song.builder()
                .name("Die with a smile")
                .audioUrl("https://res.cloudinary.com/golinguage/video/upload/v1743393600/f5de0afa-8502-4952-9c2f-c2dc4604e31f.ogg")
                .englishLyric(sampleEnglishLyric)
                .vietnameseLyric(sampleVietnameseLyric)
                .displayOrder(1)
                .wordTimeStamp(wordTimestamp)
                .build();
        Song savedSong = songRepository.save(song);
        objectMapper.registerModule(new JavaTimeModule());
        log.info("{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(savedSong));
    }

    private final String sampleEnglishLyric = """
            I, I just woke up from a dream
            Where you and I had to say goodbye
            And I don't know what it all means
            But since I survived, I realized
            Wherever you go, that's where I'll follow
            Nobody's promised tomorrow
            So I'ma love you every night like it's the last night
            Like it's the last night
            If the world was ending
            I'd wanna be next to you
            If the party was over
            And our time on Earth was through
            I'd wanna hold you just for a while
            And die with a smile
            If the world was ending
            I'd wanna be next to you
            """;

    private final String sampleVietnameseLyric = """
            Tôi, tôi vừa tỉnh dậy từ một giấc mơ
            Nơi bạn và tôi phải nói lời tạm biệt
            Và tôi không biết tất cả điều đó có nghĩa là gì
            Nhưng vì tôi đã sống sót, tôi nhận ra
            Bất cứ nơi nào bạn đi, đó là nơi tôi sẽ theo
            Không ai hứa hẹn ngày mai
            Vì vậy, tôi sẽ yêu bạn mỗi đêm như đêm cuối cùng
            Như đêm cuối cùng
            Nếu thế giới đang kết thúc
            Tôi muốn ở bên cạnh bạn
            Nếu bữa tiệc đã kết thúc
            Và thời gian của chúng ta trên Trái đất đã hết
            Tôi muốn ôm bạn chỉ một lúc
            Và chết với một nụ cười
            Nếu thế giới đang kết thúc
            Tôi muốn ở bên cạnh bạn
            """;


    private final String sampleWordTimeList = """
            {
              "text": "I, I just woke up from a dream Where you and I had to say goodbye And I don't know what it all means But since I survived, I realized  Wherever you go, that's where I'll follow Nobody's promised tomorrow So I'ma love you every every night like it's the last night Like it's the last night  If the world was ending I'd wanna be next to you If the party was over And our time on Earth was through I'd wanna hold you just for a while And die with a smile If the world was ending I'd wanna be next to you",
              "words": [
                {
                  "word": "I,",
                  "start": 2.02,
                  "end": 2.02
                },
                {
                  "word": "I",
                  "start": 3.65,
                  "end": 3.8
                },
                {
                  "word": "just",
                  "start": 3.8,
                  "end": 4.14
                },
                {
                  "word": "woke",
                  "start": 4.14,
                  "end": 4.5
                },
                {
                  "word": "up",
                  "start": 4.5,
                  "end": 4.74
                },
                {
                  "word": "from",
                  "start": 4.74,
                  "end": 5.0
                },
                {
                  "word": "a",
                  "start": 5.0,
                  "end": 5.28
                },
                {
                  "word": "dream",
                  "start": 5.28,
                  "end": 5.98
                },
                {
                  "word": "Where",
                  "start": 6.59,
                  "end": 6.74
                },
                {
                  "word": "you",
                  "start": 6.74,
                  "end": 7.28
                },
                {
                  "word": "and",
                  "start": 7.28,
                  "end": 7.94
                },
                {
                  "word": "I",
                  "start": 7.94,
                  "end": 8.3
                },
                {
                  "word": "had",
                  "start": 8.3,
                  "end": 8.6
                },
                {
                  "word": "to",
                  "start": 8.6,
                  "end": 8.9
                },
                {
                  "word": "say",
                  "start": 8.9,
                  "end": 9.1
                },
                {
                  "word": "goodbye",
                  "start": 9.312,
                  "end": 9.9
                },
                {
                  "word": "And",
                  "start": 9.9,
                  "end": 12.0
                },
                {
                  "word": "I",
                  "start": 12.0,
                  "end": 13.64
                },
                {
                  "word": "don't",
                  "start": 13.64,
                  "end": 14.68
                },
                {
                  "word": "know",
                  "start": 14.68,
                  "end": 15.0
                },
                {
                  "word": "what",
                  "start": 15.0,
                  "end": 15.32
                },
                {
                  "word": "it",
                  "start": 15.32,
                  "end": 15.54
                },
                {
                  "word": "all",
                  "start": 15.54,
                  "end": 15.82
                },
                {
                  "word": "means",
                  "start": 15.82,
                  "end": 16.4
                },
                {
                  "word": "But",
                  "start": 16.4,
                  "end": 16.9
                },
                {
                  "word": "since",
                  "start": 16.9,
                  "end": 17.46
                },
                {
                  "word": "I",
                  "start": 17.46,
                  "end": 18.0
                },
                {
                  "word": "survived,",
                  "start": 18.0,
                  "end": 19.2
                },
                {
                  "word": "I",
                  "start": 19.24,
                  "end": 19.74
                },
                {
                  "word": "realized",
                  "start": 19.74,
                  "end": 20.9
                },
                {
                  "word": "Wherever",
                  "start": 20.9,
                  "end": 22.28
                },
                {
                  "word": "you",
                  "start": 22.28,
                  "end": 22.96
                },
                {
                  "word": "go,",
                  "start": 22.96,
                  "end": 23.34
                },
                {
                  "word": "that's",
                  "start": 23.34,
                  "end": 23.7
                },
                {
                  "word": "where",
                  "start": 23.7,
                  "end": 24.2
                },
                {
                  "word": "I'll",
                  "start": 24.2,
                  "end": 24.68
                },
                {
                  "word": "follow",
                  "start": 24.68,
                  "end": 25.18
                },
                {
                  "word": "Nobody's",
                  "start": 25.18,
                  "end": 26.4
                },
                {
                  "word": "promised",
                  "start": 26.4,
                  "end": 27.04
                },
                {
                  "word": "tomorrow",
                  "start": 27.04,
                  "end": 27.84
                },
                {
                  "word": "So",
                  "start": 27.84,
                  "end": 28.48
                },
                {
                  "word": "I'ma",
                  "start": 28.48,
                  "end": 28.9
                },
                {
                  "word": "love",
                  "start": 28.9,
                  "end": 29.42
                },
                {
                  "word": "you",
                  "start": 29.42,
                  "end": 29.86
                },
                {
                  "word": "every",
                  "start": 29.86,
                  "end": 29.98
                },
                {
                  "word": "every",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "night",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "like",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "it's",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "the",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "last",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "night",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "Like",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "it's",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "the",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "last",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "night",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "If",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "the",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "world",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "was",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "ending",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "I'd",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "wanna",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "be",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "next",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "to",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "you",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "If",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "the",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "party",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "was",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "over",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "And",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "our",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "time",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "on",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "Earth",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "was",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "through",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "I'd",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "wanna",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "hold",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "you",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "just",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "for",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "a",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "while",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "And",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "die",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "with",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "a",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "smile",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "If",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "the",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "world",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "was",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "ending",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "I'd",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "wanna",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "be",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "next",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "to",
                  "start": 60.325,
                  "end": 60.325
                },
                {
                  "word": "you",
                  "start": 60.325,
                  "end": 60.325
                }
              ]
            }
            """;
}