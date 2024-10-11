import 'package:flutter/material.dart';
import 'package:file_picker/file_picker.dart';
import 'package:http/http.dart' as http; // Import http package
import 'dart:convert'; // Import dart:convert for jsonDecode

class FileUploadScreen extends StatefulWidget {
  const FileUploadScreen({super.key});

  @override
  _FileUploadScreenState createState() => _FileUploadScreenState();
}

class _FileUploadScreenState extends State<FileUploadScreen> {
  final ApiService _apiService = ApiService();
  bool _loading = false;
  String? _uploadedText;
  String? _summary;
  String? _errorMessage;

  Future<void> _pickAndUploadFile() async {
    setState(() {
      _loading = true;
      _uploadedText = null;
      _summary = null;
      _errorMessage = null;
    });

    final token = await _apiService.getToken();
    if (token != null) {
      final result = await FilePicker.platform.pickFiles();
      if (result != null && result.files.single.path != null) {
        final filePath = result.files.single.path!;
        final response = await _apiService.uploadFile(token, filePath);

        if (response != null) {
          setState(() {
            _uploadedText = 'Uploaded Text: ${response['text']}';
            _summary = 'Summary: ${response['summary']}';
          });
        } else {
          setState(() {
            _errorMessage = 'File upload failed. Please try again.';
          });
        }
      } else {
        setState(() {
          _errorMessage = 'File picking was canceled or failed.';
        });
      }
    } else {
      setState(() {
        _errorMessage = 'Failed to get authentication token.';
      });
    }

    setState(() {
      _loading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('File Upload')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            ElevatedButton(
              onPressed: _loading ? null : _pickAndUploadFile,
              style: ElevatedButton.styleFrom(
                minimumSize: const Size(double.infinity, 50),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
              child: _loading
                  ? const SizedBox(
                height: 20,
                width: 20,
                child: CircularProgressIndicator(
                  strokeWidth: 2,
                ),
              )
                  : const Text('Pick and Upload File'),
            ),
            const SizedBox(height: 16),
            if (_errorMessage != null)
              Text(
                _errorMessage!,
                style: const TextStyle(color: Colors.red, fontWeight: FontWeight.bold),
              ),
            if (_uploadedText != null)
              Text(
                _uploadedText!,
                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
              ),
            if (_summary != null)
              Padding(
                padding: const EdgeInsets.only(top: 8.0),
                child: Text(
                  _summary!,
                  style: const TextStyle(fontStyle: FontStyle.italic, fontSize: 16),
                ),
              ),
          ],
        ),
      ),
    );
  }
}

// API Service class for network operations
class ApiService {
  // Base URL for the API
  final String _baseUrl = 'https://api.yourservice.com'; // Replace with your API base URL

  // Fetches an authentication token
  Future<String?> getToken() async {
    try {
      final response = await http.post(
        Uri.parse('$_baseUrl/get-token'), // Adjust the endpoint as needed
        headers: {
          'Content-Type': 'application/json',
        },
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return data['token']; // Assuming the response contains a 'token'
      } else {
        print('Failed to get token: ${response.statusCode} - ${response.body}');
        return null;
      }
    } catch (e) {
      print('Error getting token: $e');
      return null;
    }
  }

  // Uploads a file and returns the server response
  Future<Map<String, dynamic>?> uploadFile(String token, String filePath) async {
    try {
      final request = http.MultipartRequest(
        'POST',
        Uri.parse('$_baseUrl/upload-file'), // Adjust the endpoint as needed
      );

      request.headers['Authorization'] = 'Bearer $token';
      request.files.add(await http.MultipartFile.fromPath('file', filePath));

      final response = await request.send();
      final responseBody = await http.Response.fromStream(response);

      if (response.statusCode == 200) {
        final data = jsonDecode(responseBody.body);
        return data; // Assuming the response contains 'text' and 'summary'
      } else {
        print('Failed to upload file: ${response.statusCode} - ${responseBody.body}');
        return null;
      }
    } catch (e) {
      print('Error uploading file: $e');
      return null;
    }
  }

  // Function to get the base URL (if needed)
  String getBaseUrl() {
    return _baseUrl;
  }
}
