package tech.trvihnls.commons.bootstraps;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        WordTimestamp wordTimestamp1 = objectMapper.readValue(sampleWordTimeList1, WordTimestamp.class);
        Song song1 = Song.builder()
                .name("A day in a park")
                .audioUrl("https://res.cloudinary.com/golinguage/video/upload/v1743732018/57f8a42e-d9d9-49e8-804c-f5bd3108bd4b.ogg")
                .englishLyric(sampleEnglishLyric1)
                .vietnameseLyric(sampleVietnameseLyric1)
                .displayOrder(1)
                .wordTimeStamp(wordTimestamp1)
                .build();
        songRepository.save(song1);

        WordTimestamp wordTimestamp2 = objectMapper.readValue(sampleWordTimeList2, WordTimestamp.class);
        Song song2 = Song.builder()
                .name("Die with a smile")
                .audioUrl("https://res.cloudinary.com/golinguage/video/upload/v1743393600/f5de0afa-8502-4952-9c2f-c2dc4604e31f.ogg")
                .englishLyric(sampleEnglishLyric2)
                .vietnameseLyric(sampleVietnameseLyric2)
                .displayOrder(2)
                .wordTimeStamp(wordTimestamp2)
                .build();
        Song savedSong = songRepository.save(song2);
    }

    private final String sampleEnglishLyric1 = """
            In the park, a woman walks,
            With a smile, she softly talks.
            A man sits under a tree,
            Reading a book, calm and free.
            A girl and a boy play in the sun,
            Laughing together, having fun.
            They chase each other, run and hide,
            Joy and laughter, side by side.
            The sun is bright, the sky is blue,
            A perfect day for me and you.
            The woman waves, the man stands tall,
            Together they enjoy it all.
            A girl and a boy play in the sun,
            Laughing together, having fun.
            They chase each other, run and hide,
            Joy and laughter, side by side.
            As the day ends, they wave goodbye,
            Under the stars, in the night sky.
            A woman, a man, a girl, a boy,
            In the park, they found their joy.
            """;

    private final String sampleVietnameseLyric1 = """
            Trong công viên, một người phụ nữ đi,
            Với nụ cười, cô nhẹ nhàng nói.
            Một người đàn ông ngồi dưới gốc cây,
            Đọc sách, thư thái tự do.
            Một cô gái và một cậu bé chơi dưới nắng,
            Cười đùa cùng nhau, thật vui vẻ.
            Họ đuổi nhau, chạy và trốn,
            Niềm vui và tiếng cười, bên nhau.
            Mặt trời sáng, bầu trời xanh,
            Một ngày hoàn hảo cho tôi và bạn.
            Người phụ nữ vẫy tay, người đàn ông đứng thẳng,
            Cùng nhau, họ tận hưởng tất cả.
            Một cô gái và một cậu bé chơi dưới nắng,
            Cười đùa cùng nhau, thật vui vẻ.
            Họ đuổi nhau, chạy và trốn,
            Niềm vui và tiếng cười, bên nhau.
            Khi ngày tàn, họ vẫy tay chào,
            Dưới những ngôi sao, trong bầu trời đêm.
            Một người phụ nữ, một người đàn ông, một cô gái, một cậu bé,
            Trong công viên, họ tìm thấy niềm vui.
            """;

    private final String sampleEnglishLyric2 = """
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

    private final String sampleVietnameseLyric2 = """
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

    private final String sampleWordTimeList1 = """
            {
              "text": "In the park, a woman walks, With a smile, she softly talks. A man sits under a tree, Reading a book, calm and free.  A girl and a boy play in the sun, Laughing together, having fun. They chase each other, run and hide, Joy and laughter, side by side.  The sun is bright, the sky is blue, A perfect day for me and you. The woman waves, the man stands tall, Together they enjoy it all.  A girl and a boy play in the sun, Laughing together, having fun. They chase each other, run and hide, Joy and laughter, side by side.  As the day ends, they wave goodbye, Under the stars, in the night sky. A woman, a man, a girl, a boy, In the park, they found their joy.",
              "words": [
                {
                  "word": "In",
                  "start": 9.96,
                  "end": 9.96
                },
                {
                  "word": "the",
                  "start": 9.96,
                  "end": 10.34
                },
                {
                  "word": "park,",
                  "start": 10.34,
                  "end": 10.74
                },
                {
                  "word": "a",
                  "start": 10.94,
                  "end": 10.94
                },
                {
                  "word": "woman",
                  "start": 10.94,
                  "end": 11.24
                },
                {
                  "word": "walks,",
                  "start": 11.24,
                  "end": 11.99
                },
                {
                  "word": "With",
                  "start": 14.22,
                  "end": 14.54
                },
                {
                  "word": "a",
                  "start": 14.54,
                  "end": 14.8
                },
                {
                  "word": "smile,",
                  "start": 14.8,
                  "end": 15.22
                },
                {
                  "word": "she",
                  "start": 15.24,
                  "end": 15.4
                },
                {
                  "word": "softly",
                  "start": 15.4,
                  "end": 16.08
                },
                {
                  "word": "talks.",
                  "start": 16.08,
                  "end": 17.78
                },
                {
                  "word": "A",
                  "start": 18.5,
                  "end": 18.72
                },
                {
                  "word": "man",
                  "start": 18.72,
                  "end": 19.1
                },
                {
                  "word": "sits",
                  "start": 19.1,
                  "end": 19.42
                },
                {
                  "word": "under",
                  "start": 19.42,
                  "end": 20.28
                },
                {
                  "word": "a",
                  "start": 20.28,
                  "end": 20.46
                },
                {
                  "word": "tree,",
                  "start": 20.46,
                  "end": 21.41
                },
                {
                  "word": "Reading",
                  "start": 22.0,
                  "end": 22.46
                },
                {
                  "word": "a",
                  "start": 22.46,
                  "end": 22.8
                },
                {
                  "word": "book,",
                  "start": 22.8,
                  "end": 23.7
                },
                {
                  "word": "calm",
                  "start": 24.06,
                  "end": 24.52
                },
                {
                  "word": "and",
                  "start": 24.52,
                  "end": 25.26
                },
                {
                  "word": "free.",
                  "start": 25.26,
                  "end": 26.54
                },
                {
                  "word": "A",
                  "start": 26.54,
                  "end": 26.8
                },
                {
                  "word": "girl",
                  "start": 26.8,
                  "end": 27.06
                },
                {
                  "word": "and",
                  "start": 27.06,
                  "end": 27.28
                },
                {
                  "word": "a",
                  "start": 27.28,
                  "end": 27.56
                },
                {
                  "word": "boy",
                  "start": 27.56,
                  "end": 27.92
                },
                {
                  "word": "play",
                  "start": 27.92,
                  "end": 28.58
                },
                {
                  "word": "in",
                  "start": 28.58,
                  "end": 29.04
                },
                {
                  "word": "the",
                  "start": 29.04,
                  "end": 29.54
                },
                {
                  "word": "sun,",
                  "start": 29.54,
                  "end": 30.44
                },
                {
                  "word": "Laughing",
                  "start": 31.12,
                  "end": 31.6
                },
                {
                  "word": "together,",
                  "start": 31.6,
                  "end": 32.84
                },
                {
                  "word": "having",
                  "start": 33.14,
                  "end": 33.56
                },
                {
                  "word": "fun.",
                  "start": 33.56,
                  "end": 35.6
                },
                {
                  "word": "They",
                  "start": 35.62,
                  "end": 36.0
                },
                {
                  "word": "chase",
                  "start": 36.0,
                  "end": 36.34
                },
                {
                  "word": "each",
                  "start": 36.34,
                  "end": 36.82
                },
                {
                  "word": "other,",
                  "start": 36.82,
                  "end": 37.86
                },
                {
                  "word": "run",
                  "start": 37.92,
                  "end": 38.2
                },
                {
                  "word": "and",
                  "start": 38.2,
                  "end": 38.76
                },
                {
                  "word": "hide,",
                  "start": 38.76,
                  "end": 39.12
                },
                {
                  "word": "Joy",
                  "start": 40.16,
                  "end": 40.86
                },
                {
                  "word": "and",
                  "start": 40.86,
                  "end": 41.18
                },
                {
                  "word": "laughter,",
                  "start": 41.18,
                  "end": 42.36
                },
                {
                  "word": "side",
                  "start": 42.82,
                  "end": 42.82
                },
                {
                  "word": "by",
                  "start": 42.84,
                  "end": 43.38
                },
                {
                  "word": "side.",
                  "start": 43.38,
                  "end": 45.13
                },
                {
                  "word": "The",
                  "start": 51.9,
                  "end": 51.9
                },
                {
                  "word": "sun",
                  "start": 51.9,
                  "end": 52.2
                },
                {
                  "word": "is",
                  "start": 52.2,
                  "end": 52.44
                },
                {
                  "word": "bright,",
                  "start": 52.44,
                  "end": 53.19
                },
                {
                  "word": "the",
                  "start": 53.7,
                  "end": 54.14
                },
                {
                  "word": "sky",
                  "start": 54.14,
                  "end": 54.58
                },
                {
                  "word": "is",
                  "start": 54.58,
                  "end": 54.98
                },
                {
                  "word": "blue,",
                  "start": 54.98,
                  "end": 56.08
                },
                {
                  "word": "A",
                  "start": 56.26,
                  "end": 56.54
                },
                {
                  "word": "perfect",
                  "start": 56.54,
                  "end": 57.0
                },
                {
                  "word": "day",
                  "start": 57.0,
                  "end": 57.68
                },
                {
                  "word": "for",
                  "start": 57.68,
                  "end": 58.28
                },
                {
                  "word": "me",
                  "start": 58.28,
                  "end": 58.92
                },
                {
                  "word": "and",
                  "start": 58.92,
                  "end": 59.4
                },
                {
                  "word": "you.",
                  "start": 59.4,
                  "end": 60.74
                },
                {
                  "word": "The",
                  "start": 60.74,
                  "end": 61.06
                },
                {
                  "word": "woman",
                  "start": 61.06,
                  "end": 61.58
                },
                {
                  "word": "waves,",
                  "start": 61.58,
                  "end": 62.56
                },
                {
                  "word": "the",
                  "start": 62.64,
                  "end": 62.78
                },
                {
                  "word": "man",
                  "start": 62.78,
                  "end": 63.34
                },
                {
                  "word": "stands",
                  "start": 63.36,
                  "end": 63.94
                },
                {
                  "word": "tall,",
                  "start": 63.94,
                  "end": 65.3
                },
                {
                  "word": "Together",
                  "start": 65.46,
                  "end": 66.06
                },
                {
                  "word": "they",
                  "start": 66.06,
                  "end": 67.0
                },
                {
                  "word": "enjoy",
                  "start": 67.0,
                  "end": 67.94
                },
                {
                  "word": "it",
                  "start": 67.94,
                  "end": 68.48
                },
                {
                  "word": "all.",
                  "start": 68.48,
                  "end": 69.96
                },
                {
                  "word": "A",
                  "start": 69.96,
                  "end": 70.2
                },
                {
                  "word": "girl",
                  "start": 70.2,
                  "end": 70.48
                },
                {
                  "word": "and",
                  "start": 70.48,
                  "end": 70.7
                },
                {
                  "word": "a",
                  "start": 70.7,
                  "end": 71.02
                },
                {
                  "word": "boy",
                  "start": 71.02,
                  "end": 71.4
                },
                {
                  "word": "play",
                  "start": 71.4,
                  "end": 72.02
                },
                {
                  "word": "in",
                  "start": 72.02,
                  "end": 72.48
                },
                {
                  "word": "the",
                  "start": 72.48,
                  "end": 73.0
                },
                {
                  "word": "sun,",
                  "start": 73.0,
                  "end": 73.95
                },
                {
                  "word": "Laughing",
                  "start": 74.52,
                  "end": 75.02
                },
                {
                  "word": "together,",
                  "start": 75.02,
                  "end": 76.38
                },
                {
                  "word": "having",
                  "start": 76.64,
                  "end": 77.02
                },
                {
                  "word": "fun.",
                  "start": 77.02,
                  "end": 78.78
                },
                {
                  "word": "They",
                  "start": 79.0,
                  "end": 79.42
                },
                {
                  "word": "chase",
                  "start": 79.42,
                  "end": 79.78
                },
                {
                  "word": "each",
                  "start": 80.12,
                  "end": 80.12
                },
                {
                  "word": "other,",
                  "start": 80.12,
                  "end": 81.24
                },
                {
                  "word": "run",
                  "start": 81.62,
                  "end": 81.62
                },
                {
                  "word": "and",
                  "start": 81.62,
                  "end": 82.18
                },
                {
                  "word": "hide,",
                  "start": 82.18,
                  "end": 82.84
                },
                {
                  "word": "Joy",
                  "start": 84.08,
                  "end": 84.32
                },
                {
                  "word": "and",
                  "start": 84.32,
                  "end": 84.6
                },
                {
                  "word": "laughter,",
                  "start": 84.6,
                  "end": 85.76
                },
                {
                  "word": "side",
                  "start": 86.1,
                  "end": 86.32
                },
                {
                  "word": "by",
                  "start": 86.7,
                  "end": 86.8
                },
                {
                  "word": "side.",
                  "start": 95.46,
                  "end": 95.46
                },
                {
                  "word": "As",
                  "start": 95.46,
                  "end": 95.46
                },
                {
                  "word": "the",
                  "start": 95.46,
                  "end": 95.64
                },
                {
                  "word": "day",
                  "start": 95.64,
                  "end": 95.94
                },
                {
                  "word": "ends,",
                  "start": 95.94,
                  "end": 96.69
                },
                {
                  "word": "they",
                  "start": 97.2,
                  "end": 97.2
                },
                {
                  "word": "wave",
                  "start": 97.2,
                  "end": 97.68
                },
                {
                  "word": "goodbye,",
                  "start": 97.68,
                  "end": 98.88
                },
                {
                  "word": "Under",
                  "start": 99.66,
                  "end": 100.16
                },
                {
                  "word": "the",
                  "start": 100.16,
                  "end": 100.54
                },
                {
                  "word": "stars,",
                  "start": 100.54,
                  "end": 101.12
                },
                {
                  "word": "in",
                  "start": 101.12,
                  "end": 101.56
                },
                {
                  "word": "the",
                  "start": 101.56,
                  "end": 101.86
                },
                {
                  "word": "night",
                  "start": 101.86,
                  "end": 102.24
                },
                {
                  "word": "sky.",
                  "start": 102.24,
                  "end": 103.34
                },
                {
                  "word": "A",
                  "start": 105.08,
                  "end": 105.12
                },
                {
                  "word": "woman,",
                  "start": 105.12,
                  "end": 105.96
                },
                {
                  "word": "a",
                  "start": 105.96,
                  "end": 106.1
                },
                {
                  "word": "man,",
                  "start": 106.1,
                  "end": 107.18
                },
                {
                  "word": "a",
                  "start": 107.18,
                  "end": 107.32
                },
                {
                  "word": "girl,",
                  "start": 107.32,
                  "end": 108.34
                },
                {
                  "word": "a",
                  "start": 108.34,
                  "end": 108.5
                },
                {
                  "word": "boy,",
                  "start": 108.5,
                  "end": 109.1
                },
                {
                  "word": "In",
                  "start": 109.1,
                  "end": 109.46
                },
                {
                  "word": "the",
                  "start": 109.46,
                  "end": 109.72
                },
                {
                  "word": "park,",
                  "start": 109.72,
                  "end": 110.1
                },
                {
                  "word": "they",
                  "start": 110.56,
                  "end": 110.88
                },
                {
                  "word": "found",
                  "start": 110.88,
                  "end": 111.38
                },
                {
                  "word": "their",
                  "start": 111.46,
                  "end": 112.04
                },
                {
                  "word": "joy.",
                  "start": 112.04,
                  "end": 113.19
                }
              ]
            }
            """;

    private final String sampleWordTimeList2 = """
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
