import 'package:flutter/material.dart';
import 'ad_creation.dart';

class CreateAds extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Create Ad',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20),
        ),
        backgroundColor: Colors.blueAccent, // Improved color contrast
        elevation: 4.0, // Add shadow for depth
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0), // Add padding for better spacing
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: <Widget>[
              Text(
                'Ready to create an ad?',
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.w600,
                  color: Colors.black87,
                ),
              ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => AdCreationPage()),
                  );
                },
                child: Text(
                  'Start Creating',
                  style: TextStyle(fontSize: 16),
                ),
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color.fromARGB(255, 204, 109, 221),
                  foregroundColor: Colors.white, // Updated parameter
                  padding: EdgeInsets.symmetric(vertical: 15),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  elevation: 5, // Add elevation for better touch feedback
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
