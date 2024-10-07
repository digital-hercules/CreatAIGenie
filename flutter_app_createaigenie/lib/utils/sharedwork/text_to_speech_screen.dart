import 'package:flutter/material.dart';
import 'package:flutter_tts/flutter_tts.dart';
import 'package:path_provider/path_provider.dart';

class TextToSpeechScreen extends StatefulWidget {
  const TextToSpeechScreen({super.key});

  @override
  _TextToSpeechScreenState createState() => _TextToSpeechScreenState();
}

class _TextToSpeechScreenState extends State<TextToSpeechScreen> {
  final FlutterTts flutterTts = FlutterTts();
  final TextEditingController textEditingController = TextEditingController();

  double volume = 0.5;
  double pitch = 1.0;
  double rate = 0.5;
  String? language;
  String? savedFilePath;

  @override
  void initState() {
    super.initState();
    _initTts();
  }

  void _initTts() {
    flutterTts.setStartHandler(() {
      setState(() {
        print("TTS: Playing");
      });
    });

    flutterTts.setCompletionHandler(() {
      setState(() {
        print("TTS: Completed");
      });
    });

    flutterTts.setErrorHandler((message) {
      setState(() {
        print("TTS Error: $message");
      });
    });
  }

  Future<void> _speak() async {
    await flutterTts.setVolume(volume);
    await flutterTts.setPitch(pitch);
    await flutterTts.setSpeechRate(rate);
    if (language != null) await flutterTts.setLanguage(language!);

    if (textEditingController.text.isNotEmpty) {
      await flutterTts.speak(textEditingController.text);
    }
  }

  Future<void> _stop() async {
    await flutterTts.stop();
  }

  Future<void> _saveAudio() async {
    if (textEditingController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter text to save as audio')),
      );
      return;
    }

    final directory = await getApplicationDocumentsDirectory();
    final fileName = 'tts_audio_${DateTime.now().millisecondsSinceEpoch}.wav';
    final filePath = '${directory.path}/$fileName';

    await flutterTts.setVolume(volume);
    await flutterTts.setPitch(pitch);
    await flutterTts.setSpeechRate(rate);
    if (language != null) await flutterTts.setLanguage(language!);

    var result = await flutterTts.synthesizeToFile(textEditingController.text, filePath);

    if (result == 1) {
      setState(() {
        savedFilePath = filePath;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Audio saved successfully')),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Failed to save audio')),
      );
    }
  }

  @override
  void dispose() {
    flutterTts.stop();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Text to Speech'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TextField(
              controller: textEditingController,
              maxLines: 5,
              decoration: const InputDecoration(
                hintText: 'Enter text to convert to speech',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                ElevatedButton(
                  onPressed: _speak,
                  child: const Text('Speak'),
                ),
                ElevatedButton(
                  onPressed: _stop,
                  child: const Text('Stop'),
                ),
                ElevatedButton(
                  onPressed: _saveAudio,
                  child: const Text('Save Audio'),
                ),
              ],
            ),
            if (savedFilePath != null)
              Padding(
                padding: const EdgeInsets.only(top: 20),
                child: Text('Audio saved at: $savedFilePath'),
              ),
            const SizedBox(height: 20),
            _buildSlider("Volume", volume, (value) => setState(() => volume = value)),
            _buildSlider("Pitch", pitch, (value) => setState(() => pitch = value), min: 0.5, max: 2.0),
            _buildSlider("Rate", rate, (value) => setState(() => rate = value)),
            const SizedBox(height: 20),
            FutureBuilder<dynamic>(
              future: flutterTts.getLanguages,
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  return DropdownButton<String>(
                    value: language,
                    hint: const Text("Select Language"),
                    items: (snapshot.data as List<dynamic>).map((lang) {
                      return DropdownMenuItem(
                        value: lang as String,
                        child: Text(lang),
                      );
                    }).toList(),
                    onChanged: (String? value) {
                      setState(() {
                        language = value;
                      });
                    },
                  );
                }
                return const CircularProgressIndicator();
              },
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSlider(String label, double value, ValueChanged<double> onChanged, {double min = 0.0, double max = 1.0}) {
    return Column(
      children: [
        Text(label),
        Slider(
          value: value,
          onChanged: onChanged,
          min: min,
          max: max,
          divisions: 10,
          label: value.toStringAsFixed(2),
        ),
      ],
    );
  }
}