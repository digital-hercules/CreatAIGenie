import 'package:flutter/material.dart';

class SavedAdPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        leading: IconButton(
          icon: Icon(Icons.arrow_back, color: Colors.purple),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        title: Text(
          'AD Campaign',
          style: TextStyle(
            color: Colors.black,
            fontWeight: FontWeight.bold,
          ),
        ),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              'Saved Ad Copies',
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
                color: Colors.purple,
              ),
            ),
            SizedBox(height: 20),
            CircleAvatar(
              radius: 30,
              backgroundColor: Colors.purple.shade100,
              child: Icon(Icons.ad_units, size: 40, color: Colors.purple),
            ),
            SizedBox(height: 20),
            // Search bar
            Container(
              padding: EdgeInsets.symmetric(horizontal: 10),
              decoration: BoxDecoration(
                color: Colors.grey.shade200,
                borderRadius: BorderRadius.circular(10),
              ),
              child: TextField(
                decoration: InputDecoration(
                  icon: Icon(Icons.search),
                  border: InputBorder.none,
                  hintText: 'Search',
                ),
              ),
            ),
            SizedBox(height: 20),
            // List of Saved Ads
            Expanded(
              child: ListView(
                children: [
                  AdCopyCard(title: 'Ad Copy 1: Boost your sales with our new product'),
                  AdCopyCard(title: 'Ad Copy 2: Boost your sales with our new product'),
                  AdCopyCard(title: 'Ad Copy 3: Boost your sales with our new product'),
                ],
              ),
            ),
            SizedBox(height: 20),
            // Create New Button
            ElevatedButton(
              onPressed: () {
                // Add your navigation or function for creating a new ad
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.white,
                side: BorderSide(color: Colors.black),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
                padding: EdgeInsets.symmetric(horizontal: 30, vertical: 10),
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    'Create New',
                    style: TextStyle(color: Colors.black),
                  ),
                  Icon(Icons.arrow_forward, color: Colors.black),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class AdCopyCard extends StatelessWidget {
  final String title;

  const AdCopyCard({required this.title});

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: EdgeInsets.symmetric(vertical: 10),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(10),
      ),
      child: ListTile(
        contentPadding: EdgeInsets.symmetric(horizontal: 15, vertical: 10),
        title: Text(title),
        trailing: Wrap(
          spacing: 10, // space between icons
          children: [
            Icon(Icons.edit, color: Colors.purple),
            Icon(Icons.delete, color: Colors.purple),
            Icon(Icons.share, color: Colors.purple),
          ],
        ),
      ),
    );
  }
}
