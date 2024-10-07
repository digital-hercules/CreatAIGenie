
import 'package:create_ai_genie/ui/home_screen/ADs/camp/addcamp.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(CampaignApp());
}

class CampaignApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Campaign Page',
      theme: ThemeData(
        primarySwatch: Colors.purple,
      ),
      home: CampaignScreen(),
    );
  }
}

class CampaignScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Campaign', style: TextStyle(color: Colors.black)),
        backgroundColor: Colors.white,
        elevation: 0,
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Removed the top navigation tabs

            SizedBox(height: 16.0),

            // Search Bar
            TextField(
              decoration: InputDecoration(
                prefixIcon: Icon(Icons.search),
                hintText: 'Search',
                filled: true,
                fillColor: Colors.grey[200],
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                  borderSide: BorderSide.none,
                ),
              ),
            ),

            SizedBox(height: 16.0),

            // Add Campaign Button
           Center(
  child: OutlinedButton(
    onPressed: () {
      // Navigate to the Add Campaign page
      Navigator.push(
     context,
     MaterialPageRoute(builder: (context) => AddCamp()),
);

    },
    child: Text('Add Campaign'),
    style: OutlinedButton.styleFrom(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(20.0),
      ),
    ),
  ),
),


            SizedBox(height: 16.0),

            // Campaign List
            Expanded(
              child: ListView.builder(
                itemCount: 5, // Adjust number of items dynamically if needed
                itemBuilder: (context, index) {
                  return _buildCampaignItem();
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildCampaignItem() {
    return Card(
      margin: EdgeInsets.symmetric(vertical: 8.0),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(10.0),
        side: BorderSide(color: Colors.grey[300]!),
      ),
      child: ListTile(
        leading: CircleAvatar(
          backgroundColor: Colors.purple,
          radius: 24.0,
        ),
        title: Text(
          'SKU Name',
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        subtitle: Text('ACOS'),
        trailing: Column(
          crossAxisAlignment: CrossAxisAlignment.end,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              '₹2500',
              style: TextStyle(fontWeight: FontWeight.bold),
            ),
            Text(
              '+68.3%',
              style: TextStyle(color: Colors.green),
            ),
          ],
        ),
      ),
    );
  }
}
