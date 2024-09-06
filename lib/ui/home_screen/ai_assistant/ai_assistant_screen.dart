import 'package:aigeniog/ui/home_screen/ai_assistant/feature_card.dart';
import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';

class AIAssistantScreen extends StatefulWidget {
  const AIAssistantScreen({super.key});

  @override
  _AIAssistantScreenState createState() => _AIAssistantScreenState();
}

class _AIAssistantScreenState extends State<AIAssistantScreen> {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref("features");
  List<Map<String, dynamic>> _features = [];

  @override
  void initState() {
    super.initState();
    _fetchFeatures();
  }

  void _fetchFeatures() {
    _dbRef.onValue.listen((event) {
      final data = event.snapshot.value as Map<dynamic, dynamic>?;

      if (data != null) {
        final featuresList = data.entries.map((entry) {
          final value = entry.value as Map<dynamic, dynamic>?;
          return {
            'title': entry.key as String,
            'description': value?['description'] as String? ?? 'No description',
            'icon': value?['icon'] as String? ?? 'device_unknown',
            'onTap': () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => SomeNewScreen(entry.key),
                ),
              );
            },
          };
        }).toList();

        setState(() {
          _features = featuresList;
        });
      }
    }, onError: (error) {
      // Handle errors here
      print("Error fetching data: $error");
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.black,
        title: const Text(
          'CREATEAIGENIE',
          style: TextStyle(color: Colors.purpleAccent),
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.notifications, color: Colors.purpleAccent),
            onPressed: () {
              // Handle notifications
            },
          ),
          IconButton(
            icon: const Icon(Icons.store, color: Colors.purpleAccent),
            onPressed: () {
              // Handle store
            },
          ),
        ],
      ),
      body: Column(
        children: [
          Container(
            color: Colors.black,
            padding: const EdgeInsets.all(8.0),
            child: const Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                FilterButton(text: 'All', isSelected: true),
                FilterButton(text: 'Writing'),
                FilterButton(text: 'Creative'),
              ],
            ),
          ),
          Expanded(
            child: GridView.builder(
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                crossAxisSpacing: 16.0,
                mainAxisSpacing: 16.0,
              ),
              padding: const EdgeInsets.all(16.0),
              itemCount: _features.length,
              itemBuilder: (context, index) {
                final feature = _features[index];
                return FeatureCard(
                  title: feature['title'],
                  description: feature['description'],
                  icon: _getIconFromString(feature['icon']),
                  onTap: feature['onTap'],
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  IconData _getIconFromString(String iconName) {
    switch (iconName) {
      case 'edit':
        return Icons.edit;
      case 'school':
        return Icons.school;
      case 'article':
        return Icons.article;
      case 'language':
        return Icons.language;
      default:
        return Icons.device_unknown;
    }
  }
}

class FilterButton extends StatelessWidget {
  final String text;
  final bool isSelected;

  const FilterButton({super.key, required this.text, this.isSelected = false});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 8.0),
      child: TextButton(
        style: TextButton.styleFrom(
          foregroundColor: isSelected ? Colors.black : Colors.white,
          backgroundColor: isSelected ? Colors.white : Colors.grey[800],
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20.0),
          ),
        ),
        onPressed: () {
          // Handle filter button press
        },
        child: Text(text),
      ),
    );
  }
}

class SomeNewScreen extends StatelessWidget {
  final String featureTitle;

  const SomeNewScreen(this.featureTitle, {super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(featureTitle),
      ),
      body: Center(
        child: Text("Content for $featureTitle"),
      ),
    );
  }
}
