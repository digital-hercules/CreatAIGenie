import 'package:flutter/material.dart';

class SignageTemplateDetailScreen extends StatefulWidget {
  const SignageTemplateDetailScreen({super.key});

  @override
  _SignageTemplateDetailScreenState createState() => _SignageTemplateDetailScreenState();
}

class _SignageTemplateDetailScreenState extends State<SignageTemplateDetailScreen> {
  Map<String, dynamic>? _template;
  bool _loading = true;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final Map<String, dynamic> templateData = ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;
    if (_loading) {
      _loadTemplate(templateData);
    }
  }

  void _loadTemplate(Map<String, dynamic> templateData) async {
    setState(() {
      _template = templateData;
      _loading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(_template?['title'] ?? 'Signage Template Details')),
      body: _loading
        ? const Center(child: CircularProgressIndicator())
        : _template == null
          ? const Center(child: Text('Template not found'))
          : Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Title: ${_template!['title']}', style: Theme.of(context).textTheme.headlineSmall),
                  const SizedBox(height: 8),
                  Text('Description: ${_template!['description']}'),
                  const SizedBox(height: 16),
                  Image.network('${_template!['imageUrl']}'),  // Use imageUrl from passed data
                  // Display more template details as needed
                ],
              ),
            ),
    );
  }
}
