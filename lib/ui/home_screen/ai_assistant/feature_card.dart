import 'package:flutter/material.dart';

class FeatureCard extends StatelessWidget {
  final String title;
  final String description;
  final IconData icon;
  final VoidCallback? onTap; // Optional callback for handling taps

  const FeatureCard({super.key, 
    required this.title,
    required this.description,
    required this.icon,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap, // Handle card tap if onTap is provided
      child: Card(
        color: Colors.grey[850],
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12.0),
        ),
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(icon, color: Colors.white, size: 40),
              const SizedBox(height: 16),
              Text(
                title,
                style: const TextStyle(
                  color: Colors.white,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 8),
              Flexible(
                child: Text(
                  description,
                  style: const TextStyle(color: Colors.white70),
                  textAlign: TextAlign.center,
                  overflow: TextOverflow.ellipsis, // Handle overflow with ellipsis
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
