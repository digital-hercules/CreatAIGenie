import 'package:create_ai_genie/ui/home_screen/ADs/camp/bidding.dart';
import 'package:create_ai_genie/ui/home_screen/ADs/camp/improve_ad.dart';
import 'package:create_ai_genie/ui/home_screen/ADs/camp/saved.dart';
import 'package:create_ai_genie/ui/home_screen/ADs/camp/create_adpage.dart';
import 'package:flutter/material.dart';

class AddCamp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20.0),
        child: Column(
          children: [
            SizedBox(height: 40), // Space from the top
            Text(
              'Manage Your ADs',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
                color: Colors.purple,
              ),
            ),
            SizedBox(height: 20),
            CircleAvatar(
              radius: 40,
              backgroundColor: Colors.purple.shade200,
              child: Icon(Icons.ad_units, size: 40, color: Colors.purple),
            ),
            SizedBox(height: 40),
            Expanded(
              child: GridView.count(
                crossAxisCount: 2,
                crossAxisSpacing: 20.0,
                mainAxisSpacing: 20.0,
                children: [
                  AdOptionCard(
                    icon: Icons.add_box_rounded,
                    label: 'Create Ad',
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => CreateAdPage()),
                      );
                    },
                  ),
                 AdOptionCard(
  icon: Icons.check_box_rounded,
  label: 'Improve Ad',
  onTap: () {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => AdCampaignPage()),
    );
  },
),
AdOptionCard(
  icon: Icons.save,
  label: 'Saved Ad',
  onTap: () {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => SavedAdPage()),
    );
  },
),
AdOptionCard(
  icon: Icons.add_box_rounded,
  label: 'Bidding',
  onTap: () {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => BiddingPg()),
    );
  },
),

                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class AdOptionCard extends StatelessWidget {
  final IconData icon;
  final String label;
  final Function() onTap;

  AdOptionCard({
    required this.icon,
    required this.label,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          border: Border.all(color: Colors.black),
          borderRadius: BorderRadius.circular(10),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 50, color: Colors.purple),
            SizedBox(height: 10),
            Text(
              label,
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
