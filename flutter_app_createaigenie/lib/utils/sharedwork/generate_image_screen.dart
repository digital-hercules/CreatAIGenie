import 'package:flutter/material.dart';
import '../../beint/api_service.dart';

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
    } else {
      setState(() {
        _loading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Failed to get token')),
      );
    }
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
                backgroundColor: Colors.deepPurpleAccent, // Use backgroundColor instead of primary
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
            ],
          ],
        ),
      ),
    );
  }
}
