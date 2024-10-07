import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class ApiService {
  static const String baseUrl = 'http://48.216.211.10:8000/'; // Replace with your actual base URL

  // Function to log in and get JWT token
  Future<String?> login(String email, String password) async {
    try {
      // Send HTTP POST request
      final response = await http.post(
        Uri.parse('$baseUrl/auth/jwt/create/'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'email': email, 'password': password}),
      );

      // Check for successful response
      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final token = data['access']; // Return JWT token

        // Store token in SharedPreferences
        final prefs = await SharedPreferences.getInstance();
        await prefs.setString('jwt_token', token); // Save token in SharedPreferences
        return token;
      } else {
        print('Login failed with status: ${response.statusCode}');
        print('Response body: ${response.body}');
        return null;
      }
    } catch (e) {
      print('Error during login: $e');
      return null;
    }
  }


  // Function to summarize text and return summary
  Future<String?> summarizeText(String token, String text) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/summarize-text/'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode({'text': text}),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return data['summary-result'];  // Return the summary text
      } else {
        print('Summarization failed: ${response.statusCode}');
        print('Response body: ${response.body}');
        return null;
      }
    } catch (e) {
      print('Error during summarization: $e');
      return null;
    }
  }

  // Function to generate an image from text
  Future<String?> generateImage(String token, String prompt) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/generate-image/'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode({'prompt': prompt}),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return data['image_url']; // Return the image URL
      } else {
        print('Image generation failed: ${response.statusCode}');
        print('Response body: ${response.body}');
        return null;
      }
    } catch (e) {
      print('Error during image generation: $e');
      return null;
    }
  }

  Future<List<String>?> extractKeywords(String token, String text) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/extract-keywords/'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode({'ad_copy': text}),  // Ensure 'ad_copy' matches the expected key
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final List<dynamic> keywordsList = data['keywords'];

        // Convert the dynamic list to a List<String>
        final List<String> keywords = keywordsList.map((keyword) => keyword.toString()).toList();

        return keywords;
      } else {
        print('Extraction of keywords failed: ${response.statusCode}');
        print('Response body: ${response.body}');
        return null;
      }
    } catch (e) {
      print('Error during extraction: $e');
      return null;
    }
  }

  // Modify the getSignageTemplates method in ApiService
  Future<List<Map<String, dynamic>>?> getSignageTemplates(String token) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/signagetemps/'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return (data['results'] as List).cast<Map<String, dynamic>>(); // Cast to List<Map<String, dynamic>>
      } else {
        print('Failed to retrieve signage templates: ${response.statusCode}');
        print('Response body: ${response.body}');
        return null;
      }
    } catch (e) {
      print('Error retrieving signage templates: $e');
      return null;
    }
  }

  Future<Map<String, dynamic>?> uploadFile(String token, String filePath) async {
    final request = http.MultipartRequest(
      'POST',
      Uri.parse('$baseUrl/upload-file/'),
    );

    request.headers['Authorization'] = 'Bearer $token';
    request.files.add(await http.MultipartFile.fromPath('file', filePath));

    final response = await request.send();

    if (response.statusCode == 200) {
      final responseData = await http.Response.fromStream(response);
      return jsonDecode(responseData.body);
    } else {
      print('File upload failed: ${response.statusCode}');
      return null;
    }
  }

  // Function to retrieve a signage template by its ID
  Future<Map<String, dynamic>?> getSignageTemplateById(String token, String id) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/signagetempsid/$id'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
      );

      if (response.statusCode == 200) {
        return jsonDecode(response.body) as Map<String, dynamic>;  // Return template data
      } else {
        print('Failed to retrieve signage template: ${response.statusCode}');
        print('Response body: ${response.body}');
        return null;
      }
    } catch (e) {
      print('Error retrieving signage template: $e');
      return null;
    }
  }

  // Function to get the stored token
  Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('jwt_token');
  }

  // Function to clear the stored token
  Future<void> clearToken() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('jwt_token');
  }

  String getBaseUrl() {
    if (baseUrl.isEmpty) {
      throw Exception('Base URL is not set');
    }
    return baseUrl;
  }
}
