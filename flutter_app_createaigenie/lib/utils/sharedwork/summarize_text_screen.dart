import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;

class SummarizeTextScreen extends StatefulWidget {
  @override
  _SummarizeTextScreenState createState() => _SummarizeTextScreenState();
}

class _SummarizeTextScreenState extends State<SummarizeTextScreen> {
  final TextEditingController _textController = TextEditingController();
  String? _summary;
  bool _loading = false;
  final ApiService _apiService = ApiService();

  Future<void> _summarizeText() async {
    setState(() {
      _loading = true;
      _summary = null;
    });

    try {
      final summary = await _apiService.summarizeText(_textController.text);
      if (summary != null) {
        setState(() {
          _summary = summary;
        });
      } else {
        throw Exception('Failed to summarize text');
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(e.toString())),
      );
    } finally {
      setState(() {
        _loading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Summarize Text'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _textController,
              decoration: const InputDecoration(labelText: 'Enter text to summarize'),
              maxLines: 5,
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: _summarizeText,
              child: _loading ? const CircularProgressIndicator() : const Text('Summarize'),
            ),
            const SizedBox(height: 20),
            _summary != null
                ? Text(
              _summary!,
              style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            )
                : Container(),
          ],
        ),
      ),
    );
  }
}

class ApiService {
  final String _baseUrl = 'http://48.216.211.10:8000';

  // Fetch access token from SharedPreferences
  Future<String?> _getAccessToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('accessToken');
  }

  // Function to summarize text using the stored token
  Future<String?> summarizeText(String text) async {
    try {
      final token = await _getAccessToken();

      if (token == null) {
        throw Exception('No access token found');
      }

      final response = await http.post(
        Uri.parse('$_baseUrl/summarize-text/'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode({'text': text}),
      );

      print('Summarize Response body: ${response.body}');

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return data['summary'];
      } else {
        print('Failed to summarize text: ${response.statusCode} - ${response.body}');
        return null;
      }
    } catch (e) {
      print('Error summarizing text: $e');
      return null;
    }
  }
}
