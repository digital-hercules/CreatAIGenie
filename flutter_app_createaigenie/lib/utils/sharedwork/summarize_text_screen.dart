import 'package:flutter/material.dart';
import '../../beint/api_service.dart';

class SummarizeTextScreen extends StatefulWidget {
  const SummarizeTextScreen({super.key});

  @override
  _SummarizeTextScreenState createState() => _SummarizeTextScreenState();
}

class _SummarizeTextScreenState extends State<SummarizeTextScreen> {
  final _textController = TextEditingController();
  String? _summary;
  final ApiService _apiService = ApiService();
  bool _loading = false;

  Future<void> _summarizeText() async {
    setState(() {
      _loading = true;
      _summary = null;
    });

    final token = await _apiService.getToken();
    if (token != null) {
      final summary = await _apiService.summarizeText(token, _textController.text);
      setState(() {
        _summary = summary;
      });
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Failed to get token')),
      );
    }

    setState(() {
      _loading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Summarize Text'),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            const Text(
              'Enter the text you want to summarize:',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),
            TextField(
              controller: _textController,
              decoration: InputDecoration(
                hintText: 'Enter your text here...',
                filled: true,
                fillColor: Colors.grey[200],
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
              ),
              maxLines: 5,
              textInputAction: TextInputAction.newline,
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: _loading ? null : _summarizeText,
              style: ElevatedButton.styleFrom(
                minimumSize: const Size(double.infinity, 50),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                backgroundColor: Colors.deepPurpleAccent, // Use backgroundColor instead of primary
              ),
              child: _loading
                  ? const SizedBox(
                      height: 20,
                      width: 20,
                      child: CircularProgressIndicator(
                        strokeWidth: 2,
                        valueColor: AlwaysStoppedAnimation<Color>(Colors.black),
                      ),
                    )
                  : const Text('Summarize'),
            ),
            const SizedBox(height: 20),
            if (_summary != null) ...[
              Container(
                padding: const EdgeInsets.all(16.0),
                decoration: BoxDecoration(
                  color: Colors.grey[100],
                  borderRadius: BorderRadius.circular(10),
                  boxShadow: const [
                    BoxShadow(
                      color: Colors.black26,
                      offset: Offset(0, 2),
                      blurRadius: 6,
                    ),
                  ],
                ),
                child: Text(
                  'Summary: $_summary',
                  style: const TextStyle(fontSize: 16, fontStyle: FontStyle.italic),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
