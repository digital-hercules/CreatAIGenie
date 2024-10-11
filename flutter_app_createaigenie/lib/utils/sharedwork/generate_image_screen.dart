import 'package:flutter/material.dart';
import 'package:http/http.dart' as http; // Import http package
import 'dart:convert'; // Import dart:convert for jsonDecode
import 'package:shared_preferences/shared_preferences.dart'; // Import shared_preferences

class GenerateImageScreen extends StatefulWidget {
  const GenerateImageScreen({super.key});

  @override
  _GenerateImageScreenState createState() => _GenerateImageScreenState();
}

class _GenerateImageScreenState extends State<GenerateImageScreen> {
  final _promptController = TextEditingController();
  String? _imageUrl;
  bool _loading = false;
  final ApiService _apiService = ApiService();

  void _generateImage() async {
    if (_promptController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter a prompt')),
      );
      return;
    }

    setState(() {
      _loading = true;
      _imageUrl = null;
    });

    final token = await _apiService.getToken();
    if (token != null) {
      final imageUrl = await _apiService.generateImage(token, _promptController.text);
      setState(() {
        _imageUrl = imageUrl;
        _loading = false;
      });

      if (imageUrl == null) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Failed to generate image')),
        );
      }
    } else {
      setState(() {
        _loading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Failed to get token')),
      );
    }
  }

  void _clear() {
    setState(() {
      _promptController.clear();
      _imageUrl = null;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Generate Image'),
        centerTitle: true,
        backgroundColor: Colors.deepPurple,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Enter a prompt to generate an image:',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),
            TextField(
              controller: _promptController,
              maxLines: 3,
              decoration: InputDecoration(
                labelText: 'Prompt',
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12.0),
                ),
                contentPadding: const EdgeInsets.all(12),
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: _loading ? null : _generateImage,
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.deepPurpleAccent,
                minimumSize: const Size(double.infinity, 50),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12.0),
                ),
              ),
              child: _loading
                  ? const SizedBox(
                height: 20,
                width: 20,
                child: CircularProgressIndicator(
                  color: Colors.black,
                  strokeWidth: 2,
                ),
              )
                  : const Text('Generate Image'),
            ),
            const SizedBox(height: 20),
            if (_imageUrl != null) ...[
              Center(
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(12.0),
                  child: Image.network(
                    _imageUrl!,
                    fit: BoxFit.cover,
                    width: double.infinity,
                    height: 300,
                  ),
                ),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: _clear,
                child: const Text('Clear'),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

// API Service class for network operations
class ApiService {
  // Base URL for the API
  final String _baseUrl = 'http://48.216.211.10:8000'; // Replace with your API base URL

  // Fetches an authentication token
  Future<String?> getToken() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      return prefs.getString('token'); // Retrieve the token from shared preferences
    } catch (e) {
      print('Error getting token: $e');
      return null;
    }
  }

  // Generates an image based on the prompt
  Future<String?> generateImage(String token, String prompt) async {
    try {
      final response = await http.post(
        Uri.parse('$_baseUrl/generate-image'), // Adjust the endpoint as needed
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode({'prompt': prompt}), // Send the prompt in the request body
      );

      // Debugging line
      print('Generate Image Response body: ${response.body}');

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return data['imageUrl']; // Assuming the response contains 'imageUrl'
      } else {
        print('Failed to generate image: ${response.statusCode} - ${response.body}');
        return null;
      }
    } catch (e) {
      print('Error generating image: $e');
      return null;
    }
  }

  // Function to get the base URL (if needed)
  String getBaseUrl() {
    return _baseUrl;
  }
}
