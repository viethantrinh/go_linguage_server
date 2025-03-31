package tech.trvihnls.commons.domains;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_song")
public class Song extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "english_lyric", length = 5000)
    private String englishLyric;

    @Column(name = "vietnamese_lyric", length = 5000)
    private String vietnameseLyric;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "word_timestamp", columnDefinition = "jsonb")
    private WordTimestamp wordTimeStamp;

    @Column(name = "display_order")
    private int displayOrder;
}
