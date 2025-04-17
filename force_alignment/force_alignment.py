import argparse
import json
import os
import stable_whisper


def force_align(audio_path, lyrics_path, output_path=None):
    """
    Force align audio with lyrics text file using stable-ts.

    Parameters:
    -----------
    audio_path : str
        Path to the audio file
    lyrics_path : str
        Path to the lyrics text file
    output_path : str, optional
        Path to save the output JSON file. If not provided, will use the audio filename with .json extension

    Returns:
    --------
    str
        Path to the output JSON file
    """
    print(f"Loading model...")
    model = stable_whisper.load_model('medium')

    # Read lyrics from file
    with open(lyrics_path, 'r', encoding='utf-8') as f:
        lyrics_text = f.read().strip()

    print(f"Aligning audio with lyrics...")
    # Use stable-ts align function to perform force alignment
    result = model.align(
        audio=audio_path,
        text=lyrics_text,
        language='en'
    )

    # If alignment failed
    if result is None:
        print("Alignment failed. Please check your audio and lyrics files.")
        return None

    # Create output JSON with word-level timestamps
    word_timestamps = []

    # Process each segment in the result
    for segment in result.segments:
        # Process each word in the segment
        if hasattr(segment, 'words') and segment.words:
            for word in segment.words:
                word_data = {
                    "word": word.word.strip(),
                    "start": word.start,
                    "end": word.end,
                }
                word_timestamps.append(word_data)

    # Determine output path if not provided
    if output_path is None:
        base_name = os.path.splitext(audio_path)[0]
        output_path = f"{base_name}_aligned.json"

    # Save the JSON output
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump({"words": word_timestamps}, f, indent=2)

    print(f"Alignment complete. Results saved to: {output_path}")
    return output_path


def main():
    parser = argparse.ArgumentParser(description="Force align audio with lyrics text file using stable-ts")
    parser.add_argument("audio", help="Path to the audio file")
    parser.add_argument("lyrics", help="Path to the lyrics text file")
    parser.add_argument("--output", "-o", help="Path to save the output JSON file")

    args = parser.parse_args()

    force_align(args.audio, args.lyrics, args.output)


if __name__ == "__main__":
    main()