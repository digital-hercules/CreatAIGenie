import 'package:flutter/material.dart';
import '../../../beint/api_service.dart';
import 'result_page.dart'; // Import result page to display keywords

class AdCreationPage extends StatefulWidget {
  @override
  _AdCreationPageState createState() => _AdCreationPageState();
}

class _AdCreationPageState extends State<AdCreationPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _brandController = TextEditingController();

  Future<void> _generateAd() async {
    if (_formKey.currentState!.validate()) {
      String title = _titleController.text;
      String description = _descriptionController.text;
      String brand = _brandController.text;

      String adContent = "Introducing $title! $description Get it now from $brand.";

      final apiService = ApiService();
      final token = await apiService.getToken(); // Get the stored JWT token

      if (token != null) {
        final keywords = await apiService.extractKeywords(token, adContent);

        if (keywords != null) {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => ResultPage(keywords: keywords),
            ),
          );
        } else {
          _showErrorDialog('Failed to extract keywords.');
        }
      } else {
        _showErrorDialog('No token found. Please log in again.');
      }
    }
  }

  void _showErrorDialog(String message) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Error'),
          content: Text(message),
          actions: <Widget>[
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: Text('OK'),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Create Ad',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20),
        ),
        backgroundColor: Colors.blueAccent, // Consistent color theme
        elevation: 4.0,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              _buildTextFormField(
                controller: _titleController,
                label: 'Product Title',
                hintText: 'Enter the title of the product',
              ),
              SizedBox(height: 16),
              _buildTextFormField(
                controller: _descriptionController,
                label: 'Product Description',
                hintText: 'Enter a brief description of the product',
              ),
              SizedBox(height: 16),
              _buildTextFormField(
                controller: _brandController,
                label: 'Brand Description',
                hintText: 'Enter the brand name or description',
              ),
              SizedBox(height: 24),
              ElevatedButton(
                onPressed: _generateAd,
                child: Text(
                  'Create Ad',
                  style: TextStyle(fontSize: 16),
                ),
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color.fromARGB(255, 204, 109, 221), // Updated parameter
                  foregroundColor: Colors.white, // Updated parameter
                  padding: EdgeInsets.symmetric(vertical: 15),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  elevation: 5,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildTextFormField({
    required TextEditingController controller,
    required String label,
    required String hintText,
  }) {
    return TextFormField(
      controller: controller,
      decoration: InputDecoration(
        labelText: label,
        hintText: hintText,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
        ),
        contentPadding: EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      ),
      validator: (value) {
        if (value == null || value.isEmpty) {
          return 'Please enter $label';
        }
        return null;
      },
    );
  }
}
