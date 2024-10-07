import 'dart:convert'; // For JSON encoding and decoding
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http; // For HTTP requests
import '../../beint/api_service.dart'; // Ensure you have the correct path

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
      final summary = await summarizeText(token, _textController.text);
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

  Future<String?> summarizeText(String token, String text) async {
    const String baseUrl = 'http://192.168.1.12:8000'; // Update with your actual API URL
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/summarize/'), // Update this endpoint as needed
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode({'text': text}), // Ensure your API expects this structure
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return data['summary']; // Adjust based on your API's response structure
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Failed to summarize text')),
        );
      }
    } catch (e) {
      print('Error during summary request: $e');
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('An error occurred')),
      );
    }
    return null;
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
