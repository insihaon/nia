# pyannote.audio 3.1
# pyannote/segmentation-3.0  (Access grant required)
# pyannote/speaker-diarization-3.1  (Access grant required)
# HuggingFace access token required

# !pip install torch torchvision torchaudio
# !pip install whisperx

import whisperx
import gc
import torch

# device = "cuda"
device = "cuda" if torch.cuda.is_available() else "cpu"
audio_file = "audio.wav"
batch_size = 16 # reduce if low on GPU mem
# compute_type = "float16"  # change to "int8" if low on GPU mem
compute_type = "float16" if torch.cuda.is_available() else "int8"

# 1. Transcribe with original whisper (batched)
model = whisperx.load_model("large-v2", device, compute_type = compute_type)

# save model to local path (optional)
# model_dir = "/path/"
# model = whisperx.load_model("large-v2", device, compute_type = compute_type, download_root=model_dir)

audio = whisperx.load_audio(audio_file)
result = model.transcribe(audio, batch_size=batch_size)
print(result["segments"])  # before alignment

# delete model if low on GPU resources
import torch
gc.collect(); torch.cuda.empty_cache(); del model

# 2. Align whisper output
model_a, metadata = whisperx.load_align_model(language_code=result["language"], device=device)
result = whisperx.align(result["segments"], model_a, metadata, audio, device, return_char_alignments=False)
print(result["segments"])  # after alignment

# delete model if low on GPU resources
gc.collect(); torch.cuda.empty_cache(); del model_a

# 3. Assign speaker labels
diarize_model = whisperx.DiarizationPipeline(use_auth_token="", device=device)

# add min/max number of speakers if known
diarize_segments = diarize_model(audio, min_speakers=2, max_speakers=2)

result = whisperx.assign_word_speakers(diarize_segments, result)
print(diarize_segments)
print(result["segments"])  # segments are now assigned speaker IDs

f = open("trans_file.txt", "a")
result1 = result["segments"]
c_speaker = result1[0]["words"][0]["speaker"]
data = "<" + c_speaker + ">" + "\n"
for i in range(len(result1)):
    for j in range(len(result1[i]["words"])):
        if result1[i]["words"][j].get("speaker") is not None:
            if result1[i]["words"][j]["speaker"] == c_speaker:
                data = data + " " + result1[i]["words"][j]["word"]
            else:
                f.write(data)
                c_speaker =  result1[i]["words"][j]["speaker"]
                data = "\n" + "<" + c_speaker + ">" + "\n" + " " + result1[i]["words"][j]["word"]
        elif result1[i]["words"][j].get("word") is not None:
            data = data + " " + result1[i]["words"][j]["word"]
f.write(data)
f.close()





