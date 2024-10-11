import 'package:flutter/material.dart';
import 'package:http/http.dart' as http; // Import http package
import 'dart:convert'; // Import dart:convert for jsonDecode

class SignageTemplatesScreen extends StatefulWidget {
  const SignageTemplatesScreen({super.key});

  @override
  _SignageTemplatesScreenState createState() => _SignageTemplatesScreenState();
}

class _SignageTemplatesScreenState extends State<SignageTemplatesScreen> {
  final ApiService _apiService = ApiService(); // Instantiate ApiService
  List<dynamic> _templates = [];
  List<dynamic> _filteredTemplates = [];
  bool _loading = true;
  final TextEditingController _searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadTemplates();
    _searchController.addListener(() {
      _filterTemplates();
    });
  }

  Future<void> _loadTemplates() async {
    final token = await _apiService.getToken();
    if (token != null) {
      final templates = await _apiService.getSignageTemplates(token);
      if (templates != null) {
        setState(() {
          _templates = templates;
          _filteredTemplates = templates; // Initially show all templates
          _loading = false;
        });
      } else {
        setState(() {
          _loading = false;
        });
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Failed to load templates')));
      }
    } else {
      setState(() {
        _loading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Failed to get token')));
    }
  }

  void _filterTemplates() {
    final query = _searchController.text.toLowerCase();
    if (query.isNotEmpty) {
      setState(() {
        _filteredTemplates = _templates.where((template) {
          final idMatch = template['id'].toString().contains(query);
          final nameMatch = template['title'].toLowerCase().contains(query);
          return idMatch || nameMatch;
        }).toList();
      });
    } else {
      setState(() {
        _filteredTemplates = _templates;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Signage Templates', style: Theme.of(context).textTheme.titleLarge),
        bottom: PreferredSize(
          preferredSize: const Size.fromHeight(80.0),
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16.0),
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                hintText: 'Search by ID or Name',
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12.0),
                  borderSide: BorderSide.none,
                ),
                filled: true,
                fillColor: Colors.grey[200],
                prefixIcon: Icon(Icons.search, color: Colors.grey[600]),
              ),
              keyboardType: TextInputType.text,
            ),
          ),
        ),
      ),
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : _filteredTemplates.isEmpty
          ? Center(child: Text('No templates found', style: Theme.of(context).textTheme.bodyLarge))
          : ListView.builder(
        padding: const EdgeInsets.all(8.0),
        itemCount: _filteredTemplates.length,
        itemBuilder: (context, index) {
          final template = _filteredTemplates[index];
          final imageUrl = '${_apiService.getBaseUrl()}${template['signage_image']}';
          return Card(
            elevation: 4,
            margin: const EdgeInsets.symmetric(vertical: 8.0),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12.0),
            ),
            child: ListTile(
              contentPadding: const EdgeInsets.all(12.0),
              leading: GestureDetector(
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => FullScreenImage(imageUrl: imageUrl),
                    ),
                  );
                },
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(8.0),
                  child: Image.network(
                    imageUrl,
                    width: 100,
                    height: 100,
                    fit: BoxFit.cover,
                  ),
                ),
              ),
              title: Text(template['title'], style: Theme.of(context).textTheme.titleMedium!.copyWith(fontWeight: FontWeight.bold)),
              subtitle: Text(template['description']),
              onTap: () {
                Navigator.pushNamed(
                  context,
                  '/signage-detail',
                  arguments: {
                    'id': template['id'].toString(), // Convert id to string
                    'title': template['title'],
                    'description': template['description'],
                    'imageUrl': imageUrl,
                  },
                );
              },
            ),
          );
        },
      ),
    );
  }
}

class FullScreenImage extends StatelessWidget {
  final String imageUrl;

  const FullScreenImage({super.key, required this.imageUrl});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Image', style: Theme.of(context).textTheme.titleLarge),
        backgroundColor: Colors.purple,
      ),
      body: Center(
        child: InteractiveViewer(
          child: Image.network(imageUrl),
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

      // Debugging line
      print('Token Response body: ${response.body}');

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

  // Fetches signage templates
  Future<List<dynamic>?> getSignageTemplates(String token) async {
    try {
      final response = await http.get(
        Uri.parse('$_baseUrl/signage-templates'), // Adjust the endpoint as needed
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
      );

      // Debugging line
      print('Signage Templates Response body: ${response.body}');

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return data['templates']; // Assuming the response contains a 'templates' array
      } else {
        print('Failed to load signage templates: ${response.statusCode} - ${response.body}');
        return null;
      }
    } catch (e) {
      print('Error fetching signage templates: $e');
      return null;
    }
  }

  // Function to get the base URL (if needed)
  String getBaseUrl() {
    return _baseUrl;
  }
}
