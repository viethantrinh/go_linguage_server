import argparse
import json
import os
import tempfile
import uuid
from flask import Flask, request, jsonify
import stable_whisper

app = Flask(__name__)


def force_align(audio_path, lyrics_text, output_path=None):
    """
    Force align audio with lyrics text using stable-ts.

    Parameters:
    -----------
    audio_path : str
        Path to the audio file
    lyrics_text : str
        Lyrics text content
    output_path : str, optional
        Path to save the output JSON file. If not provided, will use the audio filename with .json extension

    Returns:
    --------
    dict
        Dictionary containing the word-level timestamps
    """
    print(f"Loading model...")
    model = stable_whisper.load_model('medium')

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

    # Save the JSON output if output_path is provided
    if output_path:
        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump({"words": word_timestamps}, f, indent=2)
        print(f"Alignment complete. Results saved to: {output_path}")

    # Return the word timestamps
    return {"words": word_timestamps}


@app.route('/align', methods=['POST'])
def align_endpoint():
    """
    API endpoint for force alignment.

    Expected input (multipart/form-data):
    - audio_file: Audio file to align
    - lyrics: Lyrics text content

    Returns:
    --------
    JSON with word-level timestamps
    """
    try:
        # Check if required files are provided
        if 'audio_file' not in request.files or 'lyrics' not in request.form:
            return jsonify({"error": "Missing audio_file or lyrics"}), 400

        audio_file = request.files['audio_file']
        lyrics_text = request.form['lyrics']

        # Create a temporary directory for processing
        temp_dir = tempfile.mkdtemp()

        # Save the audio file
        audio_filename = f"{uuid.uuid4()}{os.path.splitext(audio_file.filename)[1]}"
        audio_path = os.path.join(temp_dir, audio_filename)
        audio_file.save(audio_path)

        try:
            # Process the alignment
            result = force_align(audio_path, lyrics_text)

            if result is None:
                return jsonify({"error": "Alignment failed"}), 500

            return jsonify(result), 200

        finally:
            # Clean up temporary files
            try:
                os.remove(audio_path)
                os.rmdir(temp_dir)
            except Exception as e:
                print(f"Error cleaning up temporary files: {e}")

    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Run the force alignment API server")
    parser.add_argument("--host", default="127.0.0.1", help="Host to run the server on")
    parser.add_argument("--port", type=int, default=5000, help="Port to run the server on")

    args = parser.parse_args()

    print(f"Starting Force Alignment API server on {args.host}:{args.port}")
    app.run(host=args.host, port=args.port, debug=False)