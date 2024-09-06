import 'package:flutter/material.dart';
import 'package:file_picker/file_picker.dart';
import '../../beint/api_service.dart';

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
